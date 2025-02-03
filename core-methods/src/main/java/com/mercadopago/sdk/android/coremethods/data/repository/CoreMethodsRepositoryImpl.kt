package com.mercadopago.sdk.android.coremethods.data.repository

import com.mercadopago.sdk.android.core.data.remote.utils.MPResponse
import com.mercadopago.sdk.android.coremethods.data.datasource.remote.CoreMethodsRemoteDataSource
import com.mercadopago.sdk.android.coremethods.data.remote.mappers.toCardTokenRequest
import com.mercadopago.sdk.android.coremethods.data.remote.mappers.toResultError
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.CardTokenFields
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class CoreMethodsRepositoryImpl(
    private val dataSource: CoreMethodsRemoteDataSource
) : CoreMethodsRepository {

    override suspend fun generateCardToken(cardTokenFields: CardTokenFields): Result<CardToken, ResultError> {
        return try {
            val response = dataSource.generateCardToken(cardTokenFields.toCardTokenRequest())
            when (response) {
                is MPResponse.Success -> {
                    Result.Success(CardToken(response.response.id.orEmpty()))
                }

                is MPResponse.Error -> {
                    Result.Error(response.errorResponse.toResultError())
                }
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}
