"""Pure E4 credential PLAN builder; never verifies a PAT or executes an operation.

Input events are already normalized E5 data, not GitHub webhook payloads. Live
resource/identity proofs, Actions policy preflight, directory approval and driver
ownership belong to the caller. A returned plan is not authorization to execute.
"""

import re

from native_sdk_mirror_events_v2 import ConfigInvalid, validate_event
from native_sdk_registry_v2 import (
    PAIRS, CredentialError, checked_registry, exact, fingerprint, positive_id,
    reference_base, require, strict_json, text_matches,
)

__all__ = ["CredentialError", "identify_binding", "resolve_binding"]


OPERATIONS = {
    "publisher_open": "publisher_public", "publisher_sync": "publisher_public",
    "publisher_merge": "publisher_public", "reviewer_public": "reviewer_public",
    "reviewer_internal": "reviewer_internal", "public_ci_read": "public_reader",
    "public_import_read": "public_reader", "internal_import": "internal_importer",
    "internal_finalize": "internal_importer", "internal_ci_comment": None,
}


def _event(event: object, operation: str) -> tuple:
    require(type(operation) is str and operation in OPERATIONS)
    try:
        private = validate_event(event)
    except ConfigInvalid:
        raise CredentialError() from None
    require(event["operation"] == operation)
    return private, PAIRS[private]


def _target(private: str, public: str, operation: str) -> str:
    return private if operation in ("reviewer_internal", "internal_import",
                                    "internal_finalize", "internal_ci_comment") else public


def _selection(registry: dict, event: dict, operation: str) -> dict:
    role = OPERATIONS[operation]
    binding, person, owner = None, None, None
    if role in ("public_reader", "internal_importer"):
        binding = registry["automation"].get(role)
        if binding and binding.get("kind") == "pat":
            owner = binding["owner"]
    else:
        side = "public" if role == "reviewer_internal" else "internal"
        for candidate in registry["people"]:
            if candidate[side]["id"] == event["actor_id"]:
                person = candidate
                binding = candidate["bindings"].get(role)
                owner = candidate["internal" if role == "reviewer_internal" else "public"]
                break
        # Known wrong-side and technical actors must never fall back to legacy.
        known_ids = {p[s]["id"] for p in registry["people"] for s in ("internal", "public")}
        known_ids.update(b["owner"]["id"] for b in registry["automation"].values()
                         if b.get("kind") == "pat")
        require(person is not None or event["actor_id"] not in known_ids)
    return {"role": role, "binding": binding, "person": person, "owner": owner}


def _identify(registry, event, operation, private, public) -> tuple:
    """Select and fingerprint exactly one validated registry snapshot."""
    if operation == "internal_ci_comment":
        selection, snapshot = {"binding": None}, None
    else:
        snapshot = checked_registry(registry, private, public)
        selection = _selection(snapshot, event, operation)
    binding = selection["binding"]
    pat = binding is not None and binding.get("kind") != "github_token"
    human = pat and "owner" not in binding
    names = {
        "mode_variable": binding["mode_variable"] if pat else None,
        "active_variable": binding["active_variable"] if pat else None,
        "identity_variable": reference_base(binding) + "_IDENTITY" if human else None,
        "binding_fingerprint": fingerprint({"registry": snapshot, "event": event,
                                             "operation": operation, "selection": selection}),
    }
    return snapshot, selection, names


def identify_binding(registry, event, operation) -> dict:
    """Return variable names and a fingerprint binding the snapshot/event/selection.

    No variables or secrets are read. None names indicate an absent binding or
    platform credential. The complete validated registry and event are bound,
    preventing cross-person, cross-operation and changed-registry substitution.
    """
    private, public = _event(event, operation)
    return _identify(registry, event, operation, private, public)[2]


def _plan(mode: str, operation: str, repository: str, commit=None,
          credential=None, secret=None, owner=None) -> dict:
    return {"schema": "native-sdk-resolution/v1", "mode": mode, "operation": operation,
            "credential_id": credential, "secret_name": secret,
            "expected_login": owner["login"] if owner else None,
            "expected_id": owner["id"] if owner else None,
            "repository": repository, "registry_commit": commit}


def _attestation(raw: object, selection: dict) -> None:
    attestation = strict_json(raw, 2048)
    exact(attestation, ("schema", "credential_id", "internal", "public", "role", "reference_base"))
    person, binding = selection["person"], selection["binding"]
    # Exact types matter: True must never compare equal to an account ID of 1.
    for side in ("internal", "public"):
        exact(attestation[side], ("login", "id"))
        require(positive_id(attestation[side]["id"]))
    require(attestation == {"schema": "native-sdk-identity-binding/v1",
                            "credential_id": binding["credential_id"],
                            "internal": person["internal"], "public": person["public"],
                            "role": selection["role"], "reference_base": reference_base(binding)})


def resolve_binding(registry, event, operation, enabled, variables, registry_commit) -> dict:
    """Build a plan only, using selected repository variables, never PAT values.

    ``enabled`` is None (absent), 'false', or 'true', with exact string types.
    For a PAT binding, ``variables`` is an exact dict with keys:
    binding_fingerprint (from identify_binding), MODE_VALUE (None means absent).
    Fine-grained mode additionally requires ACTIVE_VALUE and, for a human,
    IDENTITY_VALUE (the <=2 KiB JSON administrative attestation). Legacy mode
    permits but never reads ACTIVE_VALUE/IDENTITY_VALUE. No global vars map.
    Missing/platform bindings ignore variables. Flag off bypasses registry,
    variables and registry_commit entirely. internal_ci_comment bypasses all
    credential settings. Enabled resolutions require a full 40-hex commit.
    Only offline preview/fixture scopes are accepted in this increment.
    """
    private, public = _event(event, operation)
    repository = _target(private, public, operation)
    if operation == "internal_ci_comment":
        return _plan("github_token", operation, repository,
                     credential="automation-internal-platform")
    require(enabled is None or (type(enabled) is str and enabled in ("true", "false")))
    if enabled is None or enabled == "false":
        return _plan("legacy", operation, repository)
    require(text_matches(registry_commit, r"[0-9a-fA-F]{40}", 40))
    _, selection, identified = _identify(registry, event, operation, private, public)
    binding = selection["binding"]
    if binding is None:
        return _plan("legacy", operation, repository, registry_commit)
    if binding.get("kind") == "github_token":
        return _plan("github_token", operation, repository, registry_commit,
                     credential="automation-internal-platform")
    require(type(variables) is dict)
    required = {"binding_fingerprint", "MODE_VALUE"}
    require(required <= variables.keys() <= required | {"ACTIVE_VALUE", "IDENTITY_VALUE"})
    require(variables["binding_fingerprint"] == identified["binding_fingerprint"])
    mode = variables["MODE_VALUE"]
    require(mode is None or (type(mode) is str and mode in ("legacy", "fine_grained")))
    if mode is None or mode == "legacy":
        return _plan("legacy", operation, repository, registry_commit, binding["credential_id"])
    expected_keys = required | {"ACTIVE_VALUE"}
    if selection["person"] is not None:
        expected_keys.add("IDENTITY_VALUE")
    exact(variables, expected_keys)
    secret = variables["ACTIVE_VALUE"]
    base = reference_base(binding)
    require(text_matches(secret, re.escape(base) + r"_R[0-9A-F]{32}_V[1-9][0-9]*"))
    if selection["person"] is not None:
        _attestation(variables["IDENTITY_VALUE"], selection)
    return _plan("fine_grained", operation, repository, registry_commit,
                 binding["credential_id"], secret, selection["owner"])
