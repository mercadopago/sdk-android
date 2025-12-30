package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeAuthentication
import com.mercadopago.sdk.android.coremethods.domain.model.params.AuthenticateThreeDSChallengeParams
import com.mercadopago.sdk.android.coremethods.domain.repository.ThreeDSRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

private const val CHALLENGE_NOT_EMPTY = "challenge id cannot be empty"

internal class AuthenticateThreeDSChallengeUseCase(
    private val repository: ThreeDSRepository,
) {
    suspend operator fun invoke(
        challengeId: String,
    ): Result<ThreeDSChallengeAuthentication, ResultError> =
        if (challengeId.isEmpty()) {
            Result.Error(ResultError.Validation(CHALLENGE_NOT_EMPTY))
        } else {
            repository.authenticateThreeDSChallenge(
                AuthenticateThreeDSChallengeParams(
                    challengeId = challengeId,
                ),
            )
        }
}
