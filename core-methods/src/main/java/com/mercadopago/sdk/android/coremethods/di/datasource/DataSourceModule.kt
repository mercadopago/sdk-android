package com.mercadopago.sdk.android.coremethods.di.datasource

import com.mercadopago.sdk.android.coremethods.data.datasource.remote.CoreMethodsRemoteDataSource
import com.mercadopago.sdk.android.coremethods.data.datasource.remote.CoreMethodsRemoteDataSourceImpl
import com.mercadopago.sdk.android.coremethods.data.datasource.remote.ThreeDSDataSource
import com.mercadopago.sdk.android.coremethods.data.datasource.remote.ThreeDSDataSourceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun provideDataSourceModule(): Module =
    module {
        factory<CoreMethodsRemoteDataSource> {
            CoreMethodsRemoteDataSourceImpl(get())
        }
        factory<ThreeDSDataSource> {
            ThreeDSDataSourceImpl(get())
        }
    }
