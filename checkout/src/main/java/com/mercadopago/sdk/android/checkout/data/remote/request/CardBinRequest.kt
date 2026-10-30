package com.mercadopago.sdk.android.checkout.data.remote.request

internal data class CardBinRequest(
    val bin: String,
    val orderId: String,
    val clientToken: String?,
    val amount: String,
    val checkoutType: String,
    val processingMode: String,
    val excludedPaymentTypes: String?,
    val excludedPaymentMethods: String?,
    val screens: String? = null,
)
