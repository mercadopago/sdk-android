package com.mercadopago.sdk.android.core.di

import android.content.Context
import androidx.annotation.RestrictTo
import com.mercadopago.sdk.android.core.utils.isDebugApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.Koin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.koinApplication

/**
 * Interface that`s provide the module list
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface CoreKoinModuleProvider {

    /**
     * Provide the koin application
     */
    @get:RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    val koinApp: Koin

    /**
     * Provide the module list
     */
    fun provideModules(): List<Module>
}

/**
 * Factory that`s create a Koin Application instance in a isolated context
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
object CoreKoinFactory {

    /**
     * Create a Koin instance in a isolated context
     * @param provider: [CoreKoinModuleProvider] with the module list
     * @param loggerLevel: [Level] logger level
     */
    fun createKoinApp(
        provider: CoreKoinModuleProvider,
        loggerLevel: Level = if (isDebugApp()) {
            Level.DEBUG
        } else {
            Level.NONE
        },
        context: Context,
    ): Koin {
        return koinApplication {
            androidContext(context)
            // Add a print logger with a logger level
            androidLogger(loggerLevel)
            // Add modules definitions
            modules(provider.provideModules())
        }.koin.also { koin ->
            koin.loadModules(provider.provideModules())
        }
    }

    fun setKoinModules(
        koin: Koin,
        modules: List<Module>,
    ): Koin = koin.also { koin.loadModules(modules) }
}
