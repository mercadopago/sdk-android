package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class CheckoutConfigurationExtensionsTest {
    private val order = MPOrder(
        orderId = "ORD_TEST",
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
        val checkoutType = MPCheckoutType.Payment(order = order)

        assertEquals(PAYMENT, configWith(checkoutType).toCheckoutType())
    }

    @Test
    fun `given null configuration when toCheckoutType then returns empty`() {
        assertEquals("", configWith(null).toCheckoutType())
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
        assertFalse(configWith(MPCheckoutType.Payment(order = order)).isCardTransaction())
    }

    @Test
    fun `given null configuration when showsInstallments then returns false`() {
        assertFalse(configWith(null).isCardTransaction())
    }

    @Test
    fun `given payment when startsWithPayment then returns true`() {
        assertTrue(configWith(MPCheckoutType.Payment(order = order)).startsWithPayment())
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

    @Test
    fun `given payment with sellerInfo when getSellerInfo then returns seller`() {
        val seller = MPSellerInfo(name = "Adidas Store")

        assertEquals(seller, configWith(MPCheckoutType.Payment(order, sellerInfo = seller)).getSellerInfo())
    }

    @Test
    fun `given card transaction with sellerInfo when getSellerInfo then returns seller`() {
        val seller = MPSellerInfo(name = "Nike Store")

        assertEquals(
            seller,
            configWith(MPCheckoutType.CardTransaction(order, sellerInfo = seller)).getSellerInfo(),
        )
    }

    @Test
    fun `given card save when getSellerInfo then returns null`() {
        assertNull(configWith(MPCheckoutType.CardSave).getSellerInfo())
    }

    @Test
    fun `given null configuration when getSellerInfo then returns null`() {
        assertNull(configWith(null).getSellerInfo())
    }

    @Test
    fun `given payment with onEmailChangeRequested when getOnEmailChangeRequested then returns callback`() {
        var called = false
        val config = CheckoutConfiguration(
            checkoutType = MPCheckoutType.Payment(order = order),
            paymentMethodConfigs = emptyList(),
            screenConfigs = listOf(ScreenConfig.ReviewAndConfirm(onEmailChangeRequested = { called = true })),
        )

        config.getOnEmailChangeRequested()?.invoke()

        assertTrue(called)
    }

    @Test
    fun `given payment without ReviewAndConfirm config when getOnEmailChangeRequested then returns null`() {
        val config = CheckoutConfiguration(
            checkoutType = MPCheckoutType.Payment(order = order),
            paymentMethodConfigs = emptyList(),
        )

        assertNull(config.getOnEmailChangeRequested())
    }

    @Test
    fun `given null configuration when getOnEmailChangeRequested then returns null`() {
        assertNull(configWith(null).getOnEmailChangeRequested())
    }
}
