package com.mercadopago.sdk.android.checkout.domain.model

/**
 * Represents the context when a MercadoPago form is cancelled by the user.
 *
 * This sealed class provides information about which fields were filled, incomplete,
 * or invalid when the user abandoned the form without completing the payment flow.
 */
sealed class MPCancelledFormContext {
    /**
     * Represents the cancelled state of a card payment form.
     *
     * @property fields List of field states showing which fields were filled, empty,
     * incomplete, or invalid when the form was cancelled
     */
    data class CardForm(val fields: List<CancelledFieldState>) : MPCancelledFormContext()
}
