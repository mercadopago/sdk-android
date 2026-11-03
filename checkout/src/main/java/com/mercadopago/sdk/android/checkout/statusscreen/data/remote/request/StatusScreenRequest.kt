package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.request

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.checkout.data.remote.request.SellerInfoRequest

internal data class StatusScreenRequest(
    @SerializedName("order_id")
    val orderId: String,
    @SerializedName("last_four_digits")
    val lastFourDigits: String?,
    @SerializedName("seller_info")
    val sellerInfo: SellerInfoRequest?,
)
