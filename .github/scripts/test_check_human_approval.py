#!/usr/bin/env python3
"""Tests for the human approval merge gate."""

import unittest

from check_human_approval import current_human_approvers, is_human


HEAD_SHA = "a" * 40
OLD_SHA = "b" * 40


def review(login, user_type, state, commit_id=HEAD_SHA):
    return {
        "user": {"login": login, "type": user_type},
        "state": state,
        "commit_id": commit_id,
    }


class HumanIdentityTests(unittest.TestCase):
    def test_accepts_github_user(self):
        self.assertTrue(is_human({"login": "reviewer_meli", "type": "User"}))

    def test_rejects_bot_type(self):
        self.assertFalse(is_human({"login": "reviewer[bot]", "type": "Bot"}))

    def test_rejects_bot_suffix_even_if_type_is_user(self):
        self.assertFalse(is_human({"login": "reviewer[bot]", "type": "User"}))

    def test_rejects_unknown_identity_type(self):
        self.assertFalse(is_human({"login": "reviewer_meli"}))


class CurrentHumanApproverTests(unittest.TestCase):
    def test_requires_at_least_one_review(self):
        self.assertEqual(current_human_approvers([], HEAD_SHA), [])

    def test_bot_approval_does_not_count(self):
        reviews = [review("meli-agent[bot]", "Bot", "APPROVED")]
        self.assertEqual(current_human_approvers(reviews, HEAD_SHA), [])

    def test_current_human_approval_counts(self):
        reviews = [review("reviewer_meli", "User", "APPROVED")]
        self.assertEqual(current_human_approvers(reviews, HEAD_SHA), ["reviewer_meli"])

    def test_stale_human_approval_does_not_count(self):
        reviews = [review("reviewer_meli", "User", "APPROVED", OLD_SHA)]
        self.assertEqual(current_human_approvers(reviews, HEAD_SHA), [])

    def test_later_changes_requested_supersedes_approval(self):
        reviews = [
            review("reviewer_meli", "User", "APPROVED"),
            review("reviewer_meli", "User", "CHANGES_REQUESTED"),
        ]
        self.assertEqual(current_human_approvers(reviews, HEAD_SHA), [])

    def test_comment_does_not_supersede_approval(self):
        reviews = [
            review("reviewer_meli", "User", "APPROVED"),
            review("reviewer_meli", "User", "COMMENTED"),
        ]
        self.assertEqual(current_human_approvers(reviews, HEAD_SHA), ["reviewer_meli"])

    def test_dismissed_approval_does_not_count(self):
        reviews = [review("reviewer_meli", "User", "DISMISSED")]
        self.assertEqual(current_human_approvers(reviews, HEAD_SHA), [])

    def test_bot_review_does_not_supersede_human_approval(self):
        reviews = [
            review("reviewer_meli", "User", "APPROVED"),
            review("meli-agent[bot]", "Bot", "APPROVED"),
        ]
        self.assertEqual(current_human_approvers(reviews, HEAD_SHA), ["reviewer_meli"])

    def test_rejects_non_array_payload(self):
        with self.assertRaises(ValueError):
            current_human_approvers({}, HEAD_SHA)


if __name__ == "__main__":
    unittest.main()
