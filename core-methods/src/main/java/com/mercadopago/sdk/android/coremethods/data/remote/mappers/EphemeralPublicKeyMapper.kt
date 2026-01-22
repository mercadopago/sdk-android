package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.domain.model.EphemeralPublicKey
import com.mercadopago.sdk.android.coremethods.domain.model.params.EphemeralPublicKeyParams

internal fun EphemeralPublicKey.toParams(): EphemeralPublicKeyParams =
    EphemeralPublicKeyParams(
        curve = curve,
        keyType = keyType,
        x = x,
        y = y,
    )
