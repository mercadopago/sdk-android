package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorType
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.ObservedCheckoutError
import com.mercadopago.sdk.android.checkout.presentation.model.CancelReason
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class CardFormAnalyticsTrackerTest {
    private val observedError = ObservedCheckoutError(
        publicError = mockk<MercadoPagoCheckoutError>(relaxed = true),
        nativeErrorInput = NativeErrorInput.create(NativeErrorType.UNKNOWN),
    )

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

        tracker.trackInitializeError(observedError)
    }

    @Test
    fun `given error then trackSubmitError does not throw`() {
        val tracker = CardFormAnalyticsTracker(isLoading = { false })

        tracker.trackSubmitError(observedError)
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
}
