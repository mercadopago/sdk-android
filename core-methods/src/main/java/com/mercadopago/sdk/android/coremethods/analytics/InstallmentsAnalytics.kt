package com.mercadopago.sdk.android.coremethods.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.constants.AnalyticsConstants.ERROR_PATH
import com.mercadopago.sdk.android.analytics.domain.models.EventData
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
    transactionAmount: BigDecimal,
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH$INSTALLMENTS_PATH",
    type = TrackType.EVENT,
    data = InstallmentAnalyticsData(
        paymentType = paymentType,
        transactionAmount = transactionAmount,
    ),
)

@KoverIgnore("in development")
internal fun metricInstallmentsCallError(
    error: String,
    transactionAmount: BigDecimal,
) = Metric(
    path = "$SDK_NATIVE_PATH$CORE_METHODS_PATH$INSTALLMENTS_PATH$ERROR_PATH",
    type = TrackType.EVENT,
    data = InstallmentsErrorData(
        errorType = error,
        transactionAmount = transactionAmount,
    ),
)

@KoverIgnore("in development")
internal data class InstallmentAnalyticsData(
    @SerializedName("payment_type")
    val paymentType: String?,
    @SerializedName("transaction_amount")
    val transactionAmount: BigDecimal,
) : EventData

@KoverIgnore("in development")
internal data class InstallmentsErrorData(
    @SerializedName("error_type")
    val errorType: String,
    @SerializedName("transaction_amount")
    val transactionAmount: BigDecimal,
) : EventData
