package com.mercadopago.sdk.android.analytics.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.mercadopago.sdk.android.analytics.data.local.model.SessionId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.UUID
import kotlin.time.Duration.Companion.minutes

internal const val SESSION_ID_PREFERENCE_KEY = "session_id"
internal const val UID_PREFERENCE_KEY = "uid"

internal class AnalyticsLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson,
) : AnalyticsLocalDataSource {

    override fun getSessionId(): Flow<SessionId> {
        return dataStore.data.map { preferences ->
            val sessionId = try {
                gson.fromJson(
                    preferences[stringPreferencesKey(SESSION_ID_PREFERENCE_KEY)],
                    SessionId::class.java,
                )
            } catch (_: Exception) {
                SessionId(
                    sessionId = UUID.randomUUID().toString(),
                    lastUpdate = Calendar.getInstance().timeInMillis,
                )
            }
            val thirtyMinutesInMillis: Long = 30.minutes.inWholeMinutes
            if (Calendar.getInstance().timeInMillis - (sessionId?.lastUpdate ?: 0L) > thirtyMinutesInMillis) {
                val newSessionId = SessionId(
                    sessionId = UUID.randomUUID().toString(),
                    lastUpdate = Calendar.getInstance().timeInMillis,
                )
                setSessionId(newSessionId).firstOrNull()
                newSessionId
            } else {
                sessionId
            }
        }
    }

    override fun getUid(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(UID_PREFERENCE_KEY)].orEmpty().takeIf {
                it.isNotEmpty()
            } ?: UUID.randomUUID().toString().also { newUid ->
                setUid(newUid.toString()).firstOrNull()
            }.toString()
        }
    }

    override fun setSessionId(sessionId: SessionId): Flow<Unit> {
        return flow {
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey(SESSION_ID_PREFERENCE_KEY)] = gson.toJson(sessionId)
            }
            emit(Unit)
        }
    }

    override fun setUid(uid: String): Flow<Unit> {
        return flow {
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey(UID_PREFERENCE_KEY)] = uid
            }
            emit(Unit)
        }
    }
}
