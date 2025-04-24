package com.mercadopago.sdk.android.analytics.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import app.cash.turbine.test
import com.google.gson.Gson
import com.mercadopago.sdk.android.analytics.data.local.model.SessionId
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AnalyticsLocalDataSourceImplTest {

    private val dataStore = mockk<DataStore<Preferences>>(relaxed = true)
    private val gson = mockk<Gson>(relaxed = true)
    private val dataSource = AnalyticsLocalDataSourceImpl(
        dataStore = dataStore,
        gson = gson,
    )

//    @Test
//    fun `when getSessionId is called with valid session Then return sessionId`() = runTest {
//        // Given
//        val sessionId = SessionId(sessionId = "123", lastUpdate = 123)
//        val sessionJson = "session"
//        val fakeCalendar = Calendar.getInstance().apply {
//            set(2023, Calendar.DECEMBER, 25)
//        }
//        mockkStatic(Calendar::class)
//        every { Calendar.getInstance() } returns fakeCalendar
//        every {
//            gson.fromJson(sessionJson, SessionId::class.java)
//        } returns sessionId
//
//        // When
//        val result = dataSource.getSessionId()
//
//        // Then
//        result.test {
//            assertEquals(sessionId, awaitItem())
//        }
//    }

    @Test
    fun `when getSessionId is called with invalid session Then generate new session`() = runTest {
    }

    @Test
    fun `when getUid is called Then return uid`() = runTest {
        // Given
        val uid = "uid"
        every { dataStore.data } returns flowOf(
            preferencesOf(
                stringPreferencesKey(UID_PREFERENCE_KEY) to uid
            )
        )

        // When
        val result = dataSource.getUid()

        // Then
        result.test {
            assertEquals(uid, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when setSessionId is called Then save sessionId`() = runTest {
        // Given
        val sessionId = SessionId(sessionId = "123", lastUpdate = 123)
        val preferences = preferencesOf(
            stringPreferencesKey(SESSION_ID_PREFERENCE_KEY) to sessionId.toString()
        )
        coEvery {
            dataStore.updateData { (any()) }
        } returns preferences

        // When
        val result = dataSource.setSessionId(sessionId)

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
        val uid = "uid"
        val preferences = preferencesOf(
            stringPreferencesKey(UID_PREFERENCE_KEY) to uid
        )
        coEvery {
            dataStore.updateData { (any()) }
        } returns preferences

        // When
        val result = dataSource.setUid(uid)

        // Then
        result.test {
            assertEquals(Unit, awaitItem())
            awaitComplete()
            assertEquals(uid, preferences[stringPreferencesKey(UID_PREFERENCE_KEY)])
        }
    }
}
