package com.mercadopago.sdk.android.threeds.domain.usecase

import android.app.Activity
import com.mercadopago.sdk.android.threeds.data.model.ThreeDSBody
import com.mercadopago.sdk.android.threeds.domain.adapter.ThreeDSSDKAdapter
import com.mercadopago.sdk.android.threeds.domain.callback.MPThreeDSChallengeDelegate
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticated
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeError
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSDirectoryServer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first

/**
 * Use case for orchestrating the complete 3DS challenge flow.
 * This coordinates between the 3DS SDK adapter and backend authentication.
 */
internal class RequestChallengeUseCase(
    private val authenticateUseCase: AuthenticateUseCase,
    private val threeDSSDKAdapter: ThreeDSSDKAdapter,
) {

    /**
     * Executes the complete 3DS challenge flow.
     *
     * @param activity The activity context for displaying challenge UI
     * @param cardToken The card token to authenticate
     * @param directoryServer The directory server to use (determines card brand)
     * @param delegate Callback for receiving results
     */
    suspend operator fun invoke(
        activity: Activity,
        cardToken: String,
        directoryServer: MPThreeDSDirectoryServer,
        delegate: MPThreeDSChallengeDelegate,
    ) {
        var transactionId: String? = null

        try {
            // Step 1: Create transaction
            transactionId = threeDSSDKAdapter.createTransaction(directoryServer)

            // Step 2: Get authentication request parameters
            val authRequestParams = threeDSSDKAdapter.getAuthenticationRequestParameters(transactionId)

            // Step 3: Create request body and authenticate with backend
            val threeDSBody = ThreeDSBody.create(cardToken, authRequestParams)
            val authResponse = authenticateUseCase(threeDSBody)
                .catch { error ->
                    delegate.onError(MPThreeDSChallengeError.fromException(error))
                    return@catch
                }
                .first()

            // Step 4: Check authentication response
            when (authResponse.response) {
                "CHALLENGE" -> {
                    // Step 5: Perform challenge if required
                    threeDSSDKAdapter.doChallenge(activity, transactionId, authResponse, delegate)
                }
                "AUTHORIZED" -> {
                    // Authentication successful without challenge
                    delegate.onSuccess(
                        MPThreeDSAuthenticated(
                            authenticationResponse = authResponse,
                            challengeCompleted = false,
                            transactionId = transactionId
                        )
                    )
                }
                else -> {
                    // Authentication failed
                    delegate.onError(
                        MPThreeDSChallengeError.authenticationFailed(
                            "Authentication response: ${authResponse.response}"
                        )
                    )
                }
            }
        } catch (exception: Exception) {
            delegate.onError(MPThreeDSChallengeError.fromException(exception))
        } finally {
            // Clean up transaction resources
            transactionId?.let {
                try {
                    threeDSSDKAdapter.cleanUpTransaction(it)
                } catch (e: Exception) {
                    // Log cleanup error but don't propagate it
                }
            }
        }
    }
}
