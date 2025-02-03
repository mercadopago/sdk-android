package com.mercadopago.sdk.android.coremethods.di.repository

import com.mercadopago.sdk.android.coremethods.data.repository.CoreMethodsRepositoryImpl
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideRepositoryModule(): Module = module {
    factory<CoreMethodsRepository> {
        CoreMethodsRepositoryImpl(get())
    }
}
