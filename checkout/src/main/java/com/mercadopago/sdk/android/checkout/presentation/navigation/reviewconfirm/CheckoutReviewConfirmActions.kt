package com.mercadopago.sdk.android.checkout.presentation.navigation.reviewconfirm

import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.presentation.navigation.ReviewOrigin

internal class CheckoutReviewConfirmActions(
    val onNavigateUp: (ReviewOrigin) -> Unit,
    val onReturnToPaymentSelector: () -> Unit,
    val onReturnToPaymentSelectorWithGenericError: () -> Unit,
    val onFinishCheckout: (MercadoPagoCheckoutResult<*, *>) -> Unit,
    val onFinishForEmailChange: ((() -> Unit)?) -> Unit,
)
