package com.mercadopago.sdk.android.coremethods.data.remote.request

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import kotlin.test.Test

internal class CardTokenBodyRequestTest {

    @Test
    fun `test CardTokenBodyRequest creation with default values`() {
        // Cria uma instância de CardTokenBodyRequest com valores padrão
        val cardTokenBodyRequest = CardTokenBodyRequest()

        // Verifica se os valores padrão são esperados
        assertNull(cardTokenBodyRequest.cardId)
        assertNull(cardTokenBodyRequest.esc)
        assertFalse(cardTokenBodyRequest.requireEsc)
        assertNull(cardTokenBodyRequest.cardNumber)
        assertNull(cardTokenBodyRequest.securityCode)
        assertNull(cardTokenBodyRequest.expirationMonth)
        assertNull(cardTokenBodyRequest.expirationYear)
        assertNull(cardTokenBodyRequest.buyerIdentification)
    }

    @Test
    fun `test CardTokenBodyRequest creation with specified values`() {
        // Preparar dados
        val buyerIdentification = BuyerIdentificationBodyRequest(name = "John Doe", number = "123456789", type = "CPF")
        val cardTokenBodyRequest = CardTokenBodyRequest(
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
        assertEquals("card_123", cardTokenBodyRequest.cardId)
        assertEquals("esc_value", cardTokenBodyRequest.esc)
        assertFalse(cardTokenBodyRequest.requireEsc)
        assertEquals("4111111111111111", cardTokenBodyRequest.cardNumber)
        assertEquals("123", cardTokenBodyRequest.securityCode)
        assertEquals(12, cardTokenBodyRequest.expirationMonth)
        assertEquals(2025, cardTokenBodyRequest.expirationYear)
        assertEquals(buyerIdentification, cardTokenBodyRequest.buyerIdentification)
    }

    @Test
    fun `test BuyerIdentificationBodyRequest creation`() {
        // Prepare data for BuyerIdentificationBodyRequest
        val buyerIdentification = BuyerIdentificationBodyRequest(name = "Jane Doe", number = "987654321", type = "CNPJ")

        // Verify that the properties are properly set
        assertEquals("Jane Doe", buyerIdentification.name)
        assertEquals("987654321", buyerIdentification.number)
        assertEquals("CNPJ", buyerIdentification.type)
    }

    @Test
    fun `test equality of CardTokenBodyRequest instances`() {
        val buyerIdentification1 = BuyerIdentificationBodyRequest(name = "John Doe", number = "123456789", type = "CPF")
        val buyerIdentification2 = BuyerIdentificationBodyRequest(name = "John Doe", number = "123456789", type = "CPF")

        val cardTokenBodyRequest1 = CardTokenBodyRequest(
            cardId = "card_001",
            buyerIdentification = buyerIdentification1
        )
        val cardTokenBodyRequest2 = CardTokenBodyRequest(
            cardId = "card_001",
            buyerIdentification = buyerIdentification2
        )

        // Verifica se as duas instâncias são consideradas iguais
        assertEquals(cardTokenBodyRequest1, cardTokenBodyRequest2)
    }
}
