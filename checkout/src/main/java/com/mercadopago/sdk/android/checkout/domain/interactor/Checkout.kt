package com.mercadopago.sdk.android.checkout.domain.interactor

import android.content.Context
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

        internal fun getInstance(
            context: Context,
        ): Checkout {
            return instance ?: synchronized(this) {
                instance ?: Checkout(
                    koin = CheckoutModulesProvider(context.applicationContext).koinApp,
                ).also {
                    instance = it
                }
            }
        }

        /**
         * @suppress
         * Closes the current Koin session and clears the instance.
         * Called by CheckoutActivity.onDestroy to clean up after each session.
         * Also available for testing purposes.
         */
        @RestrictTo(RestrictTo.Scope.LIBRARY)
        fun clearInstance() {
            synchronized(this) {
                instance?.koin?.close()
                instance = null
            }
        }
    }
}
