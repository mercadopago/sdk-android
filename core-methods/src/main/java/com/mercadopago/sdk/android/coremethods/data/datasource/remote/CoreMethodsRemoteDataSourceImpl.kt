package com.mercadopago.sdk.android.coremethods.data.datasource.remote

import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.service.CoreMethodsService
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class CoreMethodsRemoteDataSourceImpl(
    private val service: CoreMethodsService
) : CoreMethodsRemoteDataSource {

    override suspend fun generateCardToken(
        cardTokenRequest: CardTokenBodyRequest
    ): Result<CardToken, ResultError> {
        val result = service.createToken(cardTokenRequest)
        return when (result.isSuccessful) {
            true -> {
                Result.Success(CardToken(result.body()?.id ?: ""))
            }

            false -> {
                val errorBody = result.errorBody()?.string()
                val gson = GsonBuilder().create()

                Result.Error(
                    error = errorBody?.let {
                        gson.fromJson(it, ResultError::class.java)
                    } ?: ResultError(
                        code = "UNKNOWN_ERROR",
                        message = "Ocorreu um erro desconhecido"
                    )
                )
            }
        }
    }
}
