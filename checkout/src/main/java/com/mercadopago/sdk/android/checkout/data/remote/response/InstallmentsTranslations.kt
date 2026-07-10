package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class InstallmentsTranslations(
    @SerializedName("header")
    val header: InstallmentsHeaderTranslations,
    @SerializedName("total_label")
    val totalLabel: String,
    @SerializedName("pay_button_label")
    val payButtonLabel: String,
)
