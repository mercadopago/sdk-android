package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * Represents error details for a 3DS challenge when the status is ERROR.
 *
 * @property type The type of the error
 * @property code The error code
 */
data class ThreeDSChallengeErrorDetail(
    val type: String? = null,
    val code: String? = null,
)
