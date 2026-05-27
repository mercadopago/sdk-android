package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormDropdownSelection
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormInitializeError
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormInputValidation
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormSubmit
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormSubmitError
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormUserCanceledError
import com.mercadopago.sdk.android.checkout.analytics.toAnalyticsString
import com.mercadopago.sdk.android.checkout.analytics.toErrorTypeString
import com.mercadopago.sdk.android.checkout.core.model.MPCardType
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.presentation.model.CancelReason

internal class CardFormAnalyticsTracker(
    private val isLoading: () -> Boolean,
) {
    private var canceled = false

    fun trackInitializeError(
        error: MercadoPagoCheckoutError,
    ) {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormInitializeError(errorType = error.toErrorTypeString()),
        )
    }

    fun trackInputValidation(
        field: String,
        isValid: Boolean,
    ) {
        if (canceled || isLoading()) return
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormInputValidation(field = field, isInputValid = isValid),
        )
    }

    fun trackDropdownSelection(
        type: String,
    ) {
        if (canceled || isLoading()) return
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormDropdownSelection(dropdownSelectionType = type),
        )
    }

    fun trackSubmit(
        cardBrand: String,
        transactionAmount: Double,
        issuer: String,
        paymentTypeId: String,
    ) {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormSubmit(
                cardBrand = cardBrand,
                transactionAmount = transactionAmount,
                issuer = issuer,
                paymentType = MPCardType.fromString(paymentTypeId)?.toAnalyticsString(),
            ),
        )
    }

    fun trackSubmitError(
        error: MercadoPagoCheckoutError,
    ) {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormSubmitError(errorType = error.toErrorTypeString()),
        )
    }

    fun trackUserCanceled(
        reason: CancelReason,
    ) {
        canceled = true
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormUserCanceledError(errorType = reason.analyticsValue),
        )
    }
}
