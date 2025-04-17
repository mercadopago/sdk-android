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
internal fun metricInstallmentsCallSuccess(
    paymentType: String? = null,
    merchantAccountId: String? = null,
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH/installments",
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
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH/installments_error",
    type = TrackType.EVENT,
    data = AnalyticsConstants.buildErrorData(error = error),
)

@KoverIgnore("in development")
internal data class InstallmentAnalyticsData(
    @SerializedName("is_development")
    val isDeveloping: Boolean,
    @SerializedName("payment_type")
    val paymentType: String?,
    @SerializedName("merchant_account_id")
    val merchantAccountId: String?,
) : EventData()
