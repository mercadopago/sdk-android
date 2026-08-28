package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.checkout.analytics.CHANGED_FIELD_EMAIL
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.NOT_APPLY
import com.mercadopago.sdk.android.checkout.analytics.ReviewConfirmPaymentMethodEventData
import com.mercadopago.sdk.android.checkout.analytics.metricReviewConfirmBack
import com.mercadopago.sdk.android.checkout.analytics.metricReviewConfirmContinue
import com.mercadopago.sdk.android.checkout.analytics.metricReviewConfirmImpression
import com.mercadopago.sdk.android.checkout.analytics.metricReviewConfirmPayerFieldChanged
import com.mercadopago.sdk.android.checkout.analytics.metricReviewConfirmPaymentMethodChanged
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams

internal class ReviewConfirmAnalyticsTracker(
    private val processOrderParams: ProcessOrderParams,
) {
    fun trackImpression() {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricReviewConfirmImpression(paymentMethodEventData()),
        )
    }

    fun trackContinue() {
        MPAnalytics.tryGetInstance()?.trackMetric(metricReviewConfirmContinue())
    }

    fun trackBack() {
        MPAnalytics.tryGetInstance()?.trackMetric(metricReviewConfirmBack())
    }

    fun trackPaymentMethodChanged() {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricReviewConfirmPaymentMethodChanged(paymentMethodEventData()),
        )
    }

    fun trackPayerFieldChanged() {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricReviewConfirmPayerFieldChanged(changedField = CHANGED_FIELD_EMAIL),
        )
    }

    private fun paymentMethodEventData() =
        ReviewConfirmPaymentMethodEventData(
            type = processOrderParams.paymentMethodType.ifEmpty { NOT_APPLY },
            paymentMethodId = processOrderParams.paymentMethodId.ifEmpty { NOT_APPLY },
            paymentTypeId = processOrderParams.paymentMethodType.ifEmpty { NOT_APPLY },
            issuerId = processOrderParams.issuerId ?: NOT_APPLY,
            cardId = processOrderParams.productId ?: NOT_APPLY,
            transactionAmount = processOrderParams.amount.toDoubleOrNull() ?: 0.0,
            installments = processOrderParams.installments,
        )
}
