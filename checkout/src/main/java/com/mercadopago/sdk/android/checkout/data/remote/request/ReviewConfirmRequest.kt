package com.mercadopago.sdk.android.checkout.data.remote.request

import com.google.gson.annotations.SerializedName

internal data class ReviewConfirmRequest(
    @SerializedName("order_id")
    val orderId: String,
    @SerializedName("payment_method_type")
    val paymentMethodType: String,
    @SerializedName("payment_method_id")
    val paymentMethodId: String,
    @SerializedName("issuer_id")
    val issuerId: String?,
    @SerializedName("bin")
    val bin: String?,
    @SerializedName("last_four_digits")
    val lastFourDigits: String?,
    @SerializedName("installments")
    val installments: Int?,
    @SerializedName("installment_amount")
    val installmentAmount: String?,
    @SerializedName("email_change_enabled")
    val emailChangeEnabled: Boolean,
    @SerializedName("seller_info")
    val sellerInfo: SellerInfoRequest?,
)

internal data class SellerInfoRequest(
    @SerializedName("name")
    val name: String?,
    @SerializedName("icon_url")
    val iconUrl: String?,
)
