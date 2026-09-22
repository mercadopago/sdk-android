package com.mercadopago.sdk.android.analytics.domain.usecase

import com.mercadopago.sdk.android.analytics.domain.repository.AnalyticsRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

internal class GetSessionIdUseCaseTest {

    private val analyticsRepository = mockk<AnalyticsRepository>()
    private val useCase = GetSessionIdUseCase(analyticsRepository)

    @Test
    fun `when invoke is called Then return the current session id`() = runTest {
        // Given
        every { analyticsRepository.getCurrentSessionId() } returns flowOf("session-abc-123")

        // When
        val result = useCase()

        // Then
        assertEquals("session-abc-123", result)
    }

    @Test
    fun `when repository emits nothing Then return an empty string`() = runTest {
        // Given
        every { analyticsRepository.getCurrentSessionId() } returns emptyFlow()

        // When
        val result = useCase()

        // Then
        assertEquals("", result)
    }
}
