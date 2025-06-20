package com.mercadopago.sdk.android.checkout.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.domain.interactor.Checkout
import com.mercadopago.sdk.android.checkout.presentation.controller.CheckoutBottomSheet
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

internal class CheckoutActivity : ComponentActivity() {
    private val checkoutThemePreferences: CheckoutThemePreferences by Checkout.getInstance().koin.inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MercadoPagoTheme(
                theme = checkoutThemePreferences.getCurrentThemeScheme(),
                appearance = checkoutThemePreferences.getCurrentAppearance(),
            ) {
                CheckoutBottomSheet(
                    onDismissRequest = { finish() },
                )
            }
        }
    }
}
