package com.mercadopago.sdk.android.checkout.core

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Stable
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutAppearance
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPPaymentMethodConfig
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.ScreenConfig
import com.mercadopago.sdk.android.checkout.core.model.internal.getOnEmailChangeRequested
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.interactor.Checkout
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.presentation.CheckoutActivity
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoUserInterfaceStyle

internal const val EXTRA_CONFIGURATION = "extra_configuration"

/**
 * MercadoPagoCheckout class, used to configure the checkout.
 *
 * Both type parameters are inferred from the [MPCheckoutType] passed to [Builder]:
 * - [T] types [MercadoPagoCheckoutResult.Success.paymentData] — no runtime cast needed.
 * - [C] types [MercadoPagoCheckoutResult.UserCancelled.cancelledData] — no runtime cast needed.
 */
@Stable
class MercadoPagoCheckout<T : MPPaymentData, C : MPUserCancelledContext> private constructor(
    private val context: Context,
    private val checkoutConfiguration: CheckoutConfiguration,
    private val checkoutAppearance: MPCheckoutAppearance?,
) {
    /**
     * Launches the checkout.
     * @param callback Lambda to be invoked when checkout completes with the result.
     * [MercadoPagoCheckoutResult.Success.paymentData] and
     * [MercadoPagoCheckoutResult.UserCancelled.cancelledData] are already the concrete subtypes.
     */
    fun show(
        callback: (MercadoPagoCheckoutResult<T, C>) -> Unit,
    ) {
        CheckoutCallbackHolder.setCallback(callback)
        CheckoutCallbackHolder.setEmailChangeCallback(checkoutConfiguration.getOnEmailChangeRequested())
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
     * @param checkoutType MPCheckoutType — determines [T] and [C], and therefore the concrete types of
     * [MercadoPagoCheckoutResult.Success.paymentData] and [MercadoPagoCheckoutResult.UserCancelled.cancelledData].
     * @param checkoutAppearance MPCheckoutAppearance
     */
    class Builder<T : MPPaymentData, C : MPUserCancelledContext>(
        private val context: Context,
        private val checkoutType: MPCheckoutType<T, C>,
        private val checkoutAppearance: MPCheckoutAppearance? = MPCheckoutAppearance(
            theme = MercadoPagoThemes.Default,
            style = MercadoPagoUserInterfaceStyle.System,
        ),
    ) {
        private var paymentMethodConfigs: List<MPPaymentMethodConfig> = emptyList()
        internal val screenConfigs: LinkedHashSet<ScreenConfig> = linkedSetOf()

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
        fun build(): MercadoPagoCheckout<T, C> =
            MercadoPagoCheckout(
                context = context,
                checkoutAppearance = checkoutAppearance,
                checkoutConfiguration = CheckoutConfiguration(
                    checkoutType = checkoutType,
                    paymentMethodConfigs = paymentMethodConfigs,
                    screenConfigs = screenConfigs.toList(),
                ),
            )
    }
}
