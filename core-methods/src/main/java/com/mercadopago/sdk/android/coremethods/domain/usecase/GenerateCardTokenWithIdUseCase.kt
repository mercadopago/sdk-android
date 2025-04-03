package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.params.GenerateCardTokenParams
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_TWO

internal class GenerateCardTokenWithIdUseCase(
    private val repository: CoreMethodsRepository,
) {
    suspend operator fun invoke(
        cardId: String,
        securityCode: String,
        expirationDate: String?,
    ): Result<CardToken, ResultError> {
        var expirationMonth: Int? = null
        var expirationYear: Int? = null
        if(cardId.isEmpty()) {
            return Result.Error(ResultError.Validation("cardId cannot be empty"))
        }
        if (!expirationDate.isNullOrEmpty()) {
            if (expirationDate.length < EXPIRATION_YEAR_MIN_LENGTH) {
                return Result.Error(ResultError.Validation("expirationDate cannot be empty"))
            }
            expirationMonth = expirationDate.ifEmpty { "0" }.take(INT_TWO).toInt()
            expirationYear = (EXPIRATION_YEAR_START + expirationDate.ifEmpty { "0" }.takeLast(INT_TWO)).toInt()
        }
        return repository.generateCardToken(
            GenerateCardTokenParams(
                cardId = cardId,
                expirationMonth = expirationMonth,
                expirationYear = expirationYear,
                securityCode = securityCode,
            ),
        )
    }
}
