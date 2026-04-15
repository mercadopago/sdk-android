package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface CardFormRemoteDataSource {
    suspend fun getCardBin(
        bin: String,
        amount: String,
        processingMode: String,
        locale: String,
        allowCardTypes: String?,
        allowCardBrands: String?,
    ): Result<CardBinResponse, ResultError>
}
