package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.domain.model.params.GetCardIssuersParams
import org.junit.Assert.assertEquals
import kotlin.test.Test

class GetCardIssuersParamMapperTest {
    @Test
    fun `when call GetCardIssuersParams toRequest should correctly map GetCardIssuersParams to CardIssuersRequest`() {
        val getCardIssuersParams = GetCardIssuersParams(
            bin = 123456,
            paymentMethodId = "payment_method_001",
        )

        val cardIssuersRequest = getCardIssuersParams.toRequest()

        assertEquals(getCardIssuersParams.bin, cardIssuersRequest.bin)
        assertEquals(getCardIssuersParams.paymentMethodId, cardIssuersRequest.paymentMethodId)
    }

    @Test
    fun `when call GetCardIssuersParams toRequest should handle null values correctly`() {
        val getCardIssuersParams = GetCardIssuersParams(
            bin = null,
            paymentMethodId = null,
        )

        val cardIssuersRequest = getCardIssuersParams.toRequest()
        assertEquals(getCardIssuersParams.bin, cardIssuersRequest.bin)
        assertEquals(getCardIssuersParams.paymentMethodId, cardIssuersRequest.paymentMethodId)
    }
}
