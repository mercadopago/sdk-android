package com.mercadopago.sdk.android.checkout.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mercadopago.sdk.android.checkout.core.MercadoPagoCheckout
import com.mercadopago.sdk.android.checkout.core.model.internal.Configuration
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.domain.interactor.Checkout
import com.mercadopago.sdk.android.checkout.presentation.controller.MPCardPayment
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes
import org.koin.compose.KoinContext

internal class CheckoutActivity : ComponentActivity() {
    private val checkoutThemePreferences: CheckoutThemePreferences by Checkout.getInstance().koin.inject()
    private var configuration: Configuration? = null

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)

        configuration = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(MercadoPagoCheckout.EXTRA_CONFIGURATION, Configuration::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(MercadoPagoCheckout.EXTRA_CONFIGURATION)
        }

        setContent {
            KoinContext(context = Checkout.getInstance().koin) {
                MercadoPagoTheme(
                    theme = MercadoPagoThemes.Andes,
                    appearance = checkoutThemePreferences.getCurrentAppearance(),
                ) {
                    MPCardPayment()
                }
            }
        }
    }
}
