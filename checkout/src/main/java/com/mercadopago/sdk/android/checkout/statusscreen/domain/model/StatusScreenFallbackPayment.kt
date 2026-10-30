package com.mercadopago.sdk.android.checkout.statusscreen.domain.model

import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput

internal fun rejectedFallbackPayment(
    statusDetail: String,
): OrderProcessOutput.PaymentProcessed =
    OrderProcessOutput.PaymentProcessed(
        id = null,
        status = PAYMENT_STATUS_REJECTED,
        statusDetail = statusDetail,
        amount = null,
        paymentMethod = null,
    )

internal fun errorFallbackPayment(): OrderProcessOutput.PaymentProcessed =
    OrderProcessOutput.PaymentProcessed(
        id = null,
        status = PAYMENT_STATUS_ERROR,
        statusDetail = null,
        amount = null,
        paymentMethod = null,
    )
