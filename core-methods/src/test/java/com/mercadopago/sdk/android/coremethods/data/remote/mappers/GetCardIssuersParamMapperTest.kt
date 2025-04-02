package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.domain.model.params.GetCardIssuersParams
import org.junit.Assert.assertEquals
import kotlin.test.Test

class GetCardIssuersParamMapperTest {

    @Test
    fun `when call GetCardIssuersParams toRequest should correctly map GetCardIssuersParams to CardIssuersRequest`() {
        // Arrange
        val getCardIssuersParams = GetCardIssuersParams(
            productId = "product_001",
            bin = 123456,
            paymentMethodId = "payment_method_001"
        )

        // Act
        val cardIssuersRequest = getCardIssuersParams.toRequest()

        // Assert
        assertEquals(getCardIssuersParams.productId, cardIssuersRequest.productId)
        assertEquals(getCardIssuersParams.bin, cardIssuersRequest.bin)
        assertEquals(getCardIssuersParams.paymentMethodId, cardIssuersRequest.paymentMethodId)
    }

    @Test
    fun `when call GetCardIssuersParams toRequest should handle null values correctly`() {
        // Arrange
        val getCardIssuersParams = GetCardIssuersParams(
            productId = null, // Testando com valor nulo
            bin = null, // Testando com valor nulo
            paymentMethodId = null // Testando com valor nulo
        )

        // Act
        val cardIssuersRequest = getCardIssuersParams.toRequest()

        // Assert
        assertEquals(getCardIssuersParams.productId, cardIssuersRequest.productId)
        assertEquals(getCardIssuersParams.bin, cardIssuersRequest.bin)
        assertEquals(getCardIssuersParams.paymentMethodId, cardIssuersRequest.paymentMethodId)
    }
}
