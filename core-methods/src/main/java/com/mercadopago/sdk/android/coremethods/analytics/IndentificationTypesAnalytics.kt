package com.mercadopago.sdk.android.coremethods.analytics

import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.core.utils.KoverIgnore

@KoverIgnore("in development")
internal fun metricIdentificationCallSuccess() = Metric(
    path = "/sdk-native/core-methods/identification-types",
    type = TrackType.EVENT,
)

@KoverIgnore("in development")
internal fun metricIdentificationCallError(
    error: String
) = Metric(
    path = "/sdk-native/core-methods/identification-types_error",
    type = TrackType.EVENT,
    data = MetricErrorData(error = error),
)
