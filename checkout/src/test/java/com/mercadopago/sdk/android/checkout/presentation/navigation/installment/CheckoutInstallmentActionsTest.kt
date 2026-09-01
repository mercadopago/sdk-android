package com.mercadopago.sdk.android.checkout.presentation.navigation.installment

import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

internal class CheckoutInstallmentActionsTest {
    private var confirmedInstallment: Int? = null
    private var checkoutResult: MercadoPagoCheckoutResult<*, *>? = null

    private val actions = CheckoutInstallmentActions(
        onOpenReview = {},
        onFinishCheckout = { checkoutResult = it },
        onReturnToPaymentSelectorWithGenericError = {},
        onBackClick = {},
        onMarkScreenPresented = {},
        onInstallmentConfirmed = { confirmedInstallment = it },
    )

    @Test
    fun `given payment when confirming installment then routes to payment`() {
        // Given
        val paymentData = MPPaymentData.Payment(
            orderId = "",
            orderStatus = "",
            paymentMethodId = "visa",
            paymentTypeId = "credit_card",
        )

        // When
        actions.confirmInstallment(paymentData, installment = 3)

        // Then
        assertEquals(3, confirmedInstallment)
        assertNull(checkoutResult)
    }

    @Test
    fun `given card transaction when confirming installment then routes to card form`() {
        // Given
        val paymentData = MPPaymentData.CardTransaction(
            orderId = "",
            orderStatus = "",
            paymentMethodId = "visa",
            paymentTypeId = "credit_card",
        )

        // When
        actions.confirmInstallment(paymentData, installment = 6)

        // Then
        assertEquals(6, confirmedInstallment)
        assertNull(checkoutResult)
    }

    @Test
    fun `given card save when confirming installment then finishes checkout`() {
        // Given
        val paymentData = MPPaymentData.CardSave(
            token = "TOKEN_123",
            paymentMethodId = "visa",
            paymentTypeId = "credit_card",
            payer = null,
            issuerId = null,
        )

        // When
        actions.confirmInstallment(paymentData, installment = 1)

        // Then
        val result = assertIs<MercadoPagoCheckoutResult.Success<*>>(checkoutResult)
        assertEquals(paymentData, result.paymentData)
        assertNull(confirmedInstallment)
    }
}
