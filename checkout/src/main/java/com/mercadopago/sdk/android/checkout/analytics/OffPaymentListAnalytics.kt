package com.mercadopago.sdk.android.checkout.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.CHECKOUT_PAYMENT_BRICK_PATH
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import com.mercadopago.sdk.android.checkout.domain.model.toTrackingValue
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val OFF_PAYMENT_LIST_PATH = "/off_payment_list"
private const val OFF_PAYMENT_LIST_SELECT_PATH = "/off_payment_list_select"
private const val OFF_PAYMENT_LIST_BACK_PATH = "/off_payment_list_back"

internal fun metricOffPaymentListView(
    optionsCount: Int,
    selectionType: SelectionDisplayType,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_PAYMENT_BRICK_PATH$OFF_PAYMENT_LIST_PATH",
    type = TrackType.EVENT,
    data = OffPaymentListViewEventData(
        optionsCount = optionsCount,
        selectionType = selectionType.toTrackingValue(),
    ),
)

internal fun metricOffPaymentListSelect(
    paymentMethodId: String,
    selectionType: SelectionDisplayType,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_PAYMENT_BRICK_PATH$OFF_PAYMENT_LIST_SELECT_PATH",
    type = TrackType.EVENT,
    data = OffPaymentListSelectEventData(
        paymentMethodId = paymentMethodId,
        selectionType = selectionType.toTrackingValue(),
    ),
)

internal fun metricOffPaymentListBack() =
    Metric(
        path = "$SDK_NATIVE_PATH$CHECKOUT_PAYMENT_BRICK_PATH$OFF_PAYMENT_LIST_BACK_PATH",
        type = TrackType.EVENT,
        data = null,
    )

internal data class OffPaymentListViewEventData(
    @SerializedName("options_count")
    val optionsCount: Int,
    @SerializedName("selection_type")
    val selectionType: String,
) : EventData

internal data class OffPaymentListSelectEventData(
    @SerializedName("payment_method_id")
    val paymentMethodId: String,
    @SerializedName("selection_type")
    val selectionType: String,
) : EventData
