package com.mercadopago.sdk.android.checkout.di

import android.content.Context
import com.mercadopago.sdk.android.core.BuildConfig
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.core.di.CoreKoinModuleProvider
import org.koin.core.Koin
import org.koin.core.module.Module

internal class CheckoutModulesProvider(val context: Context, private val publicKey: String) : CoreKoinModuleProvider {
    override val koinApp: Koin = CoreKoinFactory.createKoinApp(
        provider = this,
        context = context,
    )

    override fun provideModules(): List<Module> {
        return listOf(
            provideNetworkModule(
                publicKey = publicKey,
                baseUrl = BuildConfig.MERCADO_PAGO_API_URL,
            ),
            provideDataModule(),
            provideInstallmentsModule(),
        )
    }
}
