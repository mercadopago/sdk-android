package com.mercadopago.sdk.android.coremethods.data.remote.request

import com.google.gson.annotations.SerializedName

internal data class ThreeDSDeviceDataRequest(
    @SerializedName("app_id")
    val appId: String,
    @SerializedName("integrator_sdk_version")
    val integratorSdkVersion: String,
    @SerializedName("threeds_sdk_version")
    val threeDsSdkVersion: String,
    @SerializedName("card_token_id")
    val cardTokenId: String,
    @SerializedName("device_render_options")
    val deviceRenderOptions: DeviceRenderOptionsRequest,
    @SerializedName("enc_data")
    val encData: String,
    @SerializedName("ephem_pub_key")
    val ephemPubKey: EphemeralPublicKeyRequest,
    @SerializedName("max_timeout")
    val maxTimeout: Int,
    @SerializedName("protocol_version")
    val protocolVersion: String,
    @SerializedName("reference_number")
    val referenceNumber: String,
    @SerializedName("trans_id")
    val transId: String,
)

internal data class DeviceRenderOptionsRequest(
    @SerializedName("interface")
    val sdkInterface: String,
    @SerializedName("ui_types")
    val uiTypes: List<String>,
)

internal data class EphemeralPublicKeyRequest(
    @SerializedName("curve")
    val curve: String,
    @SerializedName("key_type")
    val keyType: String,
    @SerializedName("x")
    val x: String,
    @SerializedName("y")
    val y: String,
)
