package com.mercadopago.sdk.android.checkout.core

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Stable
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutAppearance
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPPaymentMethodConfig
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.interactor.Checkout
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.presentation.CheckoutActivity
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoUserInterfaceStyle

internal const val EXTRA_CONFIGURATION = "extra_configuration"

/**
 * MercadoPagoCheckout class, used to configure the checkout.
 *
 * The type parameter [T] is inferred from the [CheckoutType] passed to [Builder],
 * so the callback in [show] receives a typed [MercadoPagoCheckoutResult] —
 * no runtime cast needed to access payment-specific fields.
 */
@Stable
class MercadoPagoCheckout<T : MPPaymentData> private constructor(
    private val context: Context,
    private val checkoutConfiguration: CheckoutConfiguration,
    private val checkoutAppearance: MPCheckoutAppearance?,
) {
    /**
     * Launches the checkout.
     * @param callback Lambda to be invoked when checkout completes with the result.
     * [MercadoPagoCheckoutResult.Success.paymentData] is already the concrete [T] subtype.
     */
    fun show(
        callback: (MercadoPagoCheckoutResult<T>) -> Unit,
    ) {
        CheckoutCallbackHolder.setCallback(callback)
        Checkout.getInstance(context).koin.get<CheckoutThemePreferences>().apply {
            setCurrentStyle(
                checkoutAppearance?.style ?: MercadoPagoUserInterfaceStyle.System,
            )
            setCurrentThemeScheme(checkoutAppearance?.theme ?: MercadoPagoThemes.Default)
        }
        val intent = Intent(context, CheckoutActivity::class.java).apply {
            putExtra(EXTRA_CONFIGURATION, checkoutConfiguration)
        }
        context.startActivity(intent)
    }

    /**
     * Builder for MercadoPagoCheckout.
     * @param context Context
     * @param checkoutType MPCheckoutType — determines [T] and therefore the type of
     * [MercadoPagoCheckoutResult.Success.paymentData] delivered to [show]'s callback.
     * @param checkoutAppearance MPCheckoutAppearance
     */
    class Builder<T : MPPaymentData>(
        private val context: Context,
        private val checkoutType: MPCheckoutType<T>,
        private val checkoutAppearance: MPCheckoutAppearance? = MPCheckoutAppearance(
            theme = MercadoPagoThemes.Default,
            style = MercadoPagoUserInterfaceStyle.System,
        ),
    ) {
        private var paymentMethodConfigs: List<MPPaymentMethodConfig> = emptyList()

        /**
         * Sets the payment methods
         * @param paymentMethodConfigs List of payment methods
         */
        fun setPaymentMethodConfiguration(
            paymentMethodConfigs: List<MPPaymentMethodConfig> = MPPaymentMethodConfig.defaults,
        ) = apply { this.paymentMethodConfigs = paymentMethodConfigs }

        /**
         * Builds the MercadoPagoCheckout
         */
        fun build(): MercadoPagoCheckout<T> =
            MercadoPagoCheckout(
                context = context,
                checkoutAppearance = checkoutAppearance,
                checkoutConfiguration = CheckoutConfiguration(
                    checkoutType = checkoutType,
                    paymentMethodConfigs = paymentMethodConfigs,
                ),
            )
    }
}
