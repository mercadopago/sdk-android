package com.mercadopago.sdk.android.coremethods.data.repository

import com.mercadopago.sdk.android.coremethods.data.datasource.remote.ThreeDSDataSource
import com.mercadopago.sdk.android.coremethods.data.remote.mappers.toRequest
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeAuthentication
import com.mercadopago.sdk.android.coremethods.domain.model.params.AuthenticateThreeDSChallengeParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.UpdateThreeDSChallengeStatusParams
import com.mercadopago.sdk.android.coremethods.domain.repository.ThreeDSRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal class ThreeDSRepositoryImpl(
    private val dataSource: ThreeDSDataSource,
) : ThreeDSRepository {
    override suspend fun authenticateThreeDSChallenge(
        params: AuthenticateThreeDSChallengeParams,
    ): Result<ThreeDSChallengeAuthentication, ResultError> = dataSource.authenticateThreeDSChallenge(params.challengeId)

    override suspend fun updateThreeDSChallengeStatus(
        params: UpdateThreeDSChallengeStatusParams,
    ): Result<Unit, ResultError> =
        dataSource.updateThreeDSChallengeStatus(
            challengeId = params.challengeId,
            request = params.toRequest(),
        )
}
