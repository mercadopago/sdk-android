package com.mercadopago.sdk.android.coremethods.builder.di

import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.core.di.CoreKoinModuleProvider
import com.mercadopago.sdk.android.coremethods.builder.di.datasource.provideDataSourceModule
import com.mercadopago.sdk.android.coremethods.builder.di.repository.provideRepositoryModule
import com.mercadopago.sdk.android.coremethods.builder.di.services.provideNetworkModule
import com.mercadopago.sdk.android.coremethods.builder.di.usecases.provideUseCaseModule
import com.mercadopago.sdk.android.di.MercadoPagoKoinComponent
import org.koin.core.Koin
import org.koin.core.module.Module

internal class MPExtendedModulesProvider : CoreKoinModuleProvider, MercadoPagoKoinComponent {
    override val koinApp: Koin = CoreKoinFactory.setKoinModules(
        koin = getKoin(),
        modules = provideModules(),
    )

    override fun provideModules(): List<Module> {
        return listOf(
            provideNetworkModule(
                baseUrl = "https://beta--bricks-api.furyapps.io/",
            ),
            provideDataSourceModule(),
            provideRepositoryModule(),
            provideUseCaseModule(),
        )
    }
}
