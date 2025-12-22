package com.mercadopago.sdk.android.coremethods.domain.interactor

import android.app.Activity
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeAuthentication
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeErrorDetail
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeStatus
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSDeviceData
import com.mercadopago.sdk.android.coremethods.domain.model.params.DeviceRenderOptionsParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.EphemeralPublicKeyParams
import com.mercadopago.sdk.android.coremethods.domain.provider.ThreeDSProvider
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSAuthenticationModel
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSChallengeResult
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSWarning
import com.mercadopago.sdk.android.coremethods.domain.usecase.AuthenticateThreeDSChallengeUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.SaveThreeDSDeviceDataUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.UpdateThreeDSChallengeStatusUseCase
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

/**
 * Private property to hold the 3DS provider instance.
 * This is set by the client application through setThreeDSProvider method.
 */
private var threeDSProvider: ThreeDSProvider? = null

/**
 * Sets the 3DS provider implementation for this CoreMethods instance.
 * This method should be called once during application initialization to enable 3DS functionality.
 *
 * @param provider The ThreeDSProvider implementation to use for 3DS operations
 *
 * Example:
 * ```kotlin
 * val coreMethods = MercadoPagoSDK.getInstance().coreMethods
 * val threeDS = MPThreeDS.getInstance(context)
 * coreMethods.setThreeDSProvider(MPThreeDSProviderAdapter(threeDS))
 * ```
 */
fun CoreMethods.setThreeDSProvider(provider: ThreeDSProvider) {
    threeDSProvider = provider
}

/**
 * Checks if a 3DS provider is currently configured and available.
 *
 * This internal helper method is used to verify that a [ThreeDSProvider]
 * has been set via [setThreeDSProvider] before attempting any 3DS operations.
 *
 * @return `true` if a provider is set and available, `false` otherwise
 */
private fun CoreMethods.hasThreeDSProvider(): Boolean = threeDSProvider != null

/**
 * Retrieves the list of security warnings from the 3DS SDK.
 * These warnings indicate potential security issues or configuration problems
 * that may affect the 3DS authentication process.
 *
 * This method requires a 3DS provider to be set via [setThreeDSProvider].
 *
 * @return [Result.Success] with a list of [ThreeDSWarning] if the operation completed successfully,
 *         [Result.Error] with [ResultError] if the provider is not available or an error occurred
 *
 * Example:
 * ```kotlin
 * val result = coreMethods.getWarnings()
 * when (result) {
 *     is Result.Success -> {
 *         val warnings = result.data
 *         warnings.forEach { warning ->
 *             Log.w("3DS", "Warning: ${warning.message}")
 *         }
 *     }
 *     is Result.Error -> {
 *         // Handle error
 *     }
 * }
 * ```
 */
fun CoreMethods.getWarnings(): Result<List<ThreeDSWarning>, ResultError> {
    if (!hasThreeDSProvider()) {
        return Result.Error(
            ResultError.Validation(
                message = "3DS provider not available. Please set the provider using setThreeDSProvider() method.",
            ),
        )
    }
    return runCatching { threeDSProvider?.getWarnings() }
        .fold(
            onSuccess = { warnings ->
                warnings?.let { Result.Success(it) } ?: Result.Error(
                    ResultError.Request(
                        code = "",
                        message = "Failed to get 3DS warnings.",
                    ),
                )
            },
            onFailure = { throwable ->
                Result.Error(
                    ResultError.Request(
                        code = "",
                        message = "Error getting warnings: ${throwable.message}",
                    ),
                )
            },
        )
}

/**
 * Starts the 3DS challenge flow with the provided authentication response.
 * This method requires a 3DS provider to be set via setThreeDSProvider.
 *
 * @param activity The activity context for displaying the challenge UI
 * @param authentication The authentication response received from your backend
 * @param timeout The challenge timeout in minutes (default: 10)
 * @return [Result.Success] with ThreeDSChallengeResult if the challenge completed,
 *         [Result.Error] with ResultError if the provider is not available or an error occurred
 *
 * Example:
 * ```kotlin
 * val result = coreMethods.startThreeDSChallenge(
 *     activity = this,
 *     authentication = authenticationModel,
 *     timeout = 10
 * )
 * when (result) {
 *     is Result.Success -> {
 *         when (val challengeResult = result.data) {
 *             is ThreeDSChallengeResult.OnSuccess -> {
 *                 // Challenge completed successfully
 *                 val authenticated = challengeResult.result
 *             }
 *             is ThreeDSChallengeResult.OnError -> {
 *                 // Challenge failed
 *             }
 *             is ThreeDSChallengeResult.OnCancel -> {
 *                 // User cancelled
 *             }
 *             is ThreeDSChallengeResult.OnTimedOut -> {
 *                 // Challenge timed out
 *             }
 *         }
 *     }
 *     is Result.Error -> {
 *         // Provider not available
 *     }
 * }
 * ```
 */
