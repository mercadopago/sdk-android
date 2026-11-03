package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.OrderPaymentProcessedMethodResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.OrderPaymentProcessedResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.OrderProcessResponse
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import java.math.BigDecimal

internal fun OrderProcessResponse.toDomain(): OrderProcessOutput =
    OrderProcessOutput(
        id = id.orEmpty(),
        status = status.orEmpty(),
        statusDetail = statusDetail,
        totalAmount = totalAmount.toBigDecimalOrNull(),
        paymentProcessed = paymentProcessed?.toDomain(
            totalPaidAmount = totalPaidAmount.toBigDecimalOrNull(),
            orderTotalAmount = totalAmount.toBigDecimalOrNull(),
        ),
    )

private fun OrderPaymentProcessedResponse.toDomain(
    totalPaidAmount: BigDecimal?,
    orderTotalAmount: BigDecimal?,
): OrderProcessOutput.PaymentProcessed =
    OrderProcessOutput.PaymentProcessed(
        id = id,
        status = status,
        statusDetail = statusDetail,
        amount = amount.toBigDecimalOrNull(),
        paymentMethod = paymentMethod?.toDomain(),
        totalPaidAmount = totalPaidAmount,
        orderTotalAmount = orderTotalAmount,
    )

private fun String?.toBigDecimalOrNull(): BigDecimal? = this?.let { runCatching { it.toBigDecimal() }.getOrNull() }

private fun OrderPaymentProcessedMethodResponse.toDomain(): OrderProcessOutput.PaymentProcessedMethod =
    OrderProcessOutput.PaymentProcessedMethod(
        id = id,
        type = type,
        installments = installments,
        barcodeContent = barcodeContent,
        ticketUrl = ticketUrl,
        redirectUrl = redirectUrl,
    )
