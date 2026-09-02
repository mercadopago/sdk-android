package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.NativeError
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorDiagnostic
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorOperation
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.ObservedCheckoutError
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class CheckoutErrorObservabilityTest {
    private val analytics = mockk<MPAnalytics>(relaxed = true)
    private val observability = CheckoutErrorObservability { analytics }

    @Test
    fun `track sends retained safe fields and shares reporter ID with legacy metric`() {
        val nativeError = slot<NativeError>()
        val legacyFactory = slot<(String) -> com.mercadopago.sdk.android.analytics.domain.models.Metric>()
        every { analytics.trackError(capture(nativeError), capture(legacyFactory)) } returns Unit
        val observed = ObservedCheckoutError(
            publicError = MercadoPagoCheckoutError.ServiceError(
                code = ErrorCode.SERVICE_ERROR,
                messageError = "private raw service message",
                localized = ErrorLocalized.ORDER_PROCESS.name,
            ),
            nativeCode = NativeErrorCode.UPSTREAM_REJECTED,
            httpStatus = 503,
        )

        observability.track(observed, NativeErrorOperation.ORDER_SUBMISSION) { eventId ->
            metricOrderError("service_error", "legacy-order-id", eventId)
        }

        assertEquals(NativeErrorOperation.ORDER_SUBMISSION, nativeError.captured.operation)
        assertEquals(NativeErrorCode.UPSTREAM_REJECTED, nativeError.captured.code)
        assertEquals(503, nativeError.captured.statusCode)
        assertNull(nativeError.captured.requestCorrelationId)
        val legacy = legacyFactory.captured("shared-event-id").data as OrderErrorEventData
        assertEquals("shared-event-id", legacy.observabilityEventId)
        assertEquals("legacy-order-id", legacy.orderId)
    }

    @Test
    fun `cancellation is non critical and contains failures`() {
        every { analytics.trackError(any(), any()) } throws IllegalStateException("reporter unavailable")

        observability.trackCancellation(NativeErrorOperation.CARD_FORM_CANCELLATION) {
            metricCardFormUserCanceledError("system_back", it)
        }

        verify(exactly = 1) {
            analytics.trackError(
                match {
                    it.code == NativeErrorCode.USER_CANCELLED &&
                        it.diagnostic == NativeErrorDiagnostic.CANCELLED &&
                        !it.code.critical
                },
                any(),
            )
        }
    }
}
