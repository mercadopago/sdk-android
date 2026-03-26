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
Changes via PR are the best way to contribute.

---

## Requirements before you start

### Environment
- Android Studio (latest stable recommended)
- JDK compatible with the project (see `gradle.properties`/`build.gradle`)
- Gradle Wrapper (use `./gradlew`)

### Running the project locally
1. Fork the repository
2. Clone your fork and add upstream:
   ```bash
   git clone https://github.com/<your-user>/sdk-android.git
   cd sdk-android
   git remote add upstream https://github.com/mercadopago/sdk-android.git
3. Sync dependencies in Android Studio
4. Create a branch for your change (see naming convention below)

### Branch naming convention
Create branches from the repository base branch (e.g., main or develop, depending on the project).
Suggested names:
- `feature/<ticket or issuer>/short-description`
- `fix/<ticket or issuer>/short-description`
- `chore/<ticket or issuer>/short-description`
- `docs/<ticket or issuer>/short-description`

Example:
  ```bash
  git checkout -b fix/3795/card-number-error
  ```

### Commit message convention
Commits should be objective and describe “what” and “why”. Suggestion:

- `feat: ...`
- `fix: ...`

Example:
  ```bash
  git commit -m "fix: adjust card number invalid error key"
  ```
---

## Quality and compatibility
### General guidelines
- Avoid breaking changes without prior discussion.
- Keep binary compatibility whenever possible (library/SDK).
- Update documentation when the change affects public usage (README, KDocs, samples).

### Style and lint
Before opening a PR:
- Ensure the project is properly formatted and free of relevant warnings
- Run local checks (see “Local checks”)

### Tests
- Add tests for changes that include relevant logic.
- Ensure existing tests keep passing.
- If there are UI modules, validate rotation/config changes and error states.

### Local checks (required)
Before opening a PR, run:
  ```bash
  ./gradlew check
  ./gradlew test
  ./gradlew lint
  ```
---

## Pull Request checklist
Your PR should:
- Be small enough to review (when possible)
- Include a clear description and context
- Reference the related ticket/issue
- Be tested locally

## Review process
- Keep the PR up to date with the base branch (rebase/merge according to repo policy).
- Reply to comments with context and apply incremental updates when needed.

## Security
- Do not include credentials, tokens, keys, or sensitive data in code, logs, or screenshots.
- If you find a vulnerability, report it through the appropriate channels (see SECURITY.md).

---
## License
By contributing, you agree that your contribution will be licensed under the same license as this repository.



