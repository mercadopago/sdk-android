"""Strict, secret-free E3 registry validation (Python 3.9+, no I/O).

Bounds: 256 KiB UTF-8, depth 16, 100 people/directory members, 63-bit IDs.
Empty directories are valid offline data; production completeness needs the
separate administrative approval, which this parser does not certify.
"""

import hashlib
import json
import re


PAIRS = {f"melisource/fury_openplatform-sdk-{p}": f"mercadopago/sdk-{p}"
         for p in ("android", "ios")}
HUMAN_ROLES = frozenset(("publisher_public", "reviewer_public", "reviewer_internal"))
TECH_ROLES = frozenset(("public_reader", "internal_importer"))
RESERVED = frozenset(("NATIVE_SDK_CREDENTIALS_ENABLED", "NATIVE_SDK_SELECTED_TOKEN",
                      "NATIVE_SDK_TEAM_DIRECTORY_APPROVAL", "NATIVE_SDK_PR_DRIVER",
                      "NATIVE_SDK_CI_DRIVER", "NATIVE_SDK_REVERSE_DRIVER",
                      "NATIVE_SDK_PR_RECOVERY", "NATIVE_SDK_CI_RECOVERY",
                      "NATIVE_SDK_REVERSE_RECOVERY"))
PAT_FIELDS = frozenset(("credential_id", "resource_owner", "mode_variable", "active_variable"))


class CredentialError(ValueError):
    """Safe public failure; no user-controlled error details."""

    code = "BLOCKED"

    def __init__(self):
        super().__init__("BLOCKED: invalid native SDK credential plan")


def require(condition: bool) -> None:
    if not condition:
        raise CredentialError()


def exact(value: object, keys) -> None:
    require(type(value) is dict and value.keys() == set(keys))


def text_matches(value: object, pattern: str, limit: int = 128) -> bool:
    return (type(value) is str and 0 < len(value) <= limit
            and re.fullmatch(pattern, value, flags=re.ASCII) is not None
            and re.search(r"gh[pousr]_|github_pat_", value) is None)


def positive_id(value: object) -> bool:
    return type(value) is int and 0 < value <= 2**63 - 1


def identity(value: object, *, internal: bool = False) -> None:
    exact(value, ("login", "id"))
    require(positive_id(value["id"]))
    pattern = r"[A-Za-z0-9]+(?:-[A-Za-z0-9]+)*" + (r"(?:_meli)?" if internal else "")
    require(text_matches(value["login"], pattern, 39))


def fingerprint(value: object) -> str:
    """Match hub common.fingerprint: UTF-8 strings or sorted compact JSON."""
    raw = value.encode("utf-8") if isinstance(value, str) else json.dumps(
        value, sort_keys=True, separators=(",", ":"), allow_nan=False).encode("utf-8")
    return "sha256:" + hashlib.sha256(raw).hexdigest()


def _pairs(pairs: list) -> dict:
    result = {}
    for key, value in pairs:
        require(key not in result)
        result[key] = value
    return result


def _invalid_number(_value: str):
    raise CredentialError()


def strict_json(raw: object, limit: int) -> object:
    """Decode bounded JSON, rejecting duplicates, floats, constants and depth >16."""
    try:
        require(type(raw) in (bytes, str))
        encoded = raw.encode("utf-8") if type(raw) is str else raw
        require(len(encoded) <= limit)
        decoded = encoded.decode("utf-8")
        depth, quoted, escaped = 0, False, False
        for char in decoded:
            if quoted:
                if escaped:
                    escaped = False
                elif char == "\\":
                    escaped = True
                elif char == '"':
                    quoted = False
            elif char == '"':
                quoted = True
            elif char in "[{":
                depth += 1
                require(depth <= 16)
            elif char in "]}":
                depth -= 1
        return json.loads(decoded, object_pairs_hook=_pairs,
                          parse_constant=_invalid_number, parse_float=_invalid_number)
    except (ValueError, TypeError, UnicodeError, RecursionError, OverflowError):
        raise CredentialError() from None


