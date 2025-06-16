package com.mercadopago.sdk.android.checkout.domain.interactor

import com.mercadopago.sdk.android.checkout.di.CheckoutModulesProvider
import org.koin.core.Koin

internal class Checkout internal constructor(
    internal val koin: Koin,
) {
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
    }
}
