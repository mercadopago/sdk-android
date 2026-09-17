package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput

private const val UNKNOWN_PAYMENT_METHOD = ""

internal fun OrderProcessOutput.toPayment(): MPPaymentData.Payment =
    MPPaymentData.Payment(
        orderId = id,
        orderStatus = status,
        paymentMethodId = paymentProcessed?.paymentMethod?.id ?: UNKNOWN_PAYMENT_METHOD,
        paymentTypeId = paymentProcessed?.paymentMethod?.type ?: UNKNOWN_PAYMENT_METHOD,
        orderStatusDetail = statusDetail,
        transactionAmount = paymentProcessed?.amount ?: totalAmount,
    )
