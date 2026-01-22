package com.mercadopago.sdk.android.coremethods.domain.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal data class EphemeralPublicKey(
    @SerializedName("kty")
    val keyType: String,
    @SerializedName("crv")
    val curve: String,
    @SerializedName("x")
    val x: String,
    @SerializedName("y")
    val y: String,
)

internal fun String.ephemeralfromJson(): EphemeralPublicKey? {
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
    val ephemeralPublicKey = this.ephemeralfromJson()
        ?: return Result.Error(
            ResultError.Validation(
                message = "Failed to parse ephemeral public key from 3DS SDK.",
            ),
        )
    return Result.Success(ephemeralPublicKey)
}
