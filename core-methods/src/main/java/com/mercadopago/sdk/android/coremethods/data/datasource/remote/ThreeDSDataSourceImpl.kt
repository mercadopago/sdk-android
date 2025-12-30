package com.mercadopago.sdk.android.coremethods.data.datasource.remote

import com.mercadopago.sdk.android.coremethods.data.datasource.mappers.mapSuccess
import com.mercadopago.sdk.android.coremethods.data.datasource.mappers.toInternalResponse
import com.mercadopago.sdk.android.coremethods.data.remote.mappers.toModel
import com.mercadopago.sdk.android.coremethods.data.remote.request.UpdateThreeDSChallengeStatusRequest
import com.mercadopago.sdk.android.coremethods.data.remote.service.ThreeDSService
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeAuthentication
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class ThreeDSDataSourceImpl(
    private val service: ThreeDSService,
) : ThreeDSDataSource {
    override suspend fun authenticateThreeDSChallenge(
        challengeId: String,
    ): Result<ThreeDSChallengeAuthentication, ResultError> =
        service.authenticateThreeDSChallenge(challengeId).toInternalResponse().mapSuccess {
            this.toModel()
        }

    override suspend fun updateThreeDSChallengeStatus(
        challengeId: String,
        request: UpdateThreeDSChallengeStatusRequest,
    ): Result<Unit, ResultError> = service.updateThreeDSChallengeStatus(challengeId, request).toInternalResponse()
}
