package com.mercadopago.sdk.android.checkout.core

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Stable
import com.mercadopago.sdk.android.checkout.core.model.CheckoutAppearance
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.PaymentMethod
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallback
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.interactor.Checkout
import com.mercadopago.sdk.android.checkout.presentation.CheckoutActivity
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeAppearance
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes
import org.koin.core.Koin

internal const val EXTRA_CONFIGURATION = "extra_configuration"

/**
 * MercadoPagoCheckout class, used to configure the checkout
 */
@Stable
class MercadoPagoCheckout private constructor(
    private val context: Context,
    private val checkoutConfiguration: CheckoutConfiguration,
    private val checkoutAppearance: CheckoutAppearance?,
    private val koin: Koin = Checkout.getInstance().koin,
) {
    /**
     * Launches the checkout
     */
    fun start() {
        koin.get<CheckoutThemePreferences>().apply {
            setCurrentAppearance(checkoutAppearance?.appearance ?: MercadoPagoThemeAppearance.System)
            setCurrentThemeScheme(checkoutAppearance?.theme ?: MercadoPagoThemes.Legacy)
        }
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(EXTRA_CONFIGURATION, checkoutConfiguration)
        }
        context.startActivity(intent)
    }

    /**
     * Builder for MercadoPagoCheckout
     * @param context Context
     * @param checkoutType CheckoutType
     * @param checkoutAppearance CheckoutAppearance
     */
    class Builder(
        private val context: Context,
        private val checkoutType: CheckoutType,
        private val checkoutAppearance: CheckoutAppearance? = CheckoutAppearance(
            theme = MercadoPagoThemes.Legacy,
            appearance = MercadoPagoThemeAppearance.System,
        ),
    ) {
        private var paymentMethods: List<PaymentMethod> = emptyList()
        private var callback: CheckoutCallback? = null

        /**
         * Sets the payment methods
         * @param paymentMethods List of payment methods
         */
        fun setPaymentMethods(
            paymentMethods: List<PaymentMethod> = PaymentMethod.defaults,
        ) = apply { this.paymentMethods = paymentMethods }

        /**
         * Sets the callback to receive checkout results
         * @param callback Callback to be invoked when checkout completes
         */
        fun setCallback(
            callback: CheckoutCallback,
        ) = apply {
            this.callback = callback
        }

        /**
         * Builds the MercadoPagoCheckout
         */
        fun build(): MercadoPagoCheckout {
            callback?.let { CheckoutCallbackHolder.setCallback(it) }

            return MercadoPagoCheckout(
                context = context,
                checkoutAppearance = checkoutAppearance,
                checkoutConfiguration = CheckoutConfiguration(
                    checkoutType = checkoutType,
                    paymentMethods = paymentMethods,
                ),
            )
        }
    }
}
