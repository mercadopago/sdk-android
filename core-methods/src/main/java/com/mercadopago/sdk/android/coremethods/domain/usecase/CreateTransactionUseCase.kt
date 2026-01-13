package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.provider.ThreeDSProviderManager
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.domain.utils.ThreeDSErrorMessages
import com.mercadopago.sdk.android.coremethods.domain.utils.ThreeDSSuccessMessages

internal class CreateTransactionUseCase(
    private val providerManager: ThreeDSProviderManager,
) {
    operator fun invoke(
        cardToken: CardToken,
    ): Result<String, ResultError> =
        if (!providerManager.hasProvider()) {
            Result.Error(
                ResultError.Validation(
                    message = ThreeDSErrorMessages.PROVIDER_NOT_AVAILABLE,
                ),
            )
        } else {
            runCatching {
                providerManager.getProvider()?.createTransaction(cardToken.token)
            }
                .fold(
                    onSuccess = { Result.Success(ThreeDSSuccessMessages.TRANSACTION_CREATED) },
                    onFailure = { throwable ->
                        Result.Error(
                            ResultError.Validation(
                                message = "${ThreeDSErrorMessages.FAILED_TO_CREATE_TRANSACTION}${throwable.message}",
                            ),
                        )
                    },
                )
        }
}
