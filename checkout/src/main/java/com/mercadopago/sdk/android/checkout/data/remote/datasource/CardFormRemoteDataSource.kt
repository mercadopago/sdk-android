package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.request.CardBinRequest
import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface CardFormRemoteDataSource {
    suspend fun fetchInitialization(
        amount: String,
        checkoutType: String,
    ): Result<CardFormInitResponse, ResultError>

    suspend fun getCardBin(
        request: CardBinRequest,
    ): Result<CardBinResponse, ResultError>
}
