package com.mercadopago.sdk.android.mpextended.di

import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.core.di.CoreKoinModuleProvider
import com.mercadopago.sdk.android.di.MercadoPagoKoinComponent
import com.mercadopago.sdk.android.mpextended.di.datasource.provideDataSourceModule
import com.mercadopago.sdk.android.mpextended.di.repository.provideRepositoryModule
import com.mercadopago.sdk.android.mpextended.di.services.provideNetworkModule
import com.mercadopago.sdk.android.mpextended.di.usecases.provideUseCaseModule
import org.koin.core.Koin
import org.koin.core.module.Module

internal class MPExtendedModulesProvider : CoreKoinModuleProvider, MercadoPagoKoinComponent {
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
