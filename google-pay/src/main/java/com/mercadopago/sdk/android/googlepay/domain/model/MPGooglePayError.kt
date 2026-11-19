package com.mercadopago.sdk.android.googlepay.domain.model

/**
 * Google Pay error.
 *
 * @property code one of [MPGooglePayErrorCode]; keep an `else` branch when matching it, new codes may be added.
 * @property message technical message in English, for logs only. Never contains public key, payload or token.
 */
data class MPGooglePayError(
    val code: MPGooglePayErrorCode,
    val message: String,
) {
    /**
     * Type derived from [code]: [MPGooglePayErrorType.UNAVAILABLE] before the button shows up,
     * [MPGooglePayErrorType.PAYMENT] after the click.
     */
    val type: MPGooglePayErrorType
        get() = code.type
}
