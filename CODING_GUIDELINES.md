# Coding Guidelines

The Mercado Pago Android SDK is a collaborative effort. Contributions from different developers enrich its feature set and make it more relevant to the community.

To keep the codebase maintainable over time, contributors are expected to follow these guidelines. They are not intended to limit your tools or rewire your thinking — they encourage good neighbor behavior.

---

## Language

**Use English** — source code, comments, KDoc, commit messages, PR descriptions, and review comments must all be in English. This ensures consistency and is considerate of developers who do not share the same native language.

Typos are unavoidable, but try to reduce them with a spellchecker. Most IDEs can be configured to run one automatically.

---

## Code Style

This project enforces code style automatically via **ktlint** and **detekt**. The recommended way to catch issues before pushing is to use [pre-commit](https://pre-commit.com/), which runs the checks automatically before every `git commit`.

### Setting up pre-commit

1. Verify pre-commit is installed:

```bash
$ pre-commit --version
pre-commit 2.17.0
```

If not installed, follow the instructions at [pre-commit.com](https://pre-commit.com/#install).

2. Inside the SDK project folder, install the git hook scripts:

```bash
$ pre-commit install
```

After this, the hooks run automatically before every commit.

### Running checks manually

```bash
./gradlew ktlintFormat   # Auto-format Kotlin code
./gradlew detekt         # Static analysis
```

These checks also run automatically on pull requests. Contributions that fail them will be automatically rejected until fixed.

Configuration files:
- ktlint: enforced via Gradle plugin (v12.1.2)
- detekt: `/config/detekt/detekt.yml`

When in doubt, follow the style already present in the surrounding code. If you have questions, just ask.

---

## Kotlin Guidelines

- **Immutability first:** prefer `val` over `var`; prefer immutable collections.
- **Null safety:** avoid `!!`. Use `?.let`, `?: return`, or `requireNotNull` with a clear message.
- **Coroutines:** use `suspend` functions and structured concurrency. Never use `GlobalScope`. Always pass a `CoroutineDispatcher` as a parameter for testability.
- **Sealed classes / sealed interfaces:** use for result types and state modeling (`Result`, `ScreenState`, `MercadoPagoCheckoutResult`).
- **Extension functions:** use them to add behavior without subclassing, but keep them in a file close to their receiver type.
- **No `@SuppressWarnings` / `@Suppress` without a comment** explaining why it is safe to suppress.

---

## Architecture Guidelines

Follow the established patterns in this SDK. Deviations require a justification in the PR description.

### Module boundaries

```
sdk-android       ← initialization, top-level DI
core              ← networking, base DI infrastructure
core-methods      ← API layer (identification, installments, tokenization)
checkout          ← card payment UI (Compose), ViewModel, checkout flow
components        ← reusable Compose UI components
foundation        ← design system, theme, Material3 base
analytics         ← event tracking
```

**Dependency direction is strict:** `checkout` and `core-methods` depend on `core` + `sdk-android`; they must not depend on each other. `components` depends only on `foundation`. Do not introduce circular dependencies.

### State management

- Use `StateFlow<ScreenState>` in ViewModels. State must be immutable; update with `.copy()`.
- Never expose `MutableStateFlow` outside the ViewModel.

### Dependency injection

- Each module exposes a `KoinModuleProvider`. Register dependencies in the module's own Koin instance.
- Do not use `KoinComponent` in production classes — prefer constructor injection.

### File size

**No file may exceed 300 lines.** If a file is approaching this limit, extract before it is reached. This is not a suggestion.

---

## Security Guidelines

This SDK handles PCI-sensitive data. Security is non-negotiable.

- **Never persist PCI data to disk.** Card number, CVV, and expiration date must remain in memory only. Use `remember` (not `rememberSaveable`) for `PCIFieldState`.
- **Never log PCI data.** No card fields, tokens, or partial card numbers in `Log.*` calls.
- **Never hardcode credentials.** Public keys, tokens, and API identifiers must be injected via `BuildConfig` or environment variables — never committed to source control.
- **Always validate at boundaries.** Sanitize and validate all data coming from external sources (API responses, user input). Do not trust internal assumptions.

Report security vulnerabilities privately. See [SECURITY.md](SECURITY.md).

---

## Comment Guidelines

Comments are hard to write well. Too many comments obfuscate code; too few leave readers without guidance. Aim for relevance.

### When to comment

- **Departures from convention:** if your code does something unexpected, explain why.
- **Non-obvious decisions:** if you spent significant time on a design choice, document your reasoning so future maintainers can validate or revisit it.
- **Safety-critical invariants:** for PCI data handling, synchronization, or security primitives, always describe the property being preserved.

### When not to comment

- Do not explain behavior that is immediately obvious from reading the surrounding code.
- Do not add structural comments that just repeat what the code says (`// returns null if empty`).
- Do not leave commented-out code in commits.

### KDoc

All public API must have KDoc. This includes public classes, interfaces, functions, and properties in `core-methods`, `checkout`, `components`, `foundation`, and `sdk-android`. Internal implementation classes do not require KDoc unless the logic is non-obvious.

---

## Testing Guidelines

- **Unit tests are mandatory** for all production code. No production code is merged without tests.
- **No instrumented tests** (`androidTest` / `connectedDebugAndroidTest`) — this project uses Robolectric for UI tests without an emulator.
- **Minimum coverage: 80%** line coverage, enforced by Kover. Check before submitting:

```bash
./gradlew koverBinaryReportDebug
```

- Use **JUnit4 + MockK + Turbine** for unit tests. Use `flow.test { ... }` (Turbine) for `StateFlow` and `Flow` assertions — never use `collect` in tests.
- Use **Fakes over Mocks** when possible. If you must mock, add `unmockkAll()` in `@After`.
- For Koin in tests: `stopKoin()` in `@After` + `startKoin {}` in `@Before`.

---

## Branching Guidelines

`main` is the only long-term branch. Short-lived branches follow this naming convention:

| Pattern | Use case |
|---------|----------|
| `feature/TICKET-description` | New feature or behavioral change |
| `fix/TICKET-description` | Bug fix |
| `hotfix/description` | Critical patch on production |
| `doc/description` | Documentation-only change, no source impact |
| `refactor/description` | Code refactor with no functional change |

Always branch from `main` and keep your branch short-lived. Rebase before opening a PR to avoid reverse merge commits.

---

## Git Guidelines

All commits **should** follow the [seven rules of a great Git commit message](https://chris.beams.io/posts/git-commit):

1. Separate subject from body with a blank line.
2. Limit the subject line to 72 characters.
3. Capitalize the subject line.
4. Do not end the subject line with a period.
5. Use the imperative mood in the subject line.
6. Wrap the body at 72 characters.
7. Use the body to explain **what and why**, not how.

**Examples of unacceptable commit messages:**
- `fix tests`
- `now it works`
- `wip`
- `asdfgh`

**Examples of acceptable commit messages:**
```
Remove rememberSaveable from PCIFieldState to prevent disk persistence

PCIFieldState was using rememberSaveable with a custom Saver, which
serializes card data to the Android Bundle. The Bundle can be written
to disk during process death, violating PCI DSS (CWE-312).

Replaced with remember; integrators should use ViewModel to survive
configuration changes.
```

Deviating slightly from these rules is tolerated. Deviating heavily is not.

---

## Pull Request Guidelines

- Every PR must reference a ticket or issue.
- Fill in the PR template completely. Empty descriptions will be sent back.
- PRs that fail CI (ktlint, detekt, tests, coverage) will not be reviewed until green.
- Keep PRs focused. One concern per PR. Mixing features, refactors, and bug fixes in a single PR slows review and increases risk.
- Screenshots or screen recordings are required for any UI change.

Before marking a PR as ready for review, run the full post-implementation checklist:

```bash
./gradlew ktlintFormat
./gradlew detekt
./gradlew :module:testDebugUnitTest
./gradlew koverBinaryReportDebug
```
