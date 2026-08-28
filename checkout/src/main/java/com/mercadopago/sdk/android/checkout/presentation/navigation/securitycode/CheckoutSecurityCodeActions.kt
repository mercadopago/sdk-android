package com.mercadopago.sdk.android.checkout.presentation.navigation.securitycode

internal class CheckoutSecurityCodeActions(
    val onTokenSuccess: (cardId: String, token: String) -> Unit,
    val onTokenError: () -> Unit,
    val onUserCancelled: () -> Unit,
)
