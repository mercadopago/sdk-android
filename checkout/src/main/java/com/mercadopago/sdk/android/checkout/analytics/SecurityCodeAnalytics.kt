package com.mercadopago.sdk.android.checkout.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.CHECKOUT_PAYMENT_BRICK_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val CVV_PATH = "/cvv"
private const val CVV_CONTINUE_PATH = "/cvv_continue"
private const val CVV_CONTINUE_ERROR_PATH = "/cvv_continue_error"
private const val CVV_BACK_PATH = "/cvv_back"

internal fun metricSecurityCodeView(
    paymentMethodId: String,
    paymentTypeId: String,
    issuerId: String,
    cardId: String,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_PAYMENT_BRICK_PATH$CVV_PATH",
    type = TrackType.VIEW,
    data = SecurityCodeViewEventData(
        paymentMethodId = paymentMethodId,
        paymentTypeId = paymentTypeId,
        issuerId = issuerId,
        cardId = cardId,
    ),
)

internal fun metricSecurityCodeContinue() =
    Metric(
        path = "$SDK_NATIVE_PATH$CHECKOUT_PAYMENT_BRICK_PATH$CVV_CONTINUE_PATH",
        type = TrackType.EVENT,
        data = null,
    )

internal fun metricSecurityCodeContinueError(
    errorType: String,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_PAYMENT_BRICK_PATH$CVV_CONTINUE_ERROR_PATH",
    type = TrackType.EVENT,
    data = SecurityCodeErrorEventData(errorType = errorType),
)

internal fun metricSecurityCodeBack() =
    Metric(
        path = "$SDK_NATIVE_PATH$CHECKOUT_PAYMENT_BRICK_PATH$CVV_BACK_PATH",
        type = TrackType.EVENT,
        data = SecurityCodeErrorEventData(errorType = "back_pressed"),
    )

internal data class SecurityCodeViewEventData(
    @SerializedName("payment_method_id")
    val paymentMethodId: String,
    @SerializedName("payment_type_id")
    val paymentTypeId: String,
    @SerializedName("issuer_id")
    val issuerId: String,
    @SerializedName("card_id")
    val cardId: String,
) : EventData

internal data class SecurityCodeErrorEventData(
    @SerializedName("error_type")
    val errorType: String,
) : EventData
