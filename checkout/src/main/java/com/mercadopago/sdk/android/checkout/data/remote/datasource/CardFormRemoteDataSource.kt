package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface CardFormRemoteDataSource {
    suspend fun fetchInitialization(
        amount: String,
        checkoutType: String,
    ): Result<CardFormInitResponse, ResultError>

    @Suppress("LongParameterList")
    suspend fun getCardBin(
        bin: String,
        amount: String,
        processingMode: String,
        locale: String,
        allowPaymentTypes: String?,
        allowPaymentMethods: String?,
    ): Result<CardBinResponse, ResultError>

}
