package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadolibre.android.device.sdk.DeviceSDK
import com.mercadopago.sdk.android.coremethods.data.remote.utils.ERROR_EXPIRATION_DATE_LENGTH
import com.mercadopago.sdk.android.di.SessionIdProvider
import com.mercadopago.sdk.android.coremethods.data.remote.utils.ERROR_SECURITY_CODE_MIN_LENGTH
import com.mercadopago.sdk.android.coremethods.data.remote.utils.EXPIRATION_YEAR_MIN_LENGTH
import com.mercadopago.sdk.android.coremethods.data.remote.utils.EXPIRATION_YEAR_START
import com.mercadopago.sdk.android.coremethods.data.remote.utils.SECURITY_CODE_MIN_LENGTH
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.params.BuyerIdentificationParam
import com.mercadopago.sdk.android.coremethods.domain.model.params.GenerateCardTokenParams
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_TWO

@Suppress("ReturnCount", "NoEmptyFirstLineInMethodBlock")
internal class GenerateCardIdTokenUseCase(
    private val repository: CoreMethodsRepository,
    private val sessionIdProvider: SessionIdProvider,
) {
    suspend operator fun invoke(
        cardId: String,
        securityCode: String?,
        expirationDate: String?,
        buyerIdentification: BuyerIdentification? = null,
    ): Result<CardToken, ResultError> {
        if (cardId.isEmpty()) {
            return Result.Error(ResultError.Validation("card id cannot be empty"))
        }

        if (!securityCode.isNullOrEmpty() && securityCode.length < SECURITY_CODE_MIN_LENGTH) {
            return Result.Error(ResultError.Validation(ERROR_SECURITY_CODE_MIN_LENGTH))
        }

        var expirationMonth: Int? = null
        var expirationYear: Int? = null

        if (!expirationDate.isNullOrEmpty()) {
            if (expirationDate.length < EXPIRATION_YEAR_MIN_LENGTH) {
                return Result.Error(ResultError.Validation(ERROR_EXPIRATION_DATE_LENGTH))
            }

            expirationMonth = expirationDate.take(INT_TWO).toInt()
            expirationYear = (EXPIRATION_YEAR_START + expirationDate.takeLast(INT_TWO)).toInt()
        }

        return repository.generateCardToken(
            GenerateCardTokenParams(
                cardId = cardId,
                expirationMonth = expirationMonth,
                expirationYear = expirationYear,
                securityCode = securityCode,
                buyerIdentification = buyerIdentification?.let { buyer ->
                    BuyerIdentificationParam(
                        name = buyer.name,
                        number = buyer.number,
                        type = buyer.type,
                    )
                },
                device = DeviceSDK.getInstance()?.info,
                session = sessionIdProvider.getSessionId(),
                sdkVersion = "",
            ),
        )
    }
}
