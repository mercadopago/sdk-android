package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.constants.AnalyticsConstants.ERROR_PATH
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.coremethods.analytics.CoreMethodsAnalyticsConstants.CORE_METHODS_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val PAYMENT_METHODS_PATH = "/payment_methods"

@KoverIgnore("in development")
internal fun metricPaymentMethodCallSuccess(
    issuer: String,
    cardBrand: String,
    paymentType: String?,
    securityLength: Int?,
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH$PAYMENT_METHODS_PATH",
    type = TrackType.EVENT,
    data = PaymentMethodEventData(
        issuer = issuer,
        cardBrand = cardBrand,
        paymentType = paymentType,
        securityLength = securityLength,
    ),
)

@KoverIgnore("in development")
internal fun metricPaymentMethodCallError(
    error: String,
    issuer: String = "",
    cardBrand: String = "",
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH$PAYMENT_METHODS_PATH$ERROR_PATH",
    type = TrackType.EVENT,
    data = PaymentMethodErrorData(
        errorType = error,
        issuer = issuer,
        cardBrand = cardBrand,
    ),
)

internal data class PaymentMethodEventData(
    @SerializedName("issuer")
    val issuer: String,
    @SerializedName("card_brand")
    val cardBrand: String,
    @SerializedName("payment_type")
    val paymentType: String?,
    @SerializedName("security_length")
    val securityLength: Int?,
) : EventData

internal data class PaymentMethodErrorData(
    @SerializedName("error_type")
    val errorType: String,
    @SerializedName("issuer")
    val issuer: String,
    @SerializedName("card_brand")
    val cardBrand: String,
) : EventData
