package com.mercadopago.sdk.android.threeds.interactor

import android.app.Activity
import com.mercadopago.sdk.android.threeds.di.MPThreeDSModulesProvider
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeResult
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSWarning
import com.mercadopago.sdk.android.threeds.domain.model.params.MPThreeDSRequestParams
import com.mercadopago.sdk.android.threeds.domain.repository.ThreeDSRepository
import org.koin.core.Koin

/**
 * Mercado Pago ThreeDS SDK. This class holds logic to initialize the 3DS module and
 * perform 3DS authentication challenges using the uSDK (Universal SDK) for 3DS authentication.
 *
 * Usage:
 * ```kotlin
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
    private val threeDSRepository: ThreeDSRepository by lazy { koin.get<ThreeDSRepository>() }

    /**
     * Companion object containing static methods for initialization and instance management.
     */
    companion object {
        @Volatile
        private var instance: MPThreeDS? = null

        /**
         * Get the current instance of the MPThreeDS with a specific context.
         * This method is used for initialization.
         *
         * @return The current instance of the MPThreeDS.
         */
        fun getInstance(): MPThreeDS {
            return instance ?: synchronized(this) {
                instance ?: MPThreeDS(
                    koin = MPThreeDSModulesProvider().koinApp,
                ).also {
                    instance = it
                }
            }
        }
    }

    /**
     * Gets warnings from the 3DS SDK after initialization.
     *
     * @return List of warnings from the 3DS SDK
     */
    fun getWarnings(): List<MPThreeDSWarning> = threeDSRepository.getWarnings()

    /**
     * Creates a transaction with the specified payment method.
     *
     * @param paymentMethodId The payment method ID to create transaction for
     */
    fun createTransaction(paymentMethodId: String) = threeDSRepository.createTransaction(paymentMethodId)

    /**
     * Gets the authentication request parameters for the current transaction.
     *
     * @return Authentication request parameters needed for backend call
     */
    fun getAuthenticationRequestParameters(): MPThreeDSRequestParams? {
        return threeDSRepository.getAuthenticationRequestParameters()
    }

    /**
     * Performs the challenge flow with the provided authentication response.
     *
     * @param activity The activity context for displaying the challenge UI
     * @param authentication The response from MercadoPago backend
     * @param timeout Challenge timeout limit
     * @return Challenge result
     */
    suspend fun doChallenge(
        activity: Activity,
        authentication: MPThreeDSAuthenticationModel,
        timeout: Int = 10,
    ): MPThreeDSChallengeResult {
        return threeDSRepository.doChallenge(
            activity = activity,
            authenticationResponse = authentication,
            timeout = timeout,
        )
    }

    /**
     * Closes the current 3DS transaction and releases resources.
     */
    fun close() {
        threeDSRepository.close()
    }
}
