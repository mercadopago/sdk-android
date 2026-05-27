package com.mercadopago.sdk.android.checkout.domain.callback

import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError

/**
 * Sealed interface representing the possible outcomes of a checkout flow.
 *
 * Implementations:
 * - [Success] Checkout completed successfully with payment data.
 * - [Error] Checkout failed due to a card form or payment error.
 * - [UserCancelled] User explicitly cancelled the checkout.
 */
interface MercadoPagoCheckoutResult {
    /**
     * Checkout completed successfully.
     *
     * @property paymentData The payment data resulting from the successful checkout.
     */
    data class Success(val paymentData: MPPaymentData) : MercadoPagoCheckoutResult

    /**
     * Checkout failed with an error from the card form brick.
     *
     * @property error Details of the error that occurred.
     */
    data class Error(val error: MercadoPagoCheckoutError) : MercadoPagoCheckoutResult

    /**
     * User cancelled the checkout flow before completion.
     *
     * @property context Information about the form state when cancelled, including
     * which fields were filled, empty, incomplete, or invalid
     */
    data class UserCancelled(val context: MPUserCancelledContext) : MercadoPagoCheckoutResult
}
