package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.MPPayer
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class CheckoutConfigurationExtensionsTest {
    private val order = MPOrder(
        orderId = "ORD_TEST",
        amount = BigDecimal("188000.00"),
        payer = MPPayer(email = "buyer@mp.com"),
        clientToken = "token-test",
    )

    private fun configWith(
        checkoutType: MPCheckoutType<*, *>?,
    ) =
        checkoutType?.let { CheckoutConfiguration(checkoutType = it, paymentMethodConfigs = emptyList()) }

    @Test
    fun `given card save when toCheckoutType then returns card_save`() {
        assertEquals(CARD_SAVE, configWith(MPCheckoutType.CardSave).toCheckoutType())
    }

    @Test
    fun `given card transaction when toCheckoutType then returns card_transaction`() {
        assertEquals(CARD_TRANSACTION, configWith(MPCheckoutType.CardTransaction(order)).toCheckoutType())
    }

    @Test
    fun `given payment when toCheckoutType then returns payment`() {
        val checkoutType = MPCheckoutType.Payment(order = order, cardIds = null)

        assertEquals(PAYMENT, configWith(checkoutType).toCheckoutType())
    }

    @Test
    fun `given null configuration when toCheckoutType then returns empty`() {
        assertEquals("", configWith(null).toCheckoutType())
    }

    @Test
    fun `given card transaction when getCardFormAmount then returns order amount`() {
        val config = configWith(MPCheckoutType.CardTransaction(order))!!

        assertEquals(order.amount, config.getCardFormAmount())
        assertEquals("188000.00", config.getCardFormAmountOrZero())
    }

    @Test
    fun `given payment when getCardFormAmount then returns null and zero default`() {
        val config = configWith(MPCheckoutType.Payment(order = order, cardIds = null))!!

        assertNull(config.getCardFormAmount())
        assertEquals(AMOUNT_DEFAULT, config.getCardFormAmountOrZero())
    }

    @Test
    fun `given payment then exposes order and cardIds`() {
        val checkoutType = MPCheckoutType.Payment(
            order = order,
            cardIds = listOf("1562188766", "2409911615"),
        )

        assertEquals(listOf("1562188766", "2409911615"), checkoutType.cardIds)
        assertEquals(order, checkoutType.order)
    }

    @Test
    fun `given payment when copying with new cardIds then keeps remaining fields`() {
        val original = MPCheckoutType.Payment(order = order, cardIds = null)

        val updated = original.copy(cardIds = listOf("card_1"))

        assertEquals(listOf("card_1"), updated.cardIds)
        assertEquals(original.order, updated.order)
        assertTrue(original != updated)
    }

    @Test
    fun `given payment then toString and hashCode reflect its content`() {
        val checkoutType = MPCheckoutType.Payment(order = order, cardIds = listOf("1"))
        val same = MPCheckoutType.Payment(order = order, cardIds = listOf("1"))

        assertTrue(checkoutType.toString().contains("Payment"))
        assertEquals(same.hashCode(), checkoutType.hashCode())
        assertEquals(order, checkoutType.component1())
        assertEquals(listOf("1"), checkoutType.component2())
    }

    @Test
    fun `given card transaction then toString reflects its content`() {
        val checkoutType = MPCheckoutType.CardTransaction(order)

        assertTrue(checkoutType.toString().contains("CardTransaction"))
        assertEquals(order, checkoutType.component1())
    }

    @Test
    fun `given card transaction when showsInstallments then returns true`() {
        assertTrue(configWith(MPCheckoutType.CardTransaction(order)).isCardTransaction())
    }

    @Test
    fun `given card save when showsInstallments then returns false`() {
        assertFalse(configWith(MPCheckoutType.CardSave).isCardTransaction())
    }

    @Test
    fun `given payment when showsInstallments then returns false`() {
        assertFalse(configWith(MPCheckoutType.Payment(order = order, cardIds = null)).isCardTransaction())
    }

    @Test
    fun `given null configuration when showsInstallments then returns false`() {
        assertFalse(configWith(null).isCardTransaction())
    }

    @Test
    fun `given payment when startsWithPayment then returns true`() {
        assertTrue(configWith(MPCheckoutType.Payment(order = order, cardIds = null)).startsWithPayment())
    }

    @Test
    fun `given card transaction when startsWithPayment then returns false`() {
        assertFalse(configWith(MPCheckoutType.CardTransaction(order)).startsWithPayment())
    }

    @Test
    fun `given card save when startsWithPayment then returns false`() {
        assertFalse(configWith(MPCheckoutType.CardSave).startsWithPayment())
    }

    @Test
    fun `given null configuration when startsWithPayment then returns false`() {
        assertFalse(configWith(null).startsWithPayment())
    }
}
