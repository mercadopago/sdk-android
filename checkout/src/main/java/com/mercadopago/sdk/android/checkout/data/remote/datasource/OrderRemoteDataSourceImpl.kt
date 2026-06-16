package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.mapper.toInternalResponse
import com.mercadopago.sdk.android.checkout.data.remote.request.OrderProcessRequest
import com.mercadopago.sdk.android.checkout.data.remote.response.OrderProcessResponse
import com.mercadopago.sdk.android.checkout.data.remote.service.OrderService
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class OrderRemoteDataSourceImpl(
    private val service: OrderService,
) : OrderRemoteDataSource {
    override suspend fun process(
        params: ProcessOrderParams,
    ): Result<OrderProcessResponse, ResponseError> =
        service.process(
            orderId = params.orderId,
            clientToken = params.clientToken,
            body = OrderProcessRequest(
                amount = params.amount,
                paymentMethodId = params.paymentMethodId,
                paymentMethodType = params.paymentMethodType,
                token = params.token,
                installments = params.installments,
            ),
        ).toInternalResponse()
}
