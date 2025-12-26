package com.mercadopago.sdk.android.threeds.data.repository

import android.app.Activity
import com.mercadopago.sdk.android.threeds.data.mappers.toModel
import com.mercadopago.sdk.android.threeds.data.mappers.toParams
import com.mercadopago.sdk.android.threeds.data.wrapper.ThreeDSWrapper
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeResult
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSWarning
import com.mercadopago.sdk.android.threeds.domain.model.params.MPThreeDSRequestParams
import com.mercadopago.sdk.android.threeds.domain.repository.ThreeDSRepository

internal class ThreeDSRepositoryImpl(
    private val threeDSWrapper: ThreeDSWrapper,
) : ThreeDSRepository {
    override suspend fun initialize() {
        threeDSWrapper.initialize()
    }

    override fun getWarnings(): List<MPThreeDSWarning> {
        return threeDSWrapper.getWarnings().map { it.toModel() }
    }

    override fun close() {
        threeDSWrapper.close()
    }

    override fun createTransaction(
        paymentMethodId: String,
    ) {
        threeDSWrapper.createTransaction(paymentMethodId)
    }

    override fun getAuthenticationRequestParameters(): MPThreeDSRequestParams? {
        return threeDSWrapper.getAuthenticationRequestParameters()
    }

    override suspend fun doChallenge(
        activity: Activity,
        authenticationResponse: MPThreeDSAuthenticationModel,
        timeout: Int,
    ): MPThreeDSChallengeResult {
        return threeDSWrapper.doChallenge(
            activity = activity,
            authenticationParams = authenticationResponse.toParams(),
            timeout = timeout,
        )
    }
}
