package com.mercadopago.sdk.android.checkout.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.ORDER_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val ERROR_PATH = "/error"

internal fun metricOrderError(
    errorType: String,
    orderId: String,
) = Metric(
    path = "$SDK_NATIVE_PATH$ORDER_PATH$ERROR_PATH",
    type = TrackType.EVENT,
    data = OrderErrorEventData(
        errorType = errorType,
        orderId = orderId,
    ),
)

internal data class OrderErrorEventData(
    @SerializedName("error_type")
    val errorType: String,
    @SerializedName("order_id")
    val orderId: String,
) : EventData
