package com.mercadopago.sdk.android.checkout.data.repository

import com.mercadopago.sdk.android.checkout.data.remote.datasource.PaymentBrickInitializationRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.mapper.toDomain
import com.mercadopago.sdk.android.checkout.domain.extensions.map
import com.mercadopago.sdk.android.checkout.domain.extensions.withErrorHandling
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickInitializationParams
import com.mercadopago.sdk.android.checkout.domain.repository.PaymentBrickInitializationRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class PaymentBrickInitializationRepositoryImpl(
    private val dataSource: PaymentBrickInitializationRemoteDataSource,
) : PaymentBrickInitializationRepository {
    override suspend fun fetch(
        params: FetchPaymentBrickInitializationParams,
    ): Result<PaymentBrickInitializationOutput, ResponseError> =
        withErrorHandling {
            dataSource.fetch(params = params)
        }.map { it.toDomain() }
}
