package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.OrderProcessResponse
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput

internal fun OrderProcessResponse.toDomain(): OrderProcessOutput =
    OrderProcessOutput(
        id = id.orEmpty(),
        status = status.orEmpty(),
    )
