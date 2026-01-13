package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeErrorDetail
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeStatus
import com.mercadopago.sdk.android.coremethods.domain.model.params.UpdateThreeDSChallengeStatusParams
import com.mercadopago.sdk.android.coremethods.domain.repository.ThreeDSRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class UpdateThreeDSChallengeStatusUseCase(
    private val repository: ThreeDSRepository,
) {
    suspend operator fun invoke(
        challengeId: String,
        status: ThreeDSChallengeStatus,
        errorDetail: ThreeDSChallengeErrorDetail? = null,
    ): Result<Unit, ResultError> {
        val validationError = validateParams(challengeId, status, errorDetail)
        return if (validationError != null) {
            return Result.Error(validationError)
        } else {
            repository.updateThreeDSChallengeStatus(
                UpdateThreeDSChallengeStatusParams(
                    challengeId = challengeId,
                    status = status,
                    errorDetail = errorDetail,
                ),
            )
        }
    }

    private fun validateParams(
        challengeId: String,
        status: ThreeDSChallengeStatus,
        errorDetail: ThreeDSChallengeErrorDetail?,
    ): ResultError? =
        when {
            challengeId.isEmpty() -> ResultError.Validation("challenge id cannot be empty")
            status == ThreeDSChallengeStatus.ERROR && errorDetail == null ->
                ResultError.Validation("error detail is required when status is ERROR")

            else -> null
        }
}
