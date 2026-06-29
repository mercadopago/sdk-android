package com.mercadopago.sdk.android.checkout.domain.callback

import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError

/**
 * Sealed interface representing the possible outcomes of a checkout flow.
 *
 * Both type parameters are inferred from the [com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType]
 * passed to the builder:
 * - [T] types [Success.paymentData] — no `when` or cast required.
 * - [C] types [UserCancelled.cancelledData] — no `when` or cast required.
 *
 * Implementations:
 * - [Success] Checkout completed successfully with payment data.
 * - [Error] Checkout failed due to a card form or payment error.
 * - [UserCancelled] User explicitly cancelled the checkout.
 */
interface MercadoPagoCheckoutResult<out T : MPPaymentData, out C : MPUserCancelledContext> {
    /**
     * Checkout completed successfully.
     *
     * @property paymentData The payment data resulting from the successful checkout.
     * The concrete type matches the [com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType]
     * configured in the builder.
     */
    data class Success<T : MPPaymentData>(val paymentData: T) : MercadoPagoCheckoutResult<T, Nothing>

    /**
     * Checkout failed with an error from the card form brick.
     *
     * @property error Details of the error that occurred.
     */
    data class Error(val error: MercadoPagoCheckoutError) : MercadoPagoCheckoutResult<Nothing, Nothing>

    /**
     * User cancelled the checkout flow before completion.
     *
     * @property cancelledData The data available at the time of cancellation.
     * The concrete type matches the [com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType]
     * configured in the builder. Fields may be null if the user cancelled before filling them in.
     */
    data class UserCancelled<C : MPUserCancelledContext>(val cancelledData: C) : MercadoPagoCheckoutResult<Nothing, C>
}
