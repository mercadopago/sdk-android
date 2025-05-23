package com.mercadopago.sdk.android.analytics.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.analytics.data.datasource.local.AnalyticsLocalDataSource
import com.mercadopago.sdk.android.analytics.data.datasource.local.AnalyticsLocalDataSourceImpl
import com.mercadopago.sdk.android.analytics.data.datasource.remote.AnalyticsRemoteDataSource
import com.mercadopago.sdk.android.analytics.data.datasource.remote.AnalyticsRemoteDataSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val ANALYTICS_DATA_STORE = "ANALYTICS_DATA_STORE"

private val Context.analyticsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = ANALYTICS_DATA_STORE
)

internal fun provideDataSourceModule(): Module = module {
    factory<AnalyticsRemoteDataSource> {
        AnalyticsRemoteDataSourceImpl(
            service = get(),
            context = androidApplication(),
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
        androidApplication().analyticsDataStore
    }
    factory {
        GsonBuilder()
            .serializeNulls()
            .setLenient()
            .create()
    }
}
