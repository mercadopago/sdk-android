# Security Policy

## Supported Versions

We actively maintain and release security fixes for the following versions:

| Version | Supported |
|---------|-----------|
| Latest stable | ✅ |
| Previous minor | ✅ (critical fixes only) |
| Older versions | ❌ |

We strongly recommend always using the latest version of the BOM (`sdk-android-bom`) to receive security patches and PCI DSS compliance updates.

---

## Reporting a Vulnerability

**Do not open a public GitHub issue for security vulnerabilities.**

To report a security issue, please use one of the following channels:

- **GitHub Private Security Advisory:** [Report a vulnerability](../../security/advisories/new) *(preferred)*
- **Email:** developers@mercadopago.com (Subject: [SECURITY] bricks-sdk-android - [description])

### What to include

Please provide as much of the following information as possible to help us triage and resolve the issue quickly:

```text
**Vulnerability type:**
[Type of vulnerability (e.g., CWE identifier if known)]

**Affected component or module:**
[Affected component or module (`checkout`, `core-methods`, `core`, etc.)]

**Affected versions:**
[e.g., 0.1.0 - 0.1.1]

**Description:**
[Detailed description of the vulnerability]

**Steps to reproduce:**
1. ...
2. ...
3. ...

**Impact:**
[What can an attacker do with this?]

**Suggested severity:**
[Critical / High / Medium / Low]

**Android API level and device/emulator details (opcional)**

**Proof of concept (optional):**
[Code or screenshots demonstrating the issue]

**Suggested fix (optional):**
[If you have ideas on how to fix it]
```

---

## Response Timeline

| Severity | Initial Response | Fix Target |
|---|---|---|
| Critical | 24 hours | 48–72 hours |
| High | 48 hours | 1 week |
| Medium | 1 week | 2–4 weeks |
| Low | 2 weeks | Next release |

We will notify you when the vulnerability is fixed and coordinate a disclosure date if applicable.

---

## Scope

This policy covers the **Mercado Pago Android SDK** — all modules published under `com.mercadopago.android.sdk`:

- `sdk-android-bom`
- `core-methods`
- `checkout`
- `core`
- `components`
- `foundation`
- `analytics`

Out of scope: third-party dependencies, the Mercado Pago backend API, and the example app.

---

## Security Design

This SDK is designed to handle sensitive payment data in compliance with **PCI DSS**. Key security properties:

### Sensitive data in memory only

PCI-sensitive fields (card number, CVV, expiration date) are **never persisted to disk**. The SDK uses `remember` (not `rememberSaveable`) for `PCIFieldState` to prevent serialization to Android's Bundle, which may be written to disk during process death (CWE-312).

### No plaintext card data on disk

Card data is never written to SharedPreferences, files, or any persistent storage. After process death, PCI fields return empty — this is intentional and required by PCI DSS 3.2.1.

### Public key handling

The SDK requires a Mercado Pago **public key** for initialization. This key is **not a secret**, but integrators should avoid hardcoding it in source code. Use environment variables or build-time injection (e.g., `BuildConfig`).

```kotlin
// Recommended
MercadoPagoSDK.initialize(
    context = this,
    publicKey = BuildConfig.PUBLIC_KEY, // injected at build time, not committed to git
    countryCode = "BR"
)
```

### Tokenization

Sensitive card data is tokenized server-side via the Mercado Pago API before any payment processing. The SDK never stores raw card data beyond the active UI session.

### Device fingerprint

The SDK attaches a device fingerprint, built by the MercadoLibre Device SDK (`com.mercadolibre.android.device:sdk`, version 4.0.1), to two requests. It is used for fraud prevention.

- **Card tokenization** (`POST https://api.mercadopago.com/v1/card_tokens`, used by `core-methods` and `checkout`): sent in the `device` field as `{"fingerprint": {...}}`, with camelCase keys (`systemVersion`, `vendorIds`, `vendorSpecificAttributes`, ...).
- **Device session** (`PUT https://api.mercadopago.com/cho-off/v1/devices/session`, `mp-extended`, only when `MPExtended.deviceSession()` is called): sent in the `finger_print` field next to `site_id`, with snake_case keys (`system_version`, `vendor_ids`, ...).

Collection starts in the background when `MercadoPagoSDK.initialize(...)` is called, whether or not either request is made later. The values are kept in memory and read when a request is built; a request sent before collection finishes carries a partial fingerprint. The fingerprint contains:

- **Device data**: model (`Build.MODEL`), brand, manufacturer, product (`Build.PRODUCT`), device codename (`Build.DEVICE`), OS (`android`), OS version (`Build.VERSION.RELEASE`), CPU ABI (`ro.product.cpu.abi`), screen resolution (width x height in pixels) and density, total RAM in bytes, and total and free space of the data partition in MiB.
- **Hardware features** (`PackageManager.hasSystemFeature`): camera, front camera, camera flash, accelerometer, gyroscope, compass, GPS, Bluetooth, NFC, microphone, telephony and touchscreen.
- **Vendor IDs** (a list of `name` / `value` pairs):
  - `widevine_id`: the Widevine `MediaDrm` device unique ID (`PROPERTY_DEVICE_UNIQUE_ID`), Base64-encoded, not hashed. Omitted when Widevine is not available.
  - `advertising_id`: the Google advertising ID, read through Google Play services (`AdvertisingIdClient`) on a background thread. The Device SDK does not check the limit-ad-tracking setting. Omitted when Google Play services is not available.

The Device SDK makes no network calls of its own and writes nothing to disk (no SharedPreferences, files or databases).

The Device SDK depends on `play-services-ads-identifier` 17.1.0 (compile scope), so that library ships in the integrator's app even when the advertising ID is not needed. Its manifest declares `com.google.android.gms.permission.AD_ID`, which is merged into the app manifest. An integrator who does not want the permission can remove it with `<uses-permission android:name="com.google.android.gms.permission.AD_ID" tools:node="remove" />`.

### Network security

All API communication uses HTTPS. The SDK enforces TLS and does not allow cleartext traffic.

---

## Integrator Responsibilities

When integrating this SDK, you are responsible for:

1. **Keeping the SDK up to date** — always use the latest BOM version.
2. **Not logging PCI data** — never log card numbers, CVV, or expiration dates.
3. **Not storing PCI data** — do not persist `PCIFieldState` values to disk, database, or analytics.
4. **Securing the ViewModel** — if you host `PCIFieldState` in a ViewModel (recommended for configuration change survival), ensure the ViewModel is scoped to the Activity/Fragment and cleared on exit.
5. **Android Backup** — consider disabling `android:allowBackup="false"` or using `android:fullBackupContent` rules to exclude any payment-related data from cloud backups.

---

## License

This project is licensed under the [Apache License 2.0](LICENSE.md). Security contributions are subject to the same license.
