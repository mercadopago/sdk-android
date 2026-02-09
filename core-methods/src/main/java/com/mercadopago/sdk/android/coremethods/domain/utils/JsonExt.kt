package com.mercadopago.sdk.android.coremethods.domain.utils

import com.google.gson.Gson
import com.mercadopago.sdk.android.coremethods.domain.model.EphemeralPublicKey
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError

internal fun String.ephemeralFromJson(): EphemeralPublicKey? {
    return runCatching {
        val parsed = Gson().fromJson(this, EphemeralPublicKey::class.java)
        EphemeralPublicKey(
            curve = parsed.curve,
            keyType = parsed.keyType,
            x = parsed.x,
            y = parsed.y,
        )
    }.getOrNull()
}

internal fun String.toEphemeralPublicKey(): Result<EphemeralPublicKey, ResultError> {
    val ephemeralPublicKey = this.ephemeralFromJson()
        ?: return Result.Error(
            ResultError.Validation(
                message = "Failed to parse ephemeral public key from 3DS SDK.",
            ),
        )
    return Result.Success(ephemeralPublicKey)
}
