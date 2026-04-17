package com.mercadopago.sdk.android.mpextended

import androidx.annotation.RestrictTo
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import com.mercadopago.sdk.android.mpextended.di.MPExtendedModulesProvider
import com.mercadopago.sdk.android.mpextended.domain.model.MPDeviceSession
import com.mercadopago.sdk.android.mpextended.domain.usecase.GetDeviceSessionUseCase
import org.koin.core.Koin

/**
 * MPExtended provides additional SDK operations that complement [MercadoPagoSDK].
 * It is a standalone module — to use standard payment operations such as card tokenization
 * or installment calculations, use [MercadoPagoSDK.coreMethods] alongside it.
 *
 * Both instances share the same SDK initialization context, so no extra setup is required.
 *
 * ## Getting an instance
 *
 * ```kotlin
 * val mpExtended = MercadoPagoSDK.getInstance().mpExtended
 * ```
 *
 * ## Handling results
 *
 * All methods return a [Result] type that can be either a success or an error:
 *
 * ```kotlin
 * when (val result = sdk.mpExtended.deviceSession()) {
 *     is Result.Success -> println("Session ID: ${result.value.sessionId}")
 *     is Result.Failure -> println("Error: ${result.error}")
 * }
 * ```
 *
 * @see MercadoPagoSDK
 * @see MercadoPagoSDK.mpExtended
 */
class MPExtended internal constructor(
    internal val koin: Koin,
) {
    /**
     * @suppress
     * Gets the Device Session
     * Sends device information collected by the Device SDK to the session service.
     *
     * @return [Result]: On Success [MPDeviceSession], On Error [ResultError]
     */
    suspend fun deviceSession(): Result<MPDeviceSession, ResultError> =
        koin.get<GetDeviceSessionUseCase>().invoke()

    /**
     * Companion object for the [MPExtended] class.
     */
    companion object {
        @Volatile
        private var instance: MPExtended? = null

        /**
         * Get the instance of the [MPExtended] class to call its methods.
         */
        fun getInstance(): MPExtended {
            return instance ?: synchronized(this) {
                instance ?: MPExtended(
                    koin = MPExtendedModulesProvider().koinApp,
                ).also { instance = it }
            }
        }

        /**
         * @suppress
         * Only for internal usage. DO NOT USE IN PRODUCTION.
         * Clear the current instance of the MPExtended for testing purposes.
         */
        @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
        fun clearInstance() {
            instance = null
        }
    }
}

/**
 * Mercado Pago SDK - MPExtended
 *
 * Use this to get the instance of MPExtended and its methods
 *
 * Example:
 * ```
 * val mpExtended: MPExtended = MercadoPagoSDK.getInstance().mpExtended
 * ```
 */
val MercadoPagoSDK.mpExtended: MPExtended
    get() = MPExtended.getInstance()
