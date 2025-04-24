package com.mercadopago.sdk.android.analytics.di

import android.content.Context
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.core.di.CoreKoinModuleProvider
import kotlinx.coroutines.flow.Flow
import org.koin.core.Koin
import org.koin.core.module.Module

internal class AnalyticsModulesProvider(
    val context: Context,
    val getSiteIdFlow: Flow<String>,
) : CoreKoinModuleProvider {

    override val koinApp: Koin = CoreKoinFactory.createKoinApp(
        provider = this,
        context = context,
    )

    override fun provideModules(): List<Module> = listOf(
        provideNetworkModule(),
        provideDataSourceModule(),
        provideRepositoryModule(getSiteIdFlow),
        provideUseCaseModule(),
    )
}
