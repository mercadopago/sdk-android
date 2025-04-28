package com.mercadopago.sdk.android.analytics.domain.usecase

import app.cash.turbine.test
import com.mercadopago.sdk.android.analytics.domain.repository.AnalyticsRepository
import com.mercadopago.sdk.android.analytics.mockMetric
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

internal class TrackMetricUseCaseTest {

    private val analyticsRepository = mockk<AnalyticsRepository>()
    private val useCase = TrackMetricUseCase(analyticsRepository)

    @Test
    fun `when invoke is called with success Then emit unit`() = runTest {
        // Given
        val metric = mockMetric()
        every { analyticsRepository.trackMetric(metric) } returns flowOf(Unit)

        // When
        val result = useCase(metric)

        // Then
        result.test {
            assertEquals(Unit, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when invoke is called with error Then emit error`() = runTest {
        // Given
        val metric = mockMetric()
        val exception = Exception()
        every {
            analyticsRepository.trackMetric(metric)
        } returns flow { throw exception }

        // When
        val result = useCase(metric)

        // Then
        result.test {
            assertEquals(exception, awaitError())
        }
    }
}
