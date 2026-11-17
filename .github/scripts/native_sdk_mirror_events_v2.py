"""Standalone offline dispatch normalization; Python 3.9+ standard library.

Manifest shape: {canonical_fixture_id: canonical_event_or_nonempty_list}.
Events have exactly EVENT_KEYS, execution_scope='fixture', and matching fixture_id.
Each fixture belongs to one SDK pair; operations determine their family. All
manifest entries are validated, including unselected families. Distinct IDs may
not share a (source_repository, source_pr), regardless of actor, head or family.
These source resource identities are a conservative offline collision boundary;
future real fixtures must also prove destination isolation before any writes.

The built-in offline-v2 corpus is synthetic, with invented IDs/heads. It is no
live approval or API proof. Raw PR payload adapters, live resource rereads and
actual fixture writes are BLOCKED future increments, outside this module.
CLI uses the E9 native-sdk-mirror-validation/v1 envelope. PASS is restricted
to dispatch metadata; results never certify an operation or live approval.
"""

import json
import os
import re
import sys

SCHEMA = 'native-sdk-mirror-event/v2'
EVENT_KEYS = frozenset(('schema', 'operation', 'execution_scope',
                        'source_repository', 'source_pr', 'source_head_sha',
                        'source_review_id', 'actor_id', 'fixture_id'))
FAMILIES = {
    'pr': ('publisher_open', 'publisher_sync', 'reviewer_public', 'publisher_merge'),
    'ci': ('public_ci_read', 'internal_ci_comment'),
    'reverse': ('public_import_read', 'internal_import', 'reviewer_internal',
                'internal_finalize'),
}
PAIRS = {f'melisource/fury_openplatform-sdk-{sdk}': f'mercadopago/sdk-{sdk}'
         for sdk in ('android', 'ios')}
REPOSITORIES = frozenset(PAIRS) | frozenset(PAIRS.values())
REVIEWS = frozenset(('reviewer_public', 'reviewer_internal'))


class ConfigInvalid(ValueError):
    """Fixed public error, never including supplied input."""

    def __init__(self):
        super().__init__('CONFIG_INVALID')


def _require(condition):
    if not condition:
        raise ConfigInvalid()


def _canonical_id(value):
    return type(value) is str and re.fullmatch(r'[a-z0-9][a-z0-9_-]{0,63}', value)


def _positive(value):
    return type(value) is int and 0 < value <= 2**63 - 1


def _event(event, fixture_id):
    _require(type(event) is dict and event.keys() == EVENT_KEYS)
    _require(event['schema'] == SCHEMA and event['execution_scope'] == 'fixture')
    _require(type(event['operation']) is str)
    _require(event['operation'] in {op for ops in FAMILIES.values() for op in ops})
    _require(event['fixture_id'] == fixture_id)
    repo = event['source_repository']
    _require(type(repo) is str and repo in REPOSITORIES)
    if event['operation'].startswith('publisher_') or event['operation'] == 'reviewer_public':
        _require(repo in PAIRS)
    if event['operation'] == 'reviewer_internal':
        _require(repo in PAIRS.values())
    _require(_positive(event['source_pr']) and _positive(event['actor_id']))
    sha = event['source_head_sha']
    _require(type(sha) is str and re.fullmatch(r'[0-9a-fA-F]{40}', sha) is not None)
    review = event['source_review_id']
    _require(_positive(review) if event['operation'] in REVIEWS else review is None)
    return repo if repo in PAIRS else next(p for p, q in PAIRS.items() if q == repo)


