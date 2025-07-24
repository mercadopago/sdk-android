package com.mercadopago.sdk.android.threeds.di.datasource

import com.mercadopago.sdk.android.threeds.data.datasource.remote.ThreeDSRemoteDataSource
import com.mercadopago.sdk.android.threeds.data.datasource.remote.ThreeDSRemoteDataSourceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideDataSourceModule(): Module =
    module {
        factory<ThreeDSRemoteDataSource> {
            ThreeDSRemoteDataSourceImpl(get())
        }
    }
