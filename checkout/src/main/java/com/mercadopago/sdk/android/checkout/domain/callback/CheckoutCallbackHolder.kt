package com.mercadopago.sdk.android.checkout.domain.callback

import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError

internal object CheckoutCallbackHolder {
    private var callback: CheckoutCallback? = null
    private var activityCallback: (() -> Unit)? = null

    fun setCallback(
        callback: CheckoutCallback?,
    ) {
        this.callback = callback
    }

    fun setActivityCallback(
        callback: (() -> Unit)?,
    ) {
        this.activityCallback = callback
    }

    fun notifySuccess(
        paymentData: MPPaymentData,
    ) {
        activityCallback?.invoke()
        callback?.onResult(MercadoPagoCheckoutResult.Success(paymentData))
        clear()
    }

    fun notifyError(
        error: MercadoPagoCheckoutError,
    ) {
        activityCallback?.invoke()
        callback?.onResult(MercadoPagoCheckoutResult.Error(error))
        clear()
    }

    fun notifyCanceled() {
        callback?.onResult(MercadoPagoCheckoutResult.UserCancelled)
        clear()
    }

    private fun clear() {
        callback = null
        activityCallback = null
    }
}
