package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.CHECKOUT_STATUS_SCREEN_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val LOAD_PATH = "/load"
private const val RENDER_PATH = "/render"
private const val CLOSE_PATH = "/close"
private const val CONTRACT_VIOLATION_PATH = "/contract_violation"
private const val RETRY_PATH = "/retry"
private const val RECEIPT_PATH = "/receipt"

internal enum class StatusScreenLoadOutcome(val trackingValue: String) {
    Started("started"),
    Success("success"),
    Failure("failure"),
    Rejected("rejected"),
}

internal enum class StatusScreenCloseSource(val trackingValue: String) {
    Back("back"),
    Unavailable("unavailable"),
    Dismiss("dismiss"),
}

internal enum class StatusScreenReceiptOutcome(val trackingValue: String) {
    Requested("requested"),
    Success("success"),
    Failure("failure"),
    Dismissed("dismissed"),
    Cancelled("cancelled"),
}

internal fun metricStatusScreenLoad(
    outcome: StatusScreenLoadOutcome,
    statusType: String?,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_STATUS_SCREEN_PATH$LOAD_PATH",
    type = TrackType.EVENT,
    data = StatusScreenLoadEventData(outcome = outcome.trackingValue, statusType = statusType),
)

internal fun metricStatusScreenRender(
    statusType: String,
    canRetry: Boolean? = null,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_STATUS_SCREEN_PATH$RENDER_PATH",
    type = TrackType.VIEW,
    data = StatusScreenViewEventData(statusType = statusType, canRetry = canRetry),
)

internal fun metricStatusScreenClose(
    source: StatusScreenCloseSource,
    statusType: String?,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_STATUS_SCREEN_PATH$CLOSE_PATH",
    type = TrackType.EVENT,
    data = StatusScreenCloseEventData(source = source.trackingValue, statusType = statusType),
)

internal fun metricStatusScreenContractViolation() =
    Metric(
        path = "$SDK_NATIVE_PATH$CHECKOUT_STATUS_SCREEN_PATH$CONTRACT_VIOLATION_PATH",
        type = TrackType.EVENT,
        data = StatusScreenLoadEventData(outcome = StatusScreenLoadOutcome.Rejected.trackingValue, statusType = null),
    )

internal fun metricStatusScreenRetry(
    statusType: String?,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_STATUS_SCREEN_PATH$RETRY_PATH",
    type = TrackType.EVENT,
    data = StatusScreenLoadEventData(outcome = StatusScreenLoadOutcome.Success.trackingValue, statusType = statusType),
)

internal fun metricStatusScreenReceipt(
    outcome: StatusScreenReceiptOutcome,
    statusType: String?,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_STATUS_SCREEN_PATH$RECEIPT_PATH",
    type = TrackType.EVENT,
    data = StatusScreenLoadEventData(outcome = outcome.trackingValue, statusType = statusType),
)
