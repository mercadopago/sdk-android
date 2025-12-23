package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.request.DeviceRenderOptionsRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.EphemeralPublicKeyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.ThreeDSDeviceDataRequest
import com.mercadopago.sdk.android.coremethods.domain.model.params.SaveThreeDSDeviceDataParams

internal fun SaveThreeDSDeviceDataParams.toRequest(): ThreeDSDeviceDataRequest =
    ThreeDSDeviceDataRequest(
        appId = appId,
        integratorSdkVersion = integratorSdkVersion,
        threeDsSdkVersion = threeDsSdkVersion,
        cardTokenId = cardTokenId,
        deviceRenderOptions = DeviceRenderOptionsRequest(
            sdkInterface = deviceRenderOptions.sdkInterface,
            uiTypes = deviceRenderOptions.uiTypes,
        ),
        encData = encData,
        ephemPubKey = EphemeralPublicKeyRequest(
            curve = ephemPubKey.curve,
            keyType = ephemPubKey.keyType,
            x = ephemPubKey.x,
            y = ephemPubKey.y,
        ),
        maxTimeout = maxTimeout,
        protocolVersion = protocolVersion,
        referenceNumber = referenceNumber,
        transId = transId,
    )