suspend fun CoreMethods.startChallenge(
    activity: Activity,
    authentication: ThreeDSAuthenticationModel,
    timeout: Int = 10,
): Result<ThreeDSChallengeResult, ResultError> {
    if (!hasThreeDSProvider()) {
        return Result.Error(
            ResultError.Validation(
                message = "3DS provider not available. Please set the provider using setThreeDSProvider() method.",
            ),
        )
    }
    return runCatching {
        threeDSProvider?.doChallenge(
            activity = activity,
            authentication = authentication,
            timeout = timeout,
        )
    }.fold(
        onSuccess = { challengeResult ->
            challengeResult?.let { Result.Success(it) } ?: Result.Error(
                ResultError.Request(
                    code = "",
                    message = "Failed to execute 3DS challenge.",
                ),
            )
        },
        onFailure = { throwable ->
            Result.Error(
                ResultError.Request(
                    code = "",
                    message = "Error during 3DS challenge: ${throwable.message}",
                ),
            )
        },
    )
}

/**
 * Closes the current 3DS transaction and releases associated resources.
 * This method should be called after the 3DS authentication flow is complete,
 * regardless of whether it succeeded or failed.
 *
 * Calling this method ensures proper cleanup of the 3DS SDK resources
 * and prepares the provider for a new transaction if needed.
 *
 * @return [Result.Success] with a confirmation message if the transaction was closed successfully,
 *         [Result.Error] with [ResultError] if an error occurred during cleanup
 *
 * Example:
 * ```kotlin
 * val result = coreMethods.close()
 * when (result) {
 *     is Result.Success -> {
 *         Log.d("3DS", "Transaction closed successfully")
 *     }
 *     is Result.Error -> {
 *         Log.e("3DS", "Failed to close transaction: ${result.error.message}")
 *     }
 * }
 * ```
 */
fun CoreMethods.close(): Result<String, ResultError> {
    if (!hasThreeDSProvider()) {
        return Result.Error(
            ResultError.Validation(
                message = "3DS provider not available. Please set the provider using setThreeDSProvider() method.",
            ),
        )
    }
    return runCatching { threeDSProvider?.close() }
        .fold(
            onSuccess = { Result.Success("Transaction closed") },
            onFailure = { throwable ->
                Result.Error(
                    ResultError.Validation(
                        message = "Failed to close transaction: ${throwable.message}",
                    ),
                )
            },
        )
}

/**
 * Creates a new 3DS transaction using the provided card token.
 * This method initializes the 3DS authentication process for the specified card.
 *
 * This method requires a 3DS provider to be set via [setThreeDSProvider].
 * The transaction must be created before calling [startChallenge] to execute
 * the 3DS challenge flow.
 *
 * @param cardToken The [CardToken] containing the tokenized card information
 * @return [Result.Success] with a confirmation message if the transaction was created successfully,
 *         [Result.Error] with [ResultError] if the provider is not available or an error occurred
 *
 * Example:
 * ```kotlin
 * val cardToken = CardToken(token = "card_token_123")
 * val result = coreMethods.createTransaction(cardToken)
 * when (result) {
 *     is Result.Success -> {
 *         Log.d("3DS", "Transaction created successfully")
 *         // Proceed with 3DS authentication
 *     }
 *     is Result.Error -> {
 *         Log.e("3DS", "Failed to create transaction: ${result.error.message}")
 *     }
 * }
 * ```
 */
