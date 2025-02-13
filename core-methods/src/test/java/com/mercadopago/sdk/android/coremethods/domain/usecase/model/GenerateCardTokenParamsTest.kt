package com.mercadopago.sdk.android.coremethods.domain.usecase.model

import com.mercadopago.sdk.android.coremethods.domain.model.params.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.domain.model.params.GenerateCardTokenParams
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

internal class GenerateCardTokenParamsTest {
    @Test
    fun `test CardTokenFields creation with default values`() {
        val generateCardTokenParams = GenerateCardTokenParams()

        assertNull(generateCardTokenParams.cardId)
        assertNull(generateCardTokenParams.esc)
        assertFalse(generateCardTokenParams.requireEsc)
        assertNull(generateCardTokenParams.cardNumber)
        assertNull(generateCardTokenParams.securityCode)
        assertNull(generateCardTokenParams.expirationMonth)
        assertNull(generateCardTokenParams.expirationYear)
        assertNull(generateCardTokenParams.buyerIdentification)
    }

    @Test
    fun `test CardTokenFields creation with specified values`() {
        val buyerIdentification = BuyerIdentification(name = "John Doe", number = "123456789", type = "CPF")
        val generateCardTokenParams = GenerateCardTokenParams(
            cardId = "card_123",
            esc = "esc_value",
            requireEsc = false,
            cardNumber = "4111111111111111",
            securityCode = "123",
            expirationMonth = 12,
            expirationYear = 2025,
            buyerIdentification = buyerIdentification,
        )

        assertEquals("card_123", generateCardTokenParams.cardId)
        assertEquals("esc_value", generateCardTokenParams.esc)
        assertFalse(generateCardTokenParams.requireEsc)
        assertEquals("4111111111111111", generateCardTokenParams.cardNumber)
        assertEquals("123", generateCardTokenParams.securityCode)
        assertEquals(12, generateCardTokenParams.expirationMonth)
        assertEquals(2025, generateCardTokenParams.expirationYear)
        assertEquals(buyerIdentification, generateCardTokenParams.buyerIdentification)
    }

    @Test
    fun `test CardTokenFields equality`() {
        val buyerIdentification = BuyerIdentification(name = "Jane Doe", number = "987654321", type = "CNPJ")
        val generateCardTokenParams1 = GenerateCardTokenParams(
            cardId = "card_456",
            esc = null,
            requireEsc = true,
            cardNumber = "4111111111111111",
            securityCode = "456",
            expirationMonth = 11,
            expirationYear = 2024,
            buyerIdentification = buyerIdentification,
        )
        val generateCardTokenParams2 = GenerateCardTokenParams(
            cardId = "card_456",
            esc = null,
            requireEsc = true,
            cardNumber = "4111111111111111",
            securityCode = "456",
            expirationMonth = 11,
            expirationYear = 2024,
            buyerIdentification = buyerIdentification,
        )

        assertEquals(generateCardTokenParams1, generateCardTokenParams2)
    }
}
