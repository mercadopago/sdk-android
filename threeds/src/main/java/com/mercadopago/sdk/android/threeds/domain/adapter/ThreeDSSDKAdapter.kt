package com.mercadopago.sdk.android.threeds.domain.adapter

import android.app.Activity
import com.mercadopago.sdk.android.threeds.domain.callback.MPThreeDSChallengeDelegate
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSDirectoryServer
import com.mercadopago.sdk.android.threeds.domain.model.params.ThreeDSAuthRequestParameters

/**
 * Interface that abstracts the 3DS SDK implementation.
 * This allows the MPThreeDS module to be agnostic of the specific 3DS SDK used.
 * Different vendors can implement this interface to integrate their 3DS SDK.
 */
internal interface ThreeDSSDKAdapter {
    suspend fun initialize()

    /**
     * Creates a transaction with the specified directory server.
     *
     * @param directoryServer The directory server to use for the transaction
     * @return Transaction object identifier or reference
     */
    suspend fun createTransaction(directoryServer: MPThreeDSDirectoryServer)

    /**
     * Gets the authentication request parameters for the current transaction.
     *
     * @return Authentication request parameters needed for backend call
     */
    suspend fun getAuthenticationRequestParameters(): ThreeDSAuthRequestParameters?

    /**
     * Performs the challenge flow with the provided authentication response.
     *
     * @param activity The activity context for displaying the challenge UI
     * @param authenticationResponse The response from MercadoPago backend
     * @param delegate Callback for receiving challenge results
     */
    suspend fun doChallenge(
        activity: Activity,
        authenticationResponse: MPThreeDSAuthenticationModel,
        delegate: MPThreeDSChallengeDelegate,
    )
}
