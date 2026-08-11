package com.mercadopago.sdk.android.checkout.domain.model

import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams

internal fun PaymentMethodOutput.toProcessOrderParams(
    order: MPOrder,
    token: String = "",
    installments: Int = 1,
    amount: String?,
): ProcessOrderParams =
    ProcessOrderParams(
        orderId = order.orderId,
        clientToken = order.clientToken,
        amount = amount.orEmpty(),
        paymentMethodId = cardData?.paymentMethodId.orEmpty(),
        paymentMethodType = cardData?.paymentTypeId.orEmpty(),
        token = token,
        installments = installments,
        bin = cardData?.bin,
    )
