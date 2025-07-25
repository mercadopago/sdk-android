package com.mercadopago.sdk.android.threeds.domain.model.params

internal data class ThreeDSAuthenticationParams(
    val token: String,
    val sdkAppId: String,
    val sdkEncData: String,
    val sdkEphemPubKey: String,
    val sdkMaxTimeout: String,
    val sdkReferenceNumber: String,
    val sdkTransId: String,
)
