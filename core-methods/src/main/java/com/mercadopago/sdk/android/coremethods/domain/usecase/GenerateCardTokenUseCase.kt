package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.CardTokenFields
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_TWO

internal const val EXPIRATION_YEAR_START = "20"

internal class GenerateCardTokenUseCase(
    private val repository: CoreMethodsRepository
) {
    suspend operator fun invoke(
        cardNumber: String,
        expirationDate: String,
        securityCode: String
    ): Result<CardToken, ResultError> {
        val expirationMonth = expirationDate.ifEmpty { "0" }.take(INT_TWO).toInt()
        val expirationYear =
            (EXPIRATION_YEAR_START + expirationDate.ifEmpty { "0" }.takeLast(INT_TWO)).toInt()
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
