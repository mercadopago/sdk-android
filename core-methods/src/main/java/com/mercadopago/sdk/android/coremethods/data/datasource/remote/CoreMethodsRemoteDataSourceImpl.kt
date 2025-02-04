package com.mercadopago.sdk.android.coremethods.data.datasource.remote

import com.mercadopago.sdk.android.core.data.remote.response.MPErrorResponse
import com.mercadopago.sdk.android.core.data.remote.utils.MPResponse
import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.service.CoreMethodsService
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken

internal class CoreMethodsRemoteDataSourceImpl(
    private val service: CoreMethodsService
) : CoreMethodsRemoteDataSource {
    override suspend fun generateCardToken(
        cardTokenRequest: CardTokenBodyRequest
    ): MPResponse<CardToken, MPErrorResponse> {
        return when (val result = service.createToken(cardTokenRequest)) {
            is MPResponse.Success -> {
                MPResponse.Success(CardToken(result.response.id ?: ""))
            }

            is MPResponse.Error -> {
                result
            }
        }
    }
}
