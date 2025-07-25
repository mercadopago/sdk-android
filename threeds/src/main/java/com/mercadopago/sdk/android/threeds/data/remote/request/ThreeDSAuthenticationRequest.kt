package com.mercadopago.sdk.android.threeds.data.remote.request

import com.google.gson.annotations.SerializedName

internal data class ThreeDSAuthenticationRequest(
    @SerializedName("token")
    val token: String,
    @SerializedName("sdk_app_id")
    val sdkAppId: String,
    @SerializedName("sdk_enc_data")
    val sdkEncData: String,
    @SerializedName("sdk_ephem_pub_key")
    val sdkEphemPubKey: String,
    @SerializedName("sdk_max_timeout")
    val sdkMaxTimeout: String,
    @SerializedName("sdk_reference_number")
    val sdkReferenceNumber: String,
    @SerializedName("sdk_trans_id")
    val sdkTransId: String,
)
