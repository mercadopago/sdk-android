package com.mercadopago.sdk.android.checkout.domain.callback

import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.UserCancelledContext

/**
 * Sealed interface representing the possible outcomes of a checkout flow.
 *
 * The type parameter [T] is inferred from the [com.mercadopago.sdk.android.checkout.core.model.CheckoutType]
 * passed to the builder, so [Success.paymentData] is already the concrete subtype — no `when` required.
 *
 * Implementations:
 * - [Success] Checkout completed successfully with payment data.
 * - [Error] Checkout failed due to a card form or payment error.
 * - [UserCancelled] User explicitly cancelled the checkout.
 */
interface MercadoPagoCheckoutResult<out T : MPPaymentData> {
    /**
     * Checkout completed successfully.
     *
     * @property paymentData The payment data resulting from the successful checkout.
     * The concrete type matches the [com.mercadopago.sdk.android.checkout.core.model.CheckoutType]
     * configured in the builder.
     */
    data class Success<T : MPPaymentData>(val paymentData: T) : MercadoPagoCheckoutResult<T>

    /**
     * Checkout failed with an error from the card form brick.
     *
     * @property error Details of the error that occurred.
     */
    data class Error(val error: MercadoPagoCheckoutError) : MercadoPagoCheckoutResult<Nothing>

    /**
     * User cancelled the checkout flow before completion.
     *
     * @property context Information about the form state when cancelled, including
     * which fields were filled, empty, incomplete, or invalid
     */
    data class UserCancelled(val context: UserCancelledContext) : MercadoPagoCheckoutResult<Nothing>
}
