package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeAuthentication
import com.mercadopago.sdk.android.coremethods.domain.model.params.AuthenticateThreeDSChallengeParams
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

/**
 * Use case responsible for authenticating a 3DS challenge.
 *
 * This use case validates the input parameters and delegates the API call to the repository.
 * It returns the authentication status and optional challenge data needed to display
 * the 3DS challenge UI to the user.
 */
internal class AuthenticateThreeDSChallengeUseCase(
    private val repository: CoreMethodsRepository,
) {
    suspend operator fun invoke(challengeId: String): Result<ThreeDSChallengeAuthentication, ResultError> {
        if (challengeId.isEmpty()) {
            return Result.Error(ResultError.Validation("challenge id cannot be empty"))
        }
        return repository.authenticateThreeDSChallenge(
            AuthenticateThreeDSChallengeParams(
                challengeId = challengeId,
            ),
        )
    }
}
