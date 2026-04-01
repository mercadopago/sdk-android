package com.mercadopago.sdk.android.checkout.core

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Stable
import com.mercadopago.sdk.android.checkout.core.model.CheckoutAppearance
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.PaymentMethod
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
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
     * @param callback Lambda to be invoked when checkout completes with the result
     */
    fun show(
        callback: (MercadoPagoCheckoutResult) -> Unit,
    ) {
        CheckoutCallbackHolder.setCallback(callback)
        koin.get<CheckoutThemePreferences>().apply {
            setCurrentAppearance(checkoutAppearance?.appearance ?: MercadoPagoThemeAppearance.System)
            setCurrentThemeScheme(checkoutAppearance?.theme ?: MercadoPagoThemes.Default)
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
            theme = MercadoPagoThemes.Default,
            appearance = MercadoPagoThemeAppearance.System,
        ),
    ) {
        private var paymentMethods: List<PaymentMethod> = emptyList()

        /**
         * Sets the payment methods
         * @param paymentMethods List of payment methods
         */
        fun setPaymentMethods(
            paymentMethods: List<PaymentMethod> = PaymentMethod.defaults,
        ) = apply { this.paymentMethods = paymentMethods }

        /**
         * Builds the MercadoPagoCheckout
         */
        fun build(): MercadoPagoCheckout =
            MercadoPagoCheckout(
                context = context,
                checkoutAppearance = checkoutAppearance,
                checkoutConfiguration = CheckoutConfiguration(
                    checkoutType = checkoutType,
                    paymentMethods = paymentMethods,
                ),
            )
    }
}
