package com.mercadopago.sdk.android.coremethods.domain.interactor

import android.app.Activity
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeAuthentication
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeErrorDetail
import com.mercadopago.sdk.android.coremethods.domain.model.ThreeDSChallengeStatus
import com.mercadopago.sdk.android.coremethods.domain.provider.ThreeDSProvider
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSAuthenticationModel
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSChallengeResult
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSWarning
import com.mercadopago.sdk.android.coremethods.domain.usecase.AuthenticateThreeDSChallengeUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.UpdateThreeDSChallengeStatusUseCase
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

private object ThreeDSErrorMessages {
    const val PROVIDER_NOT_AVAILABLE = "3DS provider not available. Please use setThreeDSProvider() method."
    const val FAILED_TO_GET_WARNINGS = "Failed to get 3DS warnings."
    const val CHALLENGE_DATA_NOT_AVAILABLE = "Challenge data not available in authentication response."
    const val FAILED_TO_EXECUTE_CHALLENGE = "Failed to execute 3DS challenge."
    const val ERROR_GETTING_WARNINGS_PREFIX = "Error getting warnings: "
    const val ERROR_AUTHENTICATING_CHALLENGE_PREFIX = "Error authenticating 3DS challenge: "
    const val ERROR_DURING_CHALLENGE_PREFIX = "Error during 3DS challenge: "
    const val FAILED_TO_CLOSE_TRANSACTION_PREFIX = "Failed to close transaction: "
    const val FAILED_TO_CREATE_TRANSACTION_PREFIX = "Failed to create transaction: "
}

private object ThreeDSSuccessMessages {
    const val TRANSACTION_CLOSED = "Transaction closed"
    const val TRANSACTION_CREATED = "Transaction created"
}

private object ThreeDSErrorCodes {
    const val EMPTY = ""
    const val BAD_REQUEST = "400"
}

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
fun CoreMethods.setThreeDSProvider(
    provider: ThreeDSProvider,
) {
    threeDSProvider = provider
}

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
                message = ThreeDSErrorMessages.PROVIDER_NOT_AVAILABLE,
            ),
        )
    }
    return runCatching { threeDSProvider?.getWarnings() }
        .fold(
            onSuccess = { warnings ->
                warnings?.let { Result.Success(it) } ?: Result.Error(
                    ResultError.Request(
                        code = ThreeDSErrorCodes.EMPTY,
                        message = ThreeDSErrorMessages.FAILED_TO_GET_WARNINGS,
                    ),
                )
            },
            onFailure = { throwable ->
                Result.Error(
                    ResultError.Request(
                        code = ThreeDSErrorCodes.EMPTY,
                        message = "${ThreeDSErrorMessages.ERROR_GETTING_WARNINGS_PREFIX}${throwable.message}",
                    ),
                )
            },
        )
}

/**
 * Starts the 3DS challenge flow with the provided challenge ID.
 * This method requires a 3DS provider to be set via setThreeDSProvider.
 *
 * The method will authenticate the challenge using the provided challengeId,
 * retrieve the authentication data from the backend, and execute the challenge flow.
 *
 * @param activity The activity context for displaying the challenge UI
 * @param challengeId The challenge ID received from the backend to authenticate and start the challenge
 * @param timeout The challenge timeout in minutes
 * @return [Result.Success] with ThreeDSChallengeResult if the challenge completed,
 *         [Result.Error] with ResultError if the provider is not available or an error occurred
 *
 * Example:
 * ```kotlin
 * val result = coreMethods.startChallenge(
 *     activity = this,
 *     challengeId = "<challenge id>",
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
 *         // Provider not available or authentication failed
 *     }
 * }
 * ```
 */
suspend fun CoreMethods.startChallenge(
    activity: Activity,
    challengeId: String,
    timeout: Int,
): Result<ThreeDSChallengeResult, ResultError> {
    if (!hasThreeDSProvider()) {
        return Result.Error(
            ResultError.Validation(
                message = ThreeDSErrorMessages.PROVIDER_NOT_AVAILABLE,
            ),
        )
    }
    return executeThreeDSChallenge(activity, challengeId, timeout)
}

