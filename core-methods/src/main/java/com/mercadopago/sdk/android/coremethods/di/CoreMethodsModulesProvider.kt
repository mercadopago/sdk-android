package com.mercadopago.sdk.android.coremethods.di

import com.mercadopago.sdk.android.coremethods.di.repository.repositoryModule
import com.mercadopago.sdk.android.coremethods.di.services.provideNetworkModule
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import org.koin.core.KoinApplication
import org.koin.core.logger.Level
import org.koin.dsl.koinApplication

internal class CoreMethodsModulesProvider(
    private val publicKey: String
) {
    private val koinApp: KoinApplication = koinApplication {}

    init {
        // Add a koin log debug
        koinApp.printLogger(level = Level.DEBUG)
        // Load modules
        koinApp.koin.loadModules(
            listOf(
                provideNetworkModule(publicKey),
                repositoryModule
            )
        )
    }

    fun provideCoreMethodsRepository(): CoreMethodsRepository {
        return koinApp.koin.get()
    }
}
