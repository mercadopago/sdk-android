package com.mercadopago.sdk.android.checkout.core.model

/**
 * Defines the type of card used in the checkout flow.
 *
 * This value is used to represent the selected card category and may affect
 * available payment options and UI/UX decisions during the checkout.
 */
enum class CardType(internal val value: String) {
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
    PREPAID("prepaid"),
}