private suspend fun CoreMethods.executeThreeDSChallenge(
    activity: Activity,
    challengeId: String,
    timeout: Int,
): Result<ThreeDSChallengeResult, ResultError> =
    runCatching {
        koin.get<AuthenticateThreeDSChallengeUseCase>().invoke(challengeId = challengeId)
    }.fold(
        onSuccess = { authResult -> processChallengeAuthentication(activity, authResult, timeout) },
        onFailure = { throwable ->
            Result.Error(
                ResultError.Request(
                    code = ThreeDSErrorCodes.BAD_REQUEST,
                    message = "${ThreeDSErrorMessages.ERROR_AUTHENTICATING_CHALLENGE_PREFIX}${throwable.message}",
                ),
            )
        },
    )

private suspend fun CoreMethods.processChallengeAuthentication(
    activity: Activity,
    challengeId: String,
    authenticationResult: Result<ThreeDSChallengeAuthentication, ResultError>,
    timeout: Int,
): Result<ThreeDSChallengeResult, ResultError> =
    when (authenticationResult) {
        is Result.Success -> {
            val authentication = authenticationResult.data
            authentication.threeDSAuthenticationModel?.let { challengeData ->
                executeChallengeFlow(activity, challengeId, challengeData, timeout)
            } ?: Result.Error(
                ResultError.Validation(
                    message = ThreeDSErrorMessages.CHALLENGE_DATA_NOT_AVAILABLE,
                ),
            )
        }
        is Result.Error -> authenticationResult
    }

private suspend fun CoreMethods.executeChallengeFlow(
    activity: Activity,
    challengeId: String,
    challengeData: ThreeDSAuthenticationModel,
    timeout: Int,
): Result<ThreeDSChallengeResult, ResultError> {
    return runCatching {
        threeDSProvider?.doChallenge(
            activity = activity,
            authentication = challengeData,
            timeout = timeout,
        )
    }.fold(
        onSuccess = { challengeResult ->
            updateThreeDSChallengeStatus(
                challengeId = challengeId,
                status = challengeResult
            )
            challengeResult?.let { Result.Success(it) } ?: Result.Error(
                ResultError.Request(
                    code = ThreeDSErrorCodes.BAD_REQUEST,
                    message = ThreeDSErrorMessages.FAILED_TO_EXECUTE_CHALLENGE,
                ),
            )
        },
        onFailure = { throwable ->
            Result.Error(
                ResultError.Request(
                    code = ThreeDSErrorCodes.BAD_REQUEST,
                    message = "${ThreeDSErrorMessages.ERROR_DURING_CHALLENGE_PREFIX}${throwable.message}",
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
                message = ThreeDSErrorMessages.PROVIDER_NOT_AVAILABLE,
            ),
        )
    }
    return runCatching { threeDSProvider?.close() }
        .fold(
            onSuccess = { Result.Success(ThreeDSSuccessMessages.TRANSACTION_CLOSED) },
            onFailure = { throwable ->
                Result.Error(
                    ResultError.Validation(
                        message = "${ThreeDSErrorMessages.FAILED_TO_CLOSE_TRANSACTION_PREFIX}${throwable.message}",
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
fun CoreMethods.createTransaction(
    cardToken: CardToken,
): Result<String, ResultError> {
    if (!hasThreeDSProvider()) {
        return Result.Error(
            ResultError.Validation(
                message = ThreeDSErrorMessages.PROVIDER_NOT_AVAILABLE,
            ),
        )
    }
    return runCatching { threeDSProvider?.createTransaction(cardToken.token) }
        .fold(
            onSuccess = { Result.Success(ThreeDSSuccessMessages.TRANSACTION_CREATED) },
            onFailure = { throwable ->
                Result.Error(
                    ResultError.Validation(
                        message = "${ThreeDSErrorMessages.FAILED_TO_CREATE_TRANSACTION_PREFIX}${throwable.message}",
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
internal suspend fun CoreMethods.updateThreeDSChallengeStatus(
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
