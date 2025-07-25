package com.mercadopago.sdk.android.threeds.domain.model

/**
 * Data class representing the ThreeDS parameters that are extracted from uSDK.
 * This matches the structure used in the uSDK wrapper implementation.
 *
 * These parameters are typically obtained after creating a transaction with the uSDK
 * and are used for authentication purposes.
 */
data class ThreeDSDataOnlyParams(
    /** The SDK application ID */
    val sdkAppID: String,
    /** The device data collected by the SDK */
    val deviceData: String,
    /** The SDK ephemeral public key */
    val sdkEphemeralPublicKey: String,
    /** The SDK reference number */
    val sdkReferenceNumber: String,
    /** The SDK transaction ID */
    val sdkTransactionID: String,
    /** The 3DS message version */
    val messageVersion: String,
)
