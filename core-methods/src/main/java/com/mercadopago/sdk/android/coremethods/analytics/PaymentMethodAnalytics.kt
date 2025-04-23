package com.mercadopago.sdk.android.coremethods.analytics

import com.mercadopago.sdk.android.analytics.domain.constants.AnalyticsConstants.ERROR_PATH
import com.mercadopago.sdk.android.analytics.domain.constants.MetricErrorData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.coremethods.analytics.CoreMethodsAnalyticsConstants.CORE_METHODS_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val PAYMENT_METHOD_PATH = "payment-method"

@KoverIgnore("in development")
internal fun metricPaymentMethodCallSuccess() = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH$PAYMENT_METHOD_PATH",
    type = TrackType.EVENT,
)

@KoverIgnore("in development")
internal fun metricPaymentMethodCallError(
    error: String
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH$PAYMENT_METHOD_PATH$ERROR_PATH",
    type = TrackType.EVENT,
    data = MetricErrorData(errorType = error),
)
