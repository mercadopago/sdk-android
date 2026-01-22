package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.provider.ThreeDSProviderManager
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSRequestParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.domain.utils.ThreeDSErrorCodes
import com.mercadopago.sdk.android.coremethods.domain.utils.ThreeDSErrorMessages

internal class GetAuthenticationRequestParametersUseCase(
    private val providerManager: ThreeDSProviderManager,
) {
    operator fun invoke(): Result<ThreeDSRequestParams, ResultError> =
        if (!providerManager.hasProvider()) {
            Result.Error(
                ResultError.Validation(
                    message = ThreeDSErrorMessages.PROVIDER_NOT_AVAILABLE,
                ),
            )
        } else {
            runCatching { providerManager.getProvider()?.getAuthenticationRequestParameters() }
                .fold(
                    onSuccess = { params ->
                        params?.let { Result.Success(it) } ?: Result.Error(
                            ResultError.Request(
                                code = ThreeDSErrorCodes.EMPTY,
                                message = ThreeDSErrorMessages.FAILED_TO_GET_AUTH_PARAMETERS,
                            ),
                        )
                    },
                    onFailure = { throwable ->
                        Result.Error(
                            ResultError.Request(
                                code = ThreeDSErrorCodes.EMPTY,
                                message = "${ThreeDSErrorMessages.ERROR_GETTING_AUTH_PARAMETERS}${throwable.message}",
                            ),
                        )
                    },
                )
        }
}
