package com.mercadopago.sdk.android.coremethods.builder.di.repository

import com.mercadopago.sdk.android.coremethods.builder.data.repository.MpExtendedRepositoryImpl
import com.mercadopago.sdk.android.coremethods.builder.domain.repository.MPExtendedRepository
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideRepositoryModule(): Module =
    module {
        factory<MPExtendedRepository> {
            MpExtendedRepositoryImpl(get())
        }
    }
