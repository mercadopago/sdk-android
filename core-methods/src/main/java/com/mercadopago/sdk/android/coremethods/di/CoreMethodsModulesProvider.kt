package com.mercadopago.sdk.android.coremethods.di

import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.core.di.CoreKoinModuleProvider
import com.mercadopago.sdk.android.coremethods.di.datasource.provideDataSourceModule
import com.mercadopago.sdk.android.coremethods.di.repository.provideRepositoryModule
import com.mercadopago.sdk.android.coremethods.di.services.provideNetworkModule
import com.mercadopago.sdk.android.coremethods.di.usecases.provideUseCaseModule
import com.mercadopago.sdk.android.di.MercadoPagoKoinComponent
import org.koin.core.Koin
import org.koin.core.module.Module

/**
 * Provider for dependency injection modules in the Core Methods module.
 * This class is responsible for setting up and providing all the necessary
 * dependency injection modules for the Core Methods functionality.
 * It implements both CoreKoinModuleProvider and MercadoPagoKoinComponent interfaces.
 *
 * The provider sets up modules for:
 * 1. Network services and API clients
 * 2. Data sources (remote and local)
 * 3. Repositories
 * 4. Use cases
 *
 * Example:
 * ```kotlin
 * // Initialize the provider
 * val provider = CoreMethodsModulesProvider()
 *
 * // Get the Koin instance with all modules
 * val koin = provider.koinApp
 *
 * // Get the list of modules
 * val modules = provider.provideModules()
 * ```
 *
 * @see CoreKoinModuleProvider
 * @see MercadoPagoKoinComponent
 *
 */
internal class CoreMethodsModulesProvider : CoreKoinModuleProvider, MercadoPagoKoinComponent {
    /**
     * The Koin application instance with all required modules.
     * This property provides access to the configured Koin instance
     * that contains all the dependency injection modules for Core Methods.
     *
     * @see CoreKoinFactory.setKoinModules
     */
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
