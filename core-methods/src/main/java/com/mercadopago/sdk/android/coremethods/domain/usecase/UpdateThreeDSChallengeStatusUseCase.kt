package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeErrorDetail
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeStatus
import com.mercadopago.sdk.android.coremethods.domain.model.params.UpdateThreeDSChallengeStatusParams
import com.mercadopago.sdk.android.coremethods.domain.repository.ThreeDSRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

/**
 * Use case responsible for updating the status of a 3DS challenge.
 *
 * This use case validates the input parameters and delegates the API call to the repository.
 * It updates the backend with the result of the 3DS challenge flow (completion, cancellation,
 * error, or timeout).
 */
internal class UpdateThreeDSChallengeStatusUseCase(
    private val repository: ThreeDSRepository,
) {
    suspend operator fun invoke(
        challengeId: String,
        status: ThreeDSChallengeStatus,
        errorDetail: ThreeDSChallengeErrorDetail? = null,
    ): Result<Unit, ResultError> {
        val validationError = validateParams(challengeId, status, errorDetail)
        if (validationError != null) {
            return Result.Error(validationError)
        }
        return repository.updateThreeDSChallengeStatus(
            UpdateThreeDSChallengeStatusParams(
                challengeId = challengeId,
                status = status,
                errorDetail = errorDetail,
            ),
        )
    }

    private fun validateParams(
        challengeId: String,
        status: ThreeDSChallengeStatus,
        errorDetail: ThreeDSChallengeErrorDetail?,
    ): ResultError? {
        return when {
            challengeId.isEmpty() -> ResultError.Validation("challenge id cannot be empty")
            status == ThreeDSChallengeStatus.ERROR && errorDetail == null ->
                ResultError.Validation("error detail is required when status is ERROR")
            else -> null
        }
    }
}
