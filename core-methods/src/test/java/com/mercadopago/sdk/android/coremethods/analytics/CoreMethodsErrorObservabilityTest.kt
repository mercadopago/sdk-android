package com.mercadopago.sdk.android.coremethods.analytics

import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorEvidenceCode
import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorResponseState
import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorType
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class CoreMethodsErrorObservabilityTest {
    private val adapter = CoreMethodsErrorObservability()

    @Test
    fun `adapter emits only closed neutral evidence`() {
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
            val input = adapter.input(case.error)
            assertEquals(case.type, input.type)
            assertEquals(case.code, input.code)
            assertEquals(case.responseState, input.responseState)
            assertNull(input.httpStatus)
            assertNull(input.requestCorrelationId)
        }
    }

    @Test
    fun `adapter classification is locale independent`() {
        val originalLocale = Locale.getDefault()
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"))
            val input = adapter.input(ResultError.Request("raw", "timeoUt"))
            assertEquals(NativeErrorEvidenceCode.TIMEOUT, input.code)
        } finally {
            Locale.setDefault(originalLocale)
        }
    }

    private data class Case(
        val error: ResultError,
        val type: NativeErrorType = NativeErrorType.REQUEST,
        val code: NativeErrorEvidenceCode? = null,
        val responseState: NativeErrorResponseState? = null,
    )
}
