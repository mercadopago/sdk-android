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
        assertTrue(MPCheckoutType.CardSave is MPCheckoutType<*>)
    }

    @Test
    fun `given card transaction then exposes order`() {
        val type = MPCheckoutType.CardTransaction(order = order)

        assertEquals(order, type.order)
        assertTrue(type is MPCheckoutType<*>)
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
    fun `given payment selection with default args then review and status are enabled`() {
        val type = MPCheckoutType.PaymentSelection(order = order, cardIds = null)

        assertEquals(order, type.order)
        assertNull(type.cardIds)
        assertTrue(type.showReviewConfirm)
        assertTrue(type.showStatusScreen)
    }

    @Test
    fun `given payment selection with explicit args then exposes provided values`() {
        val type = MPCheckoutType.PaymentSelection(
            order = order,
            cardIds = listOf("card_1", "card_2"),
            showReviewConfirm = false,
            showStatusScreen = false,
        )

        assertEquals(listOf("card_1", "card_2"), type.cardIds)
        assertEquals(false, type.showReviewConfirm)
        assertEquals(false, type.showStatusScreen)
    }

    @Test
    fun `given payment selection when copy toggling flags then equality changes`() {
        val type = MPCheckoutType.PaymentSelection(order = order, cardIds = emptyList())

        val updated = type.copy(showStatusScreen = false)

        assertEquals(false, updated.showStatusScreen)
        assertTrue(updated.showReviewConfirm)
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
    fun `given equal payment selections then they are equal with same hashCode`() {
        val first = MPCheckoutType.PaymentSelection(order = order, cardIds = listOf("card_1"))
        val second = MPCheckoutType.PaymentSelection(order = order, cardIds = listOf("card_1"))

        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
        assertTrue(first.toString().isNotEmpty())
    }
}
