package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo

internal data class ReviewConfirmScreenConfig(
    val sellerInfo: MPSellerInfo?,
    val emailChangeEnabled: Boolean,
)
