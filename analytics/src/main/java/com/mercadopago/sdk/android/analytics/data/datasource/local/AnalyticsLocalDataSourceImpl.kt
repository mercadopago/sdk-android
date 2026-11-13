package com.mercadopago.sdk.android.analytics.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.mercadopago.sdk.android.analytics.data.local.model.SessionId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import java.util.UUID
import kotlin.time.Duration.Companion.minutes

internal const val SESSION_ID_PREFERENCE_KEY = "session_id"
internal const val UID_PREFERENCE_KEY = "uid"
private val SESSION_DURATION = 30.minutes

internal class AnalyticsLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson,
) : AnalyticsLocalDataSource {

    // Read, validate and rotate inside a single edit transaction so concurrent readers
    // agree on the same session instead of each generating its own UUID.
    override fun getSessionId(): Flow<SessionId> {
        return flow {
            val key = stringPreferencesKey(SESSION_ID_PREFERENCE_KEY)
            val preferences = dataStore.edit { mutablePreferences ->
                val stored = mutablePreferences[key]?.toSessionIdOrNull()
                if (stored == null || stored.isExpired()) {
                    mutablePreferences[key] = gson.toJson(
                        SessionId(
                            sessionId = UUID.randomUUID().toString(),
                            lastUpdate = Calendar.getInstance().timeInMillis,
                        ),
                    )
                }
            }
            emit(checkNotNull(preferences[key]?.toSessionIdOrNull()))
        }
    }

    override fun getUid(): Flow<String> {
        return flow {
            val key = stringPreferencesKey(UID_PREFERENCE_KEY)
            val preferences = dataStore.edit { mutablePreferences ->
                if (mutablePreferences[key].isNullOrEmpty()) {
                    mutablePreferences[key] = UUID.randomUUID().toString()
                }
            }
            emit(preferences[key].orEmpty())
        }
    }

    private fun String.toSessionIdOrNull(): SessionId? =
        runCatching { gson.fromJson(this, SessionId::class.java) }.getOrNull()

    private fun SessionId.isExpired(): Boolean =
        Calendar.getInstance().timeInMillis - lastUpdate > SESSION_DURATION.inWholeMilliseconds

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
