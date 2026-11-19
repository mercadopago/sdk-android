package com.mercadopago.sdk.android.googlepay.domain.model

/** Payment type of the card chosen in Google Pay, like `payment_type_id` in Mercado Pago. */
enum class MPGooglePayPaymentType {
    /** Credit card. */
    CREDIT_CARD,

    /** Debit card. */
    DEBIT_CARD,

    /** Prepaid card. */
    PREPAID_CARD,

    /** Funding source not informed by Google. */
    UNKNOWN,
}
