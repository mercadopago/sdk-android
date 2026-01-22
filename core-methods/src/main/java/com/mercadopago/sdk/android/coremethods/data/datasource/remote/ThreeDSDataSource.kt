package com.mercadopago.sdk.android.coremethods.data.datasource.remote

import com.mercadopago.sdk.android.coremethods.data.remote.request.ThreeDSDeviceDataRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.UpdateThreeDSChallengeStatusRequest
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeAuthentication
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface ThreeDSDataSource {
    suspend fun authenticateThreeDSChallenge(
        challengeId: String,
    ): Result<ThreeDSChallengeAuthentication, ResultError>

    suspend fun updateThreeDSChallengeStatus(
        challengeId: String,
        request: UpdateThreeDSChallengeStatusRequest,
    ): Result<Unit, ResultError>

    suspend fun saveThreeDSDeviceData(
        request: ThreeDSDeviceDataRequest,
    ): Result<Unit, ResultError>
}
