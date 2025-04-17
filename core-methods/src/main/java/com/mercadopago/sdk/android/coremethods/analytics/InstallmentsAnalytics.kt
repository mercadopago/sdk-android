package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.constants.AnalyticsConstants
import com.mercadopago.sdk.android.analytics.domain.constants.AnalyticsConstants.ERROR_PATH
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.coremethods.analytics.CoreMethodsAnalyticsConstants.CORE_METHODS_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH
import java.math.BigDecimal

private const val INSTALLMENTS_PATH = "/installments"

@KoverIgnore("in development")
internal fun metricInstallmentsCallSuccess(
    paymentType: String? = null,
    merchantAccountId: String? = null,
    transactionAmount: BigDecimal,
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH$INSTALLMENTS_PATH",
    type = TrackType.EVENT,
    data = InstallmentAnalyticsData(
        isDeveloping = true,
        paymentType = paymentType,
        merchantAccountId = merchantAccountId,
        transactionAmount = transactionAmount,
    ),
)

@KoverIgnore("in development")
internal fun metricInstallmentsCallError(
    error: String
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH$INSTALLMENTS_PATH$ERROR_PATH",
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
    @SerializedName("transaction_amount")
    val transactionAmount: BigDecimal?,
) : CoreMethodsEventData()
