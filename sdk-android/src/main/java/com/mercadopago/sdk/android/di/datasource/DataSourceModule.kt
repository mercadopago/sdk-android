package com.mercadopago.sdk.android.di.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.mercadopago.sdk.android.data.local.datasource.SdkInitializationLocalDataSource
import com.mercadopago.sdk.android.data.local.datasource.SdkInitializationLocalDataSourceImpl
import com.mercadopago.sdk.android.data.remote.datasource.SdkInitializationRemoteDataSource
import com.mercadopago.sdk.android.data.remote.datasource.SdkInitializationRemoteDataSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.security.MessageDigest

private const val MESSAGE_DIGEST_SHA_256 = "MESSAGE_DIGEST_SHA_256"
private const val SDK_INITIALIZER_DEFAULT_DATA_STORE = "SDK_INITIALIZER_DEFAULT_DATA_STORE"

internal fun provideDataSourceModule(): Module = module {
    factory<SdkInitializationLocalDataSource> {
        SdkInitializationLocalDataSourceImpl(
            dataStore = get(named(SDK_INITIALIZER_DEFAULT_DATA_STORE)),
            messageDigest = get(named(MESSAGE_DIGEST_SHA_256)),
        )
    }
    factory<SdkInitializationRemoteDataSource> {
        SdkInitializationRemoteDataSourceImpl(
            sdkInitializationService = get(),
        )
    }
    factory<MessageDigest>(named(MESSAGE_DIGEST_SHA_256)) {
        MessageDigest.getInstance("SHA-256")
    }
    single<DataStore<Preferences>>(named(SDK_INITIALIZER_DEFAULT_DATA_STORE)) {
        PreferenceDataStoreFactory.create {
            androidApplication().preferencesDataStoreFile(SDK_INITIALIZER_DEFAULT_DATA_STORE)
        }
    }
}
