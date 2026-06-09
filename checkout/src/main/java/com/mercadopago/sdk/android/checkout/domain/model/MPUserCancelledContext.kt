package com.mercadopago.sdk.android.checkout.domain.model

/**
 * Sealed class representing the context when a MercadoPago checkout is cancelled by the user.
 *
 * The subtype corresponds to the [com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType]
 * configured in the builder, providing information about the state of each form field at the
 * moment the user abandoned the flow.
 */
sealed class MPUserCancelledContext {
    /**
     * Cancellation context for a [com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType.CardSave] checkout.
     *
     * @property fields List of field states showing which fields were filled, empty,
     * incomplete, or invalid when the form was cancelled.
     */
    data class CardSave(
        val fields: List<MPCancelledFieldState>,
    ) : MPUserCancelledContext()

    /**
     * Cancellation context for a [com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType.CardTransaction] checkout.
     *
     * @property fields List of field states showing which fields were filled, empty,
     * incomplete, or invalid when the form was cancelled.
     */
    data class CardTransaction(
        val fields: List<MPCancelledFieldState>,
    ) : MPUserCancelledContext()
}
