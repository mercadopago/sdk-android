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
import com.mercadopago.sdk.android.checkout.domain.model.params.GetCardBinParams
import com.mercadopago.sdk.android.checkout.domain.model.params.InitializeCardFormParams
import com.mercadopago.sdk.android.checkout.domain.repository.CardFormRepository
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class CardFormRepositoryImpl(
    private val dataSource: CardFormRemoteDataSource,
) : CardFormRepository {
    override suspend fun fetchInitialization(
        params: InitializeCardFormParams,
    ): Result<CardFormInitializationOutput, ResultError> =
        withErrorHandling {
            dataSource.fetchInitialization(
                amount = params.amount,
                checkoutType = params.checkoutType,
            )
        }.map { it.toDomain() }

    override suspend fun getCardBin(
        params: GetCardBinParams,
    ): Result<CardBinData, ResultError> =
        withErrorHandling {
            dataSource.getCardBin(
                CardBinRequest(
                    bin = params.bin,
                    amount = params.amount,
                    checkoutType = params.checkoutType,
                    processingMode = params.processingMode,
                    allowCardTypes = params.filter.cardTypes.joinOrNull { it.value },
                    allowCardBrands = params.filter.cardBrands.joinOrNull { it.name },
                ),
            )
        }.map { it.toDomain() }
}
