package com.mercadopago.sdk.android.analytics.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.analytics.data.datasource.local.AnalyticsLocalDataSource
import com.mercadopago.sdk.android.analytics.data.datasource.local.AnalyticsLocalDataSourceImpl
import com.mercadopago.sdk.android.analytics.data.datasource.remote.AnalyticsRemoteDataSource
import com.mercadopago.sdk.android.analytics.data.datasource.remote.AnalyticsRemoteDataSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val ANALYTICS_DATA_STORE = "ANALYTICS_DATA_STORE"

internal fun provideDataSourceModule(): Module = module {
    factory<AnalyticsRemoteDataSource> {
        AnalyticsRemoteDataSourceImpl(
            context = androidContext(),
            service = get(),
            gson = get(),
        )
    }
    factory<AnalyticsLocalDataSource> {
        AnalyticsLocalDataSourceImpl(
            dataStore = get(named(ANALYTICS_DATA_STORE)),
            gson = get(),
        )
    }
    single<DataStore<Preferences>>(named(ANALYTICS_DATA_STORE)) {
        PreferenceDataStoreFactory.create {
            androidApplication().preferencesDataStoreFile(ANALYTICS_DATA_STORE)
        }
    }
    factory {
        GsonBuilder()
            .serializeNulls()
            .setLenient()
            .create()
    }
}
