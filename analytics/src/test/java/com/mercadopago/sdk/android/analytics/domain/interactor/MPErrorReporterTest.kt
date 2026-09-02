package com.mercadopago.sdk.android.analytics.domain.interactor

import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.NativeError
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorDeliveryMode
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorOperation
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.analytics.domain.repository.NativeErrorRepository
import com.mercadopago.sdk.android.analytics.domain.usecase.ReportNativeErrorUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.Test
import kotlin.test.assertEquals

internal class MPErrorReporterTest {
    private val repository = mockk<NativeErrorRepository>()
    private val useCase = ReportNativeErrorUseCase(repository)
    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(scheduler)
    private val error = NativeError(
        operation = NativeErrorOperation.ISSUERS,
        code = NativeErrorCode.REQUEST_TIMEOUT,
    )

    @Test
    fun `dual write gives one deterministic id to both deliveries`() {
        coEvery { repository.report(any()) } returns true
        val legacyIds = mutableListOf<String>()
        val reporter = reporter(NativeErrorDeliveryMode.DUAL_WRITE)

        reporter.track(
            error = error,
            legacyMetricFactory = { id ->
                legacyIds += id
            metric()
            },
            legacyMetricSender = {},
        )
        scheduler.advanceUntilIdle()

        assertEquals(listOf(EVENT_ID), legacyIds)
        coVerify(exactly = 1) {
            repository.report(match { it.eventId == EVENT_ID && it.occurredAt == TIMESTAMP })
        }
        reporter.close()
    }

    @Test
    fun `melidata only never queues v2 and observability only never builds legacy`() {
        coEvery { repository.report(any()) } returns true
        var legacyCount = 0
        reporter(NativeErrorDeliveryMode.MELIDATA_ONLY).apply {
            track(error, {
                legacyCount++
            metric()
            }, {})
            scheduler.advanceUntilIdle()
            close()
        }
        reporter(NativeErrorDeliveryMode.OBSERVABILITY_ONLY).apply {
            track(error, {
                legacyCount++
            metric()
            }, {})
            scheduler.advanceUntilIdle()
            close()
        }

        assertEquals(1, legacyCount)
        coVerify(exactly = 1) { repository.report(any()) }
    }

    @Test
    fun `65th pending error is dropped newest`() {
        coEvery { repository.report(any()) } returns true
        val pausedScheduler = TestCoroutineScheduler()
        val pausedDispatcher = StandardTestDispatcher(pausedScheduler)
        val reporter = MPErrorReporter(
            reportNativeError = useCase,
            deliveryMode = NativeErrorDeliveryMode.DUAL_WRITE,
            dispatcher = pausedDispatcher,
            eventIdProvider = incrementingIds(),
            timestampProvider = { TIMESTAMP },
        )

        repeat(65) { reporter.track(error, { metric() }, {}) }
        pausedScheduler.advanceUntilIdle()

        coVerify(exactly = 64) { repository.report(any()) }
        coVerify(exactly = 0) { repository.report(match { it.eventId == "event-65" }) }
        reporter.close()
    }

    @Test
    fun `legacy and v2 failures are isolated and close rejects new work`() {
        coEvery { repository.report(any()) } throws IllegalStateException("transport")
        val reporter = reporter(NativeErrorDeliveryMode.DUAL_WRITE)

        reporter.track(error, { throw IllegalStateException("legacy") }, {})
        scheduler.advanceUntilIdle()
        reporter.close()
        reporter.track(error, { metric() }, { throw IllegalStateException("closed") })
        scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { repository.report(any()) }
    }

    @Test
    fun `worker cancellation is not converted into a delivery failure`() {
        coEvery { repository.report(any()) } throws CancellationException("stop")
        val reporter = reporter(NativeErrorDeliveryMode.OBSERVABILITY_ONLY)

        reporter.track(error, { metric() }, {})
        reporter.track(error, { metric() }, {})
        scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { repository.report(any()) }
        reporter.close()
    }

    private fun reporter(mode: NativeErrorDeliveryMode) = MPErrorReporter(
        reportNativeError = useCase,
        deliveryMode = mode,
        dispatcher = dispatcher,
        eventIdProvider = { EVENT_ID },
        timestampProvider = { TIMESTAMP },
    )

    private fun incrementingIds(): () -> String {
        var current = 0
        return { "event-${++current}" }
    }

    private fun metric() = Metric(
        path = "/test/error",
        type = TrackType.EVENT,
        data = object : EventData {},
    )

    private companion object {
        const val EVENT_ID = "3f6fd694-4ba8-4f45-ae7c-871c4698aace"
        const val TIMESTAMP = "2026-08-27T12:00:00.000Z"
    }
}
