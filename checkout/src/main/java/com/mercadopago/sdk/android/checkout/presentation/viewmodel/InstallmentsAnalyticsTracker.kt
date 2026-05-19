package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.checkout.analytics.InstallmentsCancelReason
import com.mercadopago.sdk.android.checkout.analytics.InstallmentsInitializeEventData
import com.mercadopago.sdk.android.checkout.analytics.metricInstallmentsInitialize
import com.mercadopago.sdk.android.checkout.analytics.metricInstallmentsSelected
import com.mercadopago.sdk.android.checkout.analytics.metricInstallmentsSubmit
import com.mercadopago.sdk.android.checkout.analytics.metricInstallmentsUserCanceledError
import com.mercadopago.sdk.android.checkout.analytics.toAnalyticsString
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.Quota

internal class InstallmentsAnalyticsTracker(
    private val checkoutType: String,
    private val paymentData: MPPaymentData,
    private val installmentData: MPInstallmentData,
) {
    private var terminated = false

    fun trackInitialize() {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricInstallmentsInitialize(
                InstallmentsInitializeEventData(
                    checkoutType = checkoutType,
                    paymentMethodId = paymentData.paymentMethodId,
                    paymentType = paymentData.paymentTypeId,
                    selectionType = installmentData.display.displayType.toAnalyticsString(),
                    quotasCount = installmentData.quotas.size,
                    transactionAmount = paymentData.transactionAmount?.toDouble(),
                ),
            ),
        )
    }

    fun trackSelected(
        installment: Int,
    ) {
        if (terminated) return
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricInstallmentsSelected(installments = installment),
        )
    }

    fun trackSubmit(
        quota: Quota,
    ) {
        if (terminated) return
        val installments = quota.installments
        val installmentAmount = quota.installmentAmount?.toDouble()
        val totalAmount = quota.totalAmount?.toDouble()
        if (installments != null && installmentAmount != null && totalAmount != null) {
            terminated = true
            MPAnalytics.tryGetInstance()?.trackMetric(
                metricInstallmentsSubmit(
                    installments = installments,
                    installmentAmount = installmentAmount,
                    totalAmount = totalAmount,
                ),
            )
        }
    }

    fun trackUserCanceled(
        reason: InstallmentsCancelReason,
    ) {
        if (terminated) return
        terminated = true
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricInstallmentsUserCanceledError(errorType = reason.analyticsValue),
        )
    }
}
