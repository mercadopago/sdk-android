package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.constants.AnalyticsConstants
import com.mercadopago.sdk.android.analytics.domain.constants.AnalyticsConstants.ERROR_PATH
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.coremethods.analytics.CoreMethodsAnalyticsConstants.CORE_METHODS_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val ISSUERS_PATH = "/issuers"

@KoverIgnore("in development")
internal fun metricCardIssuersCallSuccess(
    issuers: List<String>,
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH$ISSUERS_PATH",
    type = TrackType.EVENT,
    data = CardIssuersAnalyticsEventData(
        issuers = issuers,
    ),
)

@KoverIgnore("in development")
internal fun metricCardIssuersCallError(
    error: String
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH$ISSUERS_PATH$ERROR_PATH",
    type = TrackType.EVENT,
    data = AnalyticsConstants.buildErrorData(error),
)

internal data class CardIssuersAnalyticsEventData(
    @SerializedName("issuers")
    val issuers: List<String>,
) : EventData
