package com.mercadopago.sdk.android.threeds.adapter.mappers

import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSAuthenticated
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSChallengeError
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSChallengeModel
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSChallengeResult
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticated
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeError
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeResult

/**
 * Mapper to convert between core-methods ThreeDSChallengeResult and threeds MPThreeDSChallengeResult.
 * This allows the adapter to translate between the two module's domain models.
 */
internal object ThreeDSChallengeResultMapper {
    /**
     * Converts MPThreeDSChallengeResult (threeds model) to ThreeDSChallengeResult (core-methods model).
     *
     * @param mpResult The MPThreeDSChallengeResult to convert
     * @return The converted ThreeDSChallengeResult
     */
    fun toThreeDSChallengeResult(
        mpResult: MPThreeDSChallengeResult,
    ): ThreeDSChallengeResult {
        return when (mpResult) {
            is MPThreeDSChallengeResult.OnSuccess -> ThreeDSChallengeResult.OnSuccess(
                result = toThreeDSAuthenticated(mpResult.result),
            )
            is MPThreeDSChallengeResult.OnError -> ThreeDSChallengeResult.OnError(
                error = toThreeDSChallengeError(mpResult.error),
            )
            is MPThreeDSChallengeResult.OnCancel -> ThreeDSChallengeResult.OnCancel
            is MPThreeDSChallengeResult.OnTimedOut -> ThreeDSChallengeResult.OnTimedOut
        }
    }

    /**
     * Converts ThreeDSChallengeResult (core-methods model) to MPThreeDSChallengeResult (threeds model).
     *
     * @param result The ThreeDSChallengeResult to convert
     * @return The converted MPThreeDSChallengeResult
     */
    fun toMPThreeDSChallengeResult(
        result: ThreeDSChallengeResult,
    ): MPThreeDSChallengeResult {
        return when (result) {
            is ThreeDSChallengeResult.OnSuccess -> MPThreeDSChallengeResult.OnSuccess(
                result = toMPThreeDSAuthenticated(result.result),
            )
            is ThreeDSChallengeResult.OnError -> MPThreeDSChallengeResult.OnError(
                error = toMPThreeDSChallengeError(result.error),
            )
            is ThreeDSChallengeResult.OnCancel -> MPThreeDSChallengeResult.OnCancel
            is ThreeDSChallengeResult.OnTimedOut -> MPThreeDSChallengeResult.OnTimedOut
        }
    }

    private fun toThreeDSAuthenticated(
        mpAuthenticated: MPThreeDSAuthenticated,
    ): ThreeDSAuthenticated {
        return ThreeDSAuthenticated(
            challengeResponse = toThreeDSChallengeModel(mpAuthenticated.challengeResponse),
            challengeCompleted = mpAuthenticated.challengeCompleted,
        )
    }

    private fun toMPThreeDSAuthenticated(
        authenticated: ThreeDSAuthenticated,
    ): MPThreeDSAuthenticated {
        return MPThreeDSAuthenticated(
            challengeResponse = toMPThreeDSChallengeModel(authenticated.challengeResponse),
            challengeCompleted = authenticated.challengeCompleted,
        )
    }

    private fun toThreeDSChallengeModel(
        mpModel: MPThreeDSChallengeModel,
    ): ThreeDSChallengeModel {
        return ThreeDSChallengeModel(
            threeDSServerTransID = mpModel.threeDSServerTransID,
            acsReferenceNumber = mpModel.acsReferenceNumber,
            dsTransID = mpModel.dsTransID,
            acsTransID = mpModel.acsTransID,
            acsSignedContent = mpModel.acsSignedContent,
        )
    }

    private fun toMPThreeDSChallengeModel(
        model: ThreeDSChallengeModel,
    ): MPThreeDSChallengeModel {
        return MPThreeDSChallengeModel(
            threeDSServerTransID = model.threeDSServerTransID,
            acsReferenceNumber = model.acsReferenceNumber,
            dsTransID = model.dsTransID,
            acsTransID = model.acsTransID,
            acsSignedContent = model.acsSignedContent,
        )
    }

    private fun toThreeDSChallengeError(
        mpError: MPThreeDSChallengeError,
    ): ThreeDSChallengeError {
        return ThreeDSChallengeError(
            code = mpError.code,
            message = mpError.message,
            details = mpError.details,
            cause = mpError.cause,
        )
    }

    private fun toMPThreeDSChallengeError(
        error: ThreeDSChallengeError,
    ): MPThreeDSChallengeError {
        return MPThreeDSChallengeError(
            code = error.code,
            message = error.message,
            details = error.details,
            cause = error.cause,
        )
    }
}
