package com.mercadopago.sdk.android.coremethods.analytics

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.NativeError
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorDiagnostic
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorOperation
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class CoreMethodsErrorObservabilityTest {
    private val analytics = mockk<MPAnalytics>(relaxed = true)
    private val observability = CoreMethodsErrorObservability { analytics }

    @Test
    fun `classification is closed and never copies raw messages`() {
        val cases = listOf(
            ResultError.Validation("PAN must not leave this object") to NativeErrorCode.INPUT_VALIDATION_FAILED,
            ResultError.Request("raw", "TIMEOUT") to NativeErrorCode.REQUEST_TIMEOUT,
            ResultError.Request("raw", "504") to NativeErrorCode.REQUEST_TIMEOUT,
            ResultError.Request("raw", "NO_INTERNET") to NativeErrorCode.CONNECTION_UNAVAILABLE,
            ResultError.Request("empty body", "200") to NativeErrorCode.RESPONSE_CONTRACT_INVALID,
            ResultError.Request("raw", "401") to NativeErrorCode.SDK_CONFIGURATION_INVALID,
            ResultError.Request("raw", "403") to NativeErrorCode.SDK_CONFIGURATION_INVALID,
            ResultError.Request("raw", "422") to NativeErrorCode.UPSTREAM_REJECTED,
            ResultError.Request("raw", "UNKNOWN_ERROR") to NativeErrorCode.OPERATION_FAILED,
        )

        cases.forEach { (error, expected) ->
            assertEquals(expected, with(observability) { error.toNativeErrorCode() })
        }
    }

    @Test
    fun `classification is stable in a Turkish default locale`() {
        val previous = java.util.Locale.getDefault()
        try {
            java.util.Locale.setDefault(java.util.Locale("tr", "TR"))

            assertEquals(
                NativeErrorCode.REQUEST_TIMEOUT,
                with(observability) {
                    ResultError.Request("raw", "timeoUt").toNativeErrorCode()
                },
            )
        } finally {
            java.util.Locale.setDefault(previous)
        }
    }

    @Test
    fun `passes classified error and shared id without status or raw detail`() {
        val nativeError = slot<NativeError>()
        val factory = slot<(String) -> Metric>()
        every { analytics.trackError(capture(nativeError), capture(factory)) } returns Unit

        observability.track(
            error = ResultError.Request(message = "secret raw message", code = "504"),
            operation = NativeErrorOperation.ISSUERS,
        ) { id -> Metric(TrackType.EVENT, "/legacy/$id") }

        assertEquals(NativeErrorCode.REQUEST_TIMEOUT, nativeError.captured.code)
        assertEquals(NativeErrorDiagnostic.TIMEOUT, nativeError.captured.diagnostic)
        assertNull(nativeError.captured.statusCode)
        assertNull(nativeError.captured.requestCorrelationId)
        assertEquals("/legacy/shared-id", factory.captured("shared-id").path)
    }

    @Test
    fun `missing or failing analytics never throws`() {
        CoreMethodsErrorObservability { null }.track(
            ResultError.Validation("raw"),
            NativeErrorOperation.CARD_TOKENIZATION,
        ) { mockk() }
        every { analytics.trackError(any(), any()) } throws IllegalStateException("failed")

        observability.track(
            ResultError.Request("raw", "500"),
            NativeErrorOperation.ISSUERS,
        ) { mockk() }

        verify(exactly = 1) { analytics.trackError(any(), any()) }
    }
}
