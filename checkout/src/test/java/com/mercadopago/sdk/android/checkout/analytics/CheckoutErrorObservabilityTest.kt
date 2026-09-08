package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorType
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
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

internal class CheckoutErrorObservabilityTest {
    private val analytics = mockk<MPAnalytics>(relaxed = true)
    private val observability = CheckoutErrorObservability { analytics }

    @Test
    fun `track sends retained safe fields and shares reporter ID with legacy metric`() {
        val operation = slot<NativeErrorOperation>()
        val input = slot<NativeErrorInput>()
        val legacyFactory = slot<(String) -> com.mercadopago.sdk.android.analytics.domain.models.Metric>()
        every {
            analytics.trackError(capture(operation), capture(input), capture(legacyFactory))
        } returns Unit
        val observed = ObservedCheckoutError(
            publicError = MercadoPagoCheckoutError.ServiceError(
                code = ErrorCode.SERVICE_ERROR,
                messageError = "private raw service message",
                localized = ErrorLocalized.ORDER_PROCESS.name,
            ),
            nativeErrorInput = NativeErrorInput.create(NativeErrorType.SERVICE, httpStatus = 503),
        )

        observability.track(observed, NativeErrorOperation.ORDER_SUBMISSION) { eventId ->
            metricOrderError("service_error", "legacy-order-id", eventId)
        }

        assertEquals(NativeErrorOperation.ORDER_SUBMISSION, operation.captured)
        assertEquals(NativeErrorType.SERVICE, input.captured.type)
        assertEquals(503, input.captured.httpStatus)
        val legacy = legacyFactory.captured("shared-event-id").data as OrderErrorEventData
        assertEquals("shared-event-id", legacy.observabilityEventId)
        assertEquals("legacy-order-id", legacy.orderId)
    }

    @Test
    fun `cancellation is non critical and contains failures`() {
        every { analytics.trackError(any(), any(), any()) } throws IllegalStateException("reporter unavailable")

        observability.trackCancellation(NativeErrorOperation.CARD_FORM_CANCELLATION) {
            metricCardFormUserCanceledError("system_back", it)
        }

        verify(exactly = 1) {
            analytics.trackError(
                NativeErrorOperation.CARD_FORM_CANCELLATION,
                match { it.type == NativeErrorType.USER_CANCELLATION },
                any(),
            )
        }
    }
}
