package com.mercadopago.sdk.android.coremethods.domain.repository

import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.CardTokenFields
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import kotlinx.coroutines.flow.Flow

internal interface CoreMethodsRepository {

    fun generateCardToken(
        cardTokenFields: CardTokenFields
    ): Flow<Result<CardToken, ResultError>>
}
