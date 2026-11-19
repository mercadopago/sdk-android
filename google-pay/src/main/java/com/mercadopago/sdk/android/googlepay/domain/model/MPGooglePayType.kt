package com.mercadopago.sdk.android.googlepay.domain.model

/**
 * Google Pay mode. `R` is the payment data type delivered in [MPGooglePayResult.Success].
 */
sealed interface MPGooglePayType<R> {
    /**
     * Generates a single-use card token for the given purchase.
     *
     * @property transaction purchase data.
     */
    data class Token(val transaction: MPGooglePayTransaction) : MPGooglePayType<MPGooglePayToken>
}
