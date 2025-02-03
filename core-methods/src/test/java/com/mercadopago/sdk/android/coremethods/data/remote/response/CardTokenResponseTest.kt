package com.mercadopago.sdk.android.coremethods.data.remote.response

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import kotlin.test.Test

internal class CardTokenResponseTest {
    @Test
    fun `test CardTokenResponse creation with default values`() {
        // Cria uma instância de CardTokenResponse com valores padrão
        val cardTokenResponse = CardTokenResponse()

        // Verifica se os valores padrão são esperados
        assertNull(cardTokenResponse.id)
        assertNull(cardTokenResponse.publicKey)
        assertNull(cardTokenResponse.luhnValidation)
        assertNull(cardTokenResponse.status)
        assertNull(cardTokenResponse.dateUsed)
        assertNull(cardTokenResponse.cardNumberLength)
        assertNull(cardTokenResponse.truncCardNumber)
        assertNull(cardTokenResponse.securityCodeLength)
        assertNull(cardTokenResponse.expirationMonth)
        assertNull(cardTokenResponse.expirationYear)
        assertNull(cardTokenResponse.firstSixDigits)
        assertNull(cardTokenResponse.lastFourDigits)
        assertNull(cardTokenResponse.liveMode)
        assertNull(cardTokenResponse.cardholder)
        assertNull(cardTokenResponse.esc)
    }

    @Test
    fun `test CardTokenResponse creation with specified values`() {
        // Cria uma instância de CardHolderResponse
        val identificationResponse = IdentificationResponse(number = "123456789", type = "CPF")
        val cardHolderResponse = CardHolderResponse(identification = identificationResponse, name = "John Doe")

        // Cria uma instância de CardTokenResponse com valores especificados
        val cardTokenResponse = CardTokenResponse(
            id = "token_123",
            publicKey = "public_key_123",
            luhnValidation = "VALID",
            status = "approved",
            dateUsed = "2023-01-01",
            cardNumberLength = 16,
            truncCardNumber = "411111****1111",
            securityCodeLength = 3,
            expirationMonth = 12,
            expirationYear = 2025,
            firstSixDigits = "411111",
            lastFourDigits = "1111",
            liveMode = "true",
            cardholder = cardHolderResponse,
            esc = "esc_value"
        )

        // Verifica se os valores estão corretos
        assertEquals("token_123", cardTokenResponse.id)
        assertEquals("public_key_123", cardTokenResponse.publicKey)
        assertEquals("VALID", cardTokenResponse.luhnValidation)
        assertEquals("approved", cardTokenResponse.status)
        assertEquals("2023-01-01", cardTokenResponse.dateUsed)
        assertEquals(16, cardTokenResponse.cardNumberLength)
        assertEquals("411111****1111", cardTokenResponse.truncCardNumber)
        assertEquals(3, cardTokenResponse.securityCodeLength)
        assertEquals(12, cardTokenResponse.expirationMonth)
        assertEquals(2025, cardTokenResponse.expirationYear)
        assertEquals("411111", cardTokenResponse.firstSixDigits)
        assertEquals("1111", cardTokenResponse.lastFourDigits)
        assertEquals("true", cardTokenResponse.liveMode)
        assertNotNull(cardTokenResponse.cardholder)
        assertEquals("John Doe", cardTokenResponse.cardholder?.name)
        assertEquals("123456789", cardTokenResponse.cardholder?.identification?.number)
        assertEquals("CPF", cardTokenResponse.cardholder?.identification?.type)
        assertEquals("esc_value", cardTokenResponse.esc)
    }

    @Test
    fun `test identification response creation`() {
        // Cria uma instância de IdentificationResponse
        val identificationResponse = IdentificationResponse(number = "987654321", type = "CNPJ")

        // Verifica se os valores estão corretos
        assertEquals("987654321", identificationResponse.number)
        assertEquals("CNPJ", identificationResponse.type)
    }

    @Test
    fun `test card holder response creation`() {
        // Cria uma instância de IdentificationResponse
        val identificationResponse = IdentificationResponse(number = "123456789", type = "CPF")
        val cardHolderResponse = CardHolderResponse(identification = identificationResponse, name = "Jane Doe")

        // Verifica se os valores estão corretos
        assertEquals("Jane Doe", cardHolderResponse.name)
        assertNotNull(cardHolderResponse.identification)
        assertEquals("123456789", cardHolderResponse.identification?.number)
        assertEquals("CPF", cardHolderResponse.identification?.type)
    }
}
