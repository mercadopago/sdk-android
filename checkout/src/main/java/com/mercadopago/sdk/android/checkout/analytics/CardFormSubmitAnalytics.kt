package com.mercadopago.sdk.android.checkout.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.constants.MetricErrorData
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.CHECKOUT_CARD_FORM_PATH
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val SUBMIT_PATH = "/submit"
private const val SUBMIT_ERROR_PATH = "/submit_error"
private const val USER_CANCELED_PATH = "/user_canceled_error"

@KoverIgnore("in development")
internal fun metricCardFormSubmit(
    cardBrand: String,
    transactionAmount: Double,
    issuer: String,
    paymentType: String? = null,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_CARD_FORM_PATH$SUBMIT_PATH",
    type = TrackType.EVENT,
    data = CardFormSubmitEventData(
        cardBrand = cardBrand,
        transactionAmount = transactionAmount,
        issuer = issuer,
        paymentType = paymentType,
    ),
)

@KoverIgnore("in development")
internal fun metricCardFormSubmitError(
    errorType: String,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_CARD_FORM_PATH$SUBMIT_ERROR_PATH",
    type = TrackType.EVENT,
    data = MetricErrorData(errorType = errorType),
)

@KoverIgnore("in development")
internal fun metricCardFormUserCanceledError(
    errorType: String = "",
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_CARD_FORM_PATH$USER_CANCELED_PATH",
    type = TrackType.EVENT,
    data = MetricErrorData(errorType = errorType),
)

@KoverIgnore("in development")
internal data class CardFormSubmitEventData(
    @SerializedName("card_brand")
    val cardBrand: String,
    @SerializedName("transaction_amount")
    val transactionAmount: Double,
    @SerializedName("issuer")
    val issuer: String,
    @SerializedName("payment_type")
    val paymentType: String?,
) : EventData
