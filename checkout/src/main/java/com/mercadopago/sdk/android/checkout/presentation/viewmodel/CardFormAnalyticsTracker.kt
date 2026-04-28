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
import com.mercadopago.sdk.android.checkout.core.model.CardType
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.presentation.model.CancelReason

internal class CardFormAnalyticsTracker(
    private val isCancelling: () -> Boolean,
    private val isLoading: () -> Boolean,
) {
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
        if (isCancelling() || isLoading()) return
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormInputValidation(field = field, isInputValid = isValid),
        )
    }

    fun trackDropdownSelection(
        type: String,
    ) {
        if (isCancelling() || isLoading()) return
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormDropdownSelection(dropdownSelectionType = type),
        )
    }

    fun trackSubmit(
        cardBrand: String,
        transactionAmount: Double?,
        issuer: String,
        paymentTypeId: String,
    ) {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormSubmit(
                cardBrand = cardBrand,
                transactionAmount = transactionAmount,
                issuer = issuer,
                paymentType = CardType.fromString(paymentTypeId)?.toAnalyticsString(),
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
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormUserCanceledError(errorType = reason.analyticsValue),
        )
    }
}
