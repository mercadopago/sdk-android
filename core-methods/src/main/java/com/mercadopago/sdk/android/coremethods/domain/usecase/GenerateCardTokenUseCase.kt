package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadolibre.android.device.sdk.DeviceSDK
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.MPResultError
import com.mercadopago.sdk.android.coremethods.domain.model.params.BuyerIdentificationParam
import com.mercadopago.sdk.android.coremethods.domain.model.params.GenerateCardTokenParams
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.MPResult
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.INT_TWO

internal const val EXPIRATION_YEAR_START = "20"
internal const val EXPIRATION_YEAR_MIN_LENGTH = 2
internal const val SECURITY_CODE_MIN_LENGTH = 3

@Suppress("ReturnCount")
internal class GenerateCardTokenUseCase(
    private val repository: CoreMethodsRepository,
) {
    suspend operator fun invoke(
        cardNumber: String,
        securityCode: String?,
        expirationDate: String?,
        buyerIdentification: BuyerIdentification? = null
    ): MPResult<CardToken, MPResultError> {
        val expirationDateIsNotNull = expirationDate != null

        if (cardNumber.isEmpty()) {
            return MPResult.Error(MPResultError.Validation("card id number cannot be empty"))
        }

        if (!securityCode.isNullOrEmpty() && securityCode.length < SECURITY_CODE_MIN_LENGTH) {
            return MPResult.Error(MPResultError.Validation("security code length cannot be smaller than tree"))
        }

        if (expirationDateIsNotNull) {
            if (expirationDate!!.isEmpty()) {
                return MPResult.Error(MPResultError.Validation("expiration date cannot be empty"))
            }

            if (expirationDate.length < EXPIRATION_YEAR_MIN_LENGTH) {
                return MPResult.Error(MPResultError.Validation("expiration date length cannot be smaller than two"))
            }
        }

        val expirationMonth = expirationDate?.ifEmpty { "0" }?.take(INT_TWO)?.toInt()
        val expirationYear =
            (EXPIRATION_YEAR_START + expirationDate?.ifEmpty { "0" }?.takeLast(INT_TWO)).toInt()

        return repository.generateCardToken(
            GenerateCardTokenParams(
                cardNumber = cardNumber,
                expirationMonth = if (expirationDateIsNotNull) expirationMonth else null,
                expirationYear = if (expirationDateIsNotNull) expirationYear else null,
                securityCode = securityCode,
                buyerIdentification = buyerIdentification?.let { buyer ->
                    BuyerIdentificationParam(
                        name = buyer.name,
                        number = buyer.number,
                        type = buyer.type
                    )
                },
                device = DeviceSDK.getInstance()?.info,
            ),
        )
    }
}
