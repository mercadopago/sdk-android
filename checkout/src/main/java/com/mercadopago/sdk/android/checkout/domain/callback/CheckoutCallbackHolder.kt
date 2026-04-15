package com.mercadopago.sdk.android.checkout.domain.callback

internal object CheckoutCallbackHolder {
    private var callback: ((MercadoPagoCheckoutResult) -> Unit)? = null
    private var activityCallback: (() -> Unit)? = null

    fun setCallback(
        callback: ((MercadoPagoCheckoutResult) -> Unit)?,
    ) {
        this.callback = callback
    }

    fun setActivityCallback(
        callback: (() -> Unit)?,
    ) {
        this.activityCallback = callback
    }

    fun notify(
        result: MercadoPagoCheckoutResult,
    ) {
        activityCallback?.invoke()
        callback?.invoke(result)
        clear()
    }

    private fun clear() {
        callback = null
        activityCallback = null
    }
}
