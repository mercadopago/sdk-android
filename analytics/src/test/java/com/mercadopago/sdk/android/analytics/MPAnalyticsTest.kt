package com.mercadopago.sdk.android.analytics

import android.content.Context
import android.util.Log
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.usecase.GetSessionIdUseCase
import com.mercadopago.sdk.android.analytics.domain.usecase.TrackMetricUseCase
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.core.utils.isSameLibraryGroup
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.core.Koin
import kotlin.test.assertEquals

internal class MPAnalyticsTest {

    private val context = mockk<Context>(relaxed = true)
    private val koin = mockk<Koin>(relaxed = true)
    private val trackMetricUseCase = mockk<TrackMetricUseCase>(relaxed = true)
    private val getSessionIdUseCase = mockk<GetSessionIdUseCase>(relaxed = true)

    @Before
    fun setup() {
        mockkObject(CoreKoinFactory)
        mockkStatic(Log::class)
        every {
            CoreKoinFactory.createKoinApp(any(), any(), any())
        } returns koin
        every { Log.e(any(), any(), any()) } returns 0
        every {
            koin.get<TrackMetricUseCase>()
        } returns trackMetricUseCase
        every {
            koin.get<GetSessionIdUseCase>()
        } returns getSessionIdUseCase
    }

    @Test
    fun `when initialize is called Then sdkInstance is not null`() = runTest {
        // Given
        val getSiteIdFlow = flowOf("MLA")

        // When
        MPAnalytics.initialize(
            context = context,
            getSiteIdFlow = getSiteIdFlow,
        )

        // Then
        assertNotNull(MPAnalytics.getInstance())
    }

    @Test
    fun `when track metric is called with success then expect no events`() = runTest {
        // Given
        val getSiteIdFlow = flowOf("MLA")
        val metric = mockMetric()

        // When
        MPAnalytics.initialize(
            context = context,
            getSiteIdFlow = getSiteIdFlow,
        )
        MPAnalytics.getInstance().trackMetric(metric)

        // Then
        assertNotNull(MPAnalytics.getInstance())
        verify {
            trackMetricUseCase(metric)
        }
    }

    @Test
    fun `when track metric is called with error then expect log`() = runTest {
        // Given
        val getSiteIdFlow = flowOf("MLA")
        val metric = mockMetric()
        mockkStatic("com.mercadopago.sdk.android.core.utils.DebugKt")
        every {
            trackMetricUseCase(metric)
        } returns flow { throw IllegalArgumentException() }
        every {
            koin.get<Context>()
        } returns context
        every {
            isSameLibraryGroup(context)
        } returns true

        // When
        MPAnalytics.initialize(
            context = context,
            getSiteIdFlow = getSiteIdFlow,
        )
        MPAnalytics.getInstance().trackMetric(metric)

        // Then
        assertNotNull(MPAnalytics.getInstance())
        verify {
            trackMetricUseCase(metric)
        }
    }

    @Test
    fun `when getSessionId is called Then return the use case value`() = runTest {
        // Given
        val getSiteIdFlow = flowOf("MLA")
        coEvery { getSessionIdUseCase() } returns "session-abc-123"

        // When
        MPAnalytics.initialize(
            context = context,
            getSiteIdFlow = getSiteIdFlow,
        )
        val result = MPAnalytics.getInstance().getSessionId()

        // Then
        assertEquals("session-abc-123", result)
    }
}
