package com.mercadopago.sdk.android.threeds.domain.model

/**
 * Authentication request parameters obtained from the 3DS SDK.
 * These parameters are used to create a request to the MercadoPago backend
 * for 3DS authentication.
 */
data class ThreeDSAuthRequestParameters(
    val sdkAppId: String,
    val deviceData: String,
    val sdkEphemeralPublicKey: String,
    val sdkReferenceNumber: String,
    val sdkTransactionId: String,
)
