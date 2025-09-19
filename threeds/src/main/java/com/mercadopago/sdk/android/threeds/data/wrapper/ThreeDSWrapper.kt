package com.mercadopago.sdk.android.threeds.data.wrapper

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mercadopago.sdk.android.threeds.data.mappers.toChallengeModel
import com.mercadopago.sdk.android.threeds.data.model.MPSeverityResponse
import com.mercadopago.sdk.android.threeds.data.model.MPThreeDSAuthenticationParams
import com.mercadopago.sdk.android.threeds.data.model.MPThreeDSDirectoryServer
import com.mercadopago.sdk.android.threeds.data.model.MPThreeDSWarningResponse
import com.mercadopago.sdk.android.threeds.data.model.ThreeDSAuthRequestParameters
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticated
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeError
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeResult
import com.mercadopago.sdk.android.threeds.domain.model.params.MPThreeDSRequestParams
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
internal class ThreeDSWrapper(private val context: Context) {

    @Volatile
    private lateinit var threeDSService: ThreeDS2Service

    @Volatile
    private lateinit var transaction: Transaction

    /**
     * Initializes the uSDK service if not already initialized.
     * This method follows the suspended initialization pattern with broadcast receiver.
     */
    suspend fun initialize() {
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
                        //TODO - return a erro here
                        continuation.resume(service) // Resume even on error to prevent hanging
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

    fun getWarnings(): List<MPThreeDSWarningResponse> {
        return threeDSService.warnings.map {
            MPThreeDSWarningResponse(
                id = it.id,
                message = it.message,
                severity = MPSeverityResponse.getWaningByGrade(it.severity.ordinal)
            )
        }
    }

    fun createTransaction(paymentMethodId: String) {
        val directoryServer = MPThreeDSDirectoryServer.paymentMethodDirectoryServer(paymentMethodId)
        transaction = threeDSService.createTransaction(
            directoryServer.directoryServerID,
            directoryServer.messageVersion,
        )
    }

    fun getAuthenticationRequestParameters(): MPThreeDSRequestParams {
        return transaction.authenticationRequestParameters.run {
            MPThreeDSRequestParams(
                sdkAppId = sdkAppID,
                deviceData = deviceData,
                sdkEphemeralPublicKey = sdkEphemeralPublicKey,
                sdkReferenceNumber = sdkReferenceNumber,
                sdkTransactionId = sdkTransactionID
            )
        }
    }

    fun close() {
        transaction.close()
    }

    suspend fun doChallenge(
        activity: Activity,
        authenticationParams: MPThreeDSAuthenticationParams,
        timeout: Int
    ): MPThreeDSChallengeResult {
        val threeDSChallengeParameters = ChallengeParameters().apply {
            this.set3DSServerTransactionID(authenticationParams.threeDSServerTransID)
            acsRefNumber = authenticationParams.acsReferenceNumber
            acsSignedContent = authenticationParams.acsSignedContent
            acsTransactionID = authenticationParams.acsTransID
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
                                            challengeResponse = authenticationParams.toChallengeModel(),
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
                                            message = it.errorMessage.errorDescription
                                                ?: "Protocol error occurred",
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
