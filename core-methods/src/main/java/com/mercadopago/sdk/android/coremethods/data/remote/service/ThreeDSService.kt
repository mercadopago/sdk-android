package com.mercadopago.sdk.android.coremethods.data.remote.service

import com.mercadopago.sdk.android.coremethods.data.remote.request.UpdateThreeDSChallengeStatusRequest
import com.mercadopago.sdk.android.coremethods.data.remote.response.ThreeDSChallengeAuthenticationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

private const val BRICKS_API = "cho-off"
private const val VERSION = "v1"

internal interface ThreeDSService {
    @POST("$BRICKS_API/$VERSION/challenges/threeds/{challengeId}/authenticate")
    suspend fun authenticateThreeDSChallenge(
        @Path("challengeId") challengeId: String,
    ): Response<ThreeDSChallengeAuthenticationResponse>

    @PATCH("$BRICKS_API/$VERSION/challenges/threeds/{challengeId}")
    suspend fun updateThreeDSChallengeStatus(
        @Path("challengeId") challengeId: String,
        @Body request: UpdateThreeDSChallengeStatusRequest,
    ): Response<Unit>
}
