package com.mercadopago.sdk.android.coremethods.data.datasource.remote

import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface CoreMethodsRemoteDataSource {
    suspend fun generateCardToken(cardTokenRequest: CardTokenBodyRequest): Result<CardToken, ResultError>
}
