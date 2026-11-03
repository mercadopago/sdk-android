#!/usr/bin/env python3
"""Publish the staged mirror tree as a GitHub-signed commit."""

from __future__ import annotations

import base64
import json
import os
import re
import subprocess
import sys
from dataclasses import dataclass
from pathlib import Path
from typing import Any, Mapping, Sequence
from urllib.parse import quote


ZERO_OID = "0" * 40

CREATE_COMMIT_MUTATION = """
mutation CreateSignedMirrorCommit($input: CreateCommitOnBranchInput!) {
  createCommitOnBranch(input: $input) {
    commit {
      oid
      url
      signature {
        isValid
        state
        wasSignedByGitHub
      }
    }
    ref { name }
  }
}
"""

UPDATE_REFS_MUTATION = """
mutation UpdateMirrorRef($input: UpdateRefsInput!) {
  updateRefs(input: $input) {
    clientMutationId
  }
}
"""


class MirrorError(RuntimeError):
    """A safe, actionable mirror failure."""


def run(
    args: Sequence[str],
    *,
    cwd: Path | None = None,
    env: Mapping[str, str] | None = None,
    input_bytes: bytes | None = None,
    check: bool = True,
) -> subprocess.CompletedProcess[bytes]:
    result = subprocess.run(
        list(args),
        cwd=cwd,
        env=dict(env) if env is not None else None,
        input=input_bytes,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        check=False,
    )
    if check and result.returncode != 0:
        stderr = result.stderr.decode("utf-8", errors="replace").strip()
        raise MirrorError(f"Command failed ({' '.join(args)}): {stderr}")
    return result


def git(args: Sequence[str], repo: Path) -> bytes:
    return run(["git", *args], cwd=repo).stdout


@dataclass(frozen=True)
class FileChanges:
    additions: list[dict[str, str]]
    deletions: list[dict[str, str]]


@dataclass(frozen=True)
class MirrorConfig:
    target_repo: str
    token: str
    branch: str
    title: str
    source_repo: str
    source_pr: str
    source_sha: str
    run_id: str
    run_attempt: str

    @classmethod
    def from_env(cls, env: Mapping[str, str]) -> "MirrorConfig":
        def required(name: str) -> str:
            value = env.get(name, "").strip()
            if not value:
                raise MirrorError(f"Required environment variable {name} is missing")
            return value

        token = env.get("PAT", "").strip() or env.get("GH_TOKEN", "").strip()
        if not token:
            raise MirrorError("Required environment variable PAT or GH_TOKEN is missing")

        return cls(
            target_repo=required("TARGET_REPO"),
            token=token,
            branch=required("BRANCH"),
            title=required("TITLE"),
            source_repo=required("SOURCE_REPO"),
            source_pr=required("SOURCE_PR_NUMBER"),
            source_sha=required("SOURCE_HEAD_SHA"),
            run_id=env.get("GITHUB_RUN_ID", "local"),
            run_attempt=env.get("GITHUB_RUN_ATTEMPT", "1"),
        )

    @property
    def staging_branch(self) -> str:
        raw = f"mirror-staging/pr-{self.source_pr}-{self.run_id}-{self.run_attempt}"
        return re.sub(r"[^A-Za-z0-9._/-]", "-", raw)

    @property
    def commit_body(self) -> str:
        return (
            "Created by the private SDK public-mirror workflow.\n\n"
            f"Mirror-Source-Repo: {self.source_repo}\n"
            f"Mirror-Source-PR: {self.source_pr}\n"
            f"Mirror-Source-SHA: {self.source_sha}"
        )


def _index_mode(repo: Path, path: str) -> str:
    entry = git(["ls-files", "-s", "-z", "--", path], repo)
    if not entry:
        raise MirrorError(f"Unable to read staged index entry for {path!r}")
    return entry.split(b" ", 1)[0].decode("ascii")


def staged_file_changes(repo: Path) -> FileChanges:
    """Convert the staged index into createCommitOnBranch FileChanges."""
    raw = git(["diff", "--cached", "--name-status", "--no-renames", "-z"], repo)
    fields = raw.split(b"\0")
    if fields and fields[-1] == b"":
        fields.pop()
    if len(fields) % 2:
        raise MirrorError("Unexpected NUL-delimited output from git diff --name-status")

    additions: list[dict[str, str]] = []
    deletions: list[dict[str, str]] = []
    for offset in range(0, len(fields), 2):
        status = fields[offset].decode("ascii", errors="strict")
        path = fields[offset + 1].decode("utf-8", errors="strict")

        if status == "D":
            deletions.append({"path": path})
            continue
        if status not in {"A", "M"}:
            raise MirrorError(f"Unsupported staged change {status!r} for {path!r}")

        mode = _index_mode(repo, path)
        if mode != "100644":
            raise MirrorError(
                f"createCommitOnBranch cannot explicitly preserve mode {mode} for {path!r}; "
                "mirror this change manually"
            )

        contents = git(["show", f":{path}"], repo)
        additions.append(
            {
                "path": path,
                "contents": base64.b64encode(contents).decode("ascii"),
            }
        )

    return FileChanges(additions=additions, deletions=deletions)


