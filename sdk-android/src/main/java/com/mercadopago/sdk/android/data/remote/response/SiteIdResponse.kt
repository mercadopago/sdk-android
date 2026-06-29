package com.mercadopago.sdk.android.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class SiteIdResponse(
    @SerializedName("site_id")
    val siteId: String? = null,
)
