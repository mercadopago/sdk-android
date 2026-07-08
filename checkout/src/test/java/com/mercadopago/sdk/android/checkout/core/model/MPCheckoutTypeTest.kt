package com.mercadopago.sdk.android.checkout.core.model

import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class MPCheckoutTypeTest {
    private val order = MPOrder(
        orderId = "ORD_TEST",
        amount = BigDecimal("100.00"),
        payer = MPPayer(email = "buyer@test.com"),
        clientToken = "token-test",
    )

    @Test
    fun `given card transaction then exposes order`() {
        val type = MPCheckoutType.CardTransaction(order = order)

        assertEquals(order, type.order)
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
    fun `given payment then exposes order`() {
        val type = MPCheckoutType.Payment(order = order)

        assertEquals(order, type.order)
    }

    @Test
    fun `given payment when copy with new order then equality changes`() {
        val type = MPCheckoutType.Payment(order = order)
        val newOrder = order.copy(amount = BigDecimal("200.00"))

        val updated = type.copy(order = newOrder)

        assertEquals(newOrder, updated.order)
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
        val first = MPCheckoutType.Payment(order = order)
        val second = MPCheckoutType.Payment(order = order)

        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
        assertTrue(first.toString().isNotEmpty())
    }
}
