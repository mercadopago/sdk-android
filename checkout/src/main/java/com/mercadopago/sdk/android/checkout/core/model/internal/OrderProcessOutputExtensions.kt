package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams

internal fun OrderProcessOutput.toPaymentData(
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
        MPPaymentData.Payment(
            orderId = id,
            orderStatus = status,
            paymentMethodId = params.paymentMethodId,
            paymentTypeId = params.paymentMethodType,
        )
    }
