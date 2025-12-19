package com.mercadopago.sdk.android.checkout.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.domain.interactor.Checkout
import com.mercadopago.sdk.android.checkout.presentation.controller.MPCardPayment
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import org.koin.compose.KoinContext

internal class CheckoutActivity : ComponentActivity() {
    private val checkoutThemePreferences: CheckoutThemePreferences by Checkout.getInstance().koin.inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinContext(context = Checkout.getInstance().koin) {
                MercadoPagoTheme(
                    theme = checkoutThemePreferences.getCurrentThemeScheme(),
                    appearance = checkoutThemePreferences.getCurrentAppearance(),
                ) {
                    MPCardPayment()
                }
            }
        }
    }
}
