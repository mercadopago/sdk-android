package com.mercadopago.sdk.android.checkout.di

import android.content.Context
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.core.di.CoreKoinModuleProvider
import org.koin.core.Koin
import org.koin.core.module.Module

internal class CheckoutModulesProvider(context: Context) : CoreKoinModuleProvider {
    override val koinApp: Koin = CoreKoinFactory.createKoinApp(
        provider = this,
        context = context,
    )

    override fun provideModules(): List<Module> {
        return listOf(
            provideDataModule(),
            provideInstallmentsModule(),
        )
    }
}
