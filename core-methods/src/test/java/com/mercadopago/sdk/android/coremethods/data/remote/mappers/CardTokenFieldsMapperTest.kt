package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.domain.model.CardTokenFields
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import kotlin.test.Test

internal class CardTokenFieldsMapperTest {
    @Test
    fun `test toCardTokenRequest conversion`() {
        val buyerIdentification = BuyerIdentification(name = "John Doe", number = "123456789", type = "CPF")

        val cardTokenFields = CardTokenFields(
            cardId = "card_123",
            esc = "esc_value",
            requireEsc = false,
            cardNumber = "4111111111111111",
            securityCode = "123",
            expirationMonth = 12,
            expirationYear = 2025,
            buyerIdentification = buyerIdentification
        )

        val cardTokenRequest = cardTokenFields.toCardTokenRequest()

        assertEquals("card_123", cardTokenRequest.cardId)
        assertEquals("esc_value", cardTokenRequest.esc)
        assertFalse(cardTokenRequest.requireEsc)
        assertEquals("4111111111111111", cardTokenRequest.cardNumber)
        assertEquals("123", cardTokenRequest.securityCode)
        assertEquals(12, cardTokenRequest.expirationMonth)
        assertEquals(2025, cardTokenRequest.expirationYear)

        assertNotNull(cardTokenRequest.buyerIdentification)
        assertEquals("John Doe", cardTokenRequest.buyerIdentification?.name)
        assertEquals("123456789", cardTokenRequest.buyerIdentification?.number)
        assertEquals("CPF", cardTokenRequest.buyerIdentification?.type)
    }

    @Test
    fun `test toBuyerIdentificationRequest conversion`() {
        val buyerIdentification = BuyerIdentification(name = "Jane Doe", number = "987654321", type = "CNPJ")

        val buyerIdentificationRequest = buyerIdentification.toBuyerIdentificationRequest()

        assertEquals("Jane Doe", buyerIdentificationRequest.name)
        assertEquals("987654321", buyerIdentificationRequest.number)
        assertEquals("CNPJ", buyerIdentificationRequest.type)
    }
}
