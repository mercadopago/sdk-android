package com.mercadopago.sdk.android.analytics.observability.domain.interactor

// @spec 20260825-native-coremethods-checkout-observability#DD-5

import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorFactory
import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorDeliveryMode
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorDeliveryPolicy
import com.mercadopago.sdk.android.analytics.observability.domain.models.PendingNativeError
import com.mercadopago.sdk.android.analytics.observability.domain.usecase.ReportNativeErrorUseCase
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

internal class MPErrorReporter(
    private val reportNativeError: ReportNativeErrorUseCase,
    private val errorFactory: NativeErrorFactory = NativeErrorFactory(),
    private val deliveryPolicy: NativeErrorDeliveryPolicy,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val eventIdProvider: () -> String = { UUID.randomUUID().toString() },
    private val timestampProvider: () -> String = ::utcTimestamp,
) : NativeErrorReporting {
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

    override fun capture(
        operation: com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorOperation,
        input: NativeErrorInput,
    ): NativeErrorReceipt = try {
        val error = errorFactory.from(operation, input)
        when (deliveryPolicy.modeFor(operation.module)) {
            NativeErrorDeliveryMode.MELIDATA_ONLY -> NativeErrorReceipt.MELIDATA_FALLBACK
            NativeErrorDeliveryMode.DUAL_WRITE -> NativeErrorReceipt(
                eventId = enqueueOrNull(PendingNativeError(eventIdProvider(), timestampProvider(), error)),
                shouldSendMelidata = true,
            )

            NativeErrorDeliveryMode.OBSERVABILITY_ONLY -> {
                val eventId = enqueueOrNull(PendingNativeError(eventIdProvider(), timestampProvider(), error))
                // If the queue rejects the error, preserve delivery through Melidata.
                NativeErrorReceipt(eventId = eventId, shouldSendMelidata = eventId == null)
            }
        }
    } catch (_: Throwable) {
        NativeErrorReceipt.MELIDATA_FALLBACK
    }

    override fun close() {
        channel.close()
        scope.cancel()
    }

    private fun enqueueOrNull(error: PendingNativeError): String? =
        error.eventId.takeIf { channel.trySend(error).isSuccess }

    private companion object {
        const val REPORT_BUFFER_CAPACITY = 64
        val UTC_TIMESTAMP_FORMATTER = object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                Locale.US,
            ).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
        }

        fun utcTimestamp(): String = checkNotNull(UTC_TIMESTAMP_FORMATTER.get()).format(Date())
    }
}
