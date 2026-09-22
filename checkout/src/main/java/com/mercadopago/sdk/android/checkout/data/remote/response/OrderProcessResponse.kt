package com.mercadopago.sdk.android.checkout.data.remote.response

import com.google.gson.annotations.SerializedName

internal data class OrderProcessResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("status_detail")
    val statusDetail: String?,
    @SerializedName("total_amount")
    val totalAmount: String?,
    @SerializedName("retry_possible")
    val retryPossible: Boolean?,
    @SerializedName("payment_processed")
    val paymentProcessed: OrderPaymentProcessedResponse?,
)

internal data class OrderPaymentProcessedResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("status_detail")
    val statusDetail: String?,
    @SerializedName("amount")
    val amount: String?,
    @SerializedName("payment_method")
    val paymentMethod: OrderPaymentProcessedMethodResponse?,
)

internal data class OrderPaymentProcessedMethodResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("installments")
    val installments: Int?,
    @SerializedName("barcode_content")
    val barcodeContent: String?,
    @SerializedName("ticket_url")
    val ticketUrl: String?,
    @SerializedName("redirect_url")
    val redirectUrl: String?,
)
