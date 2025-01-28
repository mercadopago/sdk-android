package com.mercadopago.sdk.android.coremethods.data.repository

import com.mercadopago.sdk.android.coremethods.data.remote.mappers.toCardTokenRequest
import com.mercadopago.sdk.android.coremethods.data.remote.mappers.toResultError
import com.mercadopago.sdk.android.coremethods.data.remote.service.CoreMethodsService
import com.mercadopago.sdk.android.coremethods.data.remote.utils.Response
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.CardTokenFields
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

internal class CoreMethodsRepositoryImpl(
    private val service: CoreMethodsService
) : CoreMethodsRepository {

    override fun generateCardToken(cardTokenFields: CardTokenFields): Flow<Result<CardToken, ResultError>> {
        return flow {
            val response = service.createToken(cardTokenFields.toCardTokenRequest())
            when (response) {
                is Response.Success -> {
                    emit(Result.Success(CardToken(response.response.id.orEmpty())))
                }

                is Response.Error -> {
                    emit(Result.Error(response.errorResponse.toResultError()))
                }
            }
        }.catch { e ->
            emit(Result.Failure(e))
        }
    }
}
