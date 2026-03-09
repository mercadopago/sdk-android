package com.mercadopago.sdk.android.checkout.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mercadopago.sdk.android.checkout.core.EXTRA_CONFIGURATION
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.interactor.Checkout
import com.mercadopago.sdk.android.checkout.presentation.controller.MPCardPayment
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes
import org.koin.compose.KoinContext

internal class CheckoutActivity : ComponentActivity() {
    private val checkoutThemePreferences: CheckoutThemePreferences by Checkout.getInstance().koin.inject()
    private var checkoutCompleted = false

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)

        CheckoutCallbackHolder.setActivityCallback {
            markCheckoutCompleted()
            finish()
        }

        val checkoutConfiguration = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_CONFIGURATION, CheckoutConfiguration::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_CONFIGURATION)
        }

        setContent {
            KoinContext(context = Checkout.getInstance().koin) {
                MercadoPagoTheme(
                    theme = MercadoPagoThemes.Andes,
                    appearance = checkoutThemePreferences.getCurrentAppearance(),
                ) {
                    MPCardPayment(checkoutConfiguration = checkoutConfiguration)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Notify cancellation only if checkout wasn't completed successfully or with error
        if (!checkoutCompleted && !isChangingConfigurations) {
            CheckoutCallbackHolder.notifyCanceled()
        }
    }

    /**
     * Marks the checkout as completed to prevent cancellation notification.
     * Should be called when checkout completes successfully or with error.
     */
    internal fun markCheckoutCompleted() {
        checkoutCompleted = true
    }
}
