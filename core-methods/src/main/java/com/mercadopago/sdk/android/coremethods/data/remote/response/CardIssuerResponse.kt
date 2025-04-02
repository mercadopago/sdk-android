package com.mercadopago.sdk.android.coremethods.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class CardIssuerResponse(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("merchant_account_id")
    val merchantAccountId: String? = null,
    @SerializedName("processing_mode")
    val processingMode: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("thumbnail")
    val thumbnail: String? = null
)
