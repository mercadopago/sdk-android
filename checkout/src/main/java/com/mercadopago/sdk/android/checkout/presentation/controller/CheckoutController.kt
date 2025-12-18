@file:OptIn(ExperimentalMaterial3Api::class)

package com.mercadopago.sdk.android.checkout.presentation.controller

import android.content.Context
import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.domain.interactor.Checkout
import com.mercadopago.sdk.android.checkout.presentation.CheckoutActivity
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeAppearance
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemeProviderScheme
import com.mercadopago.sdk.android.foundation.theme.default.MercadoPagoDefaultThemes
import org.koin.core.Koin

/**
 * Controller class responsible for managing the checkout flow and theme configuration.
 * This class handles launching the checkout flow and managing theme preferences.
 *
 * @param context [Context] The context used for launching the flow
 * @param theme [MercadoPagoThemeProviderScheme] The theme scheme to be applied to the checkout flow
 * @param appearance [MercadoPagoThemeAppearance] The appearance mode (light/dark/system) for the checkout flow
 * @param koin [Koin] The Koin instance used for dependency injection.
 *
 * @constructor internal constructor for [CheckoutController]. Use [CheckoutController.create] instead.
 */
@Stable
class CheckoutController internal constructor(
    private val context: Context,
    private val theme: MercadoPagoThemeProviderScheme = MercadoPagoDefaultThemes.Default,
    private val appearance: MercadoPagoThemeAppearance = MercadoPagoThemeAppearance.System,
    private val koin: Koin = Checkout.getInstance().koin,
) {
    /**
     * Launches the checkout flow in a bottom sheet with the configured theme
     * preferences and starting the checkout flow.
     */
    fun launchCheckout() {
        koin.get<CheckoutThemePreferences>().apply {
            setCurrentAppearance(appearance)
            setCurrentThemeScheme(theme)
        }
        val intent = Intent(context, CheckoutActivity::class.java)
        context.startActivity(intent)
    }

    /**
     * Companion object for [CheckoutController].
     */
    companion object {
        /**
         * Creates a new instance of [CheckoutController].
         * This method should be called to create the checkout instance from an activity or fragment.
         * For compose, use [rememberCheckout] instead.
         * @param context The Android context of the current screen
         * @param theme The theme scheme to be applied to the checkout flow
         * @param appearance The appearance mode (light/dark/system) for the checkout flow
         * @return A new instance of [CheckoutController]
         */
        fun create(
            context: Context,
            theme: MercadoPagoThemeProviderScheme = MercadoPagoDefaultThemes.Default,
            appearance: MercadoPagoThemeAppearance = MercadoPagoThemeAppearance.System,
        ): CheckoutController =
            CheckoutController(
                context = context,
                theme = theme,
                appearance = appearance,
            )
    }
}

/**
 * Composable function that creates and remembers a [CheckoutController] instance.
 * For an activity or fragment, use [CheckoutController.create] instead.
 *
 * @param theme The theme scheme to be applied to the checkout flow
 * @param appearance The appearance mode (light/dark/system) for the checkout flow
 * @return A remembered instance of [CheckoutController]
 */
@Composable
fun rememberCheckout(
    theme: MercadoPagoThemeProviderScheme = MercadoPagoDefaultThemes.Default,
    appearance: MercadoPagoThemeAppearance = MercadoPagoThemeAppearance.System,
): CheckoutController {
    val context = LocalContext.current

    val controller = remember(theme, appearance) {
        CheckoutController(
            context = context,
            theme = theme,
            appearance = appearance,
        )
    }
    return controller
}
