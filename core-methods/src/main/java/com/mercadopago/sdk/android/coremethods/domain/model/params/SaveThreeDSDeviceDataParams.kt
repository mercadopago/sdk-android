package com.mercadopago.sdk.android.coremethods.domain.model.params

/**
 * Parameters for saving 3DS device data to initiate the authentication process.
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
 */
internal data class SaveThreeDSDeviceDataParams(
    val appId: String,
    val integratorSdkVersion: String,
    val threeDsSdkVersion: String,
    val cardTokenId: String,
    val deviceRenderOptions: DeviceRenderOptionsParams,
    val encData: String,
    val ephemPubKey: EphemeralPublicKeyParams,
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
internal data class DeviceRenderOptionsParams(
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
internal data class EphemeralPublicKeyParams(
    val curve: String,
    val keyType: String,
    val x: String,
    val y: String,
)
