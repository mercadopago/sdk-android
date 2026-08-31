package com.mercadopago.sdk.android.checkout.domain.model.params

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

internal class ProcessOrderParamsTest {
    private val baseParams = ProcessOrderParams(
        orderId = "ORD_001",
        clientToken = "token-abc",
        paymentMethodId = "visa",
        paymentMethodType = "credit_card",
        token = "card_token",
        installments = 1,
        amount = "100.00",
    )

    @Test
    fun `given no bin then bin defaults to null`() {
        // Given / When
        val params = baseParams

        // Then
        assertNull(params.bin)
    }

    @Test
    fun `given no card id then card id defaults to null`() {
        assertNull(baseParams.cardId)
    }

    @Test
    fun `given card id when creating params then card id is exposed`() {
        val params = baseParams.copy(cardId = "card_123")

        assertEquals("card_123", params.cardId)
    }

    @Test
    fun `given bin when creating params then bin is exposed`() {
        // Given / When
        val params = baseParams.copy(bin = "453998")

        // Then
        assertEquals("453998", params.bin)
    }

    @Test
    fun `given ticket params then bin is null`() {
        // Given / When
        val params = ProcessOrderParams(
            orderId = "ORD_002",
            clientToken = "token-abc",
            paymentMethodId = "rapipago",
            paymentMethodType = "ticket",
            token = "",
            installments = 0,
            amount = "50.00",
            bin = null,
        )

        // Then
        assertNull(params.bin)
    }

    @Test
    fun `given equal params then they are equal`() {
        // Given
        val first = baseParams.copy(bin = "453998")
        val second = baseParams.copy(bin = "453998")

        // Then
        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
    }

    @Test
    fun `given params with different bin then they are not equal`() {
        // Given
        val first = baseParams.copy(bin = "453998")
        val second = baseParams.copy(bin = "411111")

        // Then
        assertNotEquals(first, second)
    }
}
