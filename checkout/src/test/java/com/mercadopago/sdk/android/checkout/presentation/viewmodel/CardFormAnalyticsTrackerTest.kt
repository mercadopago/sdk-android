package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.observability.domain.interactor.NativeErrorReceipt
import com.mercadopago.sdk.android.analytics.observability.domain.interactor.NativeErrorReporting
import com.mercadopago.sdk.android.analytics.observability.domain.models.NativeErrorOperation
import com.mercadopago.sdk.android.checkout.domain.model.ObservedCheckoutError
import com.mercadopago.sdk.android.checkout.presentation.model.CancelReason
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class CardFormAnalyticsTrackerTest {
    @Test
    fun `given trackUserCanceled was called then trackInputValidation short-circuits before isLoading`() {
        var isLoadingCalled = false
        val tracker = CardFormAnalyticsTracker(
            isLoading = {
                isLoadingCalled = true
                false
            },
        )
        tracker.trackUserCanceled(CancelReason.SystemBack)

        tracker.trackInputValidation("cvv", true)

        assertFalse(isLoadingCalled)
    }

    @Test
    fun `given not canceled and isLoading true then trackInputValidation returns early`() {
        var isLoadingCalled = false
        val tracker = CardFormAnalyticsTracker(
            isLoading = {
                isLoadingCalled = true
                true
            },
        )

        tracker.trackInputValidation("cvv", true)

        assertTrue(isLoadingCalled)
    }

    @Test
    fun `given not canceled and not loading then trackInputValidation calls isLoading`() {
        var isLoadingCalled = false
        val tracker = CardFormAnalyticsTracker(
            isLoading = {
                isLoadingCalled = true
                false
            },
        )

        tracker.trackInputValidation("card_holder", false)

        assertTrue(isLoadingCalled)
    }

    @Test
    fun `given trackUserCanceled was called then trackDropdownSelection short-circuits before isLoading`() {
        var isLoadingCalled = false
        val tracker = CardFormAnalyticsTracker(
            isLoading = {
                isLoadingCalled = true
                false
            },
        )
        tracker.trackUserCanceled(CancelReason.SystemBack)

        tracker.trackDropdownSelection("installments")

        assertFalse(isLoadingCalled)
    }

    @Test
    fun `given isLoading true then trackDropdownSelection returns early`() {
        var isLoadingCalled = false
        val tracker = CardFormAnalyticsTracker(
            isLoading = {
                isLoadingCalled = true
                true
            },
        )

        tracker.trackDropdownSelection("installments")

        assertTrue(isLoadingCalled)
    }

    @Test
    fun `given not canceled and not loading then trackDropdownSelection calls isLoading`() {
        var isLoadingCalled = false
        val tracker = CardFormAnalyticsTracker(
            isLoading = {
                isLoadingCalled = true
                false
            },
        )

        tracker.trackDropdownSelection("document")

        assertTrue(isLoadingCalled)
    }

    @Test
    fun `given error then trackInitializeError does not throw`() {
        val tracker = CardFormAnalyticsTracker(isLoading = { false })

        tracker.trackInitializeError(mockk<ObservedCheckoutError>(relaxed = true))
    }

    @Test
    fun `given error then trackSubmitError does not throw`() {
        val tracker = CardFormAnalyticsTracker(isLoading = { false })

        tracker.trackSubmitError(mockk<ObservedCheckoutError>(relaxed = true))
    }

    @Test
    fun `given params then trackSubmit does not throw`() {
        val tracker = CardFormAnalyticsTracker(isLoading = { false })

        tracker.trackSubmit(
            cardBrand = "visa",
            transactionAmount = 100.0,
            issuer = "issuer_1",
            paymentTypeId = "credit_card",
        )
    }

    @Test
    fun `given card form cancellation then it is reported through the restricted boundary once`() {
        val reporter = RecordingReporter()
        val tracker = CardFormAnalyticsTracker(
            isLoading = { false },
            nativeErrorReporter = { reporter },
        )

        tracker.trackUserCanceled(CancelReason.SystemBack)

        assertEquals(listOf(NativeErrorOperation.CARD_FORM_CANCELLATION), reporter.operations)
    }

    private class RecordingReporter : NativeErrorReporting {
        val operations = mutableListOf<NativeErrorOperation>()

        override fun capture(
            operation: NativeErrorOperation,
            input: NativeErrorInput,
        ): NativeErrorReceipt {
            operations += operation
            return NativeErrorReceipt(eventId = "event-1", shouldSendMelidata = true)
        }

        override fun close() = Unit
    }
}
