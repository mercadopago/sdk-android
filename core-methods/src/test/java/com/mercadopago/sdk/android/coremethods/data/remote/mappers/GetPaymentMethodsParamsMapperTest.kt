package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.domain.model.params.GetPaymentMethodsParams
import org.junit.Assert.assertEquals
import kotlin.test.Test

internal class GetPaymentMethodsParamsMapperTest {
    @Test
    fun `toRequest should correctly map GetPaymentMethodsParams to PaymentMethodsRequest`() {
        val getPaymentMethodsParams = GetPaymentMethodsParams(
            productId = "product_001",
            bin = 123456
        )

        val paymentMethodsRequest = getPaymentMethodsParams.toRequest()

        assertEquals(getPaymentMethodsParams.productId, paymentMethodsRequest.productId)
        assertEquals(getPaymentMethodsParams.bin, paymentMethodsRequest.bin)
    }

    @Test
    fun `toRequest should handle null values correctly`() {
        val getPaymentMethodsParams = GetPaymentMethodsParams(
            productId = null,
            bin = null
        )

        val paymentMethodsRequest = getPaymentMethodsParams.toRequest()

        assertEquals(getPaymentMethodsParams.productId, paymentMethodsRequest.productId)
        assertEquals(getPaymentMethodsParams.bin, paymentMethodsRequest.bin)
    }
}
