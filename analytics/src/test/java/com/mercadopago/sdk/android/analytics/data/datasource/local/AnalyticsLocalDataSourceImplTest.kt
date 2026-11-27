package com.mercadopago.sdk.android.analytics.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import app.cash.turbine.test
import com.google.gson.Gson
import com.mercadopago.sdk.android.analytics.data.local.model.SessionId
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Calendar
import kotlin.io.path.createTempDirectory
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.minutes

private const val CONCURRENT_READERS = 50

internal class AnalyticsLocalDataSourceImplTest {

    private val gson = Gson()
    private val dataStoreScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val dataStoreDir: File = createTempDirectory("analytics-datastore").toFile()
    private val realDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = dataStoreScope,
        produceFile = { File(dataStoreDir, "analytics.preferences_pb") },
    )
    private val dataSource = AnalyticsLocalDataSourceImpl(
        dataStore = realDataStore,
        gson = gson,
    )

    private val sessionKey = stringPreferencesKey(SESSION_ID_PREFERENCE_KEY)
    private val uidKey = stringPreferencesKey(UID_PREFERENCE_KEY)

    @AfterTest
    fun tearDown() {
        dataStoreScope.cancel()
        dataStoreDir.deleteRecursively()
    }

    private suspend fun storeSession(sessionId: SessionId) {
        realDataStore.edit { it[sessionKey] = gson.toJson(sessionId) }
    }

    private suspend fun storedSession(): SessionId =
        gson.fromJson(realDataStore.data.first()[sessionKey], SessionId::class.java)

    @Test
    fun `when getSessionId is called without stored session Then create and persist a new one`() = runTest {
        val result = dataSource.getSessionId().first()

        assertEquals(result, storedSession())
        assertEquals(result, dataSource.getSessionId().first())
    }

    @Test
    fun `when getSessionId is called with valid session Then return it unchanged`() = runTest {
        val valid = SessionId(sessionId = "valid-session", lastUpdate = Calendar.getInstance().timeInMillis)
        storeSession(valid)

        dataSource.getSessionId().test {
            assertEquals(valid, awaitItem())
            awaitComplete()
        }
        assertEquals(valid, storedSession())
    }

    @Test
    fun `when getSessionId is called with expired session Then rotate and persist a new one`() = runTest {
        val expired = SessionId(
            sessionId = "expired-session",
            lastUpdate = Calendar.getInstance().timeInMillis - 31.minutes.inWholeMilliseconds,
        )
        storeSession(expired)

        val result = dataSource.getSessionId().first()

        assertNotEquals(expired.sessionId, result.sessionId)
        assertTrue(result.lastUpdate > expired.lastUpdate)
        assertEquals(result, storedSession())
    }

    @Test
    fun `when stored session is corrupted Then persist a new one and keep returning it`() = runTest {
        realDataStore.edit { it[sessionKey] = "not-a-json" }

        val first = dataSource.getSessionId().first()
        val second = dataSource.getSessionId().first()

        assertEquals(first, second)
        assertEquals(first, storedSession())
    }

    @Test
    fun `when getSessionId is read concurrently with expired session Then all readers get the same id`() = runTest {
        storeSession(SessionId(sessionId = "expired-session", lastUpdate = 0L))

        val results = withContext(Dispatchers.IO) {
            (1..CONCURRENT_READERS).map { async { dataSource.getSessionId().first().sessionId } }.awaitAll()
        }

        assertEquals(1, results.toSet().size)
        assertEquals(results.first(), storedSession().sessionId)
    }

    @Test
    fun `when getUid is called with stored uid Then return it`() = runTest {
        realDataStore.edit { it[uidKey] = "uid" }

        dataSource.getUid().test {
            assertEquals("uid", awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when getUid is read concurrently without stored uid Then all readers get the same persisted uid`() = runTest {
        val results = withContext(Dispatchers.IO) {
            (1..CONCURRENT_READERS).map { async { dataSource.getUid().first() } }.awaitAll()
        }

        assertEquals(1, results.toSet().size)
        assertEquals(results.first(), realDataStore.data.first()[uidKey])
    }

    @Test
    fun `when setSessionId is called Then save sessionId`() = runTest {
        // Given
        val dataStore = mockk<DataStore<Preferences>>(relaxed = true)
        val sessionId = SessionId(sessionId = "123", lastUpdate = 123)
        val preferences = preferencesOf(
            stringPreferencesKey(SESSION_ID_PREFERENCE_KEY) to sessionId.toString()
        )
        coEvery {
            dataStore.updateData { (any()) }
        } returns preferences

        // When
        val result = AnalyticsLocalDataSourceImpl(dataStore, gson).setSessionId(sessionId)

        // Then
        result.test {
            assertEquals(Unit, awaitItem())
            awaitComplete()
            assertEquals(sessionId.toString(), preferences[stringPreferencesKey(SESSION_ID_PREFERENCE_KEY)])
        }
    }

    @Test
    fun `when setUid is called Then save uid`() = runTest {
        // Given
        val dataStore = mockk<DataStore<Preferences>>(relaxed = true)
        val uid = "uid"
        val preferences = preferencesOf(
            stringPreferencesKey(UID_PREFERENCE_KEY) to uid
        )
        coEvery {
            dataStore.updateData { (any()) }
        } returns preferences

        // When
        val result = AnalyticsLocalDataSourceImpl(dataStore, gson).setUid(uid)

        // Then
        result.test {
            assertEquals(Unit, awaitItem())
            awaitComplete()
            assertEquals(uid, preferences[stringPreferencesKey(UID_PREFERENCE_KEY)])
        }
    }
}
