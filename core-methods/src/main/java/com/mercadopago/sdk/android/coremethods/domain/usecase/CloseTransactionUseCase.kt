package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.provider.ThreeDSProviderManager
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.domain.utils.ThreeDSErrorMessages
import com.mercadopago.sdk.android.coremethods.domain.utils.ThreeDSSuccessMessages

/**
 * Use case responsible for closing the current 3DS transaction and releasing associated resources.
 * This should be called after the 3DS authentication flow is complete,
 * regardless of whether it succeeded or failed.
 */
internal class CloseTransactionUseCase(
    private val providerManager: ThreeDSProviderManager,
) {
    operator fun invoke(): Result<String, ResultError> {
        if (!providerManager.hasProvider()) {
            return Result.Error(
                ResultError.Validation(
                    message = ThreeDSErrorMessages.PROVIDER_NOT_AVAILABLE,
                ),
            )
        }
        return runCatching { providerManager.getProvider()?.close() }
            .fold(
                onSuccess = { Result.Success(ThreeDSSuccessMessages.TRANSACTION_CLOSED) },
                onFailure = { throwable ->
                    Result.Error(
                        ResultError.Validation(
                            message = "${ThreeDSErrorMessages.FAILED_TO_CLOSE_TRANSACTION_PREFIX}${throwable.message}",
                        ),
                    )
                },
            )
    }
}
