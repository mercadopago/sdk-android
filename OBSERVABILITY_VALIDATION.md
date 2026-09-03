# Android native error observability validation

Date: 2026-09-02

Parent: CHOBK-3831

Initiative: `observability-initiative`

## Repository validation

- `:analytics:testDebugUnitTest`, `:core-methods:testDebugUnitTest`, and `:checkout:testDebugUnitTest` passed, including the shared Android Checkout fixture, all existing affected operation paths, queue saturation/cleanup, transport containment, and identical public Checkout error assertions.
- `detekt ktlintCheck` passed after applying the repository format and documentation rules to the feature diff.
- `:analytics:lintDebug`, `:core-methods:lintDebug`, `:checkout:lintDebug`, and `:sdk-android:lintDebug` passed.
- `assembleDebug assembleRelease :example:assembleRelease --parallel` passed. Release artifacts were minified by R8 without a new broad consumer keep rule.
- `AnalyticsModulesProviderTest` and `MPAnalyticsTest` passed after the final formatting and annotation fixes, covering reporter bindings and graph cleanup.
- No documented public constructor, model, callback, or method signature was changed. New JVM-visible cross-module observability symbols are restricted to `LIBRARY_GROUP`; this repository does not configure a separate ABI-diff Gradle task.

## Compatibility and rollout constraints

- Existing `Result` values, Checkout view events, and callbacks preserve their original public errors. Observability is an internal best-effort side effect.
- The reporter owns one worker and a 64-element bounded queue. The dedicated client has no credential/public-key interceptor, retry, redirect, cookie, cache, or logger.
- `DUAL_WRITE` preserves Melidata during validation. No source gate, release, deployment, dashboard, monitor, or cutover was performed by this repository task.
- Source gates must remain disabled until the backend contract is deployed and the external operational, trusted-corroboration, performance, privacy, and security handoffs are complete.

## Performance review

- `MPErrorReporter.track` performs no await, blocking I/O, retry, logging, or task-per-event work. It uses one `SupervisorJob` worker and a fixed 64-element `Channel`; saturation drops the newest report immediately.
- `MPErrorReporterTest.caller path p95 stays below one millisecond under queue pressure` exercises the production UUID/timestamp path after warm-up and enforces the `<1ms` p95 caller-path budget while the queue is saturated.
- Reporter replacement closes the previous channel, cancels its single scope, and closes the previous Koin graph. Existing lifecycle tests cover reconfiguration and cleanup.
- The `<1%` CoreMethods/Checkout public-operation latency comparison requires coordinated release E2E measurements and remains a rollout handoff; it is not evidence that can be produced by JVM unit tests.

## Security and privacy review

- The request is constructed from closed enums and explicitly bounded optional fields. Tests reject serialization of credentials, cookies, PAN/BIN/CVV, payer data, order/payment identifiers, package/device identifiers, URLs/bodies, raw errors/messages, and arbitrary detail keys.
- The dedicated client sends no authorization or cookie header, refuses redirects, disables application retry, uses bounded timeouts, and does not install logging, caching, credential, or public-key interceptors.
- No report is persisted or replayed. Transport, queue, serialization, and analytics failures are contained without logging raw input or changing the product result.
- ApplicationSecurityMCP was unavailable in this local agent runtime. Its issue catalog/fix-suggestion scan and the required AppSec approval remain explicit external handoffs before any source gate, paging, or observability-only cutover can be enabled.
