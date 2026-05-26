package com.mercadopago.sdk.android.checkout.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.constants.MetricErrorData
import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.CHECKOUT_CARD_FORM_PATH
import com.mercadopago.sdk.android.core.utils.KoverIgnore
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH

private const val INITIALIZE_PATH = "/initialize"
private const val INITIALIZE_ERROR_PATH = "/initialize_error"

@KoverIgnore("in development")
internal fun metricCardFormInitialize(
    checkoutType: String,
    appearance: String,
    sellerCustomization: List<String>,
    excludedPaymentTypes: List<String>,
    excludedPaymentMethods: List<String>,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_CARD_FORM_PATH$INITIALIZE_PATH",
    type = TrackType.EVENT,
    data = CardFormInitEventData(
        checkoutType = checkoutType,
        appearance = appearance,
        sellerCustomization = sellerCustomization,
        excludedPaymentTypes = excludedPaymentTypes,
        excludedPaymentMethods = excludedPaymentMethods,
    ),
)

@KoverIgnore("in development")
internal fun metricCardFormInitializeError(
    errorType: String,
) = Metric(
    path = "$SDK_NATIVE_PATH$CHECKOUT_CARD_FORM_PATH$INITIALIZE_ERROR_PATH",
    type = TrackType.EVENT,
    data = MetricErrorData(errorType = errorType),
)

@KoverIgnore("in development")
internal data class CardFormInitEventData(
    @SerializedName("checkout_type")
    val checkoutType: String,
    @SerializedName("appearance")
    val appearance: String,
    @SerializedName("seller_customization")
    val sellerCustomization: List<String>,
    @SerializedName("excluded_payment_types")
    val excludedPaymentTypes: List<String>,
    @SerializedName("excluded_payment_methods")
    val excludedPaymentMethods: List<String>,
) : EventData
