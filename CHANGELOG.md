# Changelog

All notable changes to the Mercado Pago SDK Android will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [CI/CD] - 2026-04-28

### Added
- `check-changelog` CircleCI job warns when `CHANGELOG.md` has not been updated in the branch — non-blocking, pipeline continues (#205)
- `lib.sh` created with shared helpers (`config_to_module`, `project_ref_to_module`, `artifact_exists`, `bom_published_modules`) used by `check-version-consistency` and `publish-maven` (#204)

### Fixed
- `check-version-consistency` now derives the list of modules to validate from `sdk-android-bom/build.gradle.kts` via `bom_published_modules`, avoiding false positives on non-published modules (#204)
- Unbound variable errors in bash associative arrays fixed with `${var:-}` pattern (#204)

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
