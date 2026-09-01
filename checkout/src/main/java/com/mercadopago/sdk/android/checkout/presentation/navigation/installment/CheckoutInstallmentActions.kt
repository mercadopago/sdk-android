package com.mercadopago.sdk.android.checkout.presentation.navigation.installment

import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams

internal class CheckoutInstallmentActions(
    val onOpenReview: (ProcessOrderParams) -> Unit,
    val onFinishCheckout: (MercadoPagoCheckoutResult<*, *>) -> Unit,
    val onReturnToPaymentSelectorWithGenericError: () -> Unit,
    val onBackClick: () -> Unit,
    val onMarkScreenPresented: (Screen) -> Unit,
    private val onInstallmentConfirmed: (Int) -> Unit,
) {
    fun confirmInstallment(
        paymentData: MPPaymentData,
        installment: Int,
    ) {
        when (paymentData) {
            is MPPaymentData.Payment,
            is MPPaymentData.CardTransaction,
            -> onInstallmentConfirmed(installment)
            is MPPaymentData.CardSave -> onFinishCheckout(MercadoPagoCheckoutResult.Success(paymentData))
        }
    }
}
