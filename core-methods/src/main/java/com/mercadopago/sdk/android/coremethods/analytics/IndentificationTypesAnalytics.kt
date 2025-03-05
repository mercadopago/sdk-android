package com.mercadopago.sdk.android.coremethods.analytics

import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType

internal fun metricIdentificationCallSuccess() =
    Metric(
        path = "/sdk-native/core-methods/identification-types_call",
        type = TrackType.EVENT,
    )

internal fun metricIdentificationCallError(
    code: String,
    message: String,
) = Metric(
    path = "/sdk-native/core-methods/identification-types_call/error",
    type = TrackType.EVENT,
    data = MetricErrorData(code = code, message = message),
)
