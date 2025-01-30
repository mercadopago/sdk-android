package com.mercadopago.sdk.android.coremethods.di

import com.mercadopago.sdk.android.core.di.CoreKoinModuleProvider
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.coremethods.di.repository.repositoryModule
import com.mercadopago.sdk.android.coremethods.di.services.provideNetworkModule
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import org.koin.core.Koin
import org.koin.core.logger.Level
import org.koin.core.module.Module

internal class CoreMethodsModulesProvider(
    private val publicKey: String
) : CoreKoinModuleProvider {

    private val isolatedKoin: Koin = CoreKoinFactory.createKoinApp(
        provider = this,
        loggerLevel = Level.DEBUG
    )

    init {
        // Load modules
        isolatedKoin.loadModules(provideModules())
    }

    override fun provideModules(): List<Module> {
        return listOf(
            provideNetworkModule(publicKey),
            repositoryModule
        )
    }

    internal fun provideCoreMethodsRepository(): CoreMethodsRepository {
        return isolatedKoin.get()
    }
}
