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

### What to include

Please provide as much of the following information as possible to help us triage and resolve the issue quickly:

- Type of vulnerability (e.g., CWE identifier if known)
- Affected component or module (`checkout`, `core-methods`, `core`, etc.)
- Steps to reproduce or a minimal proof of concept
- Android API level and device/emulator details (if relevant)
- Potential impact assessment
- Any suggested mitigations

---

## Response Timeline

| Stage | Target |
|-------|--------|
| Acknowledgement | 2 business days |
| Initial triage | 5 business days |
| Status update | 10 business days |
| Patch release (critical) | 15 business days |

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
