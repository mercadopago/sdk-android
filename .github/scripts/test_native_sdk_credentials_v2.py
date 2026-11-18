"""Offline resolver behavior and the shared dispatch/resolver boundary."""

import copy
import json
import os
import socket
import subprocess
import unittest
from unittest import mock

import native_sdk_credentials_v2 as resolver
from native_sdk_credentials_v2 import CredentialError, identify_binding, resolve_binding
from native_sdk_registry_v2 import PAIRS, reference_base
from native_sdk_mirror_events_v2 import (
    PAIRS as EVENT_PAIRS, FAMILIES, ConfigInvalid, normalize_dispatch, offline_manifest, validate_event,
)
from test_native_sdk_registry_v2 import binding, parsed, registry


COMMIT = "a" * 40


def event(operation="reviewer_public", platform="android"):
    private = "melisource/fury_openplatform-sdk-" + platform
    public = "mercadopago/sdk-" + platform
    reverse = operation == "reviewer_internal"
    return dict(schema="native-sdk-mirror-event/v2", operation=operation,
                execution_scope="preview", source_repository=public if operation in (
                    "reviewer_internal", "public_ci_read", "public_import_read") else private,
                source_pr=7, source_head_sha="b" * 40,
                source_review_id=9 if operation.startswith("reviewer_") else None,
                actor_id=202 if reverse else 101, fixture_id="offline-v2")


def selected(config, source):
    operation = source["operation"]
    result = identify_binding(config, source, operation)
    role = {"publisher_open": "publisher_public", "publisher_sync": "publisher_public",
            "publisher_merge": "publisher_public"}.get(operation, operation)
    person = config["people"][0]
    value = person["bindings"][role]
    base = reference_base(value)
    attestation = dict(schema="native-sdk-identity-binding/v1", credential_id=value["credential_id"],
                       internal=person["internal"], public=person["public"], role=role,
                       reference_base=base)
    return dict(binding_fingerprint=result["binding_fingerprint"], MODE_VALUE="fine_grained",
                ACTIVE_VALUE=base + "_R" + "A" * 32 + "_V1", IDENTITY_VALUE=json.dumps(attestation))


