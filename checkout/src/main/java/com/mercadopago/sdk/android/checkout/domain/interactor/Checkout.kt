package com.mercadopago.sdk.android.checkout.domain.interactor

import androidx.annotation.RestrictTo
import com.mercadopago.sdk.android.checkout.di.CheckoutModulesProvider
import org.koin.core.Koin

/**
 * @suppress
 * Checkout is the main class responsible for handling all checkout-related operations in the MercadoPago SDK.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
class Checkout internal constructor(
    internal val koin: Koin,
) {
    /**
     * @suppress
     */
    companion object {
        @Volatile
        private var instance: Checkout? = null

        internal fun getInstance(): Checkout {
            return instance ?: synchronized(this) {
                instance ?: Checkout(
                    koin = CheckoutModulesProvider().koinApp,
                ).also {
                    instance = it
                }
            }
        }

        /**
         * @suppress
         * Only for internal usage. DO NOT USE IN PRODUCTION.
         * Clear the current instance of the Checkout for testing purposes.
         */
        @RestrictTo(RestrictTo.Scope.LIBRARY)
        fun clearInstance() {
            instance = null
        }
    }
}
