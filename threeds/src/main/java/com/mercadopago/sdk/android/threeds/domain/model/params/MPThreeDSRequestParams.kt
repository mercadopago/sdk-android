package com.mercadopago.sdk.android.threeds.domain.model.params

/**
 * Authentication request parameters required for 3DS authentication.
 * These parameters should be sent to your backend for authentication.
 *
 * @param sdkAppId SDK application identifier
 * @param deviceData Encrypted device data
 * @param sdkEphemeralPublicKey SDK ephemeral public key
 * @param sdkReferenceNumber SDK reference number
 * @param sdkTransactionId SDK transaction identifier
 */
data class MPThreeDSRequestParams(
    val sdkAppId: String,
    val deviceData: String,
    val sdkEphemeralPublicKey: String,
    val sdkReferenceNumber: String,
    val sdkTransactionId: String,
)
