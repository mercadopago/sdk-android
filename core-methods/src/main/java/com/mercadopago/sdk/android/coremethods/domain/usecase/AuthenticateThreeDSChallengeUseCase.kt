package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeAuthentication
import com.mercadopago.sdk.android.coremethods.domain.model.params.AuthenticateThreeDSChallengeParams
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class AuthenticateThreeDSChallengeUseCase(
    private val repository: CoreMethodsRepository,
) {
    suspend operator fun invoke(
        challengeId: String,
    ): Result<ThreeDSChallengeAuthentication, ResultError> =
        if (challengeId.isEmpty()) {
            Result.Error(ResultError.Validation("challenge id cannot be empty"))
        } else {
            repository.authenticateThreeDSChallenge(
                AuthenticateThreeDSChallengeParams(
                    challengeId = challengeId,
                ),
            )
        }
}
