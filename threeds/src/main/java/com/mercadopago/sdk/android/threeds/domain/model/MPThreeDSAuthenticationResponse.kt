package com.mercadopago.sdk.android.threeds.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Response model for 3DS authentication containing the authentication result
 * and necessary parameters for the challenge flow.
 */
data class MPThreeDSAuthenticationResponse(
    @SerializedName("response")
    val response: String, // NOAUTHORIZED / CHALLENGE

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
