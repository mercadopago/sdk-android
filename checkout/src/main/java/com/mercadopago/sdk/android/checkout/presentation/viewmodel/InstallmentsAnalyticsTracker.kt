package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.observability.domain.interactor.NativeErrorReporting
import com.mercadopago.sdk.android.analytics.observability.domain.interactor.captureOrFallback
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorOperation
import com.mercadopago.sdk.android.analytics.observability.runtime.NativeErrorReporterProvider
import com.mercadopago.sdk.android.checkout.analytics.CheckoutErrorObservability
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
    private val orderId: String,
    private val nativeErrorReporter: () -> NativeErrorReporting? = NativeErrorReporterProvider::getOrNull,
    private val errorObservability: CheckoutErrorObservability = CheckoutErrorObservability(),
) {
    private var terminated = false

    fun trackInitialize() {
        val transaction = paymentData as? MPPaymentData.CardTransaction
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricInstallmentsInitialize(
                InstallmentsInitializeEventData(
                    checkoutType = checkoutType,
                    paymentMethodId = transaction?.paymentMethodId.orEmpty(),
                    paymentType = transaction?.paymentTypeId.orEmpty(),
                    selectionType = installmentData.display.displayType.toAnalyticsString(),
                    quotasCount = installmentData.quotas.size,
                    transactionAmount = 0.0,
                    orderId = orderId,
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
        val receipt = nativeErrorReporter.captureOrFallback(NativeErrorOperation.INSTALLMENTS_CANCELLATION) {
            errorObservability.cancellationInput()
        }
        if (receipt.shouldSendMelidata) {
            MPAnalytics.tryGetInstance()?.trackMetric(
                metricInstallmentsUserCanceledError(
                    errorType = reason.analyticsValue,
                    observabilityEventId = receipt.eventId,
                ),
            )
        }
    }
}
