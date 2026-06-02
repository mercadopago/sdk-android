package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPInstallment
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.MPPayer
import com.mercadopago.sdk.android.checkout.core.model.MPPaymentMethodConfig
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class CheckoutConfigurationExtensionsTest {
    private val order = MPOrder(amount = BigDecimal("188000.00"), payer = MPPayer(email = "buyer@mp.com"))

    private fun configWith(
        checkoutType: MPCheckoutType<*>?,
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
    fun `given payment selection when toCheckoutType then returns payment_selection`() {
        val checkoutType = MPCheckoutType.PaymentSelection(order = order, cardIds = null)

        assertEquals(PAYMENT_SELECTION, configWith(checkoutType).toCheckoutType())
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
    fun `given payment selection when getCardFormAmount then returns null and zero default`() {
        val config = configWith(MPCheckoutType.PaymentSelection(order = order, cardIds = null))!!

        assertNull(config.getCardFormAmount())
        assertEquals(AMOUNT_DEFAULT, config.getCardFormAmountOrZero())
    }

    @Test
    fun `given payment selection then defaults show review confirm and status screen`() {
        val checkoutType = MPCheckoutType.PaymentSelection(
            order = order,
            cardIds = listOf("1562188766", "2409911615"),
        )

        assertTrue(checkoutType.showReviewConfirm)
        assertTrue(checkoutType.showStatusScreen)
        assertEquals(listOf("1562188766", "2409911615"), checkoutType.cardIds)
        assertEquals(order, checkoutType.order)
    }

    @Test
    fun `given payment selection when overriding flags then copy keeps remaining fields`() {
        val original = MPCheckoutType.PaymentSelection(order = order, cardIds = null)

        val updated = original.copy(showReviewConfirm = false, showStatusScreen = false)

        assertEquals(false, updated.showReviewConfirm)
        assertEquals(false, updated.showStatusScreen)
        assertEquals(original.order, updated.order)
        assertTrue(original != updated)
    }

    @Test
    fun `given payment brick cancelled context then it is a user cancelled context`() {
        assertTrue(MPUserCancelledContext.PaymentBrick is MPUserCancelledContext)
    }

    @Test
    fun `given payment selection then toString and hashCode reflect its content`() {
        val checkoutType = MPCheckoutType.PaymentSelection(order = order, cardIds = listOf("1"))
        val same = MPCheckoutType.PaymentSelection(order = order, cardIds = listOf("1"))

        assertTrue(checkoutType.toString().contains("PaymentSelection"))
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
    fun `given loan payment method config then exposes its installment`() {
        val installment = MPInstallment(minInstallments = 1, maxInstallments = 6)

        val config = MPPaymentMethodConfig.Loan(installment = installment)

        assertEquals(installment, config.installment)
        assertEquals(1, installment.minInstallments)
        assertEquals(6, installment.maxInstallments)
    }
}
