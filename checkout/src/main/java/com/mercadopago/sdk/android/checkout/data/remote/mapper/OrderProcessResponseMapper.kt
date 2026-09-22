package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.OrderPaymentProcessedMethodResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.OrderPaymentProcessedResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.OrderProcessResponse
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput

internal fun OrderProcessResponse.toDomain(): OrderProcessOutput =
    OrderProcessOutput(
        id = id.orEmpty(),
        status = status.orEmpty(),
        statusDetail = statusDetail,
        totalAmount = totalAmount?.let { runCatching { it.toBigDecimal() }.getOrNull() },
        retryPossible = retryPossible,
        paymentProcessed = paymentProcessed?.toDomain(),
    )

private fun OrderPaymentProcessedResponse.toDomain(): OrderProcessOutput.PaymentProcessed =
    OrderProcessOutput.PaymentProcessed(
        id = id,
        status = status,
        statusDetail = statusDetail,
        amount = amount?.let { runCatching { it.toBigDecimal() }.getOrNull() },
        paymentMethod = paymentMethod?.toDomain(),
    )

private fun OrderPaymentProcessedMethodResponse.toDomain(): OrderProcessOutput.PaymentProcessedMethod =
    OrderProcessOutput.PaymentProcessedMethod(
        id = id,
        type = type,
        installments = installments,
        barcodeContent = barcodeContent,
        ticketUrl = ticketUrl,
        redirectUrl = redirectUrl,
    )
