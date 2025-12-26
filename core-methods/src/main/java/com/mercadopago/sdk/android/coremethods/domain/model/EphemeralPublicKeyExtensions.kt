package com.mercadopago.sdk.android.coremethods.domain.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * Internal model for parsing the ephemeral public key JSON from 3DS SDK.
 * The JSON follows the JWK (JSON Web Key) format with EC (Elliptic Curve) key type.
 */
private data class EphemeralPublicKeyJson(
    @SerializedName("kty")
    val keyType: String,
    @SerializedName("crv")
    val curve: String,
    @SerializedName("x")
    val x: String,
    @SerializedName("y")
    val y: String,
)

/**
 * Parses a JSON string representing an ephemeral public key into an [EphemeralPublicKey] object.
 *
 * The JSON is expected to follow the JWK (JSON Web Key) format:
 * ```json
 * {
 *   "kty": "EC",
 *   "crv": "P-256",
 *   "x": "mPUKT_bAWGHIhg0TpjjqVsP1rXWQu_vwVOHHtNkdYoA",
 *   "y": "8BQAsImGeAS46fyWw5MhYfGTT0IjBpFw2SS34Dv4Irs"
 * }
 * ```
 *
 * @param json The JSON string containing the ephemeral public key in JWK format
 * @return [EphemeralPublicKey] if parsing was successful, null otherwise
 *
 * Example:
 * ```kotlin
 * val json = """{"kty":"EC","crv":"P-256","x":"abc123","y":"def456"}"""
 * val ephemeralKey = EphemeralPublicKey.fromJson(json)
 * ephemeralKey?.let {
 *     println("Curve: ${it.curve}, KeyType: ${it.keyType}")
 * }
 * ```
 */
internal fun EphemeralPublicKey.Companion.fromJson(
    json: String,
): EphemeralPublicKey? {
    return runCatching {
        val parsed = Gson().fromJson(json, EphemeralPublicKeyJson::class.java)
        EphemeralPublicKey(
            curve = parsed.curve,
            keyType = parsed.keyType,
            x = parsed.x,
            y = parsed.y,
        )
    }.getOrNull()
}
