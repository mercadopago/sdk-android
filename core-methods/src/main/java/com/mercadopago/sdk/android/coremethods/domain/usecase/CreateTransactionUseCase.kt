package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.provider.ThreeDSProviderManager
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.domain.utils.ThreeDSErrorMessages
import com.mercadopago.sdk.android.coremethods.domain.utils.ThreeDSSuccessMessages

/**
 * Use case responsible for creating a new 3DS transaction using the provided card token.
 * This initializes the 3DS authentication process for the specified card.
 */
internal class CreateTransactionUseCase(
    private val providerManager: ThreeDSProviderManager,
) {
    operator fun invoke(
        cardToken: CardToken,
    ): Result<String, ResultError> {
        if (!providerManager.hasProvider()) {
            return Result.Error(
                ResultError.Validation(
                    message = ThreeDSErrorMessages.PROVIDER_NOT_AVAILABLE,
                ),
            )
        }
        return runCatching { providerManager.getProvider()?.createTransaction(cardToken.token) }
            .fold(
                onSuccess = { Result.Success(ThreeDSSuccessMessages.TRANSACTION_CREATED) },
                onFailure = { throwable ->
                    Result.Error(
                        ResultError.Validation(
                            message = "${ThreeDSErrorMessages.FAILED_TO_CREATE_TRANSACTION_PREFIX}${throwable.message}",
                        ),
                    )
                },
            )
    }
}
