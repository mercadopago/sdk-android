package com.mercadopago.sdk.android.checkout.core.model

import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class MPCheckoutTypeTest {
    private val order = MPOrder(amount = BigDecimal("100.00"), payer = MPPayer(email = "buyer@test.com"))

    @Test
    fun `given card save then it is a checkout type`() {
        assertTrue(MPCheckoutType.CardSave is MPCheckoutType<*, *>)
    }

    @Test
    fun `given card transaction then exposes order`() {
        val type = MPCheckoutType.CardTransaction(order = order)

        assertEquals(order, type.order)
        assertTrue(type is MPCheckoutType<*, *>)
    }

    @Test
    fun `given card transaction when copy with new order then equality changes`() {
        val type = MPCheckoutType.CardTransaction(order = order)
        val newOrder = order.copy(amount = BigDecimal("200.00"))

        val updated = type.copy(order = newOrder)

        assertEquals(newOrder, updated.order)
        assertNotEquals(type, updated)
    }

    @Test
    fun `given payment then exposes order and null cardIds`() {
        val type = MPCheckoutType.Payment(order = order, cardIds = null)

        assertEquals(order, type.order)
        assertNull(type.cardIds)
    }

    @Test
    fun `given payment with explicit cardIds then exposes provided values`() {
        val type = MPCheckoutType.Payment(
            order = order,
            cardIds = listOf("card_1", "card_2"),
        )

        assertEquals(listOf("card_1", "card_2"), type.cardIds)
    }

    @Test
    fun `given payment when copy with new cardIds then equality changes`() {
        val type = MPCheckoutType.Payment(order = order, cardIds = emptyList())

        val updated = type.copy(cardIds = listOf("card_1"))

        assertEquals(listOf("card_1"), updated.cardIds)
        assertNotEquals(type, updated)
    }

    @Test
    fun `given card save then toString and equality execute object body`() {
        assertEquals(MPCheckoutType.CardSave, MPCheckoutType.CardSave)
        assertTrue(MPCheckoutType.CardSave.toString().isNotEmpty())
        assertEquals(MPCheckoutType.CardSave.hashCode(), MPCheckoutType.CardSave.hashCode())
    }

    @Test
    fun `given equal card transactions then they are equal with same hashCode`() {
        val first = MPCheckoutType.CardTransaction(order = order)
        val second = MPCheckoutType.CardTransaction(order = order)

        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
        assertTrue(first.toString().isNotEmpty())
    }

    @Test
    fun `given equal payments then they are equal with same hashCode`() {
        val first = MPCheckoutType.Payment(order = order, cardIds = listOf("card_1"))
        val second = MPCheckoutType.Payment(order = order, cardIds = listOf("card_1"))

        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
        assertTrue(first.toString().isNotEmpty())
    }
}
