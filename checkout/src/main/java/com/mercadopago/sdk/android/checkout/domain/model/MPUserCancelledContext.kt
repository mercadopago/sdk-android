package com.mercadopago.sdk.android.checkout.domain.model

/**
 * Represents the context when a MercadoPago form is cancelled by the user.
 *
 * This sealed class provides information about which fields were filled, incomplete,
 * or invalid when the user abandoned the form without completing the payment flow.
 */
sealed class MPUserCancelledContext {
    /**
     * Represents the cancelled state of a card payment form.
     *
     * @property context Context containing the state of all card form fields when cancelled
     */
    data class CardForm(val context: MPCardFormUserCancelledContext) : MPUserCancelledContext()
}
