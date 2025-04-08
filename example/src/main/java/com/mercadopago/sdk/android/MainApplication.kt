package com.mercadopago.sdk.android

import android.app.Application
import com.mercadolibre.android.device.sdk.DeviceSDK
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import com.mercadopago.sdk.android.presentation.PaymentScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

internal class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DeviceSDK.getInstance().execute(this)
        MercadoPagoSDK.initialize(
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
