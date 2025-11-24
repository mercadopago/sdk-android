package com.mercadopago.sdk.android.threeds.di

import android.content.Context
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.core.di.CoreKoinModuleProvider
import com.mercadopago.sdk.android.di.MercadoPagoKoinComponent
import com.mercadopago.sdk.android.threeds.di.adapters.provideWrapperModule
import com.mercadopago.sdk.android.threeds.di.repository.provideRepositoryModule
import org.koin.core.Koin
import org.koin.core.module.Module

internal class MPThreeDSModulesProvider(val context: Context) : CoreKoinModuleProvider, MercadoPagoKoinComponent {

    override val koinApp: Koin = CoreKoinFactory.setKoinModules(
        koin = getKoin(),
        modules = provideModules(),
    )

    override fun provideModules(): List<Module> =
        listOf(
            provideWrapperModule(context),
            provideRepositoryModule(),
        )
}
