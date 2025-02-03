package com.mercadopago.sdk.android.core.di

import org.koin.core.Koin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.koinApplication

/**
 * Interface that`s provide the module list
 */
interface CoreKoinModuleProvider {
    /**
     * Provide the module list
     */
    fun provideModules(): List<Module>
}

/**
 * Factory that`s create a Koin Application instance in a isolated context
 */
object CoreKoinFactory {

    /**
     * Create a Koin instance in a isolated context
     * @param provider: [CoreKoinModuleProvider] with the module list
     * @param loggerLevel: [Level] logger level
     */
    fun createKoinApp(
        provider: CoreKoinModuleProvider,
        loggerLevel: Level = Level.INFO
    ): Koin {
        return koinApplication {
            // Add a print logger with a logger level
            printLogger(level = loggerLevel)
            // Add modules definitions
            modules(provider.provideModules())
        }.koin
    }
}
