package com.mercadopago.sdk.android.checkout.core.model

/**
 * Defines the type of card used in the checkout flow.
 *
 * This value is used to represent the selected card category and may affect
 * available payment options and UI/UX decisions during the checkout.
 *
 * - [CREDIT] Credit card.
 * - [DEBIT] Debit card.
 * - [PREPAID] Prepaid card.
 */
enum class CardType {
    /**
     * CREDIT: Credit card.
     */
    CREDIT,

    /**
     * DEBIT: Debit card.
     */
    DEBIT,

    /**
     * PREPAID: Prepaid card.
     */
    PREPAID
}
