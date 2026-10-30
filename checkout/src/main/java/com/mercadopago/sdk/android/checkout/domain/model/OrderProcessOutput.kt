package com.mercadopago.sdk.android.checkout.domain.model

import java.math.BigDecimal

internal data class OrderProcessOutput(
    val id: String,
    val status: String,
    val statusDetail: String? = null,
    val totalAmount: BigDecimal? = null,
    val paymentProcessed: PaymentProcessed? = null,
) {
    internal data class PaymentProcessed(
        val id: String?,
        val status: String?,
        val statusDetail: String?,
        val amount: BigDecimal?,
        val paymentMethod: PaymentProcessedMethod?,
        val totalPaidAmount: BigDecimal? = null,
        val orderTotalAmount: BigDecimal? = null,
    )

    internal data class PaymentProcessedMethod(
        val id: String?,
        val type: String?,
        val installments: Int?,
        val barcodeContent: String?,
        val ticketUrl: String?,
        val redirectUrl: String?,
    )
}
