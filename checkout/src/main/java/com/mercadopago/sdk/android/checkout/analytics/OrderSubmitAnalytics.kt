package com.mercadopago.sdk.android.checkout.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.ORDER_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val SUBMIT_PATH = "/submit"

internal fun metricOrderSubmit(
    orderId: String,
    orderStatus: String,
) = Metric(
    path = "$SDK_NATIVE_PATH$ORDER_PATH$SUBMIT_PATH",
    type = TrackType.EVENT,
    data = OrderSubmitEventData(
        orderId = orderId,
        orderStatus = orderStatus,
    ),
)

internal data class OrderSubmitEventData(
    @SerializedName("order_id")
    val orderId: String,
    @SerializedName("order_status")
    val orderStatus: String,
) : EventData
