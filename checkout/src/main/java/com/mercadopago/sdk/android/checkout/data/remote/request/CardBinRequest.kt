package com.mercadopago.sdk.android.checkout.data.remote.request

internal data class CardBinRequest(
    val bin: String,
    val amount: String,
    val checkoutType: String,
    val processingMode: String,
    val allowCardTypes: String?,
    val allowCardBrands: String?,
)
