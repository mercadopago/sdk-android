package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class InstallmentsHeaderTranslations(
    @SerializedName("title")
    val title: String,
)
