package com.mercadopago.sdk.android.googlepay.domain.model

/** Error type. */
enum class MPGooglePayErrorType {
    /** Google Pay cannot be offered; the button is not shown. */
    UNAVAILABLE,

    /** Failure after the click (sheet, tokenize, network, interrupted flow). */
    PAYMENT,
}
