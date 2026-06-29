package com.mercadopago.sdk.android.checkout.core.model

/**
 * Defines the type of card used in the checkout flow.
 *
 * This value is used to represent the selected card category and may affect
 * available payment options and UI/UX decisions during the checkout.
 */
enum class MPCardType(internal val value: String) {
    /**
     * CREDIT: Credit card.
     */
    CREDIT("credit_card"),

    /**
     * DEBIT: Debit card.
     */
    DEBIT("debit_card"),

    /**
     * PREPAID: Prepaid card.
     */
    PREPAID("prepaid_card"),
    ;

    internal companion object {
        internal fun fromString(
            value: String,
        ): MPCardType? =
            entries.find {
                it.value.equals(value, ignoreCase = true) ||
                    it.name.equals(value, ignoreCase = true)
            }
    }
}
