package com.mercadopago.sdk.android.coremethods.domain.usecase

import android.app.Activity
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeAuthentication
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeErrorDetail
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeStatus
import com.mercadopago.sdk.android.coremethods.domain.provider.ThreeDSProviderManager
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSAuthenticationModel
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSChallengeResult
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.domain.utils.ThreeDSErrorCodes
import com.mercadopago.sdk.android.coremethods.domain.utils.ThreeDSErrorMessages
import com.mercadopago.sdk.android.coremethods.domain.utils.convertChallengeResultToStatus

internal class StartChallengeUseCase(
    private val providerManager: ThreeDSProviderManager,
    private val authenticateThreeDSChallengeUseCase: AuthenticateThreeDSChallengeUseCase,
    private val updateThreeDSChallengeStatusUseCase: UpdateThreeDSChallengeStatusUseCase,
) {
    suspend operator fun invoke(
        activity: Activity,
        challengeId: String,
        timeout: Int,
    ): Result<ThreeDSChallengeResult, ResultError> {
        if (!providerManager.hasProvider()) {
            return Result.Error(
                ResultError.Validation(
                    message = ThreeDSErrorMessages.PROVIDER_NOT_AVAILABLE,
                ),
            )
        }
        return executeThreeDSChallenge(activity, challengeId, timeout)
    }

    private suspend fun executeThreeDSChallenge(
        activity: Activity,
        challengeId: String,
        timeout: Int,
    ): Result<ThreeDSChallengeResult, ResultError> =
        runCatching {
            authenticateThreeDSChallengeUseCase.invoke(challengeId = challengeId)
        }.fold(
            onSuccess = { authResult ->
                processChallengeAuthentication(activity, challengeId, authResult, timeout)
            },
            onFailure = { throwable ->
                Result.Error(
                    ResultError.Request(
                        code = ThreeDSErrorCodes.BAD_REQUEST,
                        message = "${ThreeDSErrorMessages.ERROR_AUTHENTICATING_CHALLENGE}${throwable.message}",
                    ),
                )
            },
        )

    private suspend fun processChallengeAuthentication(
        activity: Activity,
        challengeId: String,
        authenticationResult: Result<ThreeDSChallengeAuthentication, ResultError>,
        timeout: Int,
    ): Result<ThreeDSChallengeResult, ResultError> =
        when (authenticationResult) {
            is Result.Success -> {
                val authentication = authenticationResult.data
                authentication.threeDSAuthenticationModel?.let { challengeData ->
                    executeChallengeFlow(activity, challengeId, challengeData, timeout)
                } ?: Result.Error(
                    ResultError.Validation(
                        message = ThreeDSErrorMessages.CHALLENGE_DATA_NOT_AVAILABLE,
                    ),
                )
            }

            is Result.Error -> authenticationResult
        }

    private suspend fun executeChallengeFlow(
        activity: Activity,
        challengeId: String,
        challengeData: ThreeDSAuthenticationModel,
        timeout: Int,
    ): Result<ThreeDSChallengeResult, ResultError> =
        runCatching {
            providerManager.getProvider()?.doChallenge(
                activity = activity,
                authentication = challengeData,
                timeout = timeout,
            )
        }.fold(
            onSuccess = { challengeResult ->
                challengeResult?.let { result ->
                    val (status, errorDetail) = convertChallengeResultToStatus(result)
                    updateThreeDSChallengeStatus(
                        challengeId = challengeId,
                        status = status,
                        errorDetail = errorDetail,
                    )
                    Result.Success(result)
                } ?: Result.Error(
                    ResultError.Request(
                        code = ThreeDSErrorCodes.BAD_REQUEST,
                        message = ThreeDSErrorMessages.FAILED_TO_EXECUTE_CHALLENGE,
                    ),
                )
            },
            onFailure = { throwable ->
                Result.Error(
                    ResultError.Request(
                        code = ThreeDSErrorCodes.BAD_REQUEST,
                        message = "${ThreeDSErrorMessages.ERROR_DURING_CHALLENGE}${throwable.message}",
                    ),
                )
            },
        )

    private suspend fun updateThreeDSChallengeStatus(
        challengeId: String,
        status: ThreeDSChallengeStatus,
        errorDetail: ThreeDSChallengeErrorDetail? = null,
    ): Result<Unit, ResultError> =
        runCatching {
            updateThreeDSChallengeStatusUseCase.invoke(
                challengeId = challengeId,
                status = status,
                errorDetail = errorDetail,
            )
        }.fold(
            onSuccess = { result -> result },
            onFailure = { throwable ->
                Result.Error(
                    ResultError.Request(
                        code = ThreeDSErrorCodes.BAD_REQUEST,
                        message = "${ThreeDSErrorMessages.ERROR_DURING_CHALLENGE}${throwable.message}",
                    ),
                )
            },
        )
}
