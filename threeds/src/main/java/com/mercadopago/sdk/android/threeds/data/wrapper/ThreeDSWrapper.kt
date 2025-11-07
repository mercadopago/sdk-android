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

const val ERROR_RUNTIME: String = "RUNTIME_ERROR"
const val ERROR_PROTOCOL: String = "PROTOCOL_ERROR"
const val ERROR_COMPLETION: String = "COMPLETION_ERROR"
const val ERROR_INVALID_INPUT: String = "INVALID_INPUT"

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

    /**
     * Retrieves warnings from the ThreeDS service initialization.
     * These warnings contain information about potential issues or configuration problems.
     *
     * @return List of warnings from the 3DS SDK
     */
    fun getWarnings(): List<MPThreeDSWarningResponse> {
        return threeDSService.warnings.map {
            MPThreeDSWarningResponse(
                id = it.id,
                message = it.message,
                severity = MPSeverityResponse.getWaningByGrade(it.severity.ordinal),
            )
        }
    }

    /**
     * Creates a new 3DS transaction for the specified payment method.
     * This transaction will be used for subsequent authentication and challenge operations.
     *
     * @param paymentMethodId The payment method ID (e.g., "visa", "mastercard") to create transaction for
     */
    fun createTransaction(paymentMethodId: String) {
        val directoryServer = MPThreeDSDirectoryServer.paymentMethodDirectoryServer(paymentMethodId)
        transaction = threeDSService.createTransaction(
            directoryServer.directoryServerID,
            directoryServer.messageVersion,
        )
    }

    /**
     * Retrieves the authentication request parameters from the current transaction.
     * These parameters are required to perform the authentication request to the backend.
     *
     * @return Authentication request parameters containing SDK information needed for 3DS flow
     */
    fun getAuthenticationRequestParameters(): MPThreeDSRequestParams {
        return transaction.authenticationRequestParameters.run {
            MPThreeDSRequestParams(
                sdkAppId = sdkAppID,
                deviceData = deviceData,
                sdkEphemeralPublicKey = sdkEphemeralPublicKey,
                sdkReferenceNumber = sdkReferenceNumber,
                sdkTransactionId = sdkTransactionID,
            )
        }
    }

    /**
     * Closes the current 3DS transaction and releases associated resources.
     * This should be called when the 3DS flow is completed or cancelled.
     */
    fun close() {
        transaction.close()
    }

    /**
     * Performs the 3DS challenge flow with the provided authentication parameters.
     * This method handles the challenge UI presentation and user interaction.
     *
     * @param activity The activity context where the challenge UI will be displayed
     * @param authenticationParams Authentication parameters received from the backend
     * @param timeout Challenge timeout in minutes (default: 10)
     * @return Challenge result indicating success, error, cancellation, or timeout
     */
    suspend fun doChallenge(
        activity: Activity,
        authenticationParams: MPThreeDSAuthenticationParams,
        timeout: Int,
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
                                        ),
                                    ),
                                )
                            } ?: continuation.resume(
                                MPThreeDSChallengeResult.OnError(
                                    error = MPThreeDSChallengeError(
                                        code = ERROR_COMPLETION,
                                        message = "Completion event is null",
                                    ),
                                ),
                            )
                        }

                        override fun protocolError(event: ProtocolErrorEvent?) {
                            event?.let {
                                continuation.resume(
                                    MPThreeDSChallengeResult.OnError(
                                        error = MPThreeDSChallengeError(
                                            code = it.errorMessage.errorCode ?: ERROR_PROTOCOL,
                                            message = it.errorMessage.errorDescription
                                                ?: "Protocol error occurred",
                                            details = it.errorMessage.errorDetails ?: "",
                                        ),
                                    ),
                                )
                            } ?: continuation.resume(
                                MPThreeDSChallengeResult.OnError(
                                    error = MPThreeDSChallengeError(
                                        code = ERROR_PROTOCOL,
                                        message = "Protocol error event is null",
                                    ),
                                ),
                            )
                        }

                        override fun runtimeError(event: RuntimeErrorEvent?) {
                            event?.let {
                                continuation.resume(
                                    MPThreeDSChallengeResult.OnError(
                                        error = MPThreeDSChallengeError(
                                            code = it.errorCode ?: ERROR_RUNTIME,
                                            message = it.errorMessage ?: "Runtime error occurred",
                                        ),
                                    ),
                                )
                            } ?: continuation.resume(
                                MPThreeDSChallengeResult.OnError(
                                    error = MPThreeDSChallengeError(
                                        code = ERROR_RUNTIME,
                                        message = "Runtime error event is null",
                                    ),
                                ),
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
                    code = ERROR_INVALID_INPUT,
                    message = "[3DS] Error trying to do the challenge - Message: ${e.message}",
                ),
            )
        }
    }
}
