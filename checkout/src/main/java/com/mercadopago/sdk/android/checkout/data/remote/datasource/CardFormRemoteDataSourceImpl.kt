package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.mapper.toInternalResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.data.remote.service.CardFormService
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class CardFormRemoteDataSourceImpl(
    private val service: CardFormService,
) : CardFormRemoteDataSource {
    override suspend fun fetchInitialization(
        amount: String,
        checkoutType: String,
    ): Result<CardFormInitResponse, ResultError> =
        service.initialization(
            amount = amount,
            checkoutType = checkoutType,
        ).toInternalResponse()

    override suspend fun getCardBin(
        bin: String,
        amount: String,
        checkoutType: String,
        processingMode: String,
        allowCardTypes: String?,
        allowCardBrands: String?,
    ): Result<CardBinResponse, ResultError> =
        service.getCardBin(
            bin = bin,
            amount = amount,
            checkoutType = checkoutType,
            processingMode = processingMode,
            allowCardTypes = allowCardTypes,
            allowCardBrands = allowCardBrands,
        ).toInternalResponse()
}
