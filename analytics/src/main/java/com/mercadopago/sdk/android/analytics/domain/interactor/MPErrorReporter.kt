package com.mercadopago.sdk.android.analytics.domain.interactor

import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.NativeError
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorDeliveryMode
import com.mercadopago.sdk.android.analytics.domain.models.PendingNativeError
import com.mercadopago.sdk.android.analytics.domain.usecase.ReportNativeErrorUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

@Suppress("DEPRECATION", "DEPRECATION_ERROR")
internal class MPErrorReporter(
    private val reportNativeError: ReportNativeErrorUseCase,
    private val deliveryMode: NativeErrorDeliveryMode,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val eventIdProvider: () -> String = { UUID.randomUUID().toString() },
    private val timestampProvider: () -> String = ::utcTimestamp,
) {
    private val channel = Channel<PendingNativeError>(REPORT_BUFFER_CAPACITY)
    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    init {
        scope.launch {
            for (pendingError in channel) {
                try {
                    reportNativeError(pendingError)
                } catch (cancellation: CancellationException) {
                    throw cancellation
                } catch (_: Throwable) {
                    // Best effort by design: transport failures never escape the worker.
                }
            }
        }
    }

    fun track(
        error: NativeError,
        legacyMetricFactory: (String) -> Metric,
        legacyMetricSender: (Metric) -> Unit,
    ) {
        try {
            val eventId = eventIdProvider()
            val pending = PendingNativeError(
                eventId = eventId,
                occurredAt = timestampProvider(),
                error = error,
            )
            if (deliveryMode != NativeErrorDeliveryMode.OBSERVABILITY_ONLY) {
                try {
                    legacyMetricSender(legacyMetricFactory(eventId))
                } catch (_: Throwable) {
                    // Legacy analytics is isolated from v2 delivery.
                }
            }
            if (deliveryMode != NativeErrorDeliveryMode.MELIDATA_ONLY) {
                try {
                    channel.offer(pending)
                } catch (_: Throwable) {
                    // A closed/full reporter drops newest without changing public behavior.
                }
            }
        } catch (_: Throwable) {
            // ID/time creation and any injected test implementation remain non-throwing.
        }
    }

    fun close() {
        channel.close()
        scope.cancel()
    }

    private companion object {
        const val REPORT_BUFFER_CAPACITY = 64

        fun utcTimestamp(): String = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.US,
        ).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date())
    }
}
