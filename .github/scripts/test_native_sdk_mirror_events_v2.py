"""Synthetic offline contracts; no live identity, approval or API evidence."""

import copy
import importlib.util
import json
import os
from pathlib import Path
import subprocess
import sys
import unittest
from unittest.mock import patch

SCRIPT = Path(__file__).with_name('native_sdk_mirror_events_v2.py')
SPEC = importlib.util.spec_from_file_location('standalone_events', SCRIPT)
events = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(events)
PRIVATE = 'melisource/fury_openplatform-sdk-android'
DEFAULT = object()


class NormalizationTests(unittest.TestCase):
    def setUp(self):
        self.inputs = {'execution_scope': 'preview', 'fixture_id': 'offline-v2'}
        self.manifest = events.offline_manifest(PRIVATE)

    def reject(self, inputs=DEFAULT, family='pr', manifest=DEFAULT):
        with self.assertRaisesRegex(events.ConfigInvalid, '^CONFIG_INVALID$'):
            events.normalize_dispatch(self.inputs if inputs is DEFAULT else inputs,
                                      family, self.manifest if manifest is DEFAULT else manifest)

    def test_all_operations_scopes_and_sdk_pairs(self):
        seen = set()
        for repo in events.PAIRS:
            manifest = events.offline_manifest(repo)
            for scope in ('preview', 'fixture'):
                for family, operations in events.FAMILIES.items():
                    plan = events.normalize_dispatch(dict(self.inputs, execution_scope=scope),
                                                     family, manifest)
                    self.assertEqual([e['operation'] for e in plan], list(operations))
                    for event in plan:
                        self.assertEqual(set(event), events.EVENT_KEYS)
                        self.assertEqual(event['schema'], 'native-sdk-mirror-event/v2')
                        self.assertEqual(event['execution_scope'], scope)
                        self.assertEqual(event['fixture_id'], 'offline-v2')
                        seen.add(event['operation'])
        self.assertEqual(len(seen), 10)

    def test_inputs_are_closed_and_canonical(self):
        for key, values in {
            'execution_scope': ('production', 'reconcile', 'Preview', 'preview ', None, True, []),
            'fixture_id': ('OFFLINE-v2', 'offline-v2 ', '../x', '', 'x' * 65, None, True, [], 'alias'),
        }.items():
            for value in values:
                with self.subTest(key=key, value=value):
                    self.reject(dict(self.inputs, **{key: value}))
        for value in ({}, [], dict(self.inputs, body={}), {'fixture_id': 'offline-v2'}):
            self.reject(value)
        for family in ('PR', '', None, [], True, 'publisher_open'):
            self.reject(family=family)

    def test_fixture_id_boundaries_and_single_dictionary(self):
        for fixture_id in ('a', 'a' * 64, '0_abc-123'):
            event = dict(self.manifest['offline-v2'][0], fixture_id=fixture_id)
            result = events.normalize_dispatch(dict(self.inputs, fixture_id=fixture_id),
                                               'pr', {fixture_id: event})
            self.assertEqual(result[0]['fixture_id'], fixture_id)

    def test_manifest_shape_and_unselected_entries(self):
        for value in (None, [], {}, {'offline-v2': []}, {'offline-v2': ()},
                      {'offline-v2': [None]}, {'OFFLINE-v2': self.manifest['offline-v2']}):
            self.reject(manifest=value)
        self.manifest['other'] = dict(self.manifest['offline-v2'][0], fixture_id='other', body={})
        self.reject()

    def test_event_types_schema_and_extra_fields(self):
        invalid = {
            'schema': ('native-sdk-mirror-event/v1', None, []),
            'operation': ('approve', 'production', None, []),
            'execution_scope': ('preview', 'production', None),
            'source_repository': ('mercadopago/sdk-ANDROID', 'evil/sdk-ios', None, []),
            'source_pr': (None, True, False, 0, -1, '1', 1.0, 2**63),
            'actor_id': (None, True, 0, -1, '1', 1.0, 2**63),
            'source_head_sha': ('a' * 39, 'g' * 40, 'a' * 41, None, True),
            'source_review_id': (1, True, '1', 0),
            'fixture_id': (None, 'other', True),
        }
        for key, values in invalid.items():
            for value in values:
                with self.subTest(key=key, value=value):
                    manifest = copy.deepcopy(self.manifest)
                    manifest['offline-v2'][0][key] = value
                    self.reject(manifest=manifest)
        for key in events.EVENT_KEYS:
            manifest = copy.deepcopy(self.manifest)
            del manifest['offline-v2'][0][key]
            self.reject(manifest=manifest)
        manifest = copy.deepcopy(self.manifest)
        manifest['offline-v2'][0]['body'] = 'ignored?'
        self.reject(manifest=manifest)

    def test_review_ids_only_on_review_operations(self):
        for operation in events.REVIEWS:
            event = next(e for e in self.manifest['offline-v2'] if e['operation'] == operation)
            for value in (None, True, 0, -2, '2', 2.0, 2**63):
                self.reject(manifest={'offline-v2': dict(event, source_review_id=value)})

    def test_human_operation_directions_and_id_upper_bound(self):
        for event in self.manifest['offline-v2']:
            op = event['operation']
            if op.startswith('publisher_') or op in events.REVIEWS:
                expected = events.PAIRS[PRIVATE] if op == 'reviewer_internal' else PRIVATE
                self.assertEqual(event['source_repository'], expected)
                wrong = PRIVATE if expected != PRIVATE else events.PAIRS[PRIVATE]
                self.reject(manifest={'offline-v2': dict(event, source_repository=wrong)})
            maximum = dict(event, source_pr=2**63 - 1, actor_id=2**63 - 1)
            if op in events.REVIEWS:
                maximum['source_review_id'] = 2**63 - 1
            family = next(f for f, ops in events.FAMILIES.items() if op in ops)
            self.assertTrue(events.normalize_dispatch(self.inputs, family, {'offline-v2': maximum}))

    def test_mixed_sdk_pair_rejected_even_across_families(self):
        self.manifest['offline-v2'][-1]['source_repository'] = events.PAIRS[
            'melisource/fury_openplatform-sdk-ios']
        self.reject()

    def test_cross_family_collisions_ignore_operation_head_actor_and_scope(self):
        for scope in ('preview', 'fixture'):
            self.inputs['execution_scope'] = scope
            for operation in ('internal_ci_comment', 'internal_finalize', 'publisher_sync'):
                event = dict(self.manifest['offline-v2'][0], fixture_id='other',
                             operation=operation, actor_id=77, source_head_sha='a' * 40)
                self.reject(manifest=dict(self.manifest, other=event))
        self.manifest['other'] = dict(self.manifest['offline-v2'][0],
                                      fixture_id='other', source_pr=123)
        self.assertTrue(events.normalize_dispatch(self.inputs, 'pr', self.manifest))

    def test_no_matching_family_rejected(self):
        self.reject(family='ci', manifest={'offline-v2': self.manifest['offline-v2'][0]})

    def test_purity_and_fresh_output(self):
        before = copy.deepcopy((self.inputs, self.manifest))
        with patch.dict(os.environ, {'SENTINEL': 'unchanged'}, clear=True):
            environment = dict(os.environ)
            result = events.normalize_dispatch(self.inputs, 'pr', self.manifest)
            result[0]['source_pr'] = 22
            self.assertEqual(dict(os.environ), environment)
        self.assertEqual((self.inputs, self.manifest), before)


