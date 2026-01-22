package com.mercadopago.sdk.android.coremethods.domain.interactor

import android.app.Activity
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.provider.ThreeDSProvider
import com.mercadopago.sdk.android.coremethods.domain.provider.ThreeDSProviderManager
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSChallengeResult
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSRequestParams
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSWarning
import com.mercadopago.sdk.android.coremethods.domain.usecase.CloseTransactionUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.CreateTransactionUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetAuthenticationRequestParametersUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.SaveThreeDSDeviceDataOrchestratorUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetWarningsUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.StartChallengeUseCase
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

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
    koin.get<ThreeDSProviderManager>().setProvider(provider)
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
fun CoreMethods.getWarnings(): Result<List<ThreeDSWarning>, ResultError> = koin.get<GetWarningsUseCase>().invoke()

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
): Result<ThreeDSChallengeResult, ResultError> =
    koin.get<StartChallengeUseCase>().invoke(
        activity = activity,
        challengeId = challengeId,
        timeout = timeout,
    )

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
fun CoreMethods.close(): Result<String, ResultError> = koin.get<CloseTransactionUseCase>().invoke()

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
): Result<String, ResultError> = koin.get<CreateTransactionUseCase>().invoke(cardToken)

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
fun CoreMethods.getAuthenticationRequestParameters(): Result<ThreeDSRequestParams, ResultError> =
    koin.get<GetAuthenticationRequestParametersUseCase>().invoke()

/**
 * Saves the 3DS device data collected by the SDK to initiate the authentication process.
 *
 * This method orchestrates the following operations:
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
): Result<Unit, ResultError> = koin.get<SaveThreeDSDeviceDataOrchestratorUseCase>().invoke(cardToken)
