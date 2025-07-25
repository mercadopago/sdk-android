package com.mercadopago.sdk.android.threeds.domain.usecase

import com.mercadopago.sdk.android.threeds.data.remote.response.MPThreeDSAuthenticationResponse
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.params.ThreeDSAuthenticationParams
import com.mercadopago.sdk.android.threeds.domain.repository.ThreeDSRepository
import kotlinx.coroutines.flow.Flow

internal class AuthenticateUseCase(
    private val repository: ThreeDSRepository,
) {
    operator fun invoke(
        token: String,
        sdkAppId: String,
        sdkEncData: String,
        sdkEphemPubKey: String,
        sdkMaxTimeout: String,
        sdkReferenceNumber: String,
        sdkTransId: String,
    ): Flow<MPThreeDSAuthenticationModel> {
        return repository.authenticate(
            ThreeDSAuthenticationParams(
                token = token,
                sdkAppId = sdkAppId,
                sdkEncData = sdkEncData,
                sdkEphemPubKey = sdkEphemPubKey,
                sdkMaxTimeout = sdkMaxTimeout,
                sdkReferenceNumber = sdkReferenceNumber,
                sdkTransId = sdkTransId
            )
        )
    }
}
