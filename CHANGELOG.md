# Changelog

All notable changes to the Mercado Pago SDK Android will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
## Unreleased
### Added
- Add `CardSave` and `CardTransaction` in `Builder` and `Callback`
- `koverVerify` step added to CI pipeline to enforce 80% overall coverage threshold
- `AndroidManifest.xml` added to `core-methods` test source set — enables Robolectric-based unit tests that require an `Activity` context

### Changed
- CI coverage tasks updated from `koverHtmlReportDebug`/`koverXmlReportDebug` to `koverHtmlReport`/`koverXmlReport` (variant-agnostic tasks)
- `components` and `foundation` modules excluded from Kover coverage (`kover { disable() }`) — UI-only modules with no business logic to measure

### Fixed
- ProGuard rule added for `checkout`'s `ResponseError` — prevents member stripping in release builds
- ProGuard rules added for `core-methods`' `ResultError.Request` and `ResultError.Validation` — prevents member stripping in release builds

### Changed
- `PaymentMethod.Card` fields renamed from `allowedTypes`/`allowedBrands` to `excludedPaymentTypes`/`excludedPaymentMethods` — filter semantics changed from allowlist to excludelist; defaults changed from full lists to empty lists (breaking change for callers using named parameters)

## [0.2.2] - 2026-05-21
### Added
- `core-methods` error tracks now carry domain-specific context through dedicated event-data classes: `CardIssuersErrorData`, `IdentificationTypesErrorData`, `InstallmentsErrorData`, `PaymentMethodErrorData`, and `GenerateCardTokenErrorData`
- `payment_type` and `security_length` fields added to the `payment_methods` success event (`PaymentMethodEventData`)
- Unit tests `GenerateCardTokenAnalyticsTest` and `PaymentMethodAnalyticsTest` covering success and error metrics
- `check-changelog` CircleCI job warns when `CHANGELOG.md` has not been updated in the branch — non-blocking, pipeline continues (#205)
- `lib.sh` created with shared helpers (`config_to_module`, `project_ref_to_module`, `artifact_exists`, `bom_published_modules`) used by `check-version-consistency` and `publish-maven` (#204)
- `lint` job runs in parallel with `test-coverage`, providing faster feedback on static analysis without blocking the test pipeline
- Android SDK cached in CI with key `v1-android-sdk-11076708` — download skipped on cache hit, saving setup time on every pipeline run
- `build-artifacts` Collect AARs step now filters by BOM modules via `bom_published_modules`, avoiding collection of non-published modules

### Changed
- `transaction_amount` is no longer nullable in `CardFormSubmitEventData` (Double) and `InstallmentAnalyticsData` (BigDecimal) — callers default to `0.0` when amount is unavailable
- `core-methods` error tracks (`card_issuers`, `identification_types`, `installments`, `payment_methods`, `tokenization`) no longer use the generic `AnalyticsConstants.buildErrorData` / `MetricErrorData` — each endpoint uses its own event-data class with relevant fields
- `metricGenerateCardTokenCallError` signature simplified: `error` is now required, `isSavedCard` removed, and the payload uses the new `GenerateCardTokenErrorData`
- Monolithic `build-test` job split into `lint`, `test-coverage`, `documentation-write`, and `build` for better parallelism and separation of concerns
- `lint` uses `resource_class: large` instead of `xlarge` — detekt and ktlint do not require high memory
- `documentation-write` restricted to `main` branch only — no value generating docs on feature branches
- `build` job removed — `build-artifacts` already covers debug and release AAR compilation for all BOM modules
- `check-changelog` and `check-version-consistency` renamed to `verify-changelog` and `verify-version-consistency` for naming consistency
- `verify-changelog` filter changed to `ignore: main` — runs only on feature branches where the reminder is actionable
- `build-artifacts` and `verify-artifacts` now run on all branches to catch artifact issues before merge
- `save_cache` for Gradle dependencies moved to `lint` job with `when: always` — `lint` always completes before `test-coverage`, ensuring cache is available for downstream jobs
- Git LFS installation removed from all CI jobs — LFS content is not used by any active pipeline step

### Removed
- `error_type` field removed from `SdkInitializerEventData` and the error metric on SDK reconfiguration failures in `ConfigureSdkUseCase` — failures now log only, avoiding duplicate/misleading initialization events

### Fixed
- `check-version-consistency` now derives the list of modules to validate from `sdk-android-bom/build.gradle.kts` via `bom_published_modules`, avoiding false positives on non-published modules (#204)
- Unbound variable errors in bash associative arrays fixed with `${var:-}` pattern (#204)
- `verify-changelog` `git fetch` failure now exits cleanly with a warning instead of producing a false positive
- `verify-changelog` no longer runs on `main` branch, preventing a dependency chain that would block `lint` and all downstream jobs on `main`

## [0.2.1] - 2026-04-24

### Fixed
- `RetrofitServiceFactory` constructor annotated with `@JvmOverloads` — Java callers no longer need to specify all parameters explicitly (#187)
- `mp-extended` module correctly included in the Maven publish pipeline (#185)

### Changed
- Version `0.2.1` released across all modules: `sdk-android`, `bom`, `core`, `core-methods`, `checkout`, `components`, `foundation`, `analytics`, `mp-extended`

## [CI/CD] - 2026-04-23

### Added
- Added `store_artifacts` for Detekt, KtLint and Kover HTML reports under `artifacts/reports/` per module
- Added Gradle dependency cache keyed on `libs.versions.toml` + `build.gradle.kts` + `settings.gradle.kts` with `v1-` prefix for manual invalidation
- Enforced 80% diff coverage threshold via `diff-cover`: only lines added or modified in the PR are checked, avoiding failures on existing legacy code
- Added version consistency check in `publish-maven`: fails if a module version is bumped without bumping its dependents, preventing partial releases

### Changed
- Fixed `store_test_results` to use JUnit XML format — test results now appear in CircleCI UI
- Extracted duplicated Android SDK setup into a reusable `setup-android` command
- Unified artifacts under a single `store_artifacts` with `builds/` and `reports/` subfolders — eliminates duplicate artifact folders
- `publish-maven` halts early when no modules have version changes

## [0.2.0] - 2026-04-17

### Added
- MPExtended module with Device Session ID support (#175)
- `MPExtended` public API for device session retrieval
- Clean architecture layers (data/domain) for MPExtended module
- Koin DI modules for MPExtended (network, datasource, repository, use cases)
- Gson snake_case converter as default in Retrofit

### Changed
- Updated `RetrofitServiceFactory` to support Gson converter
- Updated `PublicKeyInterceptor` logic
- `MercadoPagoSDK` now initializes MPExtended module

## [0.1.7] - 2026-03-30

### Fixed
- Changed card_bin_length from 8 to 6 digits (#149)
- Removed rememberSaveable from PCIFieldState to prevent PCI data serialization (CWE-312) (#135)

### Security
- **CWE-312 Fix**: Prevented PCI data serialization in PCIFieldState

## [0.1.5] - 2026-02-11

### Fixed
- Card token verification (#114)
- Security code verification (#111)
- Removed git lfs fetch (#113)

## [0.1.4] - 2026-02-09

### Fixed
- Security code verification (#111)

## [0.1.3] - 2025-11-11

### Added
- Card payment form creation (#84)
- 3DS agnostic implementation (#65)
- Header component (#85)
- Footer component (#86)
- Input component (#83)
- Tooltip component (#81)
- Button component (#79)
- Meli session id and SDK version (#108)

### Changed
- Updated editorconfig file (#92)

### Fixed
- Analytics tokenization (#73)
- Generate card token card id call (#72)

## [0.1.2] - 2025-09-29

### Fixed
- Removed site ID fetch functionality

## [0.1.1] - 2025-09-26

### Added
- New methods to change public key and countryCode (#80)

## [0.0.9] - 2025-09-23

### Added
- List item component (#77)
- Pill component (#66)
- MPRadioButton component (#67)

### Fixed
- Analytics tokenization (#73)
- Changed cached site id call in repository (#75)
- Fixed site id route (#74)
- Added .first to flow work (#76)

## [0.0.8] - 2025-09-01

### Fixed
- Modified card token generation card ID call (#72)
- Fixed Expiration Date null value on generate card (#70)

## [0.0.7] - 2025-08-07

### Added
- Radio button component (#67)
- Pill component (#66)

### Fixed
- Resolved expiration date null value issue (#70)

## [v0.0.6] - 2025-07-29

### Added
- JvmOverloads annotations
- XML activity example
- Core SDK functionality

## [v0.0.5] - 2025-07-29

### Added
- Foundation modules
- Analytics module
- Components module

## [0.0.4] - 2025-07-29

### Added
- Analytics module

## [0.0.3] - 2025-07-29

### Added
- Components module

## [0.0.2] - 2025-07-29

### Added
- Core module structure

## [0.0.1] - 2025-07-29

### Added
- Initial SDK release
- Basic SDK initialization
- Public key and country code configuration
- Core Methods SDK with identification types support
- Error handling with Result.Error and Result.Success patterns
- Request and validation error types
- Apache License 2.0

---

## Release Notes

### Version Compatibility
- MinSDK 23+
- Jetpack Compose BOM 2024.12.01+
- Kotlin 2.0.0+

### Migration Notes

#### Migrating to 0.1.7
- **BREAKING CHANGE**: Card BIN length has been changed from 8 to 6 digits
- **IMPORTANT**: PCI data is no longer serialized - input will be cleared after configuration changes (device rotation)
- Ensure your implementation accounts for the new 6-digit BIN length
- If you need to preserve PCI data across configuration changes, use ViewModel pattern with PCIFieldState.create()

#### Migrating to 0.1.5
- Security code verification has been fixed
- No breaking changes in public API

#### Migrating to 0.0.9
- Added new UI components (List Item, Pill, RadioButton)
- Site ID fetching has been optimized

### Security Updates

#### 0.1.7
- **CWE-312 Fix**: Removed PCI data serialization vulnerability in PCIFieldState
- Card BIN validation strengthened with minimum 6-digit requirement

### Links
- [GitHub Repository](https://github.com/mercadopago/sdk-android)
- [Documentation](https://mercadopago.github.io/sdk-android/)
- [Apache License 2.0](https://github.com/mercadopago/sdk-android/blob/main/LICENSE.md)

[0.2.0]: https://github.com/mercadopago/sdk-android/releases/tag/v0.2.0
[0.1.7]: https://github.com/mercadopago/sdk-android/releases/tag/v.0.1.7
[0.1.5]: https://github.com/mercadopago/sdk-android/releases/tag/v.0.1.5
[0.1.4]: https://github.com/mercadopago/sdk-android/releases/tag/v.0.1.4
[0.1.3]: https://github.com/mercadopago/sdk-android/releases/tag/v.0.1.3
[0.1.2]: https://github.com/mercadopago/sdk-android/releases/tag/v.0.1.2
[0.1.1]: https://github.com/mercadopago/sdk-android/releases/tag/v.0.1.1
[0.0.9]: https://github.com/mercadopago/sdk-android/releases/tag/v.0.0.9
[0.0.8]: https://github.com/mercadopago/sdk-android/releases/tag/v.0.0.8
[0.0.7]: https://github.com/mercadopago/sdk-android/releases/tag/v.0.0.7
[v0.0.6]: https://github.com/mercadopago/sdk-android/releases/tag/v0.0.6
[v0.0.5]: https://github.com/mercadopago/sdk-android/releases/tag/v0.0.5
[0.0.4]: https://github.com/mercadopago/sdk-android/releases/tag/0.0.4
[0.0.3]: https://github.com/mercadopago/sdk-android/releases/tag/0.0.3
[0.0.2]: https://github.com/mercadopago/sdk-android/releases/tag/0.0.2
[0.0.1]: https://github.com/mercadopago/sdk-android/releases/tag/0.0.1