class CliTests(unittest.TestCase):
    def run_cli(self, args, **changes):
        env = {'PATH': os.environ.get('PATH', ''), 'EXECUTION_SCOPE': 'preview',
               'FIXTURE_ID': 'offline-v2', 'GITHUB_REPOSITORY': PRIVATE}
        env.update(changes)
        return subprocess.run([sys.executable, '-I', '-B', str(SCRIPT), *args],
                              env=env, text=True, capture_output=True, check=False)

    def test_success_is_only_an_offline_plan(self):
        for repository in events.PAIRS:
            for scope in ('preview', 'fixture'):
                for family in events.FAMILIES:
                    result = self.run_cli(['--family', family], GITHUB_REPOSITORY=repository,
                                          EXECUTION_SCOPE=scope)
                    self.assertEqual(result.returncode, 0, result.stderr)
                    output = json.loads(result.stdout)
                    self.assertEqual(set(output), {'schema', 'mode', 'credential_id', 'state',
                                                  'overall', 'reason_code', 'results',
                                                  'next_action', 'journal_path'})
                    self.assertEqual(output['schema'], 'native-sdk-mirror-validation/v1')
                    self.assertIsNone(output['reason_code'])
                    self.assertEqual(output['overall'], 'PASS')
                    checks = {entry['check']: entry for entry in output['results']}
                    self.assertTrue(checks['dispatch_metadata']['synthetic'])
                    self.assertEqual(checks['real_identity']['state'], 'UNVERIFIED')
                    self.assertEqual(checks['api_compatibility']['state'], 'UNVERIFIED')
                    self.assertEqual(checks['fixture_writes']['state'], 'BLOCKED')
                    self.assertEqual([c['check'] for c in output['results'] if c['state'] == 'PASS'],
                                     ['dispatch_metadata'])
                    self.assertEqual(result.stderr, '')

    def test_invalid_arguments_and_environment_are_sanitized(self):
        token = 'github_pat_FAKE_DO_NOT_ECHO'
        cases = [([], {}), (['--family'], {}), (['--family', token], {}),
                 (['--help', token], {}), (['--family=pr'], {}),
                 (['--family', 'pr', '--source-pr', token], {})]
        for name in ('FIXTURE_ID', 'EXECUTION_SCOPE', 'GITHUB_REPOSITORY'):
            cases.append((['--family', 'pr'], {name: token}))
            cases.append((['--family', 'pr'], {name: ''}))
        for args, env in cases:
            result = self.run_cli(args, **env)
            self.assertEqual(result.returncode, 2)
            self.assertEqual(json.loads(result.stdout), events._envelope(False))
            self.assertNotIn(token, result.stdout + result.stderr)
            self.assertEqual(result.stderr, '')

    def test_help_safe_without_configuration(self):
        result = self.run_cli(['--help'], GITHUB_REPOSITORY='github_pat_FAKE')
        self.assertEqual(result.returncode, 0)
        self.assertIn('Offline manual validation only', result.stdout)
        self.assertIn('UNVERIFIED', result.stdout)
        self.assertNotIn('github_pat_FAKE', result.stdout + result.stderr)


if __name__ == '__main__':
    unittest.main()
