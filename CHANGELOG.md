# Changelog

All notable changes to the Mercado Pago SDK Android will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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
