package com.mercadopago.sdk.android.checkout.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormInitialize
import com.mercadopago.sdk.android.checkout.analytics.sellerCustomization
import com.mercadopago.sdk.android.checkout.analytics.toAnalyticsString
import com.mercadopago.sdk.android.checkout.core.EXTRA_CONFIGURATION
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.CARD_SAVE
import com.mercadopago.sdk.android.checkout.core.model.internal.CARD_TRANSACTION
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.preferences.CheckoutThemePreferences
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.extensions.extractCardFilters
import com.mercadopago.sdk.android.checkout.domain.interactor.Checkout
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import org.koin.compose.KoinContext

internal class CheckoutActivity : ComponentActivity() {
    private val checkoutThemePreferences: CheckoutThemePreferences by Checkout.getInstance(this).koin.inject()

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)

        CheckoutCallbackHolder.setActivityCallback {
            finish()
        }

        val checkoutConfiguration = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_CONFIGURATION, CheckoutConfiguration::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_CONFIGURATION)
        }

        trackInitialize(checkoutConfiguration)

        setContent {
            KoinContext(context = Checkout.getInstance(this).koin) {
                MercadoPagoTheme(
                    theme = checkoutThemePreferences.getCurrentThemeScheme(),
                    style = checkoutThemePreferences.getCurrentStyle(),
                ) {
                    CheckoutController(checkoutConfiguration = checkoutConfiguration)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Checkout.clearInstance()
    }

    private fun trackInitialize(
        checkoutConfiguration: CheckoutConfiguration?,
    ) {
        val (cardTypes, cardBrands) = checkoutConfiguration?.paymentMethods.extractCardFilters()
        val checkoutType = when (checkoutConfiguration?.checkoutType) {
            is CheckoutType.CardSave -> CARD_SAVE
            is CheckoutType.CardTransaction -> CARD_TRANSACTION
            null -> ""
        }
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormInitialize(
                checkoutType = checkoutType,
                appearance = checkoutThemePreferences.getCurrentStyle().toAnalyticsString(),
                sellerCustomization = checkoutThemePreferences.getCurrentThemeScheme().sellerCustomization,
                allowedPaymentTypes = cardTypes.map { it.toAnalyticsString() },
                allowedPaymentMethods = cardBrands.map { it.name },
            ),
        )
    }
}
