package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import kotlin.test.Test

internal class ReviewConfirmAnalyticsTrackerTest {
    private fun makeTracker(
        paymentMethodId: String = "master",
        paymentMethodType: String = "credit_card",
        issuerId: String? = "123",
        productId: String? = "card-1",
        installments: Int = 3,
        amount: String = "300.0",
    ) = ReviewConfirmAnalyticsTracker(
        processOrderParams = ProcessOrderParams(
            orderId = "order-123",
            clientToken = "token-abc",
            paymentMethodId = paymentMethodId,
            paymentMethodType = paymentMethodType,
            token = "card-token-xyz",
            installments = installments,
            amount = amount,
            issuerId = issuerId,
            productId = productId,
        ),
    )

    @Test
    fun `trackImpression does not throw`() {
        makeTracker().trackImpression()
    }

    @Test
    fun `trackImpression does not throw when issuerId productId are null`() {
        makeTracker(issuerId = null, productId = null).trackImpression()
    }

    @Test
    fun `trackImpression does not throw with non numeric amount`() {
        makeTracker(amount = "not-a-number").trackImpression()
    }

    @Test
    fun `trackContinue does not throw`() {
        makeTracker().trackContinue()
    }

    @Test
    fun `trackBack does not throw`() {
        makeTracker().trackBack()
    }

    @Test
    fun `trackPaymentMethodChanged does not throw`() {
        makeTracker().trackPaymentMethodChanged()
    }

    @Test
    fun `trackPayerFieldChanged does not throw`() {
        makeTracker().trackPayerFieldChanged()
    }
}
