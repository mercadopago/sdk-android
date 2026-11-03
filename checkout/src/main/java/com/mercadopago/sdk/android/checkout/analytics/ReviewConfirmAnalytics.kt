package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.CHECKOUT_REVIEW_CONFIRM_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val CONTINUE_SUFFIX = "_continue"
private const val BACK_SUFFIX = "_back"
private const val PAYMENT_METHOD_CHANGED_SUFFIX = "_payment_method_changed"
private const val PAYER_FIELD_CHANGED_SUFFIX = "_payer_field_changed"

internal const val CHANGED_FIELD_EMAIL = "email"

internal fun metricReviewConfirmImpression(
    data: ReviewConfirmPaymentMethodEventData,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_REVIEW_CONFIRM_PATH",
    type = TrackType.EVENT,
    data = data,
)

internal fun metricReviewConfirmContinue() =
    Metric(
        path = "$SDK_NATIVE_PATH$CHECKOUT_REVIEW_CONFIRM_PATH$CONTINUE_SUFFIX",
        type = TrackType.EVENT,
    )

internal fun metricReviewConfirmBack() =
    Metric(
        path = "$SDK_NATIVE_PATH$CHECKOUT_REVIEW_CONFIRM_PATH$BACK_SUFFIX",
        type = TrackType.EVENT,
    )

internal fun metricReviewConfirmPaymentMethodChanged(
    data: ReviewConfirmPaymentMethodEventData,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_REVIEW_CONFIRM_PATH$PAYMENT_METHOD_CHANGED_SUFFIX",
    type = TrackType.EVENT,
    data = data,
)

internal fun metricReviewConfirmPayerFieldChanged(
    changedField: String,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_REVIEW_CONFIRM_PATH$PAYER_FIELD_CHANGED_SUFFIX",
    type = TrackType.EVENT,
    data = ReviewConfirmPayerFieldChangedEventData(changedField = changedField),
)
