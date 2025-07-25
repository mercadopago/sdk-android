package com.mercadopago.sdk.android.threeds.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class MPThreeDSAuthenticationResponse(
    @SerializedName("response")
    val response: String,
    @SerializedName("threeds_server_trans_id")
    val threeDSServerTransID: String,
    @SerializedName("acs_reference_number")
    val acsReferenceNumber: String,
    @SerializedName("ds_trans_id")
    val dsTransID: String,
    @SerializedName("acs_trans_id")
    val acsTransID: String,
    @SerializedName("acs_signed_content")
    val acsSignedContent: String,
)
