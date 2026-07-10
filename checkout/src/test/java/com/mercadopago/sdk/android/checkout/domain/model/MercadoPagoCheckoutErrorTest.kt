package com.mercadopago.sdk.android.checkout.domain.model

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class MercadoPagoCheckoutErrorTest {
    @Test
    fun `given network error then maps fields to base properties`() {
        val cause = IllegalStateException("offline")
        val error = MercadoPagoCheckoutError.NetworkError(
            code = ErrorCode.NETWORK_CONNECTION_FAILED,
            messageError = "no connection",
            localized = "checkout",
            throwable = cause,
        )

        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, error.errorCode)
        assertEquals("no connection", error.errorMessage)
        assertEquals("no connection", error.message)
        assertEquals("checkout", error.errorLocalized)
        assertEquals("offline", error.errorCause)
        assertTrue(error is MercadoPagoCheckoutError)
        assertTrue(error is Throwable)
    }

    @Test
    fun `given configuration error then cause is null`() {
        val error = MercadoPagoCheckoutError.ConfigurationError(
            code = ErrorCode.INTEGRATION_ERROR,
            messageError = "missing key",
            localized = "init",
        )

        assertEquals(ErrorCode.INTEGRATION_ERROR, error.errorCode)
        assertEquals("missing key", error.errorMessage)
        assertEquals("init", error.errorLocalized)
        assertNull(error.errorCause)
    }

    @Test
    fun `given unknown error with throwable then cause is mapped`() {
        val error = MercadoPagoCheckoutError.UnknownError(
            code = ErrorCode.UNKNOWN,
            messageError = "boom",
            localized = "checkout",
            throwable = RuntimeException("oops"),
        )

        assertEquals(ErrorCode.UNKNOWN, error.errorCode)
        assertEquals("oops", error.errorCause)
    }

    @Test
    fun `given unknown error without throwable then cause is null`() {
        val error = MercadoPagoCheckoutError.UnknownError(
            code = ErrorCode.UNKNOWN,
            messageError = "boom",
            localized = "checkout",
            throwable = null,
        )

        assertNull(error.errorCause)
    }

    @Test
    fun `given service error with default throwable then cause is null`() {
        val error = MercadoPagoCheckoutError.ServiceError(
            code = ErrorCode.SERVICE_ERROR,
            messageError = "server failed",
            localized = "payment",
        )

        assertEquals(ErrorCode.SERVICE_ERROR, error.errorCode)
        assertEquals("server failed", error.errorMessage)
        assertEquals("payment", error.errorLocalized)
        assertNull(error.errorCause)
    }

    @Test
    fun `given service error with throwable then cause is mapped`() {
        val error = MercadoPagoCheckoutError.ServiceError(
            code = ErrorCode.SERVICE_ERROR,
            messageError = "server failed",
            localized = "payment",
            throwable = IllegalArgumentException("bad arg"),
        )

        assertEquals("bad arg", error.errorCause)
    }

    @Test
    fun `given two service errors with same fields then they are equal`() {
        val a = MercadoPagoCheckoutError.ServiceError(ErrorCode.SERVICE_ERROR, "m", "l")
        val b = MercadoPagoCheckoutError.ServiceError(ErrorCode.SERVICE_ERROR, "m", "l")

        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `given service error when copy with new code then equality changes`() {
        val error = MercadoPagoCheckoutError.ServiceError(ErrorCode.SERVICE_ERROR, "m", "l")

        val updated = error.copy(code = ErrorCode.UNKNOWN)

        assertEquals(ErrorCode.UNKNOWN, updated.errorCode)
        assertNotEquals(error, updated)
    }
}
