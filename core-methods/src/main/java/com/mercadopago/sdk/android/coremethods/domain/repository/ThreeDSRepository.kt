package com.mercadopago.sdk.android.coremethods.domain.repository

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeAuthentication
import com.mercadopago.sdk.android.coremethods.domain.model.params.AuthenticateThreeDSChallengeParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.UpdateThreeDSChallengeStatusParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface ThreeDSRepository {
    suspend fun authenticateThreeDSChallenge(
        params: AuthenticateThreeDSChallengeParams,
    ): Result<ThreeDSChallengeAuthentication, ResultError>

    suspend fun updateThreeDSChallengeStatus(
        params: UpdateThreeDSChallengeStatusParams,
    ): Result<Unit, ResultError>
}
