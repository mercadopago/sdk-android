package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.params.GenerateCardTokenParams
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_TWO

internal const val EXPIRATION_YEAR_START = "20"
internal const val EXPIRATION_YEAR_MIN_LENGTH = 2

@Suppress("ReturnCount")
internal class GenerateCardTokenUseCase(
    private val repository: CoreMethodsRepository,
) {
    suspend operator fun invoke(
        cardNumber: String,
        securityCode: String,
        expirationDate: String?,
    ): Result<CardToken, ResultError> {
        val expirationDateIsRequired = expirationDate != null

        if (cardNumber.isEmpty()) {
            return Result.Error(ResultError.Validation("card id number cannot be empty"))
        }

        if (expirationDateIsRequired) {
            if (expirationDate!!.isEmpty()) {
                return Result.Error(ResultError.Validation("expiration date cannot be empty"))
            }

            if (expirationDate.length < EXPIRATION_YEAR_MIN_LENGTH) {
                return Result.Error(ResultError.Validation("expiration date cannot be smaller than two"))
            }
        }

        val expirationMonth = expirationDate?.ifEmpty { "0" }?.take(INT_TWO)?.toInt()
        val expirationYear = (EXPIRATION_YEAR_START + expirationDate?.ifEmpty { "0" }?.takeLast(INT_TWO)).toInt()

        return repository.generateCardToken(
            GenerateCardTokenParams(
                cardNumber = cardNumber,
                expirationMonth = expirationMonth,
                expirationYear = expirationYear,
                securityCode = securityCode,
            ),
        )
    }
}
