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
            publicKey = "APP_USR-1d1ce135-5976-4838-938e-64d5ec7198d4",
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
