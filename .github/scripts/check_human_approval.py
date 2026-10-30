#!/usr/bin/env python3
"""Require a current approval from a human GitHub user."""

from __future__ import annotations

import argparse
import json
import sys
from typing import Any


DECISIVE_STATES = {"APPROVED", "CHANGES_REQUESTED", "DISMISSED"}


def is_human(user: Any) -> bool:
    if not isinstance(user, dict):
        return False

    login = user.get("login")
    user_type = user.get("type")
    return (
        isinstance(login, str)
        and bool(login)
        and isinstance(user_type, str)
        and user_type.casefold() == "user"
        and not login.casefold().endswith("[bot]")
    )


def current_human_approvers(reviews: Any, head_sha: str) -> list[str]:
    if not isinstance(reviews, list):
        raise ValueError("GitHub reviews payload must be a JSON array")
    if not head_sha:
        raise ValueError("head SHA must not be empty")

    latest_decisive_review: dict[str, tuple[str, str | None, str]] = {}
    for review in reviews:
        if not isinstance(review, dict) or not is_human(review.get("user")):
            continue

        state = str(review.get("state", "")).upper()
        if state not in DECISIVE_STATES:
            continue

        login = review["user"]["login"]
        latest_decisive_review[login.casefold()] = (
            state,
            review.get("commit_id"),
            login,
        )

    return sorted(
        display_login
        for state, commit_id, display_login in latest_decisive_review.values()
        if state == "APPROVED" and commit_id == head_sha
    )


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--head-sha", required=True)
    args = parser.parse_args()

    try:
        reviews = json.load(sys.stdin)
        approvers = current_human_approvers(reviews, args.head_sha)
    except (json.JSONDecodeError, ValueError) as error:
        print(f"::error::Could not validate approvals: {error}")
        return 1

    if not approvers:
        print(
            "::error::At least one human must approve the current commit. "
            "Bot approvals do not count."
        )
        return 1

    print("Current human approval: " + ", ".join(f"@{name}" for name in approvers))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
