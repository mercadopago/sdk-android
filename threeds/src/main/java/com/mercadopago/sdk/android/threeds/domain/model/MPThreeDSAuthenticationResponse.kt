package com.mercadopago.sdk.android.threeds.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Response model for 3DS authentication containing the authentication result
 * and necessary parameters for the challenge flow.
 */
data class MPThreeDSAuthenticationResponse(
    /** The authentication response status (NOAUTHORIZED / CHALLENGE / AUTHORIZED) */
    @SerializedName("response")
    val response: String,
    /** The 3DS server transaction ID */
    @SerializedName("threeds_server_trans_id")
    val threeDSServerTransID: String,
    /** The ACS reference number */
    @SerializedName("acs_reference_number")
    val acsReferenceNumber: String,
    /** The directory server transaction ID */
    @SerializedName("ds_trans_id")
    val dsTransID: String,
    /** The ACS transaction ID */
    @SerializedName("acs_trans_id")
    val acsTransID: String,
    /** The ACS signed content for challenge */
    @SerializedName("acs_signed_content")
    val acsSignedContent: String,
)
