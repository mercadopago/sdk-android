package com.mercadopago.sdk.android.checkout.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.constants.MetricErrorData
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.CHECKOUT_INSTALLMENTS_PATH
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val SELECTED_PATH = "/selected"
private const val SUBMIT_PATH = "/submit"
private const val USER_CANCELED_PATH = "/user_canceled_error"

@KoverIgnore("in development")
internal fun metricInstallmentsSelected(
    installments: Int,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_INSTALLMENTS_PATH$SELECTED_PATH",
    type = TrackType.EVENT,
    data = InstallmentsSelectedEventData(installments = installments),
)

@KoverIgnore("in development")
internal fun metricInstallmentsSubmit(
    installments: Int,
    installmentAmount: Double,
    totalAmount: Double,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_INSTALLMENTS_PATH$SUBMIT_PATH",
    type = TrackType.EVENT,
    data = InstallmentsSubmitEventData(
        installments = installments,
        installmentAmount = installmentAmount,
        totalAmount = totalAmount,
    ),
)

@KoverIgnore("in development")
internal fun metricInstallmentsUserCanceledError(
    errorType: String,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_INSTALLMENTS_PATH$USER_CANCELED_PATH",
    type = TrackType.EVENT,
    data = MetricErrorData(errorType = errorType),
)

@KoverIgnore("in development")
internal data class InstallmentsSelectedEventData(
    @SerializedName("installments")
    val installments: Int,
) : EventData

@KoverIgnore("in development")
internal data class InstallmentsSubmitEventData(
    @SerializedName("installments")
    val installments: Int,
    @SerializedName("installment_amount")
    val installmentAmount: Double,
    @SerializedName("total_amount")
    val totalAmount: Double,
) : EventData

internal enum class InstallmentsCancelReason(val analyticsValue: String) {
    BackPressed("back_pressed"),
    UserDismissed("user_dismissed"),
}
