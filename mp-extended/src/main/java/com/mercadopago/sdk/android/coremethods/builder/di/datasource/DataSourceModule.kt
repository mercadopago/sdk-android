package com.mercadopago.sdk.android.coremethods.builder.di.datasource

import com.mercadopago.sdk.android.coremethods.builder.data.datasource.remote.MPExtendedRemoteDataSource
import com.mercadopago.sdk.android.coremethods.builder.data.datasource.remote.MPExtendedRemoteDataSourceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideDataSourceModule(): Module =
    module {
        factory<MPExtendedRemoteDataSource> {
            MPExtendedRemoteDataSourceImpl(get())
        }
    }
