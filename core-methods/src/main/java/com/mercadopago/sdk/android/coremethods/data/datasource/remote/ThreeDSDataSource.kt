package com.mercadopago.sdk.android.coremethods.data.datasource.remote

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeAuthentication
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface ThreeDSDataSource {
    suspend fun authenticateThreeDSChallenge(
        challengeId: String,
    ): Result<ThreeDSChallengeAuthentication, ResultError>
}
