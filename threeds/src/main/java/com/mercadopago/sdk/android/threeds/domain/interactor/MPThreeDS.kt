package com.mercadopago.sdk.android.threeds.domain.interactor

import org.koin.core.Koin

class MPThreeDS (
    internal val koin: Koin,
) {


    companion object {
        @Volatile
        private var instance: MPThreeDS? = null

        fun getInstance(): MPThreeDS {
            return instance?: synchronized (this){
                instance ?: MPThreeDS(
                    koin =
                )
            }
        }
    }
}
