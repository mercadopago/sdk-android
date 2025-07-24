package com.mercadopago.sdk.android.threeds.data.model

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.threeds.domain.model.ThreeDSAuthRequestParameters

/**
 * Request body for 3DS authentication containing the card token and SDK parameters.
 */
data class ThreeDSBody(
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
) {
    companion object {
        /**
         * Creates a ThreeDSBody from a card token and authentication request parameters.
         *
         * @param token Card token
         * @param authenticationRequestParameters 3DS SDK authentication parameters
         * @param sdkMaxTimeout SDK maximum timeout (default: "06")
         */
        fun create(
            token: String,
            authenticationRequestParameters: ThreeDSAuthRequestParameters,
            sdkMaxTimeout: String = "06"
        ): ThreeDSBody {
            return ThreeDSBody(
                token = token,
                sdkAppId = authenticationRequestParameters.sdkAppId,
                sdkEncData = authenticationRequestParameters.deviceData,
                sdkEphemPubKey = authenticationRequestParameters.sdkEphemeralPublicKey,
                sdkMaxTimeout = sdkMaxTimeout,
                sdkReferenceNumber = authenticationRequestParameters.sdkReferenceNumber,
                sdkTransId = authenticationRequestParameters.sdkTransactionId,
            )
        }
    }
}
