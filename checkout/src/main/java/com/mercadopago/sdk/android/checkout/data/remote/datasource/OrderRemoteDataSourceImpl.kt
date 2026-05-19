package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.mapper.toInternalResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.OrderProcessResponse
import com.mercadopago.sdk.android.checkout.data.remote.service.OrderService
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class OrderRemoteDataSourceImpl(
    private val service: OrderService,
) : OrderRemoteDataSource {
    override suspend fun process(
        orderId: String,
    ): Result<OrderProcessResponse, ResponseError> = service.process(orderId = orderId).toInternalResponse()
}
