package com.mercadopago.sdk.android.googlepay.domain.model

/**
 * Result delivered by `prepare`. `R` is the payment data type of the chosen [MPGooglePayType].
 */
sealed interface MPGooglePayResult<out R> {
    /** Google Pay is available and the button can be shown. */
    data object Ready : MPGooglePayResult<Nothing>

    /**
     * Payment completed.
     *
     * @property paymentData data of the chosen [MPGooglePayType].
     */
    data class Success<R>(val paymentData: R) : MPGooglePayResult<R>

    /**
     * Google Pay unavailable or payment failed.
     *
     * @property error error with type and code.
     */
    data class Error(val error: MPGooglePayError) : MPGooglePayResult<Nothing>

    /** Buyer closed the Google Pay sheet. */
    data object Cancelled : MPGooglePayResult<Nothing>
}
