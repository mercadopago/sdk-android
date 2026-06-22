package com.mercadopago.sdk.android.checkout.data.repository

import com.mercadopago.sdk.android.checkout.data.remote.datasource.PaymentBrickCardRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.mapper.toDomain
import com.mercadopago.sdk.android.checkout.domain.extensions.map
import com.mercadopago.sdk.android.checkout.domain.extensions.withErrorHandling
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickCardOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickCardParams
import com.mercadopago.sdk.android.checkout.domain.repository.PaymentBrickCardRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class PaymentBrickCardRepositoryImpl(
    private val dataSource: PaymentBrickCardRemoteDataSource,
) : PaymentBrickCardRepository {
    override suspend fun fetch(
        params: FetchPaymentBrickCardParams,
    ): Result<PaymentBrickCardOutput, ResponseError> =
        withErrorHandling {
            dataSource.fetch(params = params)
        }.map { it.toDomain() }
}
