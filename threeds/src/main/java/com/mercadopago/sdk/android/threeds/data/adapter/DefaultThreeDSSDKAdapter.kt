package com.mercadopago.sdk.android.threeds.data.adapter

import android.app.Activity
import com.mercadopago.sdk.android.threeds.domain.adapter.ThreeDSSDKAdapter
import com.mercadopago.sdk.android.threeds.domain.callback.MPThreeDSChallengeDelegate
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationResponse
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeError
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSDirectoryServer
import com.mercadopago.sdk.android.threeds.domain.model.ThreeDSAuthRequestParameters
import java.util.UUID

/**
 * Default implementation of ThreeDSSDKAdapter.
 * This is a placeholder implementation that can be used when no specific 3DS SDK is integrated
 * or for testing purposes. It generates mock data and always returns authentication failures.
 */
internal class DefaultThreeDSSDKAdapter : ThreeDSSDKAdapter {

    override suspend fun createTransaction(directoryServer: MPThreeDSDirectoryServer): String {
        // Generate a mock transaction ID
        return "mock_transaction_${UUID.randomUUID()}"
    }

    override suspend fun getAuthenticationRequestParameters(transactionId: String): ThreeDSAuthRequestParameters {
        // Return mock authentication request parameters
        return ThreeDSAuthRequestParameters(
            sdkAppId = "mock_app_id",
            deviceData = "mock_device_data",
            sdkEphemeralPublicKey = "mock_ephemeral_key",
            sdkReferenceNumber = "mock_reference_number",
            sdkTransactionId = transactionId
        )
    }

    override suspend fun doChallenge(
        activity: Activity,
        transactionId: String,
        authenticationResponse: MPThreeDSAuthenticationResponse,
        delegate: MPThreeDSChallengeDelegate
    ) {
        // Mock challenge that always fails
        delegate.onError(
            MPThreeDSChallengeError(
                code = "NO_3DS_SDK",
                message = "No 3DS SDK implementation provided. Please integrate a 3DS SDK to enable authentication."
            )
        )
    }

    override suspend fun cleanUpTransaction(transactionId: String) {
        // Nothing to clean up in mock implementation
    }
}