def create_commit_request(
    config: MirrorConfig,
    base_oid: str,
    changes: FileChanges,
) -> dict[str, Any]:
    headline = " ".join(config.title.splitlines()).strip()
    return {
        "query": CREATE_COMMIT_MUTATION,
        "variables": {
            "input": {
                "branch": {
                    "repositoryNameWithOwner": config.target_repo,
                    "branchName": config.staging_branch,
                },
                "expectedHeadOid": base_oid,
                "message": {
                    "headline": headline,
                    "body": config.commit_body,
                },
                "fileChanges": {
                    "additions": changes.additions,
                    "deletions": changes.deletions,
                },
                "clientMutationId": f"mirror-{config.source_pr}-{config.run_id}",
            }
        },
    }


def signed_commit_oid(response: Mapping[str, Any]) -> str:
    errors = response.get("errors")
    if errors:
        raise MirrorError(f"createCommitOnBranch failed: {json.dumps(errors, sort_keys=True)}")

    try:
        commit = response["data"]["createCommitOnBranch"]["commit"]
        signature = commit["signature"]
        oid = commit["oid"]
    except (KeyError, TypeError) as error:
        raise MirrorError("createCommitOnBranch returned an incomplete response") from error

    if not (
        signature
        and signature.get("isValid") is True
        and signature.get("state") == "VALID"
        and signature.get("wasSignedByGitHub") is True
    ):
        raise MirrorError(
            "GitHub did not return a valid GitHub-signed commit; refusing to update the public branch"
        )
    return str(oid)


def update_ref_request(
    repository_id: str,
    branch: str,
    after_oid: str,
    before_oid: str | None,
    client_mutation_id: str,
) -> dict[str, Any]:
    expected_oid = before_oid or ZERO_OID
    return {
        "query": UPDATE_REFS_MUTATION,
        "variables": {
            "input": {
                "repositoryId": repository_id,
                "refUpdates": [
                    {
                        "name": f"refs/heads/{branch}",
                        "beforeOid": expected_oid,
                        "afterOid": after_oid,
                        "force": before_oid is not None,
                    }
                ],
                "clientMutationId": client_mutation_id,
            }
        },
    }


def ensure_graphql_success(response: Mapping[str, Any], operation: str) -> None:
    if response.get("errors"):
        raise MirrorError(f"{operation} failed: {json.dumps(response['errors'], sort_keys=True)}")
    if response.get("data") is None:
        raise MirrorError(f"{operation} returned no data")


class GitHubClient:
    def __init__(self, target_repo: str, token: str) -> None:
        self.target_repo = target_repo
        self.env = dict(os.environ)
        self.env["GH_TOKEN"] = token

    def _api(
        self,
        endpoint: str,
        *,
        method: str = "GET",
        payload: Mapping[str, Any] | None = None,
        check: bool = True,
    ) -> subprocess.CompletedProcess[bytes]:
        args = ["gh", "api", endpoint]
        if method != "GET":
            args.extend(["--method", method])
        input_bytes = None
        if payload is not None:
            args.extend(["--input", "-"])
            input_bytes = json.dumps(payload).encode("utf-8")
        return run(args, env=self.env, input_bytes=input_bytes, check=check)

    def graphql(self, request: Mapping[str, Any]) -> dict[str, Any]:
        result = self._api("graphql", method="POST", payload=request)
        return json.loads(result.stdout)

    def repository_id(self) -> str:
        response = json.loads(self._api(f"repos/{self.target_repo}").stdout)
        node_id = response.get("node_id")
        if not node_id:
            raise MirrorError("Unable to resolve the public repository node ID")
        return str(node_id)

    def ref_oid(self, branch: str) -> str | None:
        encoded = quote(f"heads/{branch}", safe="/")
        response = self._api(
            f"repos/{self.target_repo}/git/matching-refs/{encoded}"
        )
        refs = json.loads(response.stdout)
        expected = f"refs/heads/{branch}"
        for ref in refs:
            if ref.get("ref") == expected:
                return str(ref["object"]["sha"])
        return None

    def create_ref(self, branch: str, oid: str) -> None:
        self._api(
            f"repos/{self.target_repo}/git/refs",
            method="POST",
            payload={"ref": f"refs/heads/{branch}", "sha": oid},
        )

    def delete_ref(self, branch: str) -> None:
        encoded = quote(f"heads/{branch}", safe="/")
        self._api(
            f"repos/{self.target_repo}/git/refs/{encoded}",
            method="DELETE",
            check=False,
        )


def publish(repo: Path, config: MirrorConfig, client: GitHubClient) -> str:
    base_oid = git(["rev-parse", "HEAD"], repo).decode("ascii").strip()
    changes = staged_file_changes(repo)
    repository_id = client.repository_id()

    client.create_ref(config.staging_branch, base_oid)
    try:
        response = client.graphql(create_commit_request(config, base_oid, changes))
        commit_oid = signed_commit_oid(response)

        current_target_oid = client.ref_oid(config.branch)
        request = update_ref_request(
            repository_id,
            config.branch,
            commit_oid,
            current_target_oid,
            f"mirror-ref-{config.source_pr}-{config.run_id}",
        )
        ensure_graphql_success(client.graphql(request), "updateRefs")
    finally:
        client.delete_ref(config.staging_branch)

    return commit_oid


def main() -> int:
    try:
        config = MirrorConfig.from_env(os.environ)
        commit_oid = publish(Path.cwd(), config, GitHubClient(config.target_repo, config.token))
        print(f"Published GitHub-signed mirror commit {commit_oid} to {config.branch}")
        output = os.environ.get("GITHUB_OUTPUT")
        if output:
            with Path(output).open("a", encoding="utf-8") as stream:
                stream.write(f"commit_oid={commit_oid}\n")
        return 0
    except MirrorError as error:
        print(f"::error::{error}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
