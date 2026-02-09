package com.mercadopago.sdk.android.coremethods.domain.model

import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSRequestParams

internal data class ThreeDSDeviceData(
    val sdkVersion: String,
    val parameters: ThreeDSRequestParams,
    val ephemeralKey: EphemeralPublicKey,
)
