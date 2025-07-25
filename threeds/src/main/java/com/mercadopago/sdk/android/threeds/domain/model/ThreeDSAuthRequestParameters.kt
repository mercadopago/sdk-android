package com.mercadopago.sdk.android.threeds.domain.model

/**
 * Authentication request parameters obtained from the 3DS SDK.
 * These parameters are used to create a request to the MercadoPago backend
 * for 3DS authentication.
 */
data class ThreeDSAuthRequestParameters(
    /** The SDK application ID */
    val sdkAppId: String,
    /** The device data collected by the SDK */
    val deviceData: String,
    /** The SDK ephemeral public key */
    val sdkEphemeralPublicKey: String,
    /** The SDK reference number */
    val sdkReferenceNumber: String,
    /** The SDK transaction ID */
    val sdkTransactionId: String,
)
