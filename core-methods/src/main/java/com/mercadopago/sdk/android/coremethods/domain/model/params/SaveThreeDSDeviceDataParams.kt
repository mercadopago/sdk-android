package com.mercadopago.sdk.android.coremethods.domain.model.params

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

internal data class DeviceRenderOptionsParams(
    val sdkInterface: String,
    val uiTypes: List<String>,
)

internal data class EphemeralPublicKeyParams(
    val curve: String,
    val keyType: String,
    val x: String,
    val y: String,
)
