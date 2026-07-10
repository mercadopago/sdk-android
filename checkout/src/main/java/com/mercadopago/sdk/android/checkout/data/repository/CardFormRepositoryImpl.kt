package com.mercadopago.sdk.android.checkout.data.repository

import com.mercadopago.sdk.android.checkout.data.remote.datasource.CardFormRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.mapper.toDomain
import com.mercadopago.sdk.android.checkout.data.remote.request.CardBinRequest
import com.mercadopago.sdk.android.checkout.domain.extensions.joinOrNull
import com.mercadopago.sdk.android.checkout.domain.extensions.map
import com.mercadopago.sdk.android.checkout.domain.extensions.withErrorHandling
import com.mercadopago.sdk.android.checkout.domain.mapper.toDomain
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.GetCardBinParams
import com.mercadopago.sdk.android.checkout.domain.model.params.InitializeCardFormParams
import com.mercadopago.sdk.android.checkout.domain.repository.CardFormRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class CardFormRepositoryImpl(
    private val dataSource: CardFormRemoteDataSource,
) : CardFormRepository {
    override suspend fun fetchInitialization(
        params: InitializeCardFormParams,
    ): Result<CardFormInitializationOutput, ResponseError> =
        withErrorHandling {
            dataSource.fetchInitialization(
                orderId = params.orderId,
                clientToken = params.clientToken,
                checkoutType = params.checkoutType,
            )
        }.map { it.toDomain() }

    override suspend fun getCardBin(
        params: GetCardBinParams,
    ): Result<CardBinData, ResponseError> =
        withErrorHandling {
            dataSource.getCardBin(
                CardBinRequest(
                    bin = params.bin,
                    amount = params.amount,
                    checkoutType = params.checkoutType,
                    processingMode = params.processingMode,
                    excludedPaymentTypes = params.filter.excludedPaymentTypes.joinOrNull { it.value },
                    excludedPaymentMethods = params.filter.excludedPaymentMethods.joinOrNull { it.name },
                ),
            )
        }.map { it.toDomain() }
}
