package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.provider.ThreeDSProviderManager
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSWarning
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.domain.utils.ThreeDSErrorCodes
import com.mercadopago.sdk.android.coremethods.domain.utils.ThreeDSErrorMessages

/**
 * Use case responsible for retrieving security warnings from the 3DS SDK.
 * These warnings indicate potential security issues or configuration problems
 * that may affect the 3DS authentication process.
 */
internal class GetWarningsUseCase(
    private val providerManager: ThreeDSProviderManager,
) {
    operator fun invoke(): Result<List<ThreeDSWarning>, ResultError> {
        if (!providerManager.hasProvider()) {
            return Result.Error(
                ResultError.Validation(
                    message = ThreeDSErrorMessages.PROVIDER_NOT_AVAILABLE,
                ),
            )
        }
        return runCatching { providerManager.getProvider()?.getWarnings() }
            .fold(
                onSuccess = { warnings ->
                    warnings?.let { Result.Success(it) } ?: Result.Error(
                        ResultError.Request(
                            code = ThreeDSErrorCodes.EMPTY,
                            message = ThreeDSErrorMessages.FAILED_TO_GET_WARNINGS,
                        ),
                    )
                },
                onFailure = { throwable ->
                    Result.Error(
                        ResultError.Request(
                            code = ThreeDSErrorCodes.EMPTY,
                            message = "${ThreeDSErrorMessages.ERROR_GETTING_WARNINGS_PREFIX}${throwable.message}",
                        ),
                    )
                },
            )
    }
}
