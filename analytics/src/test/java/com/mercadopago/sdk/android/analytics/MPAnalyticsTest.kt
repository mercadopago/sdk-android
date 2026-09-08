package com.mercadopago.sdk.android.analytics

import android.content.Context
import android.util.Log
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorClassifier
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorType
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.interactor.MPErrorReporter
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorOperation
import com.mercadopago.sdk.android.analytics.domain.usecase.TrackMetricUseCase
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.core.utils.isSameLibraryGroup
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

internal class MPAnalyticsTest {

    private val context = mockk<Context>(relaxed = true)
    private val koin = mockk<Koin>(relaxed = true)
    private val trackMetricUseCase = mockk<TrackMetricUseCase>(relaxed = true)
    private val errorReporter = mockk<MPErrorReporter>(relaxed = true)
    private val errorClassifier = NativeErrorClassifier()

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
        every { koin.get<MPErrorReporter>() } returns errorReporter
        every { koin.get<NativeErrorClassifier>() } returns errorClassifier
    }

    @Test
    fun `when initialize is called Then sdkInstance is not null`() = runTest {
        // Given
        val getSiteIdFlow = flowOf("MLA")

        // When
        MPAnalytics.initialize(
            context = context,
            getSiteIdFlow = getSiteIdFlow,
            nativeSiteId = "MLA",
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
            nativeSiteId = "MLA",
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
            nativeSiteId = "MLA",
        )
        MPAnalytics.getInstance().trackMetric(metric)

        // Then
        assertNotNull(MPAnalytics.getInstance())
        verify {
            trackMetricUseCase(metric)
        }
    }

    @Test
    fun `track error delegates to the bounded reporter`() {
        MPAnalytics.initialize(context, flowOf("MLA"), "MLA")

        MPAnalytics.getInstance().trackError(
            NativeErrorOperation.ISSUERS,
            NativeErrorInput.create(NativeErrorType.REQUEST),
        ) { mockMetric() }

        verify(exactly = 1) {
            errorReporter.track(match { it.code == NativeErrorCode.UPSTREAM_REJECTED }, any(), any())
        }
    }

    @Test
    fun `reinitialization closes the previous reporter and Koin graph`() {
        MPAnalytics.initialize(context, flowOf("MLA"), "MLA")
        MPAnalytics.initialize(context, flowOf("MLB"), "MLB")

        verify(atLeast = 1) { errorReporter.close() }
        verify(atLeast = 1) { koin.close() }
    }
}
