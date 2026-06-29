package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.response.CardIssuerResponse
import org.junit.Assert.assertEquals
import kotlin.test.Test

internal class GetCardIssuerResponseMapperTest {
    @Test
    fun `when call CardIssuerResponse toModel should correctly map CardIssuerResponse to CardIssuer`() {
        val cardIssuerResponse = CardIssuerResponse(
            id = "issuer_001",
            merchantAccountId = "merchant_account_001",
            processingMode = "gateway",
            status = "active",
            thumbnail = "http://example.com/thumbnail.png",
        )

        val cardIssuer = cardIssuerResponse.toModel()

        assertEquals(cardIssuerResponse.id, cardIssuer.id)
        assertEquals(cardIssuerResponse.merchantAccountId, cardIssuer.merchantAccountId)
        assertEquals(cardIssuerResponse.processingMode, cardIssuer.processingMode)
        assertEquals(cardIssuerResponse.status, cardIssuer.status)
        assertEquals(cardIssuerResponse.thumbnail, cardIssuer.thumbnail)
    }

    @Test
    fun `when call CardIssuerResponse toModel should handle null values correctly`() {
        val cardIssuerResponse = CardIssuerResponse(
            id = "issuer_002",
            merchantAccountId = null,
            processingMode = "aggregator",
            status = "inactive",
            thumbnail = null,
        )

        val cardIssuer = cardIssuerResponse.toModel()

        assertEquals(cardIssuerResponse.id, cardIssuer.id)
        assertEquals(cardIssuerResponse.merchantAccountId, cardIssuer.merchantAccountId)
        assertEquals(cardIssuerResponse.processingMode, cardIssuer.processingMode)
        assertEquals(cardIssuerResponse.status, cardIssuer.status)
        assertEquals(cardIssuerResponse.thumbnail, cardIssuer.thumbnail)
    }
}
