package com.mercadopago.sdk.android.coremethods.analytics

import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.core.utils.KoverIgnore

@KoverIgnore("in development")
internal fun metricGenerateCardTokenCallSuccess() = Metric(
    path = "/sdk-native/core-methods/generate-card-token_call",
    type = TrackType.EVENT,
)

@KoverIgnore("in development")
internal fun metricGenerateCardTokenCallError(
    code: String,
    message: String,
) = Metric(
    path = "/sdk-native/core-methods/generate-card-token_error",
    type = TrackType.EVENT,
    data = MetricErrorData(code = code, message = message),
)
