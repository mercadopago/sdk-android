package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.CHECKOUT_PAYMENT_BRICK_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val INITIALIZE_PATH = "/initialize"
private const val INITIALIZE_ERROR_PATH = "/initialize_error"
private const val SELECTED_PATH = "/selected"

internal fun metricPaymentBrickInitialize() =
    Metric(
        path = "$SDK_NATIVE_PATH$CHECKOUT_PAYMENT_BRICK_PATH$INITIALIZE_PATH",
        type = TrackType.VIEW,
        data = null,
    )

internal fun metricPaymentBrickInitializeError() =
    Metric(
        path = "$SDK_NATIVE_PATH$CHECKOUT_PAYMENT_BRICK_PATH$INITIALIZE_ERROR_PATH",
        type = TrackType.EVENT,
        data = null,
    )

internal fun metricPaymentBrickSelected() =
    Metric(
        path = "$SDK_NATIVE_PATH$CHECKOUT_PAYMENT_BRICK_PATH$SELECTED_PATH",
        type = TrackType.EVENT,
        data = null,
    )
