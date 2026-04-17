package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.mapper.toInternalResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.data.remote.service.CardFormService
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class CardFormRemoteDataSourceImpl(
    private val service: CardFormService,
) : CardFormRemoteDataSource {
    override suspend fun fetchInitialization(
        locale: String,
        amount: String,
        checkoutType: String,
    ): Result<CardFormInitResponse, ResultError> =
        service.initialization(
            locale = locale,
            amount = amount,
            checkoutType = checkoutType,
        ).toInternalResponse()
}
