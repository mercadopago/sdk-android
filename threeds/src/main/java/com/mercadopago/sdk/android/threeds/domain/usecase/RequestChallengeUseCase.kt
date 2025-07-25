package com.mercadopago.sdk.android.threeds.domain.usecase

import android.app.Activity
import android.util.Log
import com.mercadopago.sdk.android.threeds.data.model.ThreeDSBody
import com.mercadopago.sdk.android.threeds.domain.adapter.ThreeDSSDKAdapter
import com.mercadopago.sdk.android.threeds.domain.callback.MPThreeDSChallengeDelegate
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticated
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeError
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSDirectoryServer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException
import java.security.GeneralSecurityException

/**
 * Use case for orchestrating the complete 3DS challenge flow.
 * This coordinates between the 3DS SDK adapter and backend authentication.
 */
internal class RequestChallengeUseCase(
    private val authenticateUseCase: AuthenticateUseCase,
    private val threeDSSDKAdapter: ThreeDSSDKAdapter,
) {

    companion object {
        private const val TAG = "RequestChallengeUseCase"
    }

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
        } catch (networkException: IOException) {
            delegate.onError(MPThreeDSChallengeError.fromException(networkException))
        } catch (securityException: GeneralSecurityException) {
            delegate.onError(MPThreeDSChallengeError.fromException(securityException))
        } catch (illegalStateException: IllegalStateException) {
            delegate.onError(MPThreeDSChallengeError.fromException(illegalStateException))
        } finally {
            // Clean up transaction resources
            transactionId?.let { id ->
                try {
                    threeDSSDKAdapter.cleanUpTransaction(id)
                } catch (cleanupException: IOException) {
                    // Log cleanup error but don't propagate it
                    Log.w(TAG, "Failed to cleanup transaction $id", cleanupException)
                } catch (cleanupException: IllegalStateException) {
                    // Log cleanup error but don't propagate it
                    Log.w(TAG, "Failed to cleanup transaction $id", cleanupException)
                }
            }
        }
    }
}
