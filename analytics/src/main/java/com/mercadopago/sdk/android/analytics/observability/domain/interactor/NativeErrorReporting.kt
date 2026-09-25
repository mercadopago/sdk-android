package com.mercadopago.sdk.android.analytics.observability.domain.interactor

import androidx.annotation.RestrictTo
import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorOperation

/**
 * Outcome of accepting an error for native observability delivery.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class NativeErrorReceipt(
    /** Correlation identifier shared with the legacy analytics event, or `null` when nothing was queued. */
    val eventId: String?,
    /** Whether the caller must also emit the legacy Melidata event. */
    val shouldSendMelidata: Boolean,
) {
    /** Receipt factories. */
    companion object {
        /** Legacy-only delivery: no observability event, Melidata must be sent. */
        val MELIDATA_FALLBACK = NativeErrorReceipt(eventId = null, shouldSendMelidata = true)
    }
}

/**
 * Restricted boundary used by SDK modules to report classified native failures.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface NativeErrorReporting {
    /**
     * Captures [input] for [operation] and returns the legacy-delivery decision.
     */
    fun capture(operation: NativeErrorOperation, input: NativeErrorInput): NativeErrorReceipt

    /** Releases reporter resources and rejects subsequent asynchronous delivery work. */
    fun close()
}

/**
 * Captures the error when a reporter is configured; otherwise, or on any failure,
 * falls back to legacy Melidata delivery.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun NativeErrorReporting?.captureOrFallback(
    operation: NativeErrorOperation,
    input: () -> NativeErrorInput,
): NativeErrorReceipt =
    this?.let { reporter -> runCatching { reporter.capture(operation, input()) }.getOrNull() }
        ?: NativeErrorReceipt.MELIDATA_FALLBACK

/**
 * Obtains the configured reporter and captures the error without allowing provider failures
 * to affect the SDK consumer flow.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun (() -> NativeErrorReporting?).captureOrFallback(
    operation: NativeErrorOperation,
    input: () -> NativeErrorInput,
): NativeErrorReceipt =
    runCatching { invoke() }.getOrNull().captureOrFallback(operation, input)
