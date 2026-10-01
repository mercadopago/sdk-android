package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorType
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.ObservedCheckoutError
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CheckoutErrorObservabilityTest {
    private val adapter = CheckoutErrorObservability()

    @Test
    fun `adapter preserves retained evidence without sending analytics`() {
        val expected = NativeErrorInput.create(NativeErrorType.SERVICE, httpStatus = 503)
        val observed = ObservedCheckoutError(
            publicError = MercadoPagoCheckoutError.ServiceError(
                code = ErrorCode.SERVICE_ERROR,
                messageError = "private raw service message",
                localized = ErrorLocalized.ORDER_PROCESS.name,
            ),
            nativeErrorInput = expected,
        )

        assertEquals(expected, adapter.input(observed))
    }

    @Test
    fun `adapter creates closed user cancellation evidence`() {
        val input = adapter.cancellationInput()
        assertEquals(NativeErrorType.USER_CANCELLATION, input.type)
    }
}
