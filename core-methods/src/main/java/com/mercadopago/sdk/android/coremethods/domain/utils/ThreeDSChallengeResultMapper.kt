package com.mercadopago.sdk.android.coremethods.domain.utils

import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeErrorDetail
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeStatus
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSChallengeResult

/**
 * Maps a ThreeDSChallengeResult to a Pair containing the status and optional error detail.
 *
 * @param result The challenge result to convert
 * @return A Pair containing the ThreeDSChallengeStatus and optional ThreeDSChallengeErrorDetail
 */
internal fun convertChallengeResultToStatus(
    result: ThreeDSChallengeResult,
): Pair<ThreeDSChallengeStatus, ThreeDSChallengeErrorDetail?> {
    return when (result) {
        is ThreeDSChallengeResult.OnSuccess -> {
            Pair(ThreeDSChallengeStatus.COMPLETED, null)
        }

        is ThreeDSChallengeResult.OnError -> {
            val errorDetail = ThreeDSChallengeErrorDetail(
                type = result.error.details,
                code = result.error.code,
            )
            Pair(ThreeDSChallengeStatus.ERROR, errorDetail)
        }

        is ThreeDSChallengeResult.OnCancel -> {
            Pair(ThreeDSChallengeStatus.CANCELLED, null)
        }

        is ThreeDSChallengeResult.OnTimedOut -> {
            Pair(ThreeDSChallengeStatus.TIMEOUT, null)
        }
    }
}