fun CoreMethods.createTransaction(cardToken: CardToken): Result<String, ResultError> {
    if (!hasThreeDSProvider()) {
        return Result.Error(
            ResultError.Validation(
                message = "3DS provider not available. Please set the provider using setThreeDSProvider() method.",
            ),
        )
    }
    return runCatching { threeDSProvider?.createTransaction(cardToken.token) }
        .fold(
            onSuccess = { Result.Success("Transaction created") },
            onFailure = { throwable ->
                Result.Error(
                    ResultError.Validation(
                        message = "Failed to create transaction: ${throwable.message}",
                    ),
                )
            },
        )
}

/**
 * Saves the 3DS device data collected by the SDK to initiate the authentication process.
 *
 * This method sends the device data to the backend for 3DS authentication.
 * It should be called after creating a transaction and before starting the challenge flow.
 *
 * @param deviceData The [ThreeDSDeviceData] containing all required device information
 * @return [Result.Success] with [Unit] if the device data was saved successfully,
 *         [Result.Error] with [ResultError] if an error occurred during the operation
 *
 * Example:
 * ```kotlin
 * val deviceData = ThreeDSDeviceData(
 *     appId = "com.mercadopago.checkout",
 *     integratorSdkVersion = "2.2.0",
 *     threeDsSdkVersion = "1.0.0",
 *     cardTokenId = cardToken.token,
 *     deviceRenderOptions = DeviceRenderOptions(
 *         sdkInterface = "Native",
 *         uiTypes = listOf("01", "02", "03", "04", "05")
 *     ),
 *     encData = authParams.deviceData,
 *     ephemPubKey = EphemeralPublicKey(
 *         curve = "P-256",
 *         keyType = "EC",
 *         x = ephemeralKeyX,
 *         y = ephemeralKeyY
 *     ),
 *     maxTimeout = 5,
 *     protocolVersion = "2.2.0",
 *     referenceNumber = authParams.sdkReferenceNumber,
 *     transId = authParams.sdkTransactionId
 * )
 *
 * val result = coreMethods.saveThreeDSDeviceData(deviceData)
 * when (result) {
 *     is Result.Success -> {
 *         Log.d("3DS", "Device data saved successfully")
 *         // Proceed with authentication
 *     }
 *     is Result.Error -> {
 *         Log.e("3DS", "Failed to save device data: ${result.error.message}")
 *     }
 * }
 * ```
 *
 * @see ThreeDSDeviceData
 * @see Result
 * @see ResultError
 */
private suspend fun CoreMethods.saveThreeDSDeviceData(
    deviceData: ThreeDSDeviceData,
): Result<Unit, ResultError> {
    return runCatching {
        koin.get<SaveThreeDSDeviceDataUseCase>().invoke(
            appId = deviceData.appId,
            integratorSdkVersion = deviceData.integratorSdkVersion,
            threeDsSdkVersion = deviceData.threeDsSdkVersion,
            cardTokenId = deviceData.cardTokenId,
            deviceRenderOptions = DeviceRenderOptionsParams(
                sdkInterface = deviceData.deviceRenderOptions.sdkInterface,
                uiTypes = deviceData.deviceRenderOptions.uiTypes,
            ),
            encData = deviceData.encData,
            ephemPubKey = EphemeralPublicKeyParams(
                curve = deviceData.ephemPubKey.curve,
                keyType = deviceData.ephemPubKey.keyType,
                x = deviceData.ephemPubKey.x,
                y = deviceData.ephemPubKey.y,
            ),
            maxTimeout = deviceData.maxTimeout,
            protocolVersion = deviceData.protocolVersion,
            referenceNumber = deviceData.referenceNumber,
            transId = deviceData.transId,
        )
    }.fold(
        onSuccess = { result -> result },
        onFailure = { throwable ->
            Result.Error(
                ResultError.Request(
                    code = "",
                    message = "Error saving 3DS device data: ${throwable.message}",
                ),
            )
        },
    )
}

