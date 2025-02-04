package com.mercadopago.sdk.android.coremethods.data.repository

import com.mercadopago.sdk.android.coremethods.data.datasource.remote.CoreMethodsRemoteDataSource
import com.mercadopago.sdk.android.coremethods.data.remote.mappers.toCardTokenRequest
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.CardTokenFields
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class CoreMethodsRepositoryImpl(
    private val dataSource: CoreMethodsRemoteDataSource
) : CoreMethodsRepository {

    override suspend fun generateCardToken(cardTokenFields: CardTokenFields): Result<CardToken, ResultError> {
        return dataSource.generateCardToken(cardTokenFields.toCardTokenRequest())
    }
}
