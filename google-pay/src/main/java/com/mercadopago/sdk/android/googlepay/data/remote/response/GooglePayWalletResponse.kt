package com.mercadopago.sdk.android.googlepay.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class GooglePayWalletResponse(
    @SerializedName("code")
    val code: String?,
    @SerializedName("supported_payment_methods")
    val supportedPaymentMethods: List<GooglePaySupportedPaymentMethodResponse?>?,
)

internal data class GooglePaySupportedPaymentMethodResponse(
    @SerializedName("id")
    val id: String?,
)
