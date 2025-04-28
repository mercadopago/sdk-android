package com.mercadopago.sdk.android.analytics.data.repository

import app.cash.turbine.test
import com.mercadopago.sdk.android.analytics.data.datasource.local.AnalyticsLocalDataSource
import com.mercadopago.sdk.android.analytics.data.datasource.remote.AnalyticsRemoteDataSource
import com.mercadopago.sdk.android.analytics.data.local.model.SessionId
import com.mercadopago.sdk.android.analytics.mockMetric
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertTrue

internal class AnalyticsRepositoryImplTest {

    private val remoteDataSource = mockk<AnalyticsRemoteDataSource>()
    private val localDataSource = mockk<AnalyticsLocalDataSource>()
    private val getSiteIdFlow = flowOf<String>("siteId")
    private val repository = AnalyticsRepositoryImpl(
        remoteDataSource = remoteDataSource,
        localDataSource = localDataSource,
        getSiteIdFlow = getSiteIdFlow,
    )

    @Test
    fun `when all calls return success Then emit unit`() = runTest {
        // Given
        val sessionId = SessionId(sessionId = "123", lastUpdate = 123)
        val uid = "uid"
        val metric = mockMetric()
        every {
            localDataSource.getSessionId()
        } returns flowOf()
        every {
            localDataSource.getUid()
        } returns flowOf(uid)
        every {
            remoteDataSource.trackMetric(
                metric = metric,
                siteId = "siteId",
                sessionId = sessionId.sessionId,
                uid = uid,
            )
        }

        // When
        val result = repository.trackMetric(metric)

        // Then
        result.test {
            awaitComplete()
        }
    }

    @Test
    fun `when any call returns error Then emit error`() = runTest {
        // Given
        val sessionId = SessionId(sessionId = "123", lastUpdate = 123)
        val uid = "uid"
        val metric = mockMetric()
        val exception = Exception()
        every {
            localDataSource.getSessionId()
        } returns flow { throw exception }
        every {
            localDataSource.getUid()
        } returns flowOf(uid)
        every {
            remoteDataSource.trackMetric(
                metric = metric,
                siteId = "siteId",
                sessionId = sessionId.sessionId,
                uid = uid,
            )
        }

        // When
        val result = repository.trackMetric(metric)

        // Then
        result.test {
            assertTrue(awaitError() is Exception)
        }
    }
}
