package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * Contains the device data required for 3DS authentication.
 *
 * This data class holds all the information needed to send device data to the backend
 * for initiating the 3DS authentication process.
 *
 * @property appId Unique SDK 3DS application identifier
 * @property integratorSdkVersion Version of the integrator SDK (e.g., Mastercard SDK)
 * @property threeDsSdkVersion Version of the Mercado Pago 3DS SDK
 * @property cardTokenId Unique identifier of the card token
 * @property deviceRenderOptions Device rendering configuration options
 * @property encData Encrypted device data collected by the SDK
 * @property ephemPubKey Ephemeral public key for encryption
 * @property maxTimeout Maximum timeout in minutes for the operation
 * @property protocolVersion 3DS protocol version (e.g., "2.1.0", "2.2.0")
 * @property referenceNumber SDK reference number for tracking
 * @property transId Transaction identifier for the 3DS flow
 *
 * Example:
 * ```kotlin
 * val deviceData = ThreeDSDeviceData(
 *     appId = "com.mercadopago.checkout",
 *     integratorSdkVersion = "2.2.0",
 *     threeDsSdkVersion = "1.0.0",
 *     cardTokenId = "abc123def456",
 *     deviceRenderOptions = DeviceRenderOptions(
 *         sdkInterface = "Native",
 *         uiTypes = listOf("01", "02", "03", "04", "05")
 *     ),
 *     encData = "eyJhbGciOiJSU0EtT0FFUC0yNTYiLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0...",
 *     ephemPubKey = EphemeralPublicKey(
 *         curve = "P-256",
 *         keyType = "EC",
 *         x = "mPUKT_bAWGHIhg0TpjjqVsP1rXWQu_vwVOHHtNkdYoA",
 *         y = "8BQAsImGeAS46fyWw5MhYfGTT0IjBpFw2SS34Dv4Irs"
 *     ),
 *     maxTimeout = 5,
 *     protocolVersion = "2.2.0",
 *     referenceNumber = "3DS_LOA_SDK_PPFU_020100_00007",
 *     transId = "f25084f0-5b16-4c0a-ae5d-b24808a95e4b"
 * )
 * ```
 */
data class ThreeDSDeviceData(
    val appId: String,
    val integratorSdkVersion: String,
    val threeDsSdkVersion: String,
    val cardTokenId: String,
    val deviceRenderOptions: DeviceRenderOptions,
    val encData: String,
    val ephemPubKey: EphemeralPublicKey,
    val maxTimeout: Int,
    val protocolVersion: String,
    val referenceNumber: String,
    val transId: String,
)

/**
 * Device rendering options for 3DS authentication.
 *
 * @property sdkInterface Type of SDK interface (e.g., "Native", "HTML")
 * @property uiTypes List of supported UI types for challenge display
 */
data class DeviceRenderOptions(
    val sdkInterface: String,
    val uiTypes: List<String>,
)

/**
 * Ephemeral public key for 3DS encryption.
 *
 * @property curve Elliptic curve algorithm (e.g., "P-256")
 * @property keyType Key type (e.g., "EC" for Elliptic Curve)
 * @property x X coordinate of the elliptic curve point
 * @property y Y coordinate of the elliptic curve point
 */
data class EphemeralPublicKey(
    val curve: String,
    val keyType: String,
    val x: String,
    val y: String,
)
