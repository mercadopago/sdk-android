package com.mercadopago.sdk.android.checkout.domain.repository

import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.GetCardBinParams
import com.mercadopago.sdk.android.checkout.domain.model.params.InitializeCardFormParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface CardFormRepository {
    suspend fun fetchInitialization(
        params: InitializeCardFormParams,
    ): Result<CardFormInitializationOutput, ResponseError>

    suspend fun getCardBin(
        params: GetCardBinParams,
    ): Result<CardBinData, ResponseError>
}
