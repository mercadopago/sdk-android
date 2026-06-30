package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.MPPayer
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
    fun `given null configuration when showsInstallments then returns false`() {
        assertFalse(configWith(null).isCardTransaction())
    }
}
