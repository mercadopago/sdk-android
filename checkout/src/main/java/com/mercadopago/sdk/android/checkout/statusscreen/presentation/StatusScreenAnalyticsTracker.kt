package com.mercadopago.sdk.android.checkout.statusscreen.presentation

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.checkout.analytics.StatusScreenCloseSource
import com.mercadopago.sdk.android.checkout.analytics.StatusScreenLoadOutcome
import com.mercadopago.sdk.android.checkout.analytics.StatusScreenReceiptOutcome
import com.mercadopago.sdk.android.checkout.analytics.metricStatusScreenClose
import com.mercadopago.sdk.android.checkout.analytics.metricStatusScreenContractViolation
import com.mercadopago.sdk.android.checkout.analytics.metricStatusScreenLoad
import com.mercadopago.sdk.android.checkout.analytics.metricStatusScreenReceipt
import com.mercadopago.sdk.android.checkout.analytics.metricStatusScreenRender
import com.mercadopago.sdk.android.checkout.analytics.metricStatusScreenRetry

internal class StatusScreenAnalyticsTracker {
    private var renderTracked = false
    private var exitTracked = false

    fun trackLoadStarted() {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricStatusScreenLoad(outcome = StatusScreenLoadOutcome.Started, statusType = null),
        )
    }

    fun trackLoadSuccess(
        statusType: String,
    ) {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricStatusScreenLoad(outcome = StatusScreenLoadOutcome.Success, statusType = statusType),
        )
    }

    fun trackLoadFailure() {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricStatusScreenLoad(outcome = StatusScreenLoadOutcome.Failure, statusType = null),
        )
    }

    fun trackContractViolation() {
        MPAnalytics.tryGetInstance()?.trackMetric(metricStatusScreenContractViolation())
    }

    fun trackRetry(
        statusType: String?,
    ) {
        if (exitTracked) return
        exitTracked = true
        MPAnalytics.tryGetInstance()?.trackMetric(metricStatusScreenRetry(statusType = statusType))
    }

    fun trackReceipt(
        outcome: StatusScreenReceiptOutcome,
        statusType: String?,
    ) {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricStatusScreenReceipt(outcome = outcome, statusType = statusType),
        )
    }

    fun trackRender(
        statusType: String,
        canRetry: Boolean? = null,
    ) {
        if (renderTracked) return
        renderTracked = true
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricStatusScreenRender(statusType = statusType, canRetry = canRetry),
        )
    }

    fun trackClose(
        source: StatusScreenCloseSource,
        statusType: String?,
    ) {
        if (exitTracked) return
        exitTracked = true
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricStatusScreenClose(source = source, statusType = statusType),
        )
    }
}
