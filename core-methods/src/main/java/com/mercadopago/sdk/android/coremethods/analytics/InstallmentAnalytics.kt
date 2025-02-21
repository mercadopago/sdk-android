package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType

internal fun provideMetricInstallmentFetch(
    paymentType: String? = null,
    merchantAccountId: String? = null,
    error: String? = null
) = Metric(
    path = "/sdk-native/core-methods/installment-list_call",
    type = TrackType.EVENT,
    data = InstallmentAnalyticsData(
        isDeveloping = true,
        paymentType = paymentType,
        merchantAccountId = merchantAccountId,
        error = error
    )
)

internal data class InstallmentAnalyticsData(
    @SerializedName("is_development")
    val isDeveloping: Boolean,
    @SerializedName("payment_type")
    val paymentType: String?,
    @SerializedName("merchant_account_id")
    val merchantAccountId: String?,
    @SerializedName("error")
    val error: String?,
) : EventData
