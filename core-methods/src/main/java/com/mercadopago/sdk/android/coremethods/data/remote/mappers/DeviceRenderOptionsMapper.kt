package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.domain.model.DeviceRenderOptions
import com.mercadopago.sdk.android.coremethods.domain.model.params.DeviceRenderOptionsParams

/**
 * Maps DeviceRenderOptions to DeviceRenderOptionsParams.
 */
internal fun DeviceRenderOptions.toParams(): DeviceRenderOptionsParams =
    DeviceRenderOptionsParams(
        sdkInterface = sdkInterface,
        uiTypes = uiTypes,
    )
