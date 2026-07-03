package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.request.CardBinRequest
import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface CardFormRemoteDataSource {
    suspend fun fetchInitialization(
        checkoutType: String,
    ): Result<CardFormInitResponse, ResponseError>

    suspend fun getCardBin(
        request: CardBinRequest,
    ): Result<CardBinResponse, ResponseError>
}
