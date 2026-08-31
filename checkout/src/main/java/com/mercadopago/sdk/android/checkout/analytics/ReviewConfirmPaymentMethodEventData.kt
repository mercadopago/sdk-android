package com.mercadopago.sdk.android.checkout.analytics

import com.google.gson.annotations.SerializedName
import com.mercadopago.sdk.android.analytics.domain.models.EventData

internal data class ReviewConfirmPaymentMethodEventData(
    @SerializedName("type")
    val type: String,
    @SerializedName("payment_method_id")
    val paymentMethodId: String,
    @SerializedName("payment_type_id")
    val paymentTypeId: String,
    @SerializedName("issuer_id")
    val issuerId: String,
    @SerializedName("card_id")
    val cardId: String,
    @SerializedName("transaction_amount")
    val transactionAmount: Double,
    @SerializedName("installments")
    val installments: Int,
) : EventData
