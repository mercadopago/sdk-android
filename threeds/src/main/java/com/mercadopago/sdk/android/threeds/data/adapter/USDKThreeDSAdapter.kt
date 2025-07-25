package com.mercadopago.sdk.android.threeds.data.adapter

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mercadopago.sdk.android.threeds.domain.adapter.ThreeDSSDKAdapter
import com.mercadopago.sdk.android.threeds.domain.callback.MPThreeDSChallengeDelegate
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticated
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationResponse
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeError
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSDirectoryServer
import com.mercadopago.sdk.android.threeds.domain.model.ThreeDSAuthRequestParameters
import com.usdk.android.UsdkThreeDS2ServiceImpl
import org.emvco.threeds.core.ChallengeParameters
import org.emvco.threeds.core.ChallengeStatusReceiver
import org.emvco.threeds.core.CompletionEvent
import org.emvco.threeds.core.ConfigParameters
import org.emvco.threeds.core.ProtocolErrorEvent
import org.emvco.threeds.core.RuntimeErrorEvent
import org.emvco.threeds.core.ThreeDS2Service
import org.emvco.threeds.core.Transaction
import org.emvco.threeds.core.exceptions.InvalidInputException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Adapter implementation for the uSDK ThreeDS integration.
 * This class integrates the uSDK (Universal SDK) for 3DS authentication functionality.
 *
 * The adapter handles:
 * - Suspended initialization of the uSDK service
 * - Transaction creation and management
 * - Authentication parameter retrieval
 * - Challenge flow execution
 * - Resource cleanup
 */
const val TIMEOUT = 10
internal class USDKThreeDSAdapter(
    private val context: Context,
) : ThreeDSSDKAdapter {
    @Volatile
    private var threeDSService: ThreeDS2Service? = null

    @Volatile
    private var transaction: Transaction? = null

    /**
     * Initializes the uSDK service if not already initialized.
     * This method follows the suspended initialization pattern with broadcast receiver.
     */
    override suspend fun initialize() {
        if (threeDSService != null) return

        threeDSService = suspendCoroutine { continuation ->
            val service: ThreeDS2Service = UsdkThreeDS2ServiceImpl()
            val listener: BroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(
                    context: Context,
                    intent: Intent,
                ) {
                    LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
                    if (!intent.getBooleanExtra(
                            UsdkThreeDS2ServiceImpl.INITIALIZATION_ACTION_EXTRA_SUCCESS,
                            false,
                        )
                    ) {
                        val code = intent.getStringExtra(
                            UsdkThreeDS2ServiceImpl.INITIALIZATION_ACTION_EXTRA_ERROR_CODE,
                        )
                        val type = intent.getStringExtra(
                            UsdkThreeDS2ServiceImpl.INITIALIZATION_ACTION_EXTRA_ERROR_TYPE,
                        )

                        Log.e(
                            "ThreeDSWrapper",
                            "Failed to initialize SDK, code: $code, type: $type",
                        )

                        continuation.resume(null)
                    } else {
                        // Everything is okay, start using service
                        continuation.resume(service)
                    }
                }
            }

            LocalBroadcastManager.getInstance(context).registerReceiver(
                listener,
                IntentFilter(UsdkThreeDS2ServiceImpl.INTENT_INITIALIZATION_ACTION),
            )

            service.initialize(context, ConfigParameters(), null, null)
        }
    }

    override suspend fun createTransaction(directoryServer: MPThreeDSDirectoryServer) {
        transaction = threeDSService?.createTransaction(
            directoryServer.directoryServerID,
            directoryServer.messageVersion,
        )
    }

    override suspend fun getAuthenticationRequestParameters(): ThreeDSAuthRequestParameters? {
        return transaction?.authenticationRequestParameters?.run {
            ThreeDSAuthRequestParameters(
                sdkAppID,
                deviceData,
                sdkEphemeralPublicKey,
                sdkReferenceNumber,
                sdkTransactionID,
            )
        }
    }

    override suspend fun doChallenge(
        activity: Activity,
        authenticationResponse: MPThreeDSAuthenticationResponse,
        delegate: MPThreeDSChallengeDelegate,
    ) {
        val threeDSChallengeParameters = ChallengeParameters().apply {
            this.set3DSServerTransactionID(authenticationResponse.threeDSServerTransID)
            acsRefNumber = authenticationResponse.acsReferenceNumber
            acsSignedContent = authenticationResponse.acsSignedContent
            acsTransactionID = authenticationResponse.acsTransID
        }

        try {
            transaction?.doChallenge(
                activity,
                threeDSChallengeParameters,
                object : ChallengeStatusReceiver {
                    override fun completed(event: CompletionEvent?) {
                        event?.let {
                            delegate.onSuccess(
                                result = MPThreeDSAuthenticated(
                                    authenticationResponse = authenticationResponse,
                                    challengeCompleted = it.transactionStatus == "TRUE",
                                ),
                            )
                        }
                    }

                    override fun protocolError(event: ProtocolErrorEvent?) {
                        event?.let {
                            delegate.onError(
                                error = MPThreeDSChallengeError(
                                    code = it.errorMessage.errorCode ?: "",
                                    message = it.errorMessage.errorDescription ?: "",
                                    details = it.errorMessage.errorDetails ?: "",
                                ),
                            )
                        }
                    }

                    override fun runtimeError(event: RuntimeErrorEvent?) {
                        event?.let {
                            delegate.onError(
                                error = MPThreeDSChallengeError(
                                    code = it.errorCode ?: "",
                                    message = it.errorMessage ?: "",
                                ),
                            )
                        }
                    }

                    override fun cancelled() {
                        delegate.onCancel()
                    }

                    override fun timedout() {
                        delegate.onTimedOut()
                    }
                },
                TIMEOUT,
            )
        } catch (e: InvalidInputException) {
            Exception(
                "[3DS] Error trying to do the challenge - Message: ${e.message}",
            )
        }
    }
}
