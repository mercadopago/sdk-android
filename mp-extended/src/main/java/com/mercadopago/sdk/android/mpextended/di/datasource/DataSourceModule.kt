package com.mercadopago.sdk.android.mpextended.di.datasource

import com.mercadopago.sdk.android.mpextended.data.datasource.remote.MPExtendedRemoteDataSource
import com.mercadopago.sdk.android.mpextended.data.datasource.remote.MPExtendedRemoteDataSourceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideDataSourceModule(): Module =
    module {
        factory<MPExtendedRemoteDataSource> {
            MPExtendedRemoteDataSourceImpl(get())
        }
    }
