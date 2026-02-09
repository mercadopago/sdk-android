package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadolibre.android.device.sdk.DeviceSDK
import com.mercadopago.sdk.android.coremethods.BuildConfig
import com.mercadopago.sdk.android.coremethods.data.remote.utils.CARD_BIN_LENGTH
import com.mercadopago.sdk.android.coremethods.data.remote.utils.ERROR_EXPIRATION_DATE_EMPTY
import com.mercadopago.sdk.android.coremethods.data.remote.utils.ERROR_EXPIRATION_DATE_LENGTH
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
import com.mercadopago.sdk.android.coremethods.domain.usecase.validations.IsSecurityCodeValidUseCase
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_TWO
import com.mercadopago.sdk.android.di.SessionIdProvider

@Suppress("ReturnCount", "NoEmptyFirstLineInMethodBlock")
internal class GenerateCardTokenUseCase(
    private val repository: CoreMethodsRepository,
    private val paymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val sessionIdProvider: SessionIdProvider,
    private val isSecurityCodeValidUseCase: IsSecurityCodeValidUseCase,
) {
    suspend operator fun invoke(
        cardNumber: String,
        securityCode: String?,
        expirationDate: String,
        buyerIdentification: BuyerIdentification? = null,
    ): Result<CardToken, ResultError> {
        if (cardNumber.isEmpty()) {
            return Result.Error(ResultError.Validation("card number cannot be empty"))
        }

        if (securityCode != null) {
            val securityCodeLength: Int =
                when (val result = paymentMethodsUseCase(cardNumber.take(CARD_BIN_LENGTH))) {
                    is Result.Success -> result.data.firstOrNull()?.card?.securityCode?.length
                        ?: SECURITY_CODE_MIN_LENGTH

                    is Result.Error -> SECURITY_CODE_MIN_LENGTH
                }

            if (!isSecurityCodeValidUseCase(securityCode.toInt(), securityCodeLength)) {
                return Result.Error(ResultError.Validation(ERROR_SECURITY_CODE_MIN_LENGTH))
            }
        }

        if (expirationDate.isEmpty()) {
            return Result.Error(ResultError.Validation(ERROR_EXPIRATION_DATE_EMPTY))
        }

        if (expirationDate.length < EXPIRATION_YEAR_MIN_LENGTH) {
            return Result.Error(ResultError.Validation(ERROR_EXPIRATION_DATE_LENGTH))
        }

        val expirationMonth = expirationDate.ifEmpty { "0" }.take(INT_TWO).toInt()
        val expirationYear =
            (EXPIRATION_YEAR_START + expirationDate.ifEmpty { "0" }.takeLast(INT_TWO)).toInt()

        return repository.generateCardToken(
            GenerateCardTokenParams(
                cardNumber = cardNumber,
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
                sdkVersion = BuildConfig.SdkVersion,
            ),
        )
    }
}
