@file:Suppress("TooManyFunctions")

package com.mercadopago.sdk.android.coremethods.domain.interactor

import android.app.Activity
import com.mercadopago.sdk.android.coremethods.data.remote.mappers.toParams
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.DeviceRenderOptions
import com.mercadopago.sdk.android.coremethods.domain.model.EphemeralPublicKey
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.fromJson
import com.mercadopago.sdk.android.coremethods.domain.provider.ThreeDSProvider
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSAuthenticationModel
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSChallengeResult
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSRequestParams
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSWarning
import com.mercadopago.sdk.android.coremethods.domain.usecase.SaveThreeDSDeviceDataUseCase
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.domain.utils.flatMap
import com.mercadopago.sdk.android.coremethods.domain.utils.map
import com.mercadopago.sdk.android.coremethods.domain.utils.suspendFlatMap

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
fun CoreMethods.createTransaction(
    cardToken: CardToken,
): Result<String, ResultError> {
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
 * Retrieves the authentication request parameters for the current 3DS transaction.
 * These parameters should be sent to your backend for 3DS authentication.
 *
 * This method requires a 3DS provider to be set via [setThreeDSProvider] and
 * a transaction to be created via [createTransaction] before calling this method.
 *
 * @return [Result.Success] with [ThreeDSRequestParams] containing the authentication parameters,
 *         [Result.Error] with [ResultError] if the provider is not available, no transaction exists,
 *         or an error occurred
 *
 * Example:
 * ```kotlin
 * // First create a transaction
 * coreMethods.createTransaction(cardToken)
 *
 * // Then get authentication parameters
 * val result = coreMethods.getAuthenticationRequestParameters()
 * when (result) {
 *     is Result.Success -> {
 *         val params = result.data
 *         // Send params to your backend for 3DS authentication
 *         // params.sdkAppId
 *         // params.deviceData
 *         // params.sdkEphemeralPublicKey
 *         // params.sdkReferenceNumber
 *         // params.sdkTransactionId
 *     }
 *     is Result.Error -> {
 *         Log.e("3DS", "Failed to get auth params: ${result.error.message}")
 *     }
 * }
 * ```
 *
 * @see ThreeDSRequestParams
 * @see createTransaction
 * @see Result
 * @see ResultError
 */
fun CoreMethods.getAuthenticationRequestParameters(): Result<ThreeDSRequestParams, ResultError> {
    if (!hasThreeDSProvider()) {
        return Result.Error(
            ResultError.Validation(
                message = "3DS provider not available. Please set the provider using setThreeDSProvider() method.",
            ),
        )
    }
    return runCatching { threeDSProvider?.getAuthenticationRequestParameters() }
        .fold(
            onSuccess = { params ->
                params?.let { Result.Success(it) } ?: Result.Error(
                    ResultError.Request(
                        code = "",
                        message = "Failed to get authentication request parameters. " +
                            "Make sure a transaction was created.",
                    ),
                )
            },
            onFailure = { throwable ->
                Result.Error(
                    ResultError.Request(
                        code = "",
                        message = "Error getting authentication request parameters: ${throwable.message}",
                    ),
                )
            },
        )
}

private fun CoreMethods.hasThreeDSProvider(): Boolean = threeDSProvider != null

/**
 * Validates that the 3DS provider is available and returns its SDK version.
 *
 * This is an early check function used to ensure the provider is configured
 * before performing any 3DS operations.
 *
 * @return [Result.Success] with the SDK version string if provider is available,
 *         [Result.Error] with [ResultError.Validation] if provider is not set
 */
private fun CoreMethods.validateProvider(): Result<String, ResultError> {
    val sdkVersion = threeDSProvider?.sdkVersion
        ?: return Result.Error(
            ResultError.Validation(
                message = "3DS provider not available. Please set the provider using setThreeDSProvider() method.",
            ),
        )
    return Result.Success(sdkVersion)
}

/**
 * Parses the ephemeral public key from its JSON string representation.
 *
 * @param jsonKey The JSON string containing the ephemeral public key data
 * @return [Result.Success] with the parsed [EphemeralPublicKey] if parsing succeeds,
 *         [Result.Error] with [ResultError.Validation] if parsing fails
 */
private fun parseEphemeralKey(
    jsonKey: String,
): Result<EphemeralPublicKey, ResultError> {
    val ephemeralPublicKey = EphemeralPublicKey.fromJson(jsonKey)
        ?: return Result.Error(
            ResultError.Validation(
                message = "Failed to parse ephemeral public key from 3DS SDK.",
            ),
        )
    return Result.Success(ephemeralPublicKey)
}

/**
 * Data class to hold the intermediate state needed to execute the device data save.
 */
private data class DeviceDataContext(
    val sdkVersion: String,
    val parameters: ThreeDSRequestParams,
    val ephemeralKey: EphemeralPublicKey,
)

/**
 * Executes the device data save operation using the provided context.
 *
 * @param cardToken The card token containing the tokenized card information
 * @param context The [DeviceDataContext] containing all required data for the operation
 * @return [Result.Success] with [Unit] if the save operation succeeds,
 *         [Result.Error] with [ResultError] if the operation fails
 */
private suspend fun CoreMethods.executeDeviceDataSave(
    cardToken: CardToken,
    context: DeviceDataContext,
): Result<Unit, ResultError> {
    val deviceRenderOptions = DeviceRenderOptions(
        sdkInterface = SDK_INTERFACE_NATIVE,
        uiTypes = DEFAULT_UI_TYPES,
    )
    return runCatching {
        koin.get<SaveThreeDSDeviceDataUseCase>().invoke(
            appId = context.parameters.sdkAppId,
            integratorSdkVersion = INTEGRATOR_SDK_VERSION,
            threeDsSdkVersion = context.sdkVersion,
            cardTokenId = cardToken.token,
            deviceRenderOptions = deviceRenderOptions.toParams(),
            encData = context.parameters.deviceData,
            ephemPubKey = context.ephemeralKey.toParams(),
            maxTimeout = DEFAULT_MAX_TIMEOUT,
            protocolVersion = PROTOCOL_VERSION,
            referenceNumber = context.parameters.sdkReferenceNumber,
            transId = context.parameters.sdkTransactionId,
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
 * Saves the 3DS device data collected by the SDK to initiate the authentication process.
 *
 * This method orchestrates the following operations using functional composition:
 * 1. Validates the 3DS provider is available
 * 2. Creates a transaction with the card token
 * 3. Gets authentication request parameters
 * 4. Parses the ephemeral public key
 * 5. Executes the device data save operation
 *
 * @param cardToken The [CardToken] containing the tokenized card information
 * @return [Result.Success] with [Unit] if the device data was saved successfully,
 *         [Result.Error] with [ResultError] if an error occurred during any operation
 *
 * Example:
 * ```kotlin
 * val result = coreMethods.saveThreeDSDeviceData(cardToken)
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
 * @see CardToken
 * @see Result
 * @see ResultError
 */
internal suspend fun CoreMethods.saveThreeDSDeviceData(
    cardToken: CardToken,
): Result<Unit, ResultError> {
    return validateProvider()
        .flatMap { sdkVersion ->
            createTransaction(cardToken).map { sdkVersion }
        }
        .flatMap { sdkVersion ->
            getAuthenticationRequestParameters().map { params ->
                Pair(sdkVersion, params)
            }
        }
        .flatMap { (sdkVersion, params) ->
            parseEphemeralKey(params.sdkEphemeralPublicKey).map { ephemeralKey ->
                DeviceDataContext(
                    sdkVersion = sdkVersion,
                    parameters = params,
                    ephemeralKey = ephemeralKey,
                )
            }
        }
        .suspendFlatMap { context ->
            executeDeviceDataSave(cardToken, context)
        }
}

private const val DEFAULT_MAX_TIMEOUT = 5
private const val INTEGRATOR_SDK_VERSION = "2.2.0"
private const val SDK_INTERFACE_NATIVE = "Native"
private const val PROTOCOL_VERSION = "2.2.0"
private val DEFAULT_UI_TYPES = listOf("01", "02", "03", "04", "05")
