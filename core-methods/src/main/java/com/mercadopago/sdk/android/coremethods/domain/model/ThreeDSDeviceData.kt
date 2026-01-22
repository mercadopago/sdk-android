package com.mercadopago.sdk.android.coremethods.domain.model

import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSRequestParams

/**
 * Data class to hold the intermediate state needed to execute the device data save operation.
 *
 * This class encapsulates all the required data that must be collected and validated
 * before executing the 3DS device data save operation. It combines the SDK version,
 * authentication request parameters, and the parsed ephemeral public key.
 *
 * @property sdkVersion The version of the 3DS SDK being used
 * @property parameters The [ThreeDSRequestParams] containing authentication request parameters
 * @property ephemeralKey The [EphemeralPublicKey] parsed from the SDK parameters
 *
 * Example:
 * ```kotlin
 * val context = ThreeDSDeviceData(
 *     sdkVersion = "1.0.0",
 *     parameters = threeDSRequestParams,
 *     ephemeralKey = ephemeralPublicKey
 * )
 * ```
 *
 * @see ThreeDSRequestParams
 * @see EphemeralPublicKey
 */
internal data class ThreeDSDeviceData(
    val sdkVersion: String,
    val parameters: ThreeDSRequestParams,
    val ephemeralKey: EphemeralPublicKey,
)
