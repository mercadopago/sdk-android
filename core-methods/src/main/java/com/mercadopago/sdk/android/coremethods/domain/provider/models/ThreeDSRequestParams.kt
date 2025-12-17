package com.mercadopago.sdk.android.coremethods.domain.provider.models

/**
 * Authentication request parameters required for 3DS authentication.
 * These parameters should be sent to your backend for authentication.
 *
 * @property sdkAppId SDK application identifier
 * @property deviceData Encrypted device data
 * @property sdkEphemeralPublicKey SDK ephemeral public key
 * @property sdkReferenceNumber SDK reference number
 * @property sdkTransactionId SDK transaction identifier
 */
data class ThreeDSRequestParams(
    val sdkAppId: String,
    val deviceData: String,
    val sdkEphemeralPublicKey: String,
    val sdkReferenceNumber: String,
    val sdkTransactionId: String,
)
