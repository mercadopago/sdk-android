package com.mercadopago.sdk.android.checkout.domain.callback

import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError

/**
 * Internal singleton to hold and manage checkout callbacks.
 *
 * This holder ensures callbacks are properly invoked and cleaned up after use
 * to prevent memory leaks.
 */
internal object CheckoutCallbackHolder {
    private var callback: CheckoutCallback? = null
    private var activityCallback: (() -> Unit)? = null

    /**
     * Sets the callback to be invoked when checkout completes.
     *
     * @param callback The callback to set, or null to clear.
     */
    fun setCallback(
        callback: CheckoutCallback?,
    ) {
        this.callback = callback
    }

    /**
     * Sets the activity callback to be invoked when checkout completes.
     * This is used to notify the activity to finish.
     *
     * @param callback The callback to set, or null to clear.
     */
    fun setActivityCallback(
        callback: (() -> Unit)?,
    ) {
        this.activityCallback = callback
    }

    /**
     * Notifies the callback of a successful checkout.
     *
     * @param paymentData The payment data from the successful checkout.
     */
    fun notifySuccess(
        paymentData: MPPaymentData,
    ) {
        activityCallback?.invoke()
        callback?.onResult(MercadoPagoCheckoutResult.Success(paymentData))
        clear()
    }

    /**
     * Notifies the callback of a checkout error.
     *
     * @param error The error that occurred during checkout.
     */
    fun notifyError(
        error: MercadoPagoCheckoutError,
    ) {
        activityCallback?.invoke()
        callback?.onResult(MercadoPagoCheckoutResult.Error(error))
        clear()
    }

    /**
     * Notifies the callback that the user cancelled the checkout.
     */
    fun notifyCanceled() {
        callback?.onResult(MercadoPagoCheckoutResult.UserCancelled)
        clear()
    }

    /**
     * Clears the stored callbacks to prevent memory leaks.
     */
    private fun clear() {
        callback = null
        activityCallback = null
    }
}
