package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.core.utils.KoverIgnore

@KoverIgnore("in development")
internal fun metricInstallmentsCallSuccess(
    paymentType: String? = null,
    merchantAccountId: String? = null,
) = Metric(
    path = "/sdk-native/core-methods/installments",
    type = TrackType.EVENT,
    data = InstallmentAnalyticsData(
        isDeveloping = true,
        paymentType = paymentType,
        merchantAccountId = merchantAccountId,
    ),
)

@KoverIgnore("in development")
internal fun metricInstallmentsCallError(
    error: String
) = Metric(
    path = "/sdk-native/core-methods/installments_error",
    type = TrackType.EVENT,
    data = MetricErrorData(error = error),
)

@KoverIgnore("in development")
internal data class InstallmentAnalyticsData(
    @SerializedName("is_development")
    val isDeveloping: Boolean,
    @SerializedName("payment_type")
    val paymentType: String?,
    @SerializedName("merchant_account_id")
    val merchantAccountId: String?,
) : EventData