/**
 * Authenticates a 3DS challenge and returns the authentication status along with
 * the data needed to display the challenge to the user (if required).
 *
 * This method sends the challenge ID to the backend to authenticate the 3DS challenge.
 * The response indicates whether the authentication was successful or if a challenge
 * needs to be displayed to the user.
 *
 * @param challengeId The unique identifier of the 3DS challenge to authenticate
 * @return [Result.Success] with [ThreeDSChallengeAuthentication] containing the authentication
 *         status and optional challenge data,
 *         [Result.Error] with [ResultError] if the authentication failed
 *
 * Example:
 * ```kotlin
 * val result = coreMethods.authenticateThreeDSChallenge(
 *     challengeId = "challenge_abc123"
 * )
 *
 * when (result) {
 *     is Result.Success -> {
 *         val authentication = result.data
 *         when (authentication.status) {
 *             "authenticated" -> {
 *                 // Authentication successful, proceed with payment
 *                 Log.d("3DS", "Authentication successful without challenge")
 *             }
 *             "challenge" -> {
 *                 // Challenge required, display challenge UI
 *                 val challengeData = authentication.data
 *                 Log.d("3DS", "Challenge required: ${challengeData?.acsTransId}")
 *             }
 *         }
 *     }
 *     is Result.Error -> {
 *         Log.e("3DS", "Authentication failed: ${result.error.message}")
 *     }
 * }
 * ```
 *
 * @see ThreeDSChallengeAuthentication
 * @see Result
 * @see ResultError
 */
private suspend fun CoreMethods.authenticateThreeDSChallenge(
    challengeId: String,
): Result<ThreeDSChallengeAuthentication, ResultError> {
    return runCatching {
        koin.get<AuthenticateThreeDSChallengeUseCase>().invoke(
            challengeId = challengeId,
        )
    }.fold(
        onSuccess = { result -> result },
        onFailure = { throwable ->
            Result.Error(
                ResultError.Request(
                    code = "",
                    message = "Error authenticating 3DS challenge: ${throwable.message}",
                ),
            )
        },
    )
}

/**
 * Updates the status of a 3DS challenge after user interaction.
 *
 * This method sends the challenge result (completion, cancellation, error, or timeout)
 * to the backend. It should be called after the challenge flow completes to update
 * the server with the final status.
 *
 * @param challengeId The unique identifier of the 3DS challenge to update
 * @param status The status of the challenge. Valid values are:
 *               - [ThreeDSChallengeStatus.COMPLETED]: Challenge completed successfully
 *               - [ThreeDSChallengeStatus.CANCELLED]: Challenge cancelled by the user
 *               - [ThreeDSChallengeStatus.ERROR]: Error during challenge execution
 *               - [ThreeDSChallengeStatus.TIMEOUT]: Challenge expired due to timeout
 * @param errorDetail Optional error details when status is [ThreeDSChallengeStatus.ERROR].
 *                    This parameter is required when status is ERROR.
 * @return [Result.Success] with [Unit] if the status was updated successfully,
 *         [Result.Error] with [ResultError] if the operation failed
 *
 * Example:
 * ```kotlin
 * // Challenge completed successfully
 * val result = coreMethods.updateThreeDSChallengeStatus(
 *     challengeId = "challenge_abc123",
 *     status = ThreeDSChallengeStatus.COMPLETED
 * )
 *
 * // Challenge failed with error
 * val errorResult = coreMethods.updateThreeDSChallengeStatus(
 *     challengeId = "challenge_abc123",
 *     status = ThreeDSChallengeStatus.ERROR,
 *     errorDetail = ThreeDSChallengeErrorDetail(
 *         type = "PROTOCOL_ERROR",
 *         code = "301"
 *     )
 * )
 *
 * when (result) {
 *     is Result.Success -> {
 *         Log.d("3DS", "Challenge status updated successfully")
 *     }
 *     is Result.Error -> {
 *         Log.e("3DS", "Failed to update status: ${result.error.message}")
 *     }
 * }
 * ```
 *
 * @see ThreeDSChallengeStatus
 * @see ThreeDSChallengeErrorDetail
 * @see Result
 * @see ResultError
 */
private suspend fun CoreMethods.updateThreeDSChallengeStatus(
    challengeId: String,
    status: ThreeDSChallengeStatus,
    errorDetail: ThreeDSChallengeErrorDetail? = null,
): Result<Unit, ResultError> {
    return runCatching {
        koin.get<UpdateThreeDSChallengeStatusUseCase>().invoke(
            challengeId = challengeId,
            status = status,
            errorDetail = errorDetail,
        )
    }.fold(
        onSuccess = { result -> result },
        onFailure = { throwable ->
            Result.Error(
                ResultError.Request(
                    code = "",
                    message = "Error updating 3DS challenge status: ${throwable.message}",
                ),
            )
        },
    )
}
