package com.mercadopago.sdk.android.coremethods.data.datasource.remote

import com.mercadopago.sdk.android.core.data.remote.utils.MPResponse
import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.response.CardTokenResponse

internal interface CoreMethodsRemoteDataSource {
    suspend fun generateCardToken(cardTokenRequest: CardTokenBodyRequest): MPResponse<CardTokenResponse>
}
