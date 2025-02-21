package com.mercadopago.sdk.android

import android.app.Application
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import com.mercadopago.sdk.android.presentation.PaymentScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MercadoPagoSDK.initialize(
            context = this,
            publicKey = "TEST-e4bcdfb3-7a4f-45f6-9cbf-625428f2fcec",
        )
        startKoin {
            androidContext(this@MainApplication)
            modules(
                modules = listOf(
                    module {
                        viewModel { PaymentScreenViewModel() }
                    }
                )
            )
        }
    }
}
