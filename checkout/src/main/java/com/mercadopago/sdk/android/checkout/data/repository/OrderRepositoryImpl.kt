package com.mercadopago.sdk.android.checkout.data.repository

import com.mercadopago.sdk.android.checkout.data.remote.datasource.OrderRemoteDataSource
import com.mercadopago.sdk.android.checkout.domain.extensions.withErrorHandling
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.repository.OrderRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class OrderRepositoryImpl(
    private val dataSource: OrderRemoteDataSource,
) : OrderRepository {
    override suspend fun process(
        orderId: String,
    ): Result<Unit, ResponseError> =
        withErrorHandling {
            dataSource.process(orderId = orderId)
        }

    override suspend fun transactions(
        orderId: String,
    ): Result<Unit, ResponseError> =
        withErrorHandling {
            dataSource.transactions(orderId = orderId)
        }
}
