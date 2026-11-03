package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.checkout.analytics.metricOffPaymentListBack
import com.mercadopago.sdk.android.checkout.analytics.metricOffPaymentListSelect
import com.mercadopago.sdk.android.checkout.analytics.metricOffPaymentListView
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType

internal class MethodSelectionAnalyticsTracker {
    private var cancelTracked = false

    fun trackView(
        optionsCount: Int,
        selectionType: SelectionDisplayType,
    ) {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricOffPaymentListView(optionsCount, selectionType),
        )
    }

    fun trackSelect(
        paymentMethodId: String,
        selectionType: SelectionDisplayType,
    ) {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricOffPaymentListSelect(paymentMethodId, selectionType),
        )
    }

    fun trackBack() {
        if (cancelTracked) return
        cancelTracked = true
        MPAnalytics.tryGetInstance()?.trackMetric(metricOffPaymentListBack())
    }
}
