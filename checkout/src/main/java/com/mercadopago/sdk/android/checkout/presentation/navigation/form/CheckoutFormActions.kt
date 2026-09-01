package com.mercadopago.sdk.android.checkout.presentation.navigation.form

import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams

internal class CheckoutFormActions(
    val onOpenInstallments: (MPInstallmentData, MPPaymentData) -> Unit,
    val onInstallmentConfirmed: (Int) -> Unit,
    val onOpenReview: (ProcessOrderParams) -> Unit,
    val onFinishCheckout: (MercadoPagoCheckoutResult<*, *>) -> Unit,
    val onInvalidInstallmentData: (MercadoPagoCheckoutError) -> Unit,
    val onMarkScreenPresented: (Screen) -> Unit,
)
