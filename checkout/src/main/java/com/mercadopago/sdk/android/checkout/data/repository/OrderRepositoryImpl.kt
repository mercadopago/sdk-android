package com.mercadopago.sdk.android.checkout.data.repository

import com.mercadopago.sdk.android.checkout.data.remote.datasource.OrderRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.mapper.toDomain
import com.mercadopago.sdk.android.checkout.domain.extensions.map
import com.mercadopago.sdk.android.checkout.domain.extensions.withErrorHandling
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.domain.repository.OrderRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class OrderRepositoryImpl(
    private val dataSource: OrderRemoteDataSource,
) : OrderRepository {
    override suspend fun process(
        params: ProcessOrderParams,
    ): Result<OrderProcessOutput, ResponseError> =
        withErrorHandling {
            dataSource.process(params = params)
        }.map { it.toDomain() }
}
