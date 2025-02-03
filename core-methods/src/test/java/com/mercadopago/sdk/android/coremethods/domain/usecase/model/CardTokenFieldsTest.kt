package com.mercadopago.sdk.android.coremethods.domain.usecase.model

import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.domain.model.CardTokenFields
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

internal class CardTokenFieldsTest {

    @Test
    fun `test CardTokenFields creation with default values`() {
        // Cria uma instância de CardTokenFields com valores padrão
        val cardTokenFields = CardTokenFields()

        // Verifica se os valores padrão são os esperados
        assertNull(cardTokenFields.cardId)
        assertNull(cardTokenFields.esc)
        assertFalse(cardTokenFields.requireEsc)
        assertNull(cardTokenFields.cardNumber)
        assertNull(cardTokenFields.securityCode)
        assertNull(cardTokenFields.expirationMonth)
        assertNull(cardTokenFields.expirationYear)
        assertNull(cardTokenFields.buyerIdentification)
    }

    @Test
    fun `test CardTokenFields creation with specified values`() {
        // Preparar os dados
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

        // Verifica se os valores estão corretos
        assertEquals("card_123", cardTokenFields.cardId)
        assertEquals("esc_value", cardTokenFields.esc)
        assertFalse(cardTokenFields.requireEsc)
        assertEquals("4111111111111111", cardTokenFields.cardNumber)
        assertEquals("123", cardTokenFields.securityCode)
        assertEquals(12, cardTokenFields.expirationMonth)
        assertEquals(2025, cardTokenFields.expirationYear)
        assertEquals(buyerIdentification, cardTokenFields.buyerIdentification)
    }

    @Test
    fun `test CardTokenFields equality`() {
        // Cria duas instâncias idênticas
        val buyerIdentification = BuyerIdentification(name = "Jane Doe", number = "987654321", type = "CNPJ")
        val cardTokenFields1 = CardTokenFields(
            cardId = "card_456",
            esc = null,
            requireEsc = true,
            cardNumber = "4111111111111111",
            securityCode = "456",
            expirationMonth = 11,
            expirationYear = 2024,
            buyerIdentification = buyerIdentification
        )
        val cardTokenFields2 = CardTokenFields(
            cardId = "card_456",
            esc = null,
            requireEsc = true,
            cardNumber = "4111111111111111",
            securityCode = "456",
            expirationMonth = 11,
            expirationYear = 2024,
            buyerIdentification = buyerIdentification
        )

        // Verifica se as duas instâncias são consideradas iguais
        assertEquals(cardTokenFields1, cardTokenFields2)
    }
}
