package com.mercadopago.sdk.android.data.local.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mercadopago.sdk.android.domain.model.SiteId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.security.MessageDigest

internal const val FORMAT_MODE = "%02x"

internal class SdkInitializationLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
    private val messageDigest: MessageDigest,
) : SdkInitializationLocalDataSource {

    override fun getSiteId(publicKey: String): Flow<SiteId> {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(encrypt(publicKey))].orEmpty()
        }.map { SiteId(it) }
    }

    override fun setSiteId(publicKey: String, siteId: SiteId): Flow<Unit> {
        return flow {
            val encryptedPublicKey = encrypt(publicKey)
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey(encryptedPublicKey)] = siteId.siteId
            }
            emit(Unit)
        }
    }

    private fun encrypt(publicKey: String): String {
        return messageDigest.digest(publicKey.toByteArray()).joinToString("") { FORMAT_MODE.format(it) }
    }
}
