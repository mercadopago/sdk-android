package com.mercadopago.sdk.android.checkout.domain.model

/**
 * Identifies a checkout screen that was presented to the user before cancellation.
 *
 * Delivered as a list in [MPUserCancelledContext] so integrators can understand
 * how far the user progressed through the checkout flow before cancelling.
 */
enum class Screen {
    /** The installments selection screen. */
    INSTALLMENTS,

    /** The payment method selector screen (PaymentBrick entry). */
    PAYMENT_METHOD_SELECTOR,

    /** The CVV entry screen shown for saved cards that require security code. */
    CVV,

    /** The new card form screen. */
    CARD_FORM,

    /** The offline payment method selector screen (boleto, ticket, etc.). */
    OFFLINE_METHOD_SELECTOR,
}
