package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class CardFormAnalyticsTrackerTest {
    @Test
    fun `given isCancelling true then trackInputValidation short-circuits before isLoading`() {
        var isLoadingCalled = false
        val tracker = CardFormAnalyticsTracker(
            isCancelling = { true },
            isLoading = {
                isLoadingCalled = true
                false
            },
        )

        tracker.trackInputValidation("cvv", true)

        assertFalse(isLoadingCalled)
    }

    @Test
    fun `given isCancelling false and isLoading true then trackInputValidation returns early`() {
        var isLoadingCalled = false
        val tracker = CardFormAnalyticsTracker(
            isCancelling = { false },
            isLoading = {
                isLoadingCalled = true
                true
            },
        )

        tracker.trackInputValidation("cvv", true)

        assertTrue(isLoadingCalled)
    }

    @Test
    fun `given both false then trackInputValidation calls both lambdas`() {
        var isCancellingCalled = false
        var isLoadingCalled = false
        val tracker = CardFormAnalyticsTracker(
            isCancelling = {
                isCancellingCalled = true
                false
            },
            isLoading = {
                isLoadingCalled = true
                false
            },
        )

        tracker.trackInputValidation("card_holder", false)

        assertTrue(isCancellingCalled)
        assertTrue(isLoadingCalled)
    }

    @Test
    fun `given isCancelling true then trackDropdownSelection short-circuits before isLoading`() {
        var isLoadingCalled = false
        val tracker = CardFormAnalyticsTracker(
            isCancelling = { true },
            isLoading = {
                isLoadingCalled = true
                false
            },
        )

        tracker.trackDropdownSelection("installments")

        assertFalse(isLoadingCalled)
    }

    @Test
    fun `given isLoading true then trackDropdownSelection returns early`() {
        var isLoadingCalled = false
        val tracker = CardFormAnalyticsTracker(
            isCancelling = { false },
            isLoading = {
                isLoadingCalled = true
                true
            },
        )

        tracker.trackDropdownSelection("installments")

        assertTrue(isLoadingCalled)
    }

    @Test
    fun `given both false then trackDropdownSelection calls both lambdas`() {
        var isCancellingCalled = false
        var isLoadingCalled = false
        val tracker = CardFormAnalyticsTracker(
            isCancelling = {
                isCancellingCalled = true
                false
            },
            isLoading = {
                isLoadingCalled = true
                false
            },
        )

        tracker.trackDropdownSelection("document")

        assertTrue(isCancellingCalled)
        assertTrue(isLoadingCalled)
    }

    @Test
    fun `given error then trackInitializeError does not throw`() {
        val tracker = CardFormAnalyticsTracker(
            isCancelling = { false },
            isLoading = { false },
        )

        tracker.trackInitializeError(mockk<MercadoPagoCheckoutError>(relaxed = true))
    }

    @Test
    fun `given error then trackSubmitError does not throw`() {
        val tracker = CardFormAnalyticsTracker(
            isCancelling = { false },
            isLoading = { false },
        )

        tracker.trackSubmitError(mockk<MercadoPagoCheckoutError>(relaxed = true))
    }

    @Test
    fun `given params then trackSubmit does not throw`() {
        val tracker = CardFormAnalyticsTracker(
            isCancelling = { false },
            isLoading = { false },
        )

        tracker.trackSubmit(
            cardBrand = "visa",
            transactionAmount = 100.0,
            issuer = "issuer_1",
            paymentTypeId = "credit_card",
        )
    }
}
