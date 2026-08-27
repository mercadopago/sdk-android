package com.mercadopago.sdk.android.checkout.domain.callback

import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext

internal object CheckoutCallbackHolder {
    private var callback: ((MercadoPagoCheckoutResult<*, *>) -> Unit)? = null
    private var activityCallback: (() -> Unit)? = null

    fun <T : MPPaymentData, C : MPUserCancelledContext> setCallback(
        callback: ((MercadoPagoCheckoutResult<T, C>) -> Unit)?,
    ) {
        @Suppress("UNCHECKED_CAST")
        this.callback = callback as? ((MercadoPagoCheckoutResult<*, *>) -> Unit)
    }

    fun setActivityCallback(
        callback: (() -> Unit)?,
    ) {
        this.activityCallback = callback
    }

    fun notify(
        result: MercadoPagoCheckoutResult<*, *>,
    ) {
        activityCallback?.invoke()
        callback?.invoke(result)
        clear()
    }

    fun dismiss() {
        activityCallback?.invoke()
        clear()
    }

    private fun clear() {
        callback = null
        activityCallback = null
    }
}
