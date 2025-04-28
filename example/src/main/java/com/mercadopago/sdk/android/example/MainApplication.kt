package com.mercadopago.sdk.android.example

import android.app.Application
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.example.presentation.PaymentScreenViewModel
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

internal class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MercadoPagoSDK.Companion.initialize(
            context = this,
            publicKey = BuildConfig.PUBLIC_KEY,
            countryCode = CountryCode.ARG,
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
