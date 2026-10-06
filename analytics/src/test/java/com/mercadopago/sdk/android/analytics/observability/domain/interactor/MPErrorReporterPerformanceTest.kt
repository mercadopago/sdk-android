package com.mercadopago.sdk.android.analytics.observability.domain.interactor

import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorEvidenceCode
import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorType
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorDeliveryMode
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorDeliveryPolicy
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorOperation
import com.mercadopago.sdk.android.analytics.observability.domain.repository.NativeErrorRepository
import com.mercadopago.sdk.android.analytics.observability.domain.usecase.ReportNativeErrorUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.Test
import kotlin.test.assertTrue

/** Opt-in caller-path benchmark for `:analytics:nativeErrorReporterPerformanceTest`. */
internal class MPErrorReporterPerformanceTest {
    private val repository = mockk<NativeErrorRepository>()
    private val input = NativeErrorInput.create(NativeErrorType.REQUEST, NativeErrorEvidenceCode.TIMEOUT)

    @Test
    fun `caller path p95 stays below one millisecond under queue pressure`() {
        coEvery { repository.report(any()) } returns true
        val reporter = MPErrorReporter(
            reportNativeError = ReportNativeErrorUseCase(repository),
            deliveryPolicy = NativeErrorDeliveryPolicy(
                coreMethods = NativeErrorDeliveryMode.OBSERVABILITY_ONLY,
            ),
            dispatcher = StandardTestDispatcher(TestCoroutineScheduler()),
        )

        repeat(PERFORMANCE_WARMUP_ITERATIONS) {
            reporter.capture(NativeErrorOperation.ISSUERS, input)
        }
        val samples = List(PERFORMANCE_SAMPLE_COUNT) {
            val startedAt = System.nanoTime()
            reporter.capture(NativeErrorOperation.ISSUERS, input)
            System.nanoTime() - startedAt
        }.sorted()
        val p95Nanos = samples[(samples.size * 95 / 100) - 1]

        assertTrue(
            p95Nanos < CALLER_PATH_P95_LIMIT_NANOS,
            "Expected p95 below 1ms but was ${p95Nanos / NANOS_PER_MILLISECOND.toDouble()}ms",
        )
        reporter.close()
    }

    private companion object {
        const val PERFORMANCE_WARMUP_ITERATIONS = 200
        const val PERFORMANCE_SAMPLE_COUNT = 2_000
        const val NANOS_PER_MILLISECOND = 1_000_000L
        const val CALLER_PATH_P95_LIMIT_NANOS = NANOS_PER_MILLISECOND
    }
}
