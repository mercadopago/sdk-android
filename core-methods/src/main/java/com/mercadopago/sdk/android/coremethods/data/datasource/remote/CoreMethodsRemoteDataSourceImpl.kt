package com.mercadopago.sdk.android.coremethods.data.datasource.remote

import com.mercadopago.sdk.android.core.data.remote.utils.MPResponse
import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.response.CardTokenResponse
import com.mercadopago.sdk.android.coremethods.data.remote.service.CoreMethodsService

internal class CoreMethodsRemoteDataSourceImpl(
    private val service: CoreMethodsService
) : CoreMethodsRemoteDataSource {
    override suspend fun generateCardToken(cardTokenRequest: CardTokenBodyRequest): MPResponse<CardTokenResponse> {
        return service.createToken(cardTokenRequest)
    }
}
