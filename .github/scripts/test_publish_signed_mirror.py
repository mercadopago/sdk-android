#!/usr/bin/env python3
"""Tests for the GitHub-signed mirror publisher."""

from __future__ import annotations

import importlib.util
import json
import os
import subprocess
import sys
import tempfile
import unittest
from pathlib import Path
from typing import Any


SCRIPT = Path(__file__).with_name("publish_signed_mirror.py")
SPEC = importlib.util.spec_from_file_location("publish_signed_mirror", SCRIPT)
assert SPEC and SPEC.loader
mirror = importlib.util.module_from_spec(SPEC)
sys.modules[SPEC.name] = mirror
SPEC.loader.exec_module(mirror)


def command(repo: Path, *args: str, input_bytes: bytes | None = None) -> bytes:
    result = subprocess.run(
        args,
        cwd=repo,
        input=input_bytes,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        check=False,
    )
    if result.returncode:
        raise AssertionError(result.stderr.decode(errors="replace"))
    return result.stdout


def git(repo: Path, *args: str) -> bytes:
    return command(repo, "git", *args)


def config(**overrides: str) -> Any:
    values = {
        "target_repo": "mercadopago/sdk-test",
        "token": "not-a-real-token",
        "branch": "feature/example",
        "title": "Mirror title",
        "source_repo": "melisource/private-sdk",
        "source_pr": "123",
        "source_sha": "a" * 40,
        "run_id": "456",
        "run_attempt": "1",
    }
    values.update(overrides)
    return mirror.MirrorConfig(**values)


def signed_response(oid: str = "b" * 40) -> dict[str, Any]:
    return {
        "data": {
            "createCommitOnBranch": {
                "commit": {
                    "oid": oid,
                    "url": "https://github.example/commit",
                    "signature": {
                        "isValid": True,
                        "state": "VALID",
                        "wasSignedByGitHub": True,
                    },
                },
                "ref": {"name": "mirror-staging/test"},
            }
        }
    }


class FakeGitHub:
    def __init__(
        self,
        *,
        target_oid: str | None = None,
        create_response: dict[str, Any] | None = None,
        update_response: dict[str, Any] | None = None,
    ) -> None:
        self.target_oid = target_oid
        self.create_response = create_response or signed_response()
        self.update_response = update_response or {"data": {"updateRefs": {}}}
        self.created_refs: list[tuple[str, str]] = []
        self.deleted_refs: list[str] = []
        self.requests: list[dict[str, Any]] = []

    def repository_id(self) -> str:
        return "R_test"

    def create_ref(self, branch: str, oid: str) -> None:
        self.created_refs.append((branch, oid))

    def delete_ref(self, branch: str) -> None:
        self.deleted_refs.append(branch)

    def ref_oid(self, branch: str) -> str | None:
        return self.target_oid

    def graphql(self, request: dict[str, Any]) -> dict[str, Any]:
        self.requests.append(request)
        if "CreateSignedMirrorCommit" in request["query"]:
            return self.create_response
        return self.update_response


class GitFixture:
    def __init__(self, root: Path) -> None:
        self.repo = root / "repo"
        self.repo.mkdir()
        git(self.repo, "init", "-b", "main")
        git(self.repo, "config", "user.name", "Mirror Tests")
        git(self.repo, "config", "user.email", "mirror-tests@example.com")

    def write(self, path: str, contents: bytes) -> None:
        target = self.repo / path
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_bytes(contents)

    def commit(self, message: str) -> str:
        git(self.repo, "add", "-A")
        git(self.repo, "commit", "-m", message)
        return git(self.repo, "rev-parse", "HEAD").decode().strip()


class StagedFileChangesTests(unittest.TestCase):
    def test_add_modify_delete_rename_and_binary(self) -> None:
        with tempfile.TemporaryDirectory() as directory:
            fixture = GitFixture(Path(directory))
            fixture.write("modified.txt", b"before\n")
            fixture.write("deleted.txt", b"delete me\n")
            fixture.write("old-name.txt", b"renamed\n")
            fixture.commit("base")

            fixture.write("modified.txt", b"after\n")
            fixture.write("added.bin", b"\x00\xffbinary\n")
            (fixture.repo / "deleted.txt").unlink()
            (fixture.repo / "old-name.txt").rename(fixture.repo / "new-name.txt")
            git(fixture.repo, "add", "-A")

            changes = mirror.staged_file_changes(fixture.repo)
            additions = {item["path"]: item["contents"] for item in changes.additions}
            deletions = {item["path"] for item in changes.deletions}

            self.assertEqual(
                set(additions), {"modified.txt", "added.bin", "new-name.txt"}
            )
            self.assertEqual(deletions, {"deleted.txt", "old-name.txt"})
            self.assertEqual(additions["added.bin"], "AP9iaW5hcnkK")

    def test_empty_filtered_change_creates_empty_file_changes(self) -> None:
        with tempfile.TemporaryDirectory() as directory:
            fixture = GitFixture(Path(directory))
            fixture.write("unchanged.txt", b"same\n")
            fixture.commit("base")
            changes = mirror.staged_file_changes(fixture.repo)
            self.assertEqual(changes.additions, [])
            self.assertEqual(changes.deletions, [])

    def test_rejects_executable_or_symlink_modes(self) -> None:
        with tempfile.TemporaryDirectory() as directory:
            fixture = GitFixture(Path(directory))
            fixture.write("base.txt", b"base\n")
            fixture.commit("base")
            fixture.write("executable.sh", b"#!/bin/sh\n")
            os.chmod(fixture.repo / "executable.sh", 0o755)
            git(fixture.repo, "add", "executable.sh")
            with self.assertRaisesRegex(mirror.MirrorError, "cannot explicitly preserve mode"):
                mirror.staged_file_changes(fixture.repo)


