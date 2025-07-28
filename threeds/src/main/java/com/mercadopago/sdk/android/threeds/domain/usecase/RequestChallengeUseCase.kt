package com.mercadopago.sdk.android.threeds.domain.usecase

import android.app.Activity
import com.mercadopago.sdk.android.threeds.domain.adapter.ThreeDSSDKAdapter
import com.mercadopago.sdk.android.threeds.domain.callback.MPThreeDSChallengeDelegate
import com.mercadopago.sdk.android.threeds.domain.mappers.toChallengeModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticated
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeError
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSDirectoryServer
import com.mercadopago.sdk.android.threeds.domain.model.params.ThreeDSAuthenticationParams
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
    /**
     * Executes the complete 3DS challenge flow.
     *
     * @param activity The activity context for displaying challenge UI
     * @param cardToken The card token to authenticate
     * @param paymentMethodId The paymentMethods Id
     * @param delegate Callback for receiving results
     * @param timeout Challenge time out limit
     */
    suspend operator fun invoke(
        activity: Activity,
        cardToken: String,
        paymentMethodId: String,
        delegate: MPThreeDSChallengeDelegate,
        timeout: Int
    ) {
        try {
            // Step 1: Init the adapter instance
            threeDSSDKAdapter.initialize()

            // Step 2: Create transaction
            threeDSSDKAdapter.createTransaction(
                directoryServer = MPThreeDSDirectoryServer.paymentMethodDirectoryServer(
                    paymentMethodId
                )
            )

            // Step 3: Create request body and authenticate with backend
            val authResponse = threeDSSDKAdapter.getAuthenticationRequestParameters()?.let {
                authenticateUseCase(
                    ThreeDSAuthenticationParams(
                        token = cardToken,
                        sdkAppId = it.sdkAppId,
                        sdkEncData = it.deviceData,
                        sdkEphemPubKey = it.sdkEphemeralPublicKey,
                        sdkMaxTimeout = timeout.toString(),
                        sdkReferenceNumber = it.sdkReferenceNumber,
                        sdkTransId = it.sdkTransactionId
                    )
                ).catch { error ->
                    delegate.onError(MPThreeDSChallengeError.fromException(error))
                    return@catch
                }.first()
            }

            // Step 4: Check authentication response
            when (authResponse?.response) {
                "CHALLENGE" -> {
                    // Step 5: Perform challenge if required
                    threeDSSDKAdapter.doChallenge(activity, authResponse, delegate, timeout)
                }

                "AUTHORIZED" -> {
                    // Authentication successful without challenge
                    delegate.onSuccess(
                        MPThreeDSAuthenticated(
                            challengeResponse = authResponse.toChallengeModel(),
                            challengeCompleted = false,
                        ),
                    )
                }

                else -> {
                    // Authentication failed
                    delegate.onError(
                        MPThreeDSChallengeError.authenticationFailed(
                            "Authentication response: ${authResponse?.response}",
                        ),
                    )
                }
            }
        } catch (networkException: IOException) {
            delegate.onError(MPThreeDSChallengeError.fromException(networkException))
        } catch (securityException: GeneralSecurityException) {
            delegate.onError(MPThreeDSChallengeError.fromException(securityException))
        } catch (illegalStateException: IllegalStateException) {
            delegate.onError(MPThreeDSChallengeError.fromException(illegalStateException))
        }
    }
}
