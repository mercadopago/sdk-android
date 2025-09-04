package com.mercadopago.sdk.android.threeds.data.adapter

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mercadopago.sdk.android.threeds.domain.adapter.ThreeDSWrapper
import com.mercadopago.sdk.android.threeds.domain.mappers.toChallengeModel
import com.mercadopago.sdk.android.threeds.domain.model.MPSeverity
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticated
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeError
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeResult
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSDirectoryServer
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSWarning
import com.mercadopago.sdk.android.threeds.domain.model.params.ThreeDSAuthRequestParameters
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
internal class ThreeDSWrapperImpl(
    private val context: Context,
) : ThreeDSWrapper {

    @Volatile
    private lateinit var threeDSService: ThreeDS2Service

    @Volatile
    private lateinit var transaction: Transaction

    /**
     * Initializes the uSDK service if not already initialized.
     * This method follows the suspended initialization pattern with broadcast receiver.
     */
    override suspend fun initialize() {
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

    override fun getWarnings(): List<MPThreeDSWarning> {
        return threeDSService.warnings.map {
            MPThreeDSWarning(
                id = it.id,
                message = it.message,
                severity = MPSeverity.getWaningByGrade(it.severity.ordinal)
            )
        }
    }

    override fun createTransaction(directoryServer: MPThreeDSDirectoryServer) {
        transaction = threeDSService.createTransaction(
            directoryServer.directoryServerID,
            directoryServer.messageVersion,
        )
    }

    override fun getAuthenticationRequestParameters(): ThreeDSAuthRequestParameters {
        return transaction.authenticationRequestParameters.run {
            ThreeDSAuthRequestParameters(
                sdkAppID,
                deviceData,
                sdkEphemeralPublicKey,
                sdkReferenceNumber,
                sdkTransactionID,
            )
        }
    }

    override fun close() {
        transaction.close()
    }

    override suspend fun doChallenge(
        activity: Activity,
        authenticationResponse: MPThreeDSAuthenticationModel,
        timeout: Int
    ): MPThreeDSChallengeResult {
        val threeDSChallengeParameters = ChallengeParameters().apply {
            this.set3DSServerTransactionID(authenticationResponse.threeDSServerTransID)
            acsRefNumber = authenticationResponse.acsReferenceNumber
            acsSignedContent = authenticationResponse.acsSignedContent
            acsTransactionID = authenticationResponse.acsTransID
        }

        return try {
            suspendCoroutine { continuation ->
                transaction.doChallenge(
                    activity,
                    threeDSChallengeParameters,
                    object : ChallengeStatusReceiver {

                        override fun completed(event: CompletionEvent?) {
                            event?.let {
                                continuation.resume(
                                    MPThreeDSChallengeResult.OnSuccess(
                                        result = MPThreeDSAuthenticated(
                                            challengeResponse = authenticationResponse.toChallengeModel(),
                                            challengeCompleted = it.transactionStatus == "Y",
                                        )
                                    )
                                )
                            } ?: continuation.resume(
                                MPThreeDSChallengeResult.OnError(
                                    error = MPThreeDSChallengeError(
                                        code = "COMPLETION_ERROR",
                                        message = "Completion event is null",
                                    )
                                )
                            )
                        }

                        override fun protocolError(event: ProtocolErrorEvent?) {
                            event?.let {
                                continuation.resume(
                                    MPThreeDSChallengeResult.OnError(
                                        error = MPThreeDSChallengeError(
                                            code = it.errorMessage.errorCode ?: "PROTOCOL_ERROR",
                                            message = it.errorMessage.errorDescription ?: "Protocol error occurred",
                                            details = it.errorMessage.errorDetails ?: "",
                                        )
                                    )
                                )
                            } ?: continuation.resume(
                                MPThreeDSChallengeResult.OnError(
                                    error = MPThreeDSChallengeError(
                                        code = "PROTOCOL_ERROR",
                                        message = "Protocol error event is null",
                                    )
                                )
                            )
                        }

                        override fun runtimeError(event: RuntimeErrorEvent?) {
                            event?.let {
                                continuation.resume(
                                    MPThreeDSChallengeResult.OnError(
                                        error = MPThreeDSChallengeError(
                                            code = it.errorCode ?: "RUNTIME_ERROR",
                                            message = it.errorMessage ?: "Runtime error occurred",
                                        )
                                    )
                                )
                            } ?: continuation.resume(
                                MPThreeDSChallengeResult.OnError(
                                    error = MPThreeDSChallengeError(
                                        code = "RUNTIME_ERROR",
                                        message = "Runtime error event is null",
                                    )
                                )
                            )
                        }

                        override fun cancelled() {
                            continuation.resume(MPThreeDSChallengeResult.OnCancel)
                        }

                        override fun timedout() {
                            continuation.resume(MPThreeDSChallengeResult.OnTimedOut)
                        }
                    },
                    timeout,
                )
            }
        } catch (e: InvalidInputException) {
            MPThreeDSChallengeResult.OnError(
                MPThreeDSChallengeError(
                    code = "INVALID_INPUT",
                    message = "[3DS] Error trying to do the challenge - Message: ${e.message}"
                )
            )
        }
    }
}
