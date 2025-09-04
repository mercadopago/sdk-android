package com.mercadopago.sdk.android.threeds.domain.interactor

import android.app.Activity
import android.content.Context
import com.mercadopago.sdk.android.threeds.MPThreeDSAuthRequestParameters
import com.mercadopago.sdk.android.threeds.MPThreeDSAuthenticationResponse
import com.mercadopago.sdk.android.threeds.MPThreeDSChallengeResult
import com.mercadopago.sdk.android.threeds.MPThreeDSWarning
import com.mercadopago.sdk.android.threeds.di.MPThreeDSModulesProvider
import com.mercadopago.sdk.android.threeds.domain.adapter.ThreeDSWrapper
import com.mercadopago.sdk.android.threeds.domain.exceptions.MPThreeDSAlreadyInitializedException
import com.mercadopago.sdk.android.threeds.domain.exceptions.MPThreeDSNotInitializedException
import com.mercadopago.sdk.android.threeds.domain.mappers.toInternal
import com.mercadopago.sdk.android.threeds.domain.mappers.toPublic
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSDirectoryServer
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
 * // Use in your activity with individual methods
 * val threeDS = MercadoPagoSDK.getInstance().threeDS
 * ```
 *
 * Note: The SDK provides individual methods for each step of the 3DS flow,
 * allowing clients to handle authentication externally and have full control
 * over the process.
 */
class MPThreeDS internal constructor(
    internal val koin: Koin,
) {
    private val threeDSWrapper: ThreeDSWrapper by lazy { koin.get<ThreeDSWrapper>() }

    @Volatile
    private var isWrapperInitialized = false

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
         * The ThreeDSWrapper is also automatically initialized during this process.
         *
         * @param context The application context
         */
        fun initialize(context: Context) {
            if (instance != null) {
                throw MPThreeDSAlreadyInitializedException()
            }

            val modulesProvider = MPThreeDSModulesProvider(context)
            val mpThreeDS = MPThreeDS(koin = modulesProvider.koinApp)

            // Initialize the ThreeDSWrapper automatically
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    mpThreeDS.threeDSWrapper.initialize()
                    mpThreeDS.isWrapperInitialized = true
                } catch (e: Exception) {
                    // Log error but don't fail the initialization
                    // The wrapper initialization can be retried later if needed
                    mpThreeDS.isWrapperInitialized = false
                }
            }

            instance = mpThreeDS
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
     * Checks if the ThreeDSWrapper has been successfully initialized.
     * This can be used to verify that the SDK is ready for use.
     *
     * @return true if the wrapper is initialized, false otherwise
     */
    fun isInitialized(): Boolean {
        return isWrapperInitialized
    }

    /**
     * Gets warnings from the 3DS SDK after initialization.
     *
     * @return List of warnings from the 3DS SDK
     */
    fun getWarnings(): List<MPThreeDSWarning> {
        return threeDSWrapper.getWarnings().map { it.toPublic() }
    }

    /**
     * Creates a transaction with the specified payment method.
     *
     * @param paymentMethodId The payment method ID to create transaction for
     */
    fun createTransaction(paymentMethodId: String) {
        val directoryServer = MPThreeDSDirectoryServer.paymentMethodDirectoryServer(paymentMethodId)
        threeDSWrapper.createTransaction(directoryServer)
    }

    /**
     * Gets the authentication request parameters for the current transaction.
     *
     * @return Authentication request parameters needed for backend call
     */
    fun getAuthenticationRequestParameters(): MPThreeDSAuthRequestParameters? {
        return threeDSWrapper.getAuthenticationRequestParameters()?.toPublic()
    }

    /**
     * Performs the challenge flow with the provided authentication response.
     *
     * @param activity The activity context for displaying the challenge UI
     * @param authenticationResponse The response from MercadoPago backend
     * @param timeout Challenge timeout limit
     * @return Challenge result
     */
    suspend fun doChallenge(
        activity: Activity,
        authenticationResponse: MPThreeDSAuthenticationResponse,
        timeout: Int = 10
    ): MPThreeDSChallengeResult {
        val internalResponse = authenticationResponse.toInternal()
        val internalResult = threeDSWrapper.doChallenge(activity, internalResponse, timeout)
        return internalResult.toPublic()
    }

    /**
     * Closes the current 3DS transaction and releases resources.
     */
    fun close() {
        threeDSWrapper.close()
    }
}
