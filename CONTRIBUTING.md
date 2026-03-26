# Contributing to Mercado Pago SDK Android

Thank you for contributing to **Mercado Pago SDK Android**. This guide explains how to report issues, propose changes, and open Pull Requests (PRs) with quality and safety.

## Code of Conduct

By participating in this project, you agree to keep a respectful and collaborative environment.

---

## How can I contribute?

### 1) Report bugs

Before opening an issue:
- Check whether a similar issue already exists (open or closed).
- Provide as much context as possible:
  - SDK version
  - Android version / device
  - Steps to reproduce
  - Expected result vs. actual result
  - Relevant logs (e.g., Logcat) and/or screenshots

### 2) Suggest enhancements

Suggestions are welcome. Please explain:
- What problem you are solving
- Your proposed solution
- Expected impact (API, compatibility, performance)

### 3) Submit code (Pull Request)

Changes via PR are the best way to contribute. Before writing code, read the [Coding Guidelines](CODING_GUIDELINES.md) — they cover language, style, architecture, security, testing, and commit conventions for this project.

---

## Requirements before you start

### Environment

- Android Studio (latest stable recommended)
- JDK compatible with the project (see `gradle.properties` / `build.gradle`)
- Gradle Wrapper (use `./gradlew`)
- [pre-commit](https://pre-commit.com/) — runs style and lint checks automatically before each commit

### Running the project locally

1. Fork the repository
2. Clone your fork and add upstream:
   ```bash
   git clone https://github.com/<your-user>/sdk-android.git
   cd sdk-android
   git remote add upstream https://github.com/mercadopago/sdk-android.git
   ```
3. Install pre-commit hooks:
   ```bash
   pre-commit install
   ```
4. Sync dependencies in Android Studio
5. Create a branch for your change (see naming convention below)

### Branch naming convention

Create branches from `main`. Suggested names:

- `feature/<ticket>/short-description`
- `fix/<ticket>/short-description`
- `hotfix/short-description`
- `docs/<ticket>/short-description`
- `refactor/<ticket>/short-description`

Example:
```bash
git checkout -b fix/3795/card-number-error
```

### Commit message convention

Follow the [seven rules of a great Git commit message](https://chris.beams.io/posts/git-commit). Use the imperative mood, capitalize the subject, limit to 72 characters, and explain **what and why** in the body.

```bash
git commit -m "Fix card number validation rejecting valid BIN prefix"
```

Commits like `fix tests`, `now it works`, or `wip` will not be accepted. See [Coding Guidelines — Git](CODING_GUIDELINES.md#git-guidelines) for full details and examples.

---

## Quality and compatibility

### General guidelines

- Avoid breaking changes without prior discussion.
- Keep binary compatibility whenever possible (library/SDK).
- Update documentation when the change affects public usage (README, KDocs, samples).
- All public API must include KDoc.

### Style and lint

This project uses **ktlint** and **detekt**. If you installed pre-commit hooks, they run automatically. You can also run manually:

```bash
./gradlew ktlintFormat   # auto-format
./gradlew detekt         # static analysis
```

See [Coding Guidelines — Code Style](CODING_GUIDELINES.md#code-style) for configuration details.

### Tests

- Add tests for any change that includes relevant logic.
- Ensure existing tests keep passing.
- Minimum line coverage: **80%** (enforced by Kover).
- No instrumented tests (`androidTest`) — use Robolectric instead.

See [Coding Guidelines — Testing](CODING_GUIDELINES.md#testing-guidelines) for tooling and patterns.

### Local checks (required before opening a PR)

```bash
./gradlew ktlintFormat
./gradlew detekt
./gradlew :module:testDebugUnitTest
./gradlew koverBinaryReportDebug
```

---

## Pull Request checklist

Your PR should:
- [ ] Be small and focused (one concern per PR)
- [ ] Include a clear description and reference to the related ticket/issue
- [ ] Pass all CI checks (ktlint, detekt, tests, coverage)
- [ ] Include screenshots or screen recordings for any UI change
- [ ] Not introduce new public API without KDoc

---

## Review process

- Keep the PR up to date with `main` (rebase preferred over merge commits).
- Reply to comments with context and apply incremental updates when needed.
- PRs that fail CI will not be reviewed until green.

---

## Security

This SDK handles PCI-sensitive payment data. Contributors must follow the security rules defined in [Coding Guidelines — Security](CODING_GUIDELINES.md#security-guidelines):

- Never persist card data (PAN, CVV, expiry) to disk or logs.
- Never hardcode credentials, tokens, or API keys in source code.
- Never include sensitive data in screenshots or PR descriptions.

If you discover a vulnerability, **do not open a public issue**. Report it privately following the process in [SECURITY.md](SECURITY.md).

---

## License

By contributing, you agree that your contribution will be licensed under the [Apache License 2.0](LICENSE.md).
