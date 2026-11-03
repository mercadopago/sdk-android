package com.mercadopago.sdk.android.checkout.domain.model

import com.google.gson.annotations.SerializedName

internal data class ReviewConfirmHeader(
    @SerializedName("title") val title: String,
    @SerializedName("seller_name") val sellerName: String?,
    @SerializedName("seller_icon_url") val sellerIconUrl: String?,
)