class RequestTests(unittest.TestCase):
    def test_stacked_pr_uses_the_checked_out_public_parent_as_expected_head(self) -> None:
        cfg = config(branch="stack/child", source_sha="c" * 40)
        changes = mirror.FileChanges(
            additions=[{"path": "child.txt", "contents": "Y2hpbGQK"}],
            deletions=[],
        )
        public_parent = "d" * 40
        request = mirror.create_commit_request(cfg, public_parent, changes)
        commit_input = request["variables"]["input"]
        self.assertEqual(commit_input["expectedHeadOid"], public_parent)
        self.assertEqual(
            commit_input["branch"]["branchName"],
            "mirror-staging/pr-123-456-1",
        )
        self.assertIn(f"Mirror-Source-SHA: {'c' * 40}", commit_input["message"]["body"])

    def test_existing_target_is_force_updated_with_an_optimistic_guard(self) -> None:
        old_oid = "1" * 40
        request = mirror.update_ref_request(
            "R_test", "feature", "2" * 40, old_oid, "mutation-id"
        )
        update = request["variables"]["input"]["refUpdates"][0]
        self.assertEqual(update["beforeOid"], old_oid)
        self.assertTrue(update["force"])

    def test_missing_target_is_created_only_if_it_still_does_not_exist(self) -> None:
        request = mirror.update_ref_request(
            "R_test", "feature", "2" * 40, None, "mutation-id"
        )
        update = request["variables"]["input"]["refUpdates"][0]
        self.assertEqual(update["beforeOid"], mirror.ZERO_OID)
        self.assertFalse(update["force"])

    def test_invalid_or_non_github_signature_is_rejected(self) -> None:
        response = signed_response()
        response["data"]["createCommitOnBranch"]["commit"]["signature"][
            "wasSignedByGitHub"
        ] = False
        with self.assertRaisesRegex(mirror.MirrorError, "refusing to update"):
            mirror.signed_commit_oid(response)

    def test_graphql_error_is_reported(self) -> None:
        with self.assertRaisesRegex(mirror.MirrorError, "expectedHeadOid"):
            mirror.signed_commit_oid(
                {"errors": [{"message": "expectedHeadOid does not match"}]}
            )


class PublishTests(unittest.TestCase):
    def setUp(self) -> None:
        self.temporary = tempfile.TemporaryDirectory()
        self.addCleanup(self.temporary.cleanup)
        self.fixture = GitFixture(Path(self.temporary.name))
        self.fixture.write("base.txt", b"base\n")
        self.base_oid = self.fixture.commit("public parent")
        self.fixture.write("source.txt", b"source\n")
        git(self.fixture.repo, "add", "source.txt")

    def test_publishes_signed_commit_then_cleans_up_staging_ref(self) -> None:
        client = FakeGitHub(target_oid="e" * 40)
        result = mirror.publish(self.fixture.repo, config(), client)
        self.assertEqual(result, "b" * 40)
        self.assertEqual(client.created_refs[0][1], self.base_oid)
        self.assertEqual(client.deleted_refs, [config().staging_branch])
        update = client.requests[1]["variables"]["input"]["refUpdates"][0]
        self.assertEqual(update["beforeOid"], "e" * 40)

    def test_stale_target_failure_is_propagated_and_staging_is_cleaned(self) -> None:
        client = FakeGitHub(
            target_oid="e" * 40,
            update_response={"errors": [{"message": "ref is stale"}]},
        )
        with self.assertRaisesRegex(mirror.MirrorError, "ref is stale"):
            mirror.publish(self.fixture.repo, config(), client)
        self.assertEqual(client.deleted_refs, [config().staging_branch])

    def test_unsigned_commit_never_updates_target_and_cleans_staging(self) -> None:
        response = signed_response()
        response["data"]["createCommitOnBranch"]["commit"]["signature"][
            "isValid"
        ] = False
        client = FakeGitHub(create_response=response)
        with self.assertRaises(mirror.MirrorError):
            mirror.publish(self.fixture.repo, config(), client)
        self.assertEqual(len(client.requests), 1)
        self.assertEqual(client.deleted_refs, [config().staging_branch])


if __name__ == "__main__":
    unittest.main(verbosity=2)
