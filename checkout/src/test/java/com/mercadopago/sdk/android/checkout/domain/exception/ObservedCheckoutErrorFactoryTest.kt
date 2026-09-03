package com.mercadopago.sdk.android.checkout.domain.exception

import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorDiagnostic
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

internal class ObservedCheckoutErrorFactoryTest {
    @Test
    fun `retained validation maps without changing the legacy public error`() {
        val observed = ObservedCheckoutErrorFactory.from(
            ResultError.Validation("private validation detail"),
            ErrorLocalized.TOKENIZATION,
        )

        assertEquals(NativeErrorCode.INPUT_VALIDATION_FAILED, observed.nativeCode)
        assertEquals(NativeErrorDiagnostic.VALIDATION, observed.diagnostic)
        assertIs<MercadoPagoCheckoutError.ServiceError>(observed.publicError)
        assertEquals(ErrorCode.SERVICE_ERROR, observed.publicError.errorCode)
        assertEquals(ErrorLocalized.TOKENIZATION.name, observed.publicError.errorLocalized)
    }

    @Test
    fun `connection source codes map to connection unavailable`() {
        listOf("NETWORK_CONNECTION_FAILED", "NO_INTERNET", "CONNECTION", "NETWORK", "UNREACHABLE")
            .forEach { code ->
                val observed = response(code)

                assertEquals(NativeErrorCode.CONNECTION_UNAVAILABLE, observed.nativeCode, code)
                assertEquals(NativeErrorDiagnostic.OFFLINE, observed.diagnostic, code)
            }
    }

    @Test
    fun `timeout source codes and reliable statuses map to request timeout`() {
        listOf("NETWORK_TIMEOUT", "TIMEOUT").forEach { code ->
            assertEquals(NativeErrorCode.REQUEST_TIMEOUT, response(code).nativeCode, code)
        }
        listOf(408, 504).forEach { status ->
            val observed = response("SERVICE_ERROR", status)

            assertEquals(NativeErrorCode.REQUEST_TIMEOUT, observed.nativeCode, status.toString())
            assertEquals(status, observed.httpStatus)
            assertEquals(NativeErrorDiagnostic.TIMEOUT, observed.diagnostic)
        }
    }

    @Test
    fun `empty body maps to response contract invalid`() {
        val observed = response("EMPTY_BODY")

        assertEquals(NativeErrorCode.RESPONSE_CONTRACT_INVALID, observed.nativeCode)
        assertEquals(NativeErrorDiagnostic.EMPTY_BODY, observed.diagnostic)
    }

    @Test
    fun `configuration source and auth statuses take precedence`() {
        listOf("CONFIGURATION_ERROR", "INTEGRATION_ERROR").forEach { code ->
            assertEquals(NativeErrorCode.SDK_CONFIGURATION_INVALID, response(code).nativeCode, code)
        }
        listOf(401, 403).forEach { status ->
            val observed = response("SERVICE_ERROR", status)

            assertEquals(NativeErrorCode.SDK_CONFIGURATION_INVALID, observed.nativeCode, status.toString())
        }
    }

    @Test
    fun `known service rejection maps upstream and unknown sources map operation failed`() {
        assertEquals(NativeErrorCode.UPSTREAM_REJECTED, response("SERVER_ERROR", 500).nativeCode)
        listOf("EXCEPTION", "UNKNOWN_ERROR").forEach { code ->
            assertEquals(NativeErrorCode.OPERATION_FAILED, response(code).nativeCode, code)
        }
    }

    @Test
    fun `only reliable HTTP status range is retained`() {
        assertEquals(599, response("SERVER_ERROR", 599).httpStatus)
        assertNull(response("SERVER_ERROR", 99).httpStatus)
        assertNull(response("SERVER_ERROR", 600).httpStatus)
        assertNull(
            ObservedCheckoutErrorFactory.from(
                ResultError.Request("private raw message", "SERVER_ERROR"),
                ErrorLocalized.TOKENIZATION,
            ).httpStatus,
        )
    }

    private fun response(
        code: String,
        status: Int? = null,
    ) =
        ObservedCheckoutErrorFactory.from(
            ResponseError(
                code = code,
                message = "private raw message",
                httpStatus = status,
            ),
            ErrorLocalized.CARD_FORM_INITIALIZATION,
        )
}
