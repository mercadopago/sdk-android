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

    /** The new card form screen. */
    CARD_FORM,

    /** The offline payment method selector screen (boleto, ticket, etc.). */
    OFFLINE_METHOD_SELECTOR,

    /** The security code (CVV) screen shown for a saved card before tokenization. */
    SECURITY_CODE,
}
