package com.mercadopago.sdk.android.analytics.domain.interactor

import com.mercadopago.sdk.android.analytics.domain.models.EventData
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.NativeError
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorDeliveryMode
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorDeliveryPolicy
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorOperation
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.analytics.domain.repository.NativeErrorRepository
import com.mercadopago.sdk.android.analytics.domain.usecase.ReportNativeErrorUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.Test
import kotlin.test.assertTrue

/**
 * Opt-in caller-path benchmark. Run on a controlled host with
 * `./gradlew :analytics:nativeErrorReporterPerformanceTest`.
 */
internal class MPErrorReporterPerformanceTest {
    private val repository = mockk<NativeErrorRepository>()
    private val useCase = ReportNativeErrorUseCase(repository)
    private val error = NativeError(
        operation = NativeErrorOperation.ISSUERS,
        code = NativeErrorCode.REQUEST_TIMEOUT,
    )

    @Test
    fun `caller path p95 stays below one millisecond under queue pressure`() {
        coEvery { repository.report(any()) } returns true
        val reporter = MPErrorReporter(
            reportNativeError = useCase,
            deliveryPolicy = NativeErrorDeliveryPolicy(
                coreMethods = NativeErrorDeliveryMode.OBSERVABILITY_ONLY,
            ),
            dispatcher = StandardTestDispatcher(TestCoroutineScheduler()),
        )

        repeat(PERFORMANCE_WARMUP_ITERATIONS) {
            reporter.track(error, { metric() }, {})
        }
        val samples = List(PERFORMANCE_SAMPLE_COUNT) {
            val startedAt = System.nanoTime()
            reporter.track(error, { metric() }, {})
            System.nanoTime() - startedAt
        }.sorted()
        val p95Nanos = samples[(samples.size * 95 / 100) - 1]

        assertTrue(
            p95Nanos < CALLER_PATH_P95_LIMIT_NANOS,
            "Expected p95 below 1ms but was ${p95Nanos / NANOS_PER_MILLISECOND.toDouble()}ms",
        )
        reporter.close()
    }

    private fun metric() = Metric(
        path = "/test/error",
        type = TrackType.EVENT,
        data = object : EventData {},
    )

    private companion object {
        const val PERFORMANCE_WARMUP_ITERATIONS = 200
        const val PERFORMANCE_SAMPLE_COUNT = 2_000
        const val NANOS_PER_MILLISECOND = 1_000_000L
        const val CALLER_PATH_P95_LIMIT_NANOS = NANOS_PER_MILLISECOND
    }
}
