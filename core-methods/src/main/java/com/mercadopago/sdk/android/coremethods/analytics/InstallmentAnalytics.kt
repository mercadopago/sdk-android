package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import org.koin.core.logger.MESSAGE

internal fun provideMetricInstallmentFetch(paymentType: String, merchantAccountId: String) =
    Metric(
        type = TrackType.EVENT,
        path = "/bricks-sdk-native/installments",
        data = InstallmentAnalyticsData(
            isDeveloping = true,
            paymentType = paymentType,
            merchantAccountId = merchantAccountId
        )
    )

internal fun provideMetricInstallmentFetchError(code: String, message: String) =
    Metric(
        type = TrackType.EVENT,
        path = "/bricks-sdk-native/installments/error",
        data = AnalyticsData(
            code = code,
            message = message
        )
    )

internal data class InstallmentAnalyticsData(
    @SerializedName("is_development")
    val isDeveloping: Boolean,
    @SerializedName("payment_type")
    val paymentType: String,
    @SerializedName("merchant_account_id")
    val merchantAccountId: String,
) : EventData

internal data class AnalyticsData(
    val code: String,
    val message: String
): EventData
