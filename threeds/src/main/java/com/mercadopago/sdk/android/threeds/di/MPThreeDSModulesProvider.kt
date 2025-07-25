package com.mercadopago.sdk.android.threeds.di

import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.core.di.CoreKoinModuleProvider
import com.mercadopago.sdk.android.di.MercadoPagoKoinComponent
import com.mercadopago.sdk.android.threeds.di.adapters.provideAdaptersModule
import com.mercadopago.sdk.android.threeds.di.datasource.provideDataSourceModule
import com.mercadopago.sdk.android.threeds.di.repository.provideRepositoryModule
import com.mercadopago.sdk.android.threeds.di.services.provideNetworkModule
import com.mercadopago.sdk.android.threeds.di.usecases.provideUseCaseModule
import org.koin.core.Koin
import org.koin.core.module.Module

/**
 * Provider for dependency injection modules in the ThreeDS module.
 * This class is responsible for setting up and providing all the necessary
 * dependency injection modules for the 3DS authentication functionality.
 * It implements both CoreKoinModuleProvider and MercadoPagoKoinComponent interfaces.
 *
 * The provider sets up modules for:
 * 1. Network services and API clients for 3DS authentication
 * 2. Data sources (remote and local)
 * 3. Repositories for 3DS operations
 * 4. Use cases for 3DS authentication flows
 * 5. Adapters for 3DS SDK integration
 *
 * @see CoreKoinModuleProvider
 * @see MercadoPagoKoinComponent
 */
internal class MPThreeDSModulesProvider : CoreKoinModuleProvider, MercadoPagoKoinComponent {

    /**
     * The Koin application instance with all required modules.
     * This property provides access to the configured Koin instance
     * that contains all the dependency injection modules for 3DS functionality.
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
            provideAdaptersModule(),
        )
    }
}
