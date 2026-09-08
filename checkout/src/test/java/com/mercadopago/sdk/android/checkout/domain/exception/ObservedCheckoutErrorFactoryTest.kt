package com.mercadopago.sdk.android.checkout.domain.exception

import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorEvidenceCode
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorResponseState
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorType
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

        assertEquals(NativeErrorType.VALIDATION, observed.nativeErrorInput.type)
        assertIs<MercadoPagoCheckoutError.ServiceError>(observed.publicError)
        assertEquals(ErrorCode.SERVICE_ERROR, observed.publicError.errorCode)
        assertEquals(ErrorLocalized.TOKENIZATION.name, observed.publicError.errorLocalized)
    }

    @Test
    fun `connection source codes map to connection unavailable`() {
        listOf("NETWORK_CONNECTION_FAILED", "NO_INTERNET", "CONNECTION", "NETWORK", "UNREACHABLE")
            .forEach { code ->
                val observed = response(code)

                assertEquals(NativeErrorEvidenceCode.OFFLINE, observed.nativeErrorInput.code, code)
            }
    }

    @Test
    fun `timeout source codes and reliable statuses map to request timeout`() {
        listOf("NETWORK_TIMEOUT", "TIMEOUT").forEach { code ->
            assertEquals(NativeErrorEvidenceCode.TIMEOUT, response(code).nativeErrorInput.code, code)
        }
        listOf(408, 504).forEach { status ->
            val observed = response("SERVICE_ERROR", status)

            assertEquals(status, observed.nativeErrorInput.httpStatus)
        }
    }

    @Test
    fun `request timeout codes map consistently with CoreMethods`() {
        listOf("408", "504").forEach { code ->
            val observed = ObservedCheckoutErrorFactory.from(
                ResultError.Request(message = "raw", code = code),
                ErrorLocalized.TOKENIZATION,
            )

            assertEquals(NativeErrorEvidenceCode.TIMEOUT, observed.nativeErrorInput.code)
        }
    }

    @Test
    fun `empty body maps to response contract invalid`() {
        val observed = response("EMPTY_BODY")

        assertEquals(NativeErrorResponseState.EMPTY_BODY, observed.nativeErrorInput.responseState)
    }

    @Test
    fun `successful response without body maps to response contract invalid`() {
        val observed = ObservedCheckoutErrorFactory.from(
            ResultError.Request(message = "empty body", code = "200"),
            ErrorLocalized.TOKENIZATION,
        )

        assertEquals(NativeErrorResponseState.EMPTY_BODY, observed.nativeErrorInput.responseState)
    }

    @Test
    fun `configuration source and auth statuses take precedence`() {
        listOf("CONFIGURATION_ERROR", "INTEGRATION_ERROR").forEach { code ->
            val expected = if (code == "CONFIGURATION_ERROR") {
                NativeErrorEvidenceCode.CONFIGURATION
            } else {
                NativeErrorEvidenceCode.INTEGRATION
            }
            assertEquals(expected, response(code).nativeErrorInput.code, code)
        }
        listOf(401, 403).forEach { status ->
            val observed = response("SERVICE_ERROR", status)

            assertEquals(status, observed.nativeErrorInput.httpStatus, status.toString())
        }
    }

    @Test
    fun `request auth codes map consistently with CoreMethods`() {
        mapOf(
            "401" to NativeErrorEvidenceCode.HTTP_UNAUTHORIZED,
            "403" to NativeErrorEvidenceCode.HTTP_FORBIDDEN,
        ).forEach { (code, diagnostic) ->
            val observed = ObservedCheckoutErrorFactory.from(
                ResultError.Request(message = "raw", code = code),
                ErrorLocalized.TOKENIZATION,
            )

            assertEquals(diagnostic, observed.nativeErrorInput.code)
        }
    }

    @Test
    fun `known service rejection maps upstream and unknown sources map operation failed`() {
        assertEquals(NativeErrorType.SERVICE, response("SERVER_ERROR", 500).nativeErrorInput.type)
        listOf("EXCEPTION", "UNKNOWN_ERROR").forEach { code ->
            assertEquals(NativeErrorType.UNKNOWN, response(code).nativeErrorInput.type, code)
        }
    }

    @Test
    fun `only reliable HTTP status range is retained`() {
        assertEquals(599, response("SERVER_ERROR", 599).nativeErrorInput.httpStatus)
        assertNull(response("SERVER_ERROR", 99).nativeErrorInput.httpStatus)
        assertNull(response("SERVER_ERROR", 600).nativeErrorInput.httpStatus)
        assertNull(
            ObservedCheckoutErrorFactory.from(
                ResultError.Request("private raw message", "SERVER_ERROR"),
                ErrorLocalized.TOKENIZATION,
            ).nativeErrorInput.httpStatus,
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
