"""Bounded offline E3 corpus shared byte-for-byte by both SDK worktrees."""

import copy
import json
from pathlib import Path
import unittest

from native_sdk_registry_v2 import CredentialError, RESERVED, parse_registry, strict_json


def binding(role, number=101):
    base = "NATIVE_SDK_" + role.upper() + "_" + str(number)
    return {"credential_id": role.replace("_", "-") + "-" + str(number),
            "resource_owner": "melisource" if role == "reviewer_internal" else "mercadopago",
            "mode_variable": base + "_MODE", "active_variable": base + "_ACTIVE"}


def registry(platform="android"):
    reader = dict(binding("public_reader", 303), kind="pat",
                  owner={"login": "sdk-reader", "id": 303})
    return {"schema": "native-sdk-identities/v1",
            "repository": "melisource/fury_openplatform-sdk-" + platform,
            "public_repository": "mercadopago/sdk-" + platform,
            "team_public_identities": [{"login": "public-human", "id": 202},
                                       {"login": "sdk-reader", "id": 303}],
            "people": [{"internal": {"login": "internal-human", "id": 101},
                        "public": {"login": "public-human", "id": 202},
                        "bindings": {role: binding(role) for role in
                                     ("publisher_public", "reviewer_public", "reviewer_internal")}}],
            "automation": {"public_reader": reader,
                           "internal_importer": {"kind": "github_token"}}}


def parsed(value=None):
    value = registry() if value is None else value
    return parse_registry(json.dumps(value).encode(), value["repository"], value["public_repository"])


