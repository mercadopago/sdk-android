package com.mercadopago.sdk.android.analytics.observability.domain.interactor

import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorEvidenceCode
import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorType
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorDeliveryMode
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorDeliveryPolicy
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorOperation
import com.mercadopago.sdk.android.analytics.observability.domain.models.PendingNativeError
import com.mercadopago.sdk.android.analytics.observability.domain.repository.NativeErrorRepository
import com.mercadopago.sdk.android.analytics.observability.domain.usecase.ReportNativeErrorUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class MPErrorReporterTest {
    private val repository = mockk<NativeErrorRepository>()
    private val useCase = ReportNativeErrorUseCase(repository)
    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(scheduler)
    private val input = NativeErrorInput.create(NativeErrorType.REQUEST, NativeErrorEvidenceCode.TIMEOUT)

    @Test
    fun `dual write returns one deterministic id and queues v2`() {
        coEvery { repository.report(any()) } returns true
        val reporter = reporter(NativeErrorDeliveryMode.DUAL_WRITE)

        val receipt = reporter.capture(NativeErrorOperation.ISSUERS, input)
        scheduler.advanceUntilIdle()

        assertEquals(EVENT_ID, receipt.eventId)
        assertTrue(receipt.shouldSendMelidata)
        coVerify(exactly = 1) {
            repository.report(match { it.eventId == EVENT_ID && it.occurredAt == TIMESTAMP })
        }
        reporter.close()
    }

    @Test
    fun `delivery mode independently controls receipt and queue`() {
        coEvery { repository.report(any()) } returns true
        val melidata = reporter(NativeErrorDeliveryMode.MELIDATA_ONLY)
        val observability = reporter(NativeErrorDeliveryMode.OBSERVABILITY_ONLY)

        assertTrue(melidata.capture(NativeErrorOperation.ISSUERS, input).shouldSendMelidata)
        assertFalse(observability.capture(NativeErrorOperation.ISSUERS, input).shouldSendMelidata)
        scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { repository.report(any()) }
        melidata.close()
        observability.close()
    }

    @Test
    fun `all nine module mode pairs make independent delivery decisions`() {
        NativeErrorDeliveryMode.values().forEach { coreMethodsMode ->
            NativeErrorDeliveryMode.values().forEach { checkoutMode ->
                val pairScheduler = TestCoroutineScheduler()
                val deliveries = mutableListOf<PendingNativeError>()
                val recordingRepository = object : NativeErrorRepository {
                    override suspend fun report(error: PendingNativeError): Boolean {
                        deliveries += error
                        return true
                    }
                }
                val reporter = MPErrorReporter(
                    reportNativeError = ReportNativeErrorUseCase(recordingRepository),
                    deliveryPolicy = NativeErrorDeliveryPolicy(coreMethodsMode, checkoutMode),
                    dispatcher = StandardTestDispatcher(pairScheduler),
                    eventIdProvider = incrementingIds(),
                    timestampProvider = { TIMESTAMP },
                )

                val coreReceipt = reporter.capture(NativeErrorOperation.ISSUERS, input)
                val checkoutReceipt = reporter.capture(NativeErrorOperation.ORDER_SUBMISSION, input)
                pairScheduler.advanceUntilIdle()

                assertEquals(
                    coreMethodsMode != NativeErrorDeliveryMode.OBSERVABILITY_ONLY,
                    coreReceipt.shouldSendMelidata,
                )
                assertEquals(
                    checkoutMode != NativeErrorDeliveryMode.OBSERVABILITY_ONLY,
                    checkoutReceipt.shouldSendMelidata,
                )
                assertEquals(
                    coreMethodsMode != NativeErrorDeliveryMode.MELIDATA_ONLY,
                    deliveries.any { it.error.operation == NativeErrorOperation.ISSUERS },
                )
                assertEquals(
                    checkoutMode != NativeErrorDeliveryMode.MELIDATA_ONLY,
                    deliveries.any { it.error.operation == NativeErrorOperation.ORDER_SUBMISSION },
                )
                reporter.close()
            }
        }
    }

    @Test
    fun `65th pending error is dropped newest`() {
        coEvery { repository.report(any()) } returns true
        val pausedScheduler = TestCoroutineScheduler()
        val reporter = MPErrorReporter(
            reportNativeError = useCase,
            deliveryPolicy = NativeErrorDeliveryPolicy(),
            dispatcher = StandardTestDispatcher(pausedScheduler),
            eventIdProvider = incrementingIds(),
            timestampProvider = { TIMESTAMP },
        )

        repeat(65) { reporter.capture(NativeErrorOperation.ISSUERS, input) }
        pausedScheduler.advanceUntilIdle()

        coVerify(exactly = 64) { repository.report(any()) }
        coVerify(exactly = 0) { repository.report(match { it.eventId == "event-65" }) }
        reporter.close()
    }

    @Test
    fun `transport failure is contained and close rejects new work`() {
        coEvery { repository.report(any()) } throws IllegalStateException("transport")
        val reporter = reporter(NativeErrorDeliveryMode.DUAL_WRITE)

        reporter.capture(NativeErrorOperation.ISSUERS, input)
        scheduler.advanceUntilIdle()
        reporter.close()
        reporter.capture(NativeErrorOperation.ISSUERS, input)
        scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { repository.report(any()) }
    }

    @Test
    fun `worker cancellation is not converted into a delivery failure`() {
        coEvery { repository.report(any()) } throws CancellationException("stop")
        val reporter = reporter(NativeErrorDeliveryMode.OBSERVABILITY_ONLY)

        reporter.capture(NativeErrorOperation.ISSUERS, input)
        reporter.capture(NativeErrorOperation.ISSUERS, input)
        scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { repository.report(any()) }
        reporter.close()
    }

    private fun reporter(mode: NativeErrorDeliveryMode) = MPErrorReporter(
        reportNativeError = useCase,
        deliveryPolicy = NativeErrorDeliveryPolicy(coreMethods = mode, checkout = mode),
        dispatcher = dispatcher,
        eventIdProvider = { EVENT_ID },
        timestampProvider = { TIMESTAMP },
    )

    private fun incrementingIds(): () -> String {
        var current = 0
        return { "event-${++current}" }
    }

    private companion object {
        const val EVENT_ID = "3f6fd694-4ba8-4f45-ae7c-871c4698aace"
        const val TIMESTAMP = "2026-08-27T12:00:00.000Z"
    }
}
