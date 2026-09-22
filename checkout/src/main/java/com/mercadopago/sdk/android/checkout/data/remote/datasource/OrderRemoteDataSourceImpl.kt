package com.mercadopago.sdk.android.checkout.data.remote.datasource

import android.content.Context
import com.mercadopago.sdk.android.checkout.data.remote.mapper.toInternalResponse
import com.mercadopago.sdk.android.checkout.data.remote.request.toIntegrationData
import com.mercadopago.sdk.android.checkout.data.remote.request.toOrderProcessRequest
import com.mercadopago.sdk.android.checkout.data.remote.response.OrderProcessResponse
import com.mercadopago.sdk.android.checkout.data.remote.service.OrderService
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.domain.model.params.toPaymentMethodType
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

private const val BEARER_PREFIX = "Bearer "

internal class OrderRemoteDataSourceImpl(
    private val service: OrderService,
    private val context: Context,
) : OrderRemoteDataSource {
    override suspend fun process(
        params: ProcessOrderParams,
    ): Result<OrderProcessResponse, ResponseError> =
        service.process(
            orderId = params.orderId,
            clientToken = "$BEARER_PREFIX${params.clientToken}",
            body = params.toPaymentMethodType().toOrderProcessRequest(context.toIntegrationData()),
        ).toInternalResponse()
}
