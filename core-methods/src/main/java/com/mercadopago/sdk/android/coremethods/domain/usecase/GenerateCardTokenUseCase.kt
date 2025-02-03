package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.CardTokenFields
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal const val EXPIRATION_YEAR_START = "20"

internal class GenerateCardTokenUseCase(
    private val repository: CoreMethodsRepository
) {
    suspend operator fun invoke(
        cardNumber: String,
        expirationDate: String,
        securityCode: String
    ): Result<CardToken, ResultError> {
        val expirationMonth = expirationDate.ifEmpty { "0" }.take(2).toInt()
        val expirationYear = (EXPIRATION_YEAR_START + expirationDate.ifEmpty { "0" }.takeLast(2)).toInt()
        return repository.generateCardToken(
            CardTokenFields(
                cardNumber = cardNumber,
                expirationMonth = expirationMonth,
                expirationYear = expirationYear,
                securityCode = securityCode
            )
        )
    }
}
