package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import kotlin.test.Test
import kotlin.test.assertEquals

internal class MercadoPagoCheckoutErrorAnalyticsTest {
    @Test
    fun `when NetworkError then toErrorTypeString returns network_error`() {
        val error = MercadoPagoCheckoutError.NetworkError(
            code = ErrorCode.NETWORK_CONNECTION_FAILED,
            messageError = "Connection failed",
            localized = "checkout",
            throwable = null,
        )
        assertEquals("network_error", error.toErrorTypeString())
    }

    @Test
    fun `when ServiceError then toErrorTypeString returns service_error`() {
        val error = MercadoPagoCheckoutError.ServiceError(
            code = ErrorCode.SERVICE_ERROR,
            messageError = "Service unavailable",
            localized = "checkout",
        )
        assertEquals("service_error", error.toErrorTypeString())
    }

    @Test
    fun `when ConfigurationError then toErrorTypeString returns integration_error`() {
        val error = MercadoPagoCheckoutError.ConfigurationError(
            code = ErrorCode.INTEGRATION_ERROR,
            messageError = "SDK not initialized",
            localized = "checkout",
        )
        assertEquals("integration_error", error.toErrorTypeString())
    }

    @Test
    fun `when UnknownError then toErrorTypeString returns unknown_error`() {
        val error = MercadoPagoCheckoutError.UnknownError(
            code = ErrorCode.UNKNOWN,
            messageError = "Unknown error",
            localized = "checkout",
            throwable = null,
        )
        assertEquals("unknown_error", error.toErrorTypeString())
    }
}
