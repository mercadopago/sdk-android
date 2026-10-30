package com.mercadopago.sdk.android.checkout.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.CHECKOUT_INSTALLMENTS_PATH
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val INITIALIZE_PATH = "/initialize"

@KoverIgnore("in development")
internal fun metricInstallmentsInitialize(
    data: InstallmentsInitializeEventData,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_INSTALLMENTS_PATH$INITIALIZE_PATH",
    type = TrackType.VIEW,
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
    val transactionAmount: Double,
    @SerializedName("order_id")
    val orderId: String,
) : EventData

internal fun SelectionDisplayType.toAnalyticsString(): String =
    when (this) {
        SelectionDisplayType.RadioButton -> "radio_button"
        SelectionDisplayType.Chevron -> "chevron"
    }
