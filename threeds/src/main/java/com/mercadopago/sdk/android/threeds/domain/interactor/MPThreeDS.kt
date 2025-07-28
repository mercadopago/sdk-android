package com.mercadopago.sdk.android.threeds.domain.interactor

import android.app.Activity
import android.content.Context
import com.mercadopago.sdk.android.threeds.di.MPThreeDSModulesProvider
import com.mercadopago.sdk.android.threeds.domain.callback.MPThreeDSChallengeDelegate
import com.mercadopago.sdk.android.threeds.domain.exceptions.MPThreeDSAlreadyInitializedException
import com.mercadopago.sdk.android.threeds.domain.exceptions.MPThreeDSNotInitializedException
import com.mercadopago.sdk.android.threeds.domain.usecase.RequestChallengeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.Koin

/**
 * Mercado Pago ThreeDS SDK. This class holds logic to initialize the 3DS module and
 * perform 3DS authentication challenges using the uSDK (Universal SDK) for 3DS authentication.
 *
 * Usage:
 * ```kotlin
 * // Initialize in Application class with context for uSDK integration
 * MPThreeDS.initialize(context)
 *
 * // Use in your activity (initialization is async, ensure it's complete)
 * val threeDS = MercadoPagoSDK.getInstance().threeDS
 * threeDS.requestChallenge(
 *     activity = this,
 *     cardToken = "your_card_token",
 *     paymentMethodId = "your_payment_method_id",
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
 *
 * Note: The new initialization method with Context enables proper uSDK integration
 * with suspended initialization for optimal performance and stability.
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
         * Initializes the MPThreeDS module asynchronously. This should be called before any other 3DS method.
         * Call it inside the application class only once for your application.
         *
         * This method performs suspended initialization which is required for proper uSDK setup.
         *
         * @param context The application context
         */
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
     * @param paymentMethodId: Payment method identification of your transaction
     * @param delegate Callback for receiving authentication results
     * @param timeout (optional) Challenge timeout limit
     */
    fun requestChallenge(
        activity: Activity,
        cardToken: String,
        paymentMethodId: String,
        delegate: MPThreeDSChallengeDelegate,
        timeout: Int = 10
    ) {
        val requestChallengeUseCase = koin.get<RequestChallengeUseCase>()

        // Execute the challenge flow in a coroutine
        CoroutineScope(Dispatchers.Main).launch {
            requestChallengeUseCase(
                activity = activity,
                cardToken = cardToken,
                paymentMethodId = paymentMethodId,
                delegate = delegate,
                timeout = timeout
            )
        }
    }
}
