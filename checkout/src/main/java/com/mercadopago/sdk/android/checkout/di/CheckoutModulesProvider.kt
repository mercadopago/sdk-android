package com.mercadopago.sdk.android.checkout.di

import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.core.di.CoreKoinModuleProvider
import com.mercadopago.sdk.android.di.MercadoPagoKoinComponent
import org.koin.core.Koin
import org.koin.core.module.Module

internal class CheckoutModulesProvider : CoreKoinModuleProvider, MercadoPagoKoinComponent {
    override val koinApp: Koin = CoreKoinFactory.setKoinModules(
        koin = getKoin(),
        modules = provideModules(),
    )

    override fun provideModules(): List<Module> {
        return listOf(
            provideDataModule(),
        )
    }
}
