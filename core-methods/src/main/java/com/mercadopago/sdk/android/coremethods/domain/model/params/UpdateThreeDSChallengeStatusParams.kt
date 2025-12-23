package com.mercadopago.sdk.android.coremethods.domain.model.params

import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeErrorDetail
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeStatus

/**
 * Parameters for updating the status of a 3DS challenge.
 *
 * @property challengeId Unique identifier of the 3DS challenge
 * @property status The new status of the challenge
 * @property errorDetail Optional error details when status is ERROR
 */
internal data class UpdateThreeDSChallengeStatusParams(
    val challengeId: String,
    val status: ThreeDSChallengeStatus,
    val errorDetail: ThreeDSChallengeErrorDetail? = null,
)
