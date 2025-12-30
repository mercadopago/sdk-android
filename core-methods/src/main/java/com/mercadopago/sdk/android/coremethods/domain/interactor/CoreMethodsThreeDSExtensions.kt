package com.mercadopago.sdk.android.coremethods.domain.interactor

import android.app.Activity
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.provider.ThreeDSProvider
import com.mercadopago.sdk.android.coremethods.domain.provider.ThreeDSProviderManager
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSChallengeResult
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSWarning
import com.mercadopago.sdk.android.coremethods.domain.usecase.CloseTransactionUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.CreateTransactionUseCase
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
fun CoreMethods.getWarnings(): Result<List<ThreeDSWarning>, ResultError> {
    return koin.get<GetWarningsUseCase>().invoke()
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
    return koin.get<StartChallengeUseCase>().invoke(
        activity = activity,
        challengeId = challengeId,
        timeout = timeout,
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
    return koin.get<CloseTransactionUseCase>().invoke()
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
    return koin.get<CreateTransactionUseCase>().invoke(cardToken)
}
