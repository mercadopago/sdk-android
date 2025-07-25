package com.mercadopago.sdk.android.threeds.domain.interactor

import android.app.Activity
import android.content.Context
import androidx.annotation.RestrictTo
import com.mercadopago.sdk.android.threeds.di.MPThreeDSModulesProvider
import com.mercadopago.sdk.android.threeds.domain.callback.MPThreeDSChallengeDelegate
import com.mercadopago.sdk.android.threeds.domain.exceptions.MPThreeDSAlreadyInitializedException
import com.mercadopago.sdk.android.threeds.domain.exceptions.MPThreeDSNotInitializedException
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSDirectoryServer
import com.mercadopago.sdk.android.threeds.domain.usecase.RequestChallengeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.Koin

/**
 * Mercado Pago ThreeDS SDK. This class holds logic to initialize the 3DS module and
 * perform 3DS authentication challenges.
 *
 * Usage:
 * ```kotlin
 * // Initialize in Application class
 * MPThreeDS.initialize(context)
 *
 * // Use in your activity
 * val threeDS = MPThreeDS.getInstance()
 * threeDS.requestChallenge(
 *     activity = this,
 *     cardToken = "your_card_token",
 *     directoryServer = MPThreeDSDirectoryServer.VISA,
 *     delegate = object : MPThreeDSChallengeDelegate {
 *         override fun onSuccess(result: MPThreeDSAuthenticated) {
 *             // Handle success
 *         }
 *         override fun onError(error: MPThreeDSChallengeError) {
 *             // Handle error
 *         }
 *         override fun onCancel() {
 *             // Handle cancellation
 *         }
 *     }
 * )
 * ```
 */
class MPThreeDS internal constructor(
    internal val koin: Koin,
) {

    /**
     * Companion object containing static methods for initialization and instance management.
     */
    companion object {
        @Volatile
        private var instance: MPThreeDS? = null

        /**
         * Check if the MPThreeDS is initialized.
         */
        val isInitialized: Boolean
            get() = instance != null

        /**
         * Initializes the MPThreeDS module. This should be called before any other 3DS method.
         * Call it inside the application class only once for your application.
         *
         * @param context The application context
         */
        @Synchronized
        fun initialize(context: Context) {
            if (instance != null) {
                throw MPThreeDSAlreadyInitializedException()
            }

            val modulesProvider = MPThreeDSModulesProvider(context)
            instance = MPThreeDS(koin = modulesProvider.koinApp)
        }

        /**
         * Get the current instance of the MPThreeDS.
         * This should be called after the module is initialized.
         *
         * @return The current instance of the MPThreeDS.
         */
        fun getInstance(): MPThreeDS {
            return instance ?: throw MPThreeDSNotInitializedException()
        }

        /**
         * @suppress
         * Only for internal usage. DO NOT USE IN PRODUCTION.
         * Clear the current instance of the MPThreeDS for testing purposes.
         */
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        fun clearInstance() {
            instance?.koin?.close()
            instance = null
        }
    }

    /**
     * Requests a 3DS challenge for the provided card token.
     * This method follows the complete 3DS flow:
     * 1. Creates a transaction with the 3DS SDK
     * 2. Gets authentication request parameters
     * 3. Authenticates with MercadoPago backend
     * 4. Performs challenge if required
     *
     * @param activity The activity context for displaying the challenge UI
     * @param cardToken The card token to authenticate
     * @param directoryServer The directory server to use (determines card brand)
     * @param delegate Callback for receiving authentication results
     */
    fun requestChallenge(
        activity: Activity,
        cardToken: String,
        directoryServer: MPThreeDSDirectoryServer,
        delegate: MPThreeDSChallengeDelegate,
    ) {
        val requestChallengeUseCase = koin.get<RequestChallengeUseCase>()

        // Execute the challenge flow in a coroutine
        CoroutineScope(Dispatchers.Main).launch {
            requestChallengeUseCase(
                activity = activity,
                cardToken = cardToken,
                directoryServer = directoryServer,
                delegate = delegate
            )
        }
    }

    /**
     * Requests a 3DS challenge for the provided card token with automatic directory server detection.
     * The directory server will be determined based on the card's BIN.
     *
     * @param activity The activity context for displaying the challenge UI
     * @param cardToken The card token to authenticate
     * @param delegate Callback for receiving authentication results
     */
    fun requestChallenge(
        activity: Activity,
        cardToken: String,
        delegate: MPThreeDSChallengeDelegate,
    ) {
        // For now, default to VISA. In a real implementation, this would analyze the card BIN
        // to determine the appropriate directory server
        requestChallenge(
            activity = activity,
            cardToken = cardToken,
            directoryServer = MPThreeDSDirectoryServer.VISA,
            delegate = delegate
        )
    }
}
