package com.mercadopago.sdk.android.checkout.data.remote.request

import com.google.gson.annotations.SerializedName

internal data class OrderProcessRequest(
    @SerializedName("payment_method_id")
    val paymentMethodId: String,
    @SerializedName("payment_method_type")
    val paymentMethodType: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("installments")
    val installments: Int,
    @SerializedName("amount")
    val amount: String,
)
