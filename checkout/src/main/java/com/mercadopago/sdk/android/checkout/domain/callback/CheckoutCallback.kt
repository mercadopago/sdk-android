package com.mercadopago.sdk.android.checkout.domain.callback

/**
 * Callback interface for receiving checkout flow results.
 *
 * Set this callback using [MercadoPagoCheckout.Builder.setCallback] to receive
 * notifications about the checkout outcome.
 *
 * Example usage:
 * ```
 * val checkout = MercadoPagoCheckout.Builder(context, checkoutType)
 *     .setCallback { result ->
 *         when (result) {
 *             is MercadoPagoCheckoutResult.Success -> {
 *                 // Handle success with result.paymentData
 *             }
 *             is MercadoPagoCheckoutResult.Error -> {
 *                 // Handle error with result.error
 *             }
 *             is MercadoPagoCheckoutResult.UserCancelled -> {
 *                 // Handle user cancellation
 *             }
 *         }
 *     }
 *     .build()
 * ```
 */
fun interface CheckoutCallback {
    /**
     * Called when the checkout flow completes with a result.
     *
     * @param result The checkout result - can be Success, Error, or UserCancelled.
     */
    fun onResult(
        result: MercadoPagoCheckoutResult,
    )
}
