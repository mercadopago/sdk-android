package com.mercadopago.sdk.android.coremethods.analytics

import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorEvidenceCode
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorResponseState
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorType
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorOperation
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.util.Locale
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class CoreMethodsErrorObservabilityTest {
    private val analytics = mockk<MPAnalytics>(relaxed = true)
    private val observability = CoreMethodsErrorObservability { analytics }

    @Test
    fun `classification is closed and never copies raw messages`() {
        val cases = listOf(
            Case(ResultError.Validation("private"), NativeErrorType.VALIDATION),
            Case(ResultError.Request("raw", "TIMEOUT"), code = NativeErrorEvidenceCode.TIMEOUT),
            Case(ResultError.Request("raw", "504"), code = NativeErrorEvidenceCode.TIMEOUT),
            Case(ResultError.Request("raw", "NO_INTERNET"), code = NativeErrorEvidenceCode.OFFLINE),
            Case(
                ResultError.Request("empty body", "200"),
                responseState = NativeErrorResponseState.EMPTY_BODY,
            ),
            Case(ResultError.Request("raw", "401"), code = NativeErrorEvidenceCode.HTTP_UNAUTHORIZED),
            Case(ResultError.Request("raw", "403"), code = NativeErrorEvidenceCode.HTTP_FORBIDDEN),
            Case(ResultError.Request("raw", "422")),
            Case(
                ResultError.Request("raw", "UNKNOWN_ERROR"),
                type = NativeErrorType.UNKNOWN,
                code = NativeErrorEvidenceCode.UNKNOWN_ERROR,
            ),
        )

        cases.forEach { case ->
            val input = with(observability) { case.error.toNativeErrorInput() }
            assertEquals(case.type, input.type)
            assertEquals(case.code, input.code)
            assertEquals(case.responseState, input.responseState)
        }
    }

    @Test
    fun `classification is stable in a Turkish default locale`() {
        val previous = java.util.Locale.getDefault()
        try {
            java.util.Locale.setDefault(java.util.Locale("tr", "TR"))

            assertEquals(
                NativeErrorEvidenceCode.TIMEOUT,
                with(observability) {
                    ResultError.Request("raw", "timeoUt").toNativeErrorInput()
                }.code,
            )
        } finally {
            java.util.Locale.setDefault(previous)
        }
    }

    @Test
    fun `passes classified error and shared id without status or raw detail`() {
        val operation = slot<NativeErrorOperation>()
        val input = slot<NativeErrorInput>()
        val factory = slot<(String) -> Metric>()
        every { analytics.trackError(capture(operation), capture(input), capture(factory)) } returns Unit

        observability.track(
            error = ResultError.Request(message = "secret raw message", code = "504"),
            operation = NativeErrorOperation.ISSUERS,
        ) { id -> Metric(TrackType.EVENT, "/legacy/$id") }

        assertEquals(NativeErrorOperation.ISSUERS, operation.captured)
        assertEquals(NativeErrorEvidenceCode.TIMEOUT, input.captured.code)
        assertNull(input.captured.httpStatus)
        assertNull(input.captured.requestCorrelationId)
        assertEquals("/legacy/shared-id", factory.captured("shared-id").path)
    }

    @Test
    fun `classification is locale independent`() {
        val originalLocale = Locale.getDefault()
        val input = slot<NativeErrorInput>()
        every { analytics.trackError(any(), capture(input), any()) } returns Unit

        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"))

            observability.track(
                error = ResultError.Request(message = "raw", code = "timeout"),
                operation = NativeErrorOperation.ISSUERS,
            ) { id -> Metric(TrackType.EVENT, "/legacy/$id") }

            assertEquals(NativeErrorEvidenceCode.TIMEOUT, input.captured.code)
        } finally {
            Locale.setDefault(originalLocale)
        }
    }

    @Test
    fun `missing or failing analytics never throws`() {
        CoreMethodsErrorObservability { null }.track(
            ResultError.Validation("raw"),
            NativeErrorOperation.CARD_TOKENIZATION,
        ) { mockk() }
        every { analytics.trackError(any(), any(), any()) } throws IllegalStateException("failed")

        observability.track(
            ResultError.Request("raw", "500"),
            NativeErrorOperation.ISSUERS,
        ) { mockk() }

        verify(exactly = 1) { analytics.trackError(any(), any(), any()) }
    }

    private data class Case(
        val error: ResultError,
        val type: NativeErrorType = NativeErrorType.REQUEST,
        val code: NativeErrorEvidenceCode? = null,
        val responseState: NativeErrorResponseState? = null,
    )
}
