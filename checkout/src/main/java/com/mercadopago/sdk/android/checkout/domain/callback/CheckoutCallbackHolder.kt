package com.mercadopago.sdk.android.checkout.domain.callback

import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext

internal object CheckoutCallbackHolder {
    private var callback: ((MercadoPagoCheckoutResult<*, *>) -> Unit)? = null
    private var activityCallback: (() -> Unit)? = null
    private var emailChangeCallback: (() -> Unit)? = null
    private var statusScreenExitCallback: (() -> Unit)? = null
    private var hostFinished = false

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

    fun setEmailChangeCallback(
        callback: (() -> Unit)?,
    ) {
        emailChangeCallback = callback
    }

    fun emailChangeCallbackOrNull(): (() -> Unit)? = emailChangeCallback

    fun setStatusScreenExitCallback(
        callback: (() -> Unit)?,
    ) {
        statusScreenExitCallback = callback
    }

    fun statusScreenExitCallbackOrNull(): (() -> Unit)? = statusScreenExitCallback

    fun notify(
        result: MercadoPagoCheckoutResult<*, *>,
    ) {
        if (hostFinished) return
        hostFinished = true
        activityCallback?.invoke()
        callback?.invoke(result)
        clear()
    }

    fun notifyKeepingHostOpen(
        result: MercadoPagoCheckoutResult<*, *>,
    ) {
        callback?.invoke(result)
    }

    fun dismiss(): Boolean {
        if (hostFinished) return false
        hostFinished = true
        activityCallback?.invoke()
        clear()
        return true
    }

    private fun clear() {
        callback = null
        activityCallback = null
        emailChangeCallback = null
        statusScreenExitCallback = null
        hostFinished = false
    }
}
