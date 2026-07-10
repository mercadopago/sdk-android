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

    /** The new card form screen. */
    CARD_FORM,
}