def reference_base(binding: dict) -> str:
    """Derive the fixed base only from a validated registry, never an event."""
    mode = binding["mode_variable"]
    require(text_matches(mode, r"NATIVE_SDK_[A-Z0-9]+(?:_[A-Z0-9]+)*_MODE"))
    base = mode[:-5]
    require(len(base) <= 91 and binding["active_variable"] == base + "_ACTIVE")
    return base


def _binding(binding: object, role: str, ids: set, names: set) -> None:
    technical = role in TECH_ROLES
    if technical and binding == {"kind": "github_token"}:
        require(role == "internal_importer")
        return
    exact(binding, PAT_FIELDS | ({"kind", "owner"} if technical else set()))
    if technical:
        require(binding["kind"] == "pat")
        identity(binding["owner"], internal=role == "internal_importer")
    owner = "melisource" if role in ("reviewer_internal", "internal_importer") else "mercadopago"
    require(binding["resource_owner"] == owner)
    credential = binding["credential_id"]
    require(text_matches(credential, r"[a-z][a-z0-9-]*"))
    require(credential != "automation-internal-platform" and credential not in ids)
    ids.add(credential)
    base = reference_base(binding)
    references = {base, base + "_MODE", base + "_ACTIVE", base + "_IDENTITY"}
    require(not references & (names | RESERVED))
    names.update(references)


def validate_registry(value: object, private_repo: str, public_repo: str) -> dict:
    """Validate the entire mapping, including bindings unrelated to the selection."""
    require(type(private_repo) is str and PAIRS.get(private_repo) == public_repo)
    exact(value, ("schema", "repository", "public_repository", "team_public_identities",
                  "people", "automation"))
    require(value["schema"] == "native-sdk-identities/v1")
    require(value["repository"] == private_repo and value["public_repository"] == public_repo)
    people, directory, automation = (value[k] for k in
                                    ("people", "team_public_identities", "automation"))
    require(type(people) is list and len(people) <= 100)
    require(type(directory) is list and len(directory) <= 100)
    require(type(automation) is dict and automation.keys() <= TECH_ROLES)
    directory_ids, directory_logins = {}, set()
    for member in directory:
        identity(member)
        require(member["id"] not in directory_ids and member["login"].lower() not in directory_logins)
        directory_ids[member["id"]] = member["login"]
        directory_logins.add(member["login"].lower())
    account_ids, account_logins, credentials, variables = set(), set(), set(), set()

    def account(member: dict, public: bool) -> None:
        identity(member, internal=not public)
        number, login = member["id"], member["login"]
        require(number not in account_ids and login.lower() not in account_logins)
        account_ids.add(number)
        account_logins.add(login.lower())
        if public:
            require(directory_ids.get(number) == login)
        else:
            require(number not in directory_ids and login.lower() not in directory_logins)

    for person in people:
        exact(person, ("internal", "public", "bindings"))
        account(person["internal"], False)
        account(person["public"], True)
        bindings = person["bindings"]
        require(type(bindings) is dict and bindings.keys() <= HUMAN_ROLES)
        for role, binding in bindings.items():
            _binding(binding, role, credentials, variables)
    for role, binding in automation.items():
        _binding(binding, role, credentials, variables)
        if binding.get("kind") == "pat":
            account(binding["owner"], role == "public_reader")
    return value


def parse_registry(raw: bytes, private_repo: str, public_repo: str) -> dict:
    """Parse E3 bytes for one exact supported private/public SDK repository pair."""
    require(type(raw) is bytes)
    return validate_registry(strict_json(raw, 256 * 1024), private_repo, public_repo)


def checked_registry(registry: object, private_repo: str, public_repo: str) -> dict:
    """Copy and revalidate against the caller's expected SDK repository pair."""
    try:
        require(type(registry) is dict)
        raw = json.dumps(registry, ensure_ascii=True, allow_nan=False).encode("utf-8")
        return parse_registry(raw, private_repo, public_repo)
    except (ValueError, TypeError, UnicodeError, RecursionError, OverflowError):
        raise CredentialError() from None
