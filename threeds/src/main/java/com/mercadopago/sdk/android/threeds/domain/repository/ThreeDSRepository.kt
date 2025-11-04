package com.mercadopago.sdk.android.threeds.domain.repository

import android.app.Activity
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeResult
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSWarning
import com.mercadopago.sdk.android.threeds.domain.model.params.MPThreeDSRequestParams

internal interface ThreeDSRepository {
    fun getWarnings(): List<MPThreeDSWarning>

    fun close()

    fun createTransaction(paymentMethodId: String)

    fun getAuthenticationRequestParameters(): MPThreeDSRequestParams?

    suspend fun doChallenge(
        activity: Activity,
        authenticationResponse: MPThreeDSAuthenticationModel,
        timeout: Int,
    ): MPThreeDSChallengeResult
}
