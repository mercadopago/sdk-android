package com.mercadopago.sdk.android.threeds.domain.model.params

internal data class ThreeDSAuthRequestParameters(
    val sdkAppId: String,
    val deviceData: String,
    val sdkEphemeralPublicKey: String,
    val sdkReferenceNumber: String,
    val sdkTransactionId: String,
)
