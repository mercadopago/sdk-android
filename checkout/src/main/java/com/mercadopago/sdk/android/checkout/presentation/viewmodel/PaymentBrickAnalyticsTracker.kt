package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.checkout.analytics.metricPaymentBrickInitialize
import com.mercadopago.sdk.android.checkout.analytics.metricPaymentBrickInitializeError
import com.mercadopago.sdk.android.checkout.analytics.metricPaymentBrickSelected

internal class PaymentBrickAnalyticsTracker {
    fun trackInitialize() {
        MPAnalytics.tryGetInstance()?.trackMetric(metricPaymentBrickInitialize())
    }

    fun trackInitializeError() {
        MPAnalytics.tryGetInstance()?.trackMetric(metricPaymentBrickInitializeError())
    }

    fun trackSelected() {
        MPAnalytics.tryGetInstance()?.trackMetric(metricPaymentBrickSelected())
    }
}
