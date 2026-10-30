#!/usr/bin/env python3
"""Security regression tests for the privileged mirror workflow."""

from pathlib import Path
import unittest


WORKFLOW = Path(__file__).parents[1] / "workflows" / "mirror-pr.yml"


class MirrorWorkflowSecurityTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.workflow = WORKFLOW.read_text(encoding="utf-8")

    def test_privileged_jobs_run_from_the_trusted_event_context(self):
        self.assertIn("\n  pull_request_target:\n", self.workflow)
        self.assertNotIn("\n  pull_request:\n", self.workflow)
        self.assertEqual(
            self.workflow.count("github.event_name == 'pull_request_target'"), 3
        )

    def test_pr_checkout_never_persists_the_repository_token(self):
        self.assertEqual(self.workflow.count("persist-credentials: false"), 3)

    def test_publisher_and_ignore_policy_come_from_the_default_branch(self):
        trusted_publisher = (
            'git show "${TRUSTED_REF}:.github/scripts/publish_signed_mirror.py"'
        )
        trusted_ignore = 'git show "${TRUSTED_REF}:.mirrorignore"'
        self.assertEqual(self.workflow.count(trusted_publisher), 3)
        self.assertEqual(self.workflow.count(trusted_ignore), 3)
        self.assertEqual(self.workflow.count('done < "$MIRRORIGNORE"'), 3)
        self.assertNotIn(
            "cp .github/scripts/publish_signed_mirror.py", self.workflow
        )

    def test_pr_controlled_text_is_never_evaluated_as_shell_code(self):
        self.assertNotRegex(self.workflow, r"(?m)^\s*eval\s")
        self.assertNotIn("EXCLUDE_ARGS=", self.workflow)
        self.assertEqual(self.workflow.count("EXCLUDE_PATHSPECS=()"), 3)


if __name__ == "__main__":
    unittest.main(verbosity=2)
