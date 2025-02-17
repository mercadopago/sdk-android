package com.mercadopago.sdk.android.coremethods.di

import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.core.di.CoreKoinModuleProvider
import com.mercadopago.sdk.android.coremethods.di.datasource.provideDataSourceModule
import com.mercadopago.sdk.android.coremethods.di.repository.provideRepositoryModule
import com.mercadopago.sdk.android.coremethods.di.services.provideNetworkModule
import com.mercadopago.sdk.android.coremethods.di.usecases.provideUseCaseModule
import com.mercadopago.sdk.android.initializer.MercadoPagoKoinComponent
import org.koin.core.Koin
import org.koin.core.module.Module

internal class CoreMethodsModulesProvider : CoreKoinModuleProvider, MercadoPagoKoinComponent {

    override val koinApp: Koin = CoreKoinFactory.setKoinModules(
        koin = getKoin(),
        modules = provideModules(),
    )

    override fun provideModules(): List<Module> {
        return listOf(
            provideNetworkModule(),
            provideDataSourceModule(),
            provideRepositoryModule(),
            provideUseCaseModule(),
        )
    }
}