class RegistryTests(unittest.TestCase):
    def blocked(self, value):
        with self.assertRaises(CredentialError) as result:
            parsed(value)
        self.assertEqual(result.exception.code, "BLOCKED")
        self.assertEqual(str(result.exception), "BLOCKED: invalid native SDK credential plan")

    def test_both_pairs_and_explicit_empty_offline_directory(self):
        for platform in ("android", "ios"):
            value = registry(platform)
            self.assertEqual(parsed(value), value)
            value.update(people=[], automation={}, team_public_identities=[])
            self.assertEqual(parsed(value), value)

    def test_strict_json_bytes_duplicates_nonfinite_and_depth(self):
        cases = [b'\xff', b'{"schema":1,"schema":2}', b'{"x":{"id":1,"id":2}}',
                 b'NaN', b'Infinity', b'-Infinity', b'1e999', b'1.0',
                 b'[' * 17 + b'0' + b']' * 17, b'{', b'{} trailing', b'\xef\xbb\xbf{}']
        for raw in cases:
            with self.subTest(raw=raw), self.assertRaises(CredentialError):
                strict_json(raw, 256 * 1024)
        self.assertEqual(strict_json(b'[' * 16 + b'0' + b']' * 16, 1024),
                         json.loads('[' * 16 + '0' + ']' * 16))
        self.assertEqual(strict_json(b'{"x":"[[[\\\""}', 1024), {"x": '[[["'})

    def test_duplicate_fields_in_otherwise_valid_registry(self):
        value = registry()
        raw = json.dumps(value).encode()
        duplicate = b'{"schema":"native-sdk-identities/v1",' + raw[1:]
        with self.assertRaises(CredentialError):
            parse_registry(duplicate, value["repository"], value["public_repository"])

    def test_installed_registry_is_empty_and_matches_this_sdk(self):
        root = Path(__file__).resolve().parents[2]
        platform = "ios" if (root / "Package.swift").exists() else "android"
        value = parse_registry((root / ".github/native-sdk-credential-identities.json").read_bytes(),
                               "melisource/fury_openplatform-sdk-" + platform,
                               "mercadopago/sdk-" + platform)
        self.assertEqual((value["people"], value["automation"], value["team_public_identities"]),
                         ([], {}, []))

    def test_size_exact_boundary_and_byte_input(self):
        value = registry()
        raw = json.dumps(value).encode()
        private, public = value["repository"], value["public_repository"]
        padded = raw + b' ' * (256 * 1024 - len(raw))
        self.assertEqual(parse_registry(padded, private, public), value)
        for bad in (padded + b' ', raw.decode(), None, bytearray(raw)):
            with self.assertRaises(CredentialError):
                parse_registry(bad, private, public)

    def test_closed_nested_schemas(self):
        for path in ((), ("people", 0), ("people", 0, "internal"),
                     ("people", 0, "bindings", "reviewer_public"),
                     ("automation", "public_reader"), ("automation", "internal_importer"),
                     ("team_public_identities", 0)):
            value = registry()
            node = value
            for key in path:
                node = node[key]
            node["unexpected"] = "not-a-secret"
            self.blocked(value)
        value = registry()
        del value["team_public_identities"]
        self.blocked(value)

    def test_repo_pair_and_explicit_expected_repositories(self):
        value = registry()
        private, public = value["repository"], value["public_repository"]
        for expected in ((private, "mercadopago/sdk-ios"), ("elsewhere/sdk", public),
                         (private.upper(), public), (None, public)):
            with self.assertRaises(CredentialError):
                parse_registry(json.dumps(value).encode(), *expected)
        value["public_repository"] = "mercadopago/sdk-ios"
        self.blocked(value)

    def test_positive_bounded_ids_and_github_logins(self):
        for number in (True, False, 0, -1, 1.0, "101", None, 2**63):
            value = registry()
            value["people"][0]["internal"]["id"] = number
            self.blocked(value)
        for login in ("-name", "name-", "two--hyphens", "a" * 40, "a_b", "ghp_abc",
                      "https://github.com/a", "${{vars.X}}", "x\ny", "$(id)", "é"):
            value = registry()
            value["people"][0]["internal"]["login"] = login
            self.blocked(value)

    def test_identity_uniqueness_and_case_insensitive_collisions(self):
        for side, field, bad in (("public", "id", 101), ("public", "login", "INTERNAL-HUMAN"),
                                 ("internal", "id", 303), ("internal", "login", "SDK-READER")):
            value = registry()
            value["people"][0][side][field] = bad
            self.blocked(value)
        value = registry()
        value["people"].append(copy.deepcopy(value["people"][0]))
        self.blocked(value)

    def test_directory_completeness_and_uniqueness(self):
        for index in (0, 1):
            value = registry()
            value["team_public_identities"].pop(index)
            self.blocked(value)
        for member in ({"id": 202, "login": "other"}, {"id": 999, "login": "PUBLIC-HUMAN"},
                       {"id": True, "login": "other"}):
            value = registry()
            value["team_public_identities"].append(member)
            self.blocked(value)
        value = registry()
        value["team_public_identities"].append({"id": 999, "login": "unmigrated"})
        self.assertEqual(len(parsed(value)["people"]), 1)

    def test_collection_bounds_and_types(self):
        for key in ("people", "team_public_identities"):
            for bad in ({}, None, [registry()[key][0]] * 101):
                value = registry()
                value[key] = bad
                self.blocked(value)
        value = registry()
        value["automation"] = []
        self.blocked(value)

    def test_role_resource_owner_linkage(self):
        for role in ("publisher_public", "reviewer_public", "reviewer_internal"):
            value = registry()
            item = value["people"][0]["bindings"][role]
            item["resource_owner"] = "other-org"
            self.blocked(value)
        value = registry()
        value["automation"]["reviewer_public"] = value["automation"]["public_reader"]
        self.blocked(value)

    def test_binding_and_reference_collisions(self):
        for fields in (("credential_id",), ("mode_variable", "active_variable")):
            value = registry()
            human = value["people"][0]["bindings"]
            for field in fields:
                human["reviewer_public"][field] = human["publisher_public"][field]
            self.blocked(value)
        for bad in ("NATIVE_SDK_X_ACTIVE", "NATIVE_SDK_X\n_MODE", "NATIVE_SDK_${X}_MODE",
                    "OTHER_X_MODE", "NATIVE_SDK_" + "A" * 120 + "_MODE"):
            value = registry()
            value["people"][0]["bindings"]["reviewer_public"]["mode_variable"] = bad
            self.blocked(value)

    def test_reserved_administrative_bases(self):
        for base in RESERVED:
            value = registry()
            item = value["people"][0]["bindings"]["reviewer_public"]
            item.update(mode_variable=base + "_MODE", active_variable=base + "_ACTIVE")
            self.blocked(value)

    def test_platform_only_internal_importer_and_no_human_technical_overlap(self):
        value = registry()
        value["automation"]["public_reader"] = {"kind": "github_token"}
        self.blocked(value)
        value = registry()
        value["people"][0]["bindings"]["reviewer_public"]["kind"] = "pat"
        self.blocked(value)
        value = registry()
        value["automation"]["public_reader"]["owner"] = value["people"][0]["public"]
        self.blocked(value)

    def test_static_references_and_credential_identifier_bounds(self):
        for key, bad in (("active_variable", "NATIVE_SDK_OTHER_ACTIVE"),
                         ("credential_id", "automation-internal-platform"),
                         ("credential_id", "https://x"), ("credential_id", "x" * 129),
                         ("credential_id", "$(id)"), ("credential_id", "ghp_abc")):
            value = registry()
            value["people"][0]["bindings"]["reviewer_public"][key] = bad
            self.blocked(value)


if __name__ == "__main__":
    unittest.main()