def normalize_dispatch(inputs, family, fixtures):
    """Return fresh event dictionaries for one family, without I/O or mutation.

    Inputs are exactly execution_scope and fixture_id. No identity, resource,
    operation or source payload can be selected outside the trusted manifest.
    The result is always a nonempty list, including for a single-event entry.
    """
    _require(type(inputs) is dict and inputs.keys() == {'execution_scope', 'fixture_id'})
    _require(type(family) is str and family in FAMILIES)
    scope, selected = inputs['execution_scope'], inputs['fixture_id']
    _require(type(scope) is str and scope in ('preview', 'fixture'))
    _require(bool(_canonical_id(selected)))
    _require(type(fixtures) is dict and 0 < len(fixtures) <= 100)
    owners, plan = {}, []
    for fixture_id, value in fixtures.items():
        _require(bool(_canonical_id(fixture_id)))
        events = [value] if type(value) is dict else value
        _require(type(events) is list and 0 < len(events) <= 100)
        pair = None
        for event in events:
            event_pair = _event(event, fixture_id)
            _require(pair is None or pair == event_pair)
            pair = event_pair
            identity = (event['source_repository'], event['source_pr'])
            _require(identity not in owners or owners[identity] == fixture_id)
            owners[identity] = fixture_id
            if fixture_id == selected and event['operation'] in FAMILIES[family]:
                plan.append(dict(event, execution_scope=scope))
    _require(bool(plan))
    return plan


def offline_manifest(repository):
    """Build synthetic data for the dispatch repository's SDK pair only."""
    _require(type(repository) is str and repository in PAIRS)
    public = PAIRS[repository]
    events = []
    for family, operations in FAMILIES.items():
        for operation in operations:
            source = public if operation in ('public_ci_read', 'public_import_read',
                                                   'reviewer_internal') else repository
            events.append(dict(schema=SCHEMA, operation=operation,
                               execution_scope='fixture', source_repository=source,
                               source_pr=900000001, source_head_sha='0' * 40,
                               source_review_id=900000002 if operation in REVIEWS else None,
                               actor_id=900000003, fixture_id='offline-v2'))
    return {'offline-v2': events}


def _envelope(valid, plan=None):
    # E9 PASS describes dispatch metadata only, never an operation or live proof.
    return {'schema': 'native-sdk-mirror-validation/v1', 'mode': 'offline',
            'credential_id': None, 'state': 'VALIDATED' if valid else 'BLOCKED',
            'overall': 'PASS' if valid else 'BLOCKED',
            'reason_code': None if valid else 'CONFIG_INVALID',
            'results': ([{'check': 'dispatch_metadata', 'state': 'PASS',
                          'synthetic': True, 'plan': plan},
                         {'check': 'real_identity', 'state': 'UNVERIFIED'},
                         {'check': 'api_compatibility', 'state': 'UNVERIFIED'},
                         {'check': 'fixture_writes', 'state': 'BLOCKED'}] if valid else []),
            'next_action': 'IMPLEMENT_REVIEWED_LIVE_ADAPTERS' if valid else 'CHECK_CONFIGURATION',
            'journal_path': None}


def main(argv=None):
    """Run a fixed offline plan; errors are sanitized and return exit status 2."""
    args = sys.argv[1:] if argv is None else argv
    if args == ['--help']:
        print('Offline manual validation only. Usage: --family pr|ci|reverse. '
              'Inputs: EXECUTION_SCOPE, FIXTURE_ID, GITHUB_REPOSITORY. '
              'Synthetic offline-v2 only; real writes BLOCKED; identity/API UNVERIFIED.')
        return 0
    try:
        _require(len(args) == 2 and args[0] == '--family')
        family = args[1]
        inputs = {'execution_scope': os.environ.get('EXECUTION_SCOPE'),
                  'fixture_id': os.environ.get('FIXTURE_ID')}
        repository = os.environ.get('GITHUB_REPOSITORY')
        plan = normalize_dispatch(inputs, family, offline_manifest(repository))
    except ConfigInvalid:
        print(json.dumps(_envelope(False), sort_keys=True))
        return 2
    print(json.dumps(_envelope(True, plan), sort_keys=True))
    return 0


if __name__ == '__main__':
    sys.exit(main())
