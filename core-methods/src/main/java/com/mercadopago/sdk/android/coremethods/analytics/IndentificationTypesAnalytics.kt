package com.mercadopago.sdk.android.coremethods.analytics

import com.mercadopago.sdk.android.analytics.domain.constants.AnalyticsConstants
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.coremethods.analytics.CoreMethodsAnalyticsConstants.CORE_METHODS_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

@KoverIgnore("in development")
internal fun metricIdentificationCallSuccess() = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH/identification-types",
    type = TrackType.EVENT,
)

@KoverIgnore("in development")
internal fun metricIdentificationCallError(
    error: String
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH/identification-types_error",
    type = TrackType.EVENT,
    data = AnalyticsConstants.buildErrorData(error = error),
)
