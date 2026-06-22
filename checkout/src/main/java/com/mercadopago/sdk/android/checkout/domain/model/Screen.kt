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

    /** The payment-method selector screen (PaymentBrick). */
    PAYMENT_METHOD_SELECTOR,

    /** The CVV entry screen (PaymentBrick saved card). */
    CVV,

    /** The new card form screen (PaymentBrick). */
    CARD_FORM,

    /** The offline payment method selection screen (PaymentBrick ticket). */
    OFFLINE_METHOD_SELECTOR,
}
