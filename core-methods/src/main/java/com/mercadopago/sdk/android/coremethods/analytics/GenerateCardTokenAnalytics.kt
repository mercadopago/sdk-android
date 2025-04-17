package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.constants.AnalyticsConstants
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.coremethods.analytics.CoreMethodsAnalyticsConstants.CORE_METHODS_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

@KoverIgnore("in development")
internal fun metricGenerateCardTokenCallSuccess(
    cardFlag: String? = null,
    paymentType: CardTokenPaymentTypeMetric = CardTokenPaymentTypeMetric.CREDIT,
    cardType: CardTokenCardTypeMetric = CardTokenCardTypeMetric.SAVED_CARD,
    issuer: String? = null
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH/generate-card-token",
    type = TrackType.EVENT,
    data = GenerateCardAnalyticsData(
        cardFlag,
        paymentType.type,
        cardType.type,
        issuer
    )
)

@KoverIgnore("in development")
internal fun metricGenerateCardTokenCallError(
    error: String,
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH/generate-card-token_error",
    type = TrackType.EVENT,
    data = AnalyticsConstants.buildErrorData(error = error),
)

@KoverIgnore("in development")
internal enum class CardTokenPaymentTypeMetric(val type: String) {
    CREDIT(type = "credit"),
    DEBIT(type = "debit"),
    PREPAID(type = "prepaid"),
}

@KoverIgnore("in development")
internal enum class CardTokenCardTypeMetric(val type: String) {
    SAVED_CARD(type = "credit"),
    NOT_SAVED_CARD(type = "debit"),
}

@KoverIgnore("in development")
internal data class GenerateCardAnalyticsData(
    @SerializedName("card_flag")
    val cardFlag: String?,
    @SerializedName("payment_type")
    val paymentType: String,
    @SerializedName("card_type")
    val cardType: String,
    @SerializedName("issuer")
    val issuer: String?,
) : EventData()
