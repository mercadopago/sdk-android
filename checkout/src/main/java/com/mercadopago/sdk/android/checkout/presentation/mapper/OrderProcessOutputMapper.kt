package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.isCardTransaction
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams

private const val UNKNOWN_PAYMENT_METHOD = ""

internal fun OrderProcessOutput.resolvePaymentData(
    checkoutConfiguration: CheckoutConfiguration?,
    params: ProcessOrderParams,
): MPPaymentData =
    if (checkoutConfiguration.isCardTransaction()) {
        MPPaymentData.CardTransaction(
            orderId = id,
            orderStatus = status,
            paymentMethodId = params.paymentMethodId,
            paymentTypeId = params.paymentMethodType,
        )
    } else {
        toPayment()
    }

internal fun OrderProcessOutput.toPayment(): MPPaymentData.Payment =
    MPPaymentData.Payment(
        orderId = id,
        orderStatus = status,
        paymentMethodId = paymentProcessed?.paymentMethod?.id ?: UNKNOWN_PAYMENT_METHOD,
        paymentTypeId = paymentProcessed?.paymentMethod?.type ?: UNKNOWN_PAYMENT_METHOD,
        orderStatusDetail = statusDetail,
    )
