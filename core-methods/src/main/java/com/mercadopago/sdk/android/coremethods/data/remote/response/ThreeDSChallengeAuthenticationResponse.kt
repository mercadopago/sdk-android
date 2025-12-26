package com.mercadopago.sdk.android.coremethods.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class ThreeDSChallengeAuthenticationResponse(
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("data")
    val data: ThreeDSChallengeDataResponse? = null,
)

internal data class ThreeDSChallengeDataResponse(
    @SerializedName("acs_reference_number")
    val acsReferenceNumber: String? = null,
    @SerializedName("acs_signed_content")
    val acsSignedContent: String? = null,
    @SerializedName("acs_trans_id")
    val acsTransId: String? = null,
    @SerializedName("threeds_server_trans_id")
    val threeDsServerTransId: String? = null,
    @SerializedName("ds_trans_id")
    val dsTransId: String? = null,
    @SerializedName("callback_url")
    val callbackUrl: String? = null,
)
