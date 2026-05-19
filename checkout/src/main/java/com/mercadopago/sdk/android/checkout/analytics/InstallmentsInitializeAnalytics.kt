package com.mercadopago.sdk.android.checkout.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.CHECKOUT_INSTALLMENTS_PATH
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val INITIALIZE_PATH = "/initialize"

@KoverIgnore("in development")
internal fun metricInstallmentsInitialize(
    data: InstallmentsInitializeEventData,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_INSTALLMENTS_PATH$INITIALIZE_PATH",
    type = TrackType.EVENT,
    data = data,
)

@KoverIgnore("in development")
internal data class InstallmentsInitializeEventData(
    @SerializedName("checkout_type")
    val checkoutType: String,
    @SerializedName("payment_method_id")
    val paymentMethodId: String,
    @SerializedName("payment_type")
    val paymentType: String,
    @SerializedName("selection_type")
    val selectionType: String,
    @SerializedName("quotas_count")
    val quotasCount: Int,
    @SerializedName("transaction_amount")
    val transactionAmount: Double?,
) : EventData

internal fun InstallmentsDisplayType.toAnalyticsString(): String =
    when (this) {
        InstallmentsDisplayType.RadioButton -> "radio_button"
        InstallmentsDisplayType.Chevron -> "chevron"
    }
