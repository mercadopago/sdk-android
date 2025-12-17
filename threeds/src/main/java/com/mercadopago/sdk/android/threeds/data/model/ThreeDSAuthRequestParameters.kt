package com.mercadopago.sdk.android.threeds.data.model

internal data class ThreeDSAuthRequestParameters(
    val sdkAppId: String,
    val deviceData: String,
    val sdkEphemeralPublicKey: String,
    val sdkReferenceNumber: String,
    val sdkTransactionId: String,
)
