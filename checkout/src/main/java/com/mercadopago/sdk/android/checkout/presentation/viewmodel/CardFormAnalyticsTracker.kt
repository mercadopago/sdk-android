package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.observability.domain.interactor.NativeErrorReceipt
import com.mercadopago.sdk.android.analytics.observability.domain.interactor.NativeErrorReporting
import com.mercadopago.sdk.android.analytics.observability.domain.interactor.captureOrFallback
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorOperation
import com.mercadopago.sdk.android.analytics.observability.runtime.NativeErrorReporterProvider
import com.mercadopago.sdk.android.checkout.analytics.CheckoutErrorObservability
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormDropdownSelection
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormInitializeError
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormInputValidation
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormSubmit
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormSubmitError
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormUserCanceledError
import com.mercadopago.sdk.android.checkout.analytics.metricOrderError
import com.mercadopago.sdk.android.checkout.analytics.metricOrderSubmit
import com.mercadopago.sdk.android.checkout.analytics.toAnalyticsString
import com.mercadopago.sdk.android.checkout.analytics.toErrorTypeString
import com.mercadopago.sdk.android.checkout.core.model.MPCardType
import com.mercadopago.sdk.android.checkout.domain.model.ObservedCheckoutError
import com.mercadopago.sdk.android.checkout.presentation.model.CancelReason

internal class CardFormAnalyticsTracker(
    private val isLoading: () -> Boolean,
    private val nativeErrorReporter: () -> NativeErrorReporting? = NativeErrorReporterProvider::getOrNull,
    private val errorObservability: CheckoutErrorObservability = CheckoutErrorObservability(),
) {
    private var canceled = false

    fun trackInitializeError(
        error: ObservedCheckoutError,
    ) {
        capture(NativeErrorOperation.CARD_FORM_INITIALIZATION, error).sendLegacy { eventId ->
            metricCardFormInitializeError(
                errorType = error.publicError.toErrorTypeString(),
                observabilityEventId = eventId,
            )
        }
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

    fun trackOrderSubmit(
        orderId: String,
        orderStatus: String,
    ) {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricOrderSubmit(
                orderId = orderId,
                orderStatus = orderStatus,
            ),
        )
    }

    fun trackSubmitError(
        error: ObservedCheckoutError,
    ) {
        capture(NativeErrorOperation.CARD_FORM_SUBMISSION, error).sendLegacy { eventId ->
            metricCardFormSubmitError(
                errorType = error.publicError.toErrorTypeString(),
                observabilityEventId = eventId,
            )
        }
    }

    fun trackOrderError(
        error: ObservedCheckoutError,
        orderId: String,
    ) {
        capture(NativeErrorOperation.ORDER_SUBMISSION, error).sendLegacy { eventId ->
            metricOrderError(
                errorType = error.publicError.toErrorTypeString(),
                orderId = orderId,
                observabilityEventId = eventId,
            )
        }
    }

    fun trackUserCanceled(
        reason: CancelReason,
    ) {
        canceled = true
        capture(NativeErrorOperation.CARD_FORM_CANCELLATION).sendLegacy { eventId ->
            metricCardFormUserCanceledError(
                errorType = reason.analyticsValue,
                observabilityEventId = eventId,
            )
        }
    }

    private fun capture(
        operation: NativeErrorOperation,
        error: ObservedCheckoutError? = null,
    ): NativeErrorReceipt =
        nativeErrorReporter.captureOrFallback(operation) {
            error?.let(errorObservability::input) ?: errorObservability.cancellationInput()
        }

    private fun NativeErrorReceipt.sendLegacy(
        metric: (String?) -> Metric,
    ) {
        if (shouldSendMelidata) {
            MPAnalytics.tryGetInstance()?.trackMetric(metric(eventId))
        }
    }
}