class ResolverTests(unittest.TestCase):
    def resolve(self, config, source, variables, enabled="true", commit=COMMIT):
        return resolve_binding(config, source, source["operation"], enabled, variables, commit)

    def test_human_operations_both_pairs_preserve_counterpart(self):
        for platform in ("android", "ios"):
            for operation in ("publisher_open", "publisher_sync", "publisher_merge",
                              "reviewer_public", "reviewer_internal"):
                with self.subTest(platform=platform, operation=operation):
                    value, source = registry(platform), event(operation, platform)
                    value["people"][0]["internal"]["login"] += "_meli"
                    config = parsed(value)
                    variables = selected(config, source)
                    before = copy.deepcopy((config, source, variables))
                    result = self.resolve(config, source, variables)
                    reverse = operation == "reviewer_internal"
                    self.assertEqual(result["schema"], "native-sdk-resolution/v1")
                    self.assertEqual(result["mode"], "fine_grained")
                    self.assertEqual(result["expected_id"], 101 if reverse else 202)
                    self.assertEqual(result["repository"], config["repository" if reverse else "public_repository"])
                    self.assertEqual(result["secret_name"], variables["ACTIVE_VALUE"])
                    self.assertEqual(result["registry_commit"], COMMIT)
                    self.assertEqual((config, source, variables), before)

    def test_disabled_flag_never_touches_registry_or_selected_variables(self):
        class Poison:
            def __getattribute__(self, name):
                raise AssertionError("credential configuration was accessed")
        for flag in (None, "false"):
            result = self.resolve(Poison(), event(), Poison(), flag, Poison())
            self.assertEqual(result["mode"], "legacy")
            for key in ("credential_id", "secret_name", "expected_login", "expected_id", "registry_commit"):
                self.assertIsNone(result[key])
        for flag in (True, False, 1, "TRUE", "", "false "):
            with self.assertRaises(CredentialError):
                self.resolve(None, event(), None, flag)

    def test_platform_comment_is_independent_of_all_pat_configuration(self):
        result = self.resolve(object(), event("internal_ci_comment"), object(), object(), object())
        self.assertEqual(result["mode"], "github_token")
        self.assertEqual(result["credential_id"], "automation-internal-platform")
        self.assertIsNone(result["registry_commit"])
        for key in ("secret_name", "expected_id", "expected_login"):
            self.assertIsNone(result[key])

    def test_technical_reader_and_platform_importer(self):
        config = parsed()
        for operation in ("public_ci_read", "public_import_read"):
            source = event(operation)
            identified = identify_binding(config, source, operation)
            self.assertIsNone(identified["identity_variable"])
            value = config["automation"]["public_reader"]
            variables = dict(binding_fingerprint=identified["binding_fingerprint"], MODE_VALUE="fine_grained",
                             ACTIVE_VALUE=reference_base(value) + "_R" + "C" * 32 + "_V2")
            result = self.resolve(config, source, variables)
            self.assertEqual(result["expected_id"], 303)
            self.assertEqual(result["mode"], "fine_grained")
        for operation in ("internal_import", "internal_finalize"):
            result = self.resolve(config, event(operation), object())
            self.assertEqual(result["mode"], "github_token")
            self.assertEqual(result["registry_commit"], COMMIT)
            self.assertIsNone(result["secret_name"])

    def test_technical_pat_importer_and_technical_review_rejection(self):
        config = registry()
        value = dict(binding("public_reader", 404), kind="pat", resource_owner="melisource",
                     owner={"login": "internal-bot", "id": 404})
        config["automation"]["internal_importer"] = value
        config = parsed(config)
        source = event("internal_import")
        names = identify_binding(config, source, source["operation"])
        result = self.resolve(config, source, dict(binding_fingerprint=names["binding_fingerprint"],
                              MODE_VALUE="fine_grained", ACTIVE_VALUE=reference_base(value) + "_R" + "D" * 32 + "_V1"))
        self.assertEqual((result["mode"], result["expected_id"]), ("fine_grained", 404))
        for number in (303, 404):
            with self.assertRaises(CredentialError):
                identify_binding(config, dict(event(), actor_id=number), "reviewer_public")

    def test_legacy_and_missing_bindings_do_not_consume_active_identity_values(self):
        config, source = parsed(), event()
        names = identify_binding(config, source, source["operation"])
        for mode in (None, "legacy"):
            result = self.resolve(config, source, dict(binding_fingerprint=names["binding_fingerprint"],
                                  MODE_VALUE=mode, ACTIVE_VALUE=object(), IDENTITY_VALUE=object()))
            self.assertEqual(result["mode"], "legacy")
            self.assertIsNone(result["secret_name"])
        del config["people"][0]["bindings"]["reviewer_public"]
        self.assertEqual(self.resolve(config, source, object())["mode"], "legacy")
        self.assertEqual(self.resolve(config, dict(source, actor_id=999), object())["mode"], "legacy")

    def test_all_human_operations_block_known_wrong_side_and_technical_actors(self):
        for platform in ("android", "ios"):
            config = registry(platform)
            config["automation"]["internal_importer"] = dict(
                binding("internal_importer", 404), kind="pat", resource_owner="melisource",
                owner={"login": "internal-bot_meli", "id": 404})
            config = parsed(config)
            for operation in ("publisher_open", "publisher_sync", "publisher_merge",
                              "reviewer_public", "reviewer_internal"):
                source = event(operation, platform)
                for actor in (101 if operation == "reviewer_internal" else 202, 303, 404):
                    bad = dict(source, actor_id=actor)
                    with self.subTest(platform=platform, operation=operation, actor=actor):
                        with self.assertRaises(CredentialError):
                            identify_binding(config, bad, operation)
                        with self.assertRaises(CredentialError):
                            self.resolve(config, bad, object())
                unknown = dict(source, actor_id=999)
                self.assertIsNone(identify_binding(config, unknown, operation)["mode_variable"])
                self.assertEqual(self.resolve(config, unknown, object())["mode"], "legacy")
            config["people"][0]["bindings"] = {}
            with self.assertRaises(CredentialError):
                self.resolve(config, dict(event(platform=platform), actor_id=202), object())

    def test_absent_mode_keeps_legacy_even_with_populated_active_and_identity(self):
        config, source = parsed(), event()
        variables = selected(config, source)
        variables["MODE_VALUE"] = None  # Repository MODE variable is absent.
        result = self.resolve(config, source, variables)
        self.assertEqual(result["mode"], "legacy")
        self.assertIsNone(result["secret_name"])

    def test_resolution_uses_one_snapshot_even_if_original_registry_changes(self):
        config, source = parsed(), event()
        variables = selected(config, source)
        original_check = resolver.checked_registry

        def mutate_after_snapshot(*args):
            snapshot = original_check(*args)
            config["people"][0]["public"]["id"] = 999
            return snapshot

        with mock.patch.object(resolver, "checked_registry", side_effect=mutate_after_snapshot) as check, \
             mock.patch.object(resolver, "_selection", wraps=resolver._selection) as selection:
            result = self.resolve(config, source, variables)
        self.assertEqual((result["mode"], result["expected_id"]), ("fine_grained", 202))
        check.assert_called_once_with(config, source["source_repository"], config["public_repository"])
        selection.assert_called_once()

    def test_migrated_identity_never_silently_falls_back(self):
        config, source = parsed(), event()
        good = selected(config, source)
        for key, bad in (("MODE_VALUE", "fine-grained"), ("MODE_VALUE", True),
                         ("ACTIVE_VALUE", None), ("ACTIVE_VALUE", "NATIVE_SDK_OTHER_R" + "A" * 32 + "_V1"),
                         ("ACTIVE_VALUE", good["ACTIVE_VALUE"].lower()),
                         ("binding_fingerprint", "0" * 64), ("IDENTITY_VALUE", "{}")):
            with self.subTest(key=key), self.assertRaises(CredentialError):
                self.resolve(config, source, dict(good, **{key: bad}))
        for key in good:
            bad = dict(good)
            del bad[key]
            with self.assertRaises(CredentialError):
                self.resolve(config, source, bad)
        with self.assertRaises(CredentialError):
            self.resolve(config, source, dict(good, USER_PAT_MAP="forbidden"))

    def test_attestation_and_snapshot_swaps_block(self):
        config, source = parsed(), event()
        good = selected(config, source)
        for side, key, value in (("internal", "id", 999), ("public", "id", True),
                                 ("public", "login", "someone-else")):
            attestation = json.loads(good["IDENTITY_VALUE"])
            attestation[side][key] = value
            with self.assertRaises(CredentialError):
                self.resolve(config, source, dict(good, IDENTITY_VALUE=json.dumps(attestation)))
        for key, value in (("role", "publisher_public"), ("credential_id", "other"),
                           ("reference_base", "NATIVE_SDK_OTHER"), ("extra", "x")):
            attestation = json.loads(good["IDENTITY_VALUE"])
            attestation[key] = value
            with self.assertRaises(CredentialError):
                self.resolve(config, source, dict(good, IDENTITY_VALUE=json.dumps(attestation)))
        with self.assertRaises(CredentialError):
            self.resolve(config, dict(source, source_head_sha="c" * 40), good)
        config["team_public_identities"].append({"login": "another", "id": 999})
        with self.assertRaises(CredentialError):
            self.resolve(config, source, good)

    def test_malformed_event_registry_and_commit_block_with_fixed_error(self):
        config, source = parsed(), event()
        for key, value in (("actor_id", True), ("source_pr", 0), ("source_review_id", None),
                           ("fixture_id", "UPPER"), ("fixture_id", "a" * 65),
                           ("source_repository", "mercadopago/sdk-android"), ("execution_scope", "production")):
            with self.assertRaises(CredentialError):
                self.resolve(config, dict(source, **{key: value}), {})
        for sha in (None, "main", "a" * 39, "g" * 40):
            with self.assertRaises(CredentialError):
                self.resolve(config, source, selected(config, source), commit=sha)
        config["people"][0]["bindings"]["reviewer_public"]["resource_owner"] = "melisource"
        with self.assertRaisesRegex(CredentialError, "^BLOCKED: invalid native SDK credential plan$"):
            self.resolve(config, source, {})

    def test_normalizer_and_resolver_agree_on_every_operation(self):
        self.assertEqual(EVENT_PAIRS, PAIRS)
        for platform in ("android", "ios"):
            private = "melisource/fury_openplatform-sdk-" + platform
            for family in FAMILIES:
                plan = normalize_dispatch(dict(execution_scope="preview", fixture_id="offline-v2"),
                                          family, offline_manifest(private), private)
                for source in plan:
                    result = self.resolve(None, source, None, enabled="false")
                    self.assertEqual(result["mode"], "github_token" if source["operation"] == "internal_ci_comment" else "legacy")

    def test_shared_event_validation_preserves_fixture_ids_and_fixed_resolver_error(self):
        for private in PAIRS:
            for source in offline_manifest(private)["offline-v2"]:
                for scope in ("preview", "fixture"):
                    source = dict(source, execution_scope=scope, fixture_id="ghs_synthetic")
                    self.assertEqual(validate_event(source), private)
                    self.resolve(None, source, None, enabled="false")
                    wrong = PAIRS[private] if source["source_repository"] == private else private
                    for bad in (dict(source, source_repository=wrong), dict(source, source_head_sha="A" * 40)):
                        with self.subTest(operation=source["operation"], scope=scope):
                            with self.assertRaises(ConfigInvalid):
                                validate_event(bad)
                            with self.assertRaisesRegex(CredentialError, "^BLOCKED: invalid native SDK credential plan$"):
                                self.resolve(None, bad, None, enabled="false")

    def test_pure_resolution_has_no_io_network_subprocess_or_environment_changes(self):
        config, source = parsed(), event()
        variables = selected(config, source)
        before = dict(os.environ)
        with mock.patch("builtins.open", side_effect=AssertionError("file I/O")), \
             mock.patch.object(socket, "socket", side_effect=AssertionError("network")), \
             mock.patch.object(subprocess, "run", side_effect=AssertionError("subprocess")):
            self.assertEqual(self.resolve(config, source, variables)["mode"], "fine_grained")
        self.assertEqual(dict(os.environ), before)


if __name__ == "__main__":
    unittest.main()
