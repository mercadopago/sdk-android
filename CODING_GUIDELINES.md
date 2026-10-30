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
core-methods      ← PCI-safe card operations, Secure Fields, tokenization
checkout          ← card payment UI (Compose), ViewModel, checkout flow
components        ← reusable Compose UI components
foundation        ← design system, theme tokens
analytics         ← event tracking
mp-extended       ← device session (fraud prevention)
```

**Dependency direction is strict.** The allowed dependencies per module are listed in the table below. A dependency that is not in the table is a design change: discuss it first.

| Module | May depend on |
|--------|---------------|
| `core`, `foundation` | nothing |
| `analytics` | `core` |
| `sdk-android` | `core`, `analytics` |
| `core-methods` | `core`, `sdk-android`, `analytics` |
| `components` | `foundation`, `core-methods` |
| `mp-extended` | `core-methods`, `core`, `sdk-android`, `analytics` |
| `checkout` | `core`, `sdk-android`, `analytics`, `core-methods`, `foundation`, `components` |

`core-methods` must never depend on `checkout`, `components` or `mp-extended`. Do not introduce circular dependencies.

### Creating modules

- A new module is a public artifact and a graph change. Discuss it in an issue before implementing.
- Versions live only in `buildSrc`, never in a module's `build.gradle.kts`.
- Use the publishing block and the Koin provider pattern of an existing module, such as `mp-extended`.
- A new module needs: a `buildSrc` config, a `settings.gradle.kts` entry, a BOM constraint (if published), unit tests, KDoc on the public API, and an update to the dependency table above.

### Creating classes

- Put the class in the layer that owns the rule. The layers, from the screen down to the network, are:
  - **View**: composables (`presentation`).
  - **ViewModel**: screen state and events (`presentation`).
  - **Model**: domain models and sealed results (`domain/model`).
  - **UseCase**: one business operation (`domain/usecase`).
  - **Repository**: interface in `domain/repository`, implementation in `data/repository`.
  - **DataSource**: interface and implementation in `data/datasource` (`remote` or `local`).
  - **Service**: Retrofit interface in `data/remote/service`, annotations only.
- A call goes View → ViewModel → UseCase → Repository → DataSource → Service. Skipping a layer is a violation.
- The SDK does not store data offline by default. Any local storage needs a security review first, and card data is never stored.
- Domain code does not depend on `data` or `presentation`, and does not import `android.*` or Compose.
- Use `internal` by default. Use `sealed` types for results, errors and screen states.
- Screen state is immutable and updated with `.copy()`. ViewModels expose `StateFlow`; the `MutableStateFlow` stays private.
- Inject dispatchers and dependencies through the constructor.
- Name use cases `<Verb><Noun>UseCase`, repository interfaces `<Module><Noun>Repository`, and implementations `...Impl`.

### Layer rules (strict)

The domain, data and presentation layers follow SOLID and Clean Architecture. The rules below are required, not preferences.

- **Domain** imports nothing from `data`, `presentation`, `android.*`, `androidx.*`, Compose, Retrofit, Gson or OkHttp. It depends only on interfaces it defines.
- **Data** implements domain interfaces and never exposes DTOs or HTTP types above itself. Errors are mapped to domain errors at the boundary. Repositories depend on data source interfaces; data sources depend on services; services hold no logic.
- **ViewModels** hold state and call use cases. They do not reference `Context`, `Activity`, `Intent`, repositories or Retrofit types.
- **Every class** has one reason to change (SRP). Extend behaviour through interfaces or sealed types, not through growing flags (OCP). Keep interfaces small (ISP). Inject collaborators through the constructor (DIP).
- A violation found in existing code is tracked as an issue. Do not fix it silently inside an unrelated change.

### Reusing code

- Before writing a helper, extension, base class or component, search the codebase for an existing one. Most generic needs are already covered by `core` (networking, DI, public key), `core-methods` (`Result`, card validation), `foundation` (tokens), and `components` (UI).
- If a block is used by two or more classes or modules, put it in the lowest module that all users can depend on, and keep it generic.
- Do not copy a helper into another module. Move it to a shared module or extend the existing one.
- Prefer a named extension function to a long expression repeated in several places. A name that explains intent (`isValidEmailFormat()`) reads better than the inline regex or API call. Put extensions next to the module that owns the receiver type.
- Do not create an extension for a single use that only hides a line. Keep that line inline.

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
- **Test rules, not lines.** Each test must assert an outcome of a business rule or a flow, and must fail if that rule is removed. A test that only calls code to raise coverage is not accepted.
- **Cover the flow.** For ViewModels, assert the sequence of states and events. For use cases, assert each success and error path, and that invalid input never reaches the repository.
- **No instrumented tests** (`androidTest` / `connectedDebugAndroidTest`) — this project uses Robolectric for UI tests without an emulator.
- **Minimum coverage: 80%** line coverage, enforced by Kover. The threshold is a floor; new code should meet it with meaningful assertions. Check before submitting:

```bash
./gradlew koverBinaryReportDebug
```

- Use **JUnit4 + MockK + Turbine** for unit tests. Use `flow.test { ... }` (Turbine) for `StateFlow` and `Flow` assertions — never use `collect` in tests.
- Mock only the Retrofit service (MockK: `mockk`, `coEvery`, `coVerify`). Every other class in the flow runs for real, so tests exercise the behaviour the product runs. Service mocks must stay in line with the real service: the service has a contract test on MockWebServer. Add `unmockkAll()` in `@After`.
- For Koin in tests: `stopKoin()` in `@After` + `startKoin {}` in `@Before`.

---

## Branching Guidelines

`main` is the only long-term branch. Short-lived branches follow this naming convention:

Every branch carries a ticket number as its second segment, for example `fix/3795/card-number-error`.

| Pattern | Use case |
|---------|----------|
| `feature/<ticket>/short-description` | New feature or behavioral change |
| `fix/<ticket>/short-description` | Bug fix |
| `hotfix/<ticket>/short-description` | Critical patch on production |
| `docs/<ticket>/short-description` | Documentation-only change, no source impact |
| `refactor/<ticket>/short-description` | Code refactor with no functional change |

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
