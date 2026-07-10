package com.mercadopago.sdk.android.checkout.domain.repository

import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface OrderRepository {
    suspend fun process(
        params: ProcessOrderParams,
    ): Result<OrderProcessOutput, ResponseError>
}
