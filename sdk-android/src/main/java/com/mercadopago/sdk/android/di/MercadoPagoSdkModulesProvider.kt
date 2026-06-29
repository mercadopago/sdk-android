package com.mercadopago.sdk.android.di

import android.content.Context
import androidx.annotation.RestrictTo
import com.mercadopago.sdk.android.core.BuildConfig
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.core.di.CoreKoinModuleProvider
import com.mercadopago.sdk.android.di.datasource.provideDataSourceModule
import com.mercadopago.sdk.android.di.repository.provideRepositoryModule
import com.mercadopago.sdk.android.di.services.provideNetworkModule
import com.mercadopago.sdk.android.di.usecases.provideUseCaseModule
import org.koin.core.Koin
import org.koin.core.module.Module

/**
 * The Koin Modules Provider for the SDK.
 * @param publicKey The publicKey used by the sdk.
 * @param context The context used by the sdk.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class MercadoPagoSdkModulesProvider(
    private val publicKey: String,
    val context: Context,
) : CoreKoinModuleProvider {

    override val koinApp: Koin = CoreKoinFactory.createKoinApp(
        provider = this,
        context = context,
    )

    override fun provideModules(): List<Module> = listOf(
        provideSessionIdModule(),
        provideNetworkModule(
            publicKey = publicKey,
            baseUrl = BuildConfig.MERCADO_PAGO_API_URL,
        ),
        provideDataSourceModule(),
        provideRepositoryModule(),
        provideUseCaseModule(),
    )
}
