package com.mercadopago.sdk.android.checkout.presentation.navigation

import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

internal class CheckoutNavigatorTest {
    private val navigationHost = FakeCheckoutNavigationHost()
    private val flowViewModel = CheckoutFlowViewModel()

    @Test
    fun `openReview stores context in memory and opens review destination`() {
        val params = processOrderParams()
        val navigator = makeNavigator()

        navigator.openReview(params, ReviewOrigin.OfflineMethodSelector)

        assertSame(params, flowViewModel.state.value.reviewContext?.params)
        assertEquals(ReviewOrigin.OfflineMethodSelector, flowViewModel.state.value.reviewContext?.origin)
        assertEquals(listOf<CheckoutDestination>(CheckoutDestination.ReviewConfirm), navigationHost.navigated)
    }

    @Test
    fun `navigateUpFromInstallments clears context after destination is popped`() {
        val installmentData = mockk<MPInstallmentData>()
        val paymentData = mockk<MPPaymentData>()
        val navigator = makeNavigator()
        navigator.openInstallments(installmentData, paymentData)

        navigator.navigateUpFromInstallments()

        assertEquals(1, navigationHost.popCalls)
        assertNull(flowViewModel.state.value.installmentContext)
    }

    @Test
    fun `navigateUpFromInstallments preserves context when destination is not popped`() {
        val installmentData = mockk<MPInstallmentData>()
        val paymentData = mockk<MPPaymentData>()
        val navigator = makeNavigator()
        navigator.openInstallments(installmentData, paymentData)
        navigationHost.popResult = false

        navigator.navigateUpFromInstallments()

        assertEquals(1, navigationHost.popCalls)
        assertSame(installmentData, flowViewModel.state.value.installmentContext?.installmentData)
        assertSame(paymentData, flowViewModel.state.value.installmentContext?.paymentData)
    }

    @Test
    fun `navigateUpFromReview returns to every origin and clears only review context`() {
        val methodSelectionData = mockk<MethodSelectionScreenData>()
        flowViewModel.setOfflineMethodSelectorContext(methodSelectionData)
        val navigator = makeNavigator()
        val destinationByOrigin = mapOf(
            ReviewOrigin.Payment to CheckoutDestination.Payment,
            ReviewOrigin.Form to CheckoutDestination.Form,
            ReviewOrigin.Installment to CheckoutDestination.Installment,
            ReviewOrigin.OfflineMethodSelector to CheckoutDestination.OfflineMethodSelector,
        )

        destinationByOrigin.forEach { (origin, destination) ->
            flowViewModel.setReviewContext(processOrderParams(), origin)

            navigator.navigateUpFromReview(origin)

            assertNull(flowViewModel.state.value.reviewContext)
            assertSame(methodSelectionData, flowViewModel.state.value.offlineMethodSelectorContext)
            assertEquals(destination, navigationHost.poppedTo.last())
        }
    }

    @Test
    fun `navigateUpFromReview falls back to one pop when origin is absent from stack`() {
        navigationHost.popToResult = false
        val navigator = makeNavigator()

        navigator.navigateUpFromReview(ReviewOrigin.Form)

        assertEquals(1, navigationHost.popCalls)
    }

    @Test
    fun `returnToPaymentSelector clears flow and pops to payment`() {
        flowViewModel.setReviewContext(processOrderParams(), ReviewOrigin.Form)
        flowViewModel.setOfflineMethodSelectorContext(mockk())
        val navigator = makeNavigator()

        navigator.returnToPaymentSelector()

        assertFlowIsEmpty()
        assertEquals(listOf<CheckoutDestination>(CheckoutDestination.Payment), navigationHost.poppedTo)
        assertEquals(emptyList<CheckoutDestination>(), navigationHost.navigated)
    }

    @Test
    fun `returnToPaymentSelector opens payment when it is absent from stack`() {
        navigationHost.popToResult = false
        val navigator = makeNavigator()

        navigator.returnToPaymentSelector()

        assertEquals(listOf<CheckoutDestination>(CheckoutDestination.Payment), navigationHost.navigated)
    }

    @Test
    fun `return to payment with generic error clears payloads and publishes feedback`() {
        flowViewModel.setReviewContext(processOrderParams(), ReviewOrigin.Payment)
        flowViewModel.setOfflineMethodSelectorContext(mockk())
        val navigator = makeNavigator()

        navigator.returnToPaymentSelectorWithGenericError()

        assertTransientPayloadsAreEmpty()
        assertEquals(PaymentFeedback.GenericError, flowViewModel.paymentFeedback.event.value?.feedback)
        assertEquals(listOf<CheckoutDestination>(CheckoutDestination.Payment), navigationHost.poppedTo)
    }

    @Test
    fun `finishCheckout clears transient state before notifying result`() {
        flowViewModel.setReviewContext(processOrderParams(), ReviewOrigin.Payment)
        val result = MercadoPagoCheckoutResult.Error(mockk())
        var notifiedResult: MercadoPagoCheckoutResult<*, *>? = null
        val navigator = makeNavigator(
            notifyResult = {
                assertFlowIsEmpty()
                notifiedResult = it
            },
        )

        navigator.finishCheckout(result)

        assertSame(result, notifiedResult)
    }

    @Test
    fun `finishForEmailChange clears flow dismisses checkout and then invokes callback`() {
        flowViewModel.setReviewContext(processOrderParams(), ReviewOrigin.Payment)
        val calls = mutableListOf<String>()
        val navigator = makeNavigator(
            dismissCheckout = {
                assertFlowIsEmpty()
                calls += "dismiss"
            },
        )

        navigator.finishForEmailChange { calls += "email" }

        assertEquals(listOf("dismiss", "email"), calls)
    }

    @Test
    fun `recoverTo clears flow and restores configured start destination`() {
        flowViewModel.setReviewContext(processOrderParams(), ReviewOrigin.Installment)
        navigationHost.popToResult = false
        val navigator = makeNavigator()

        navigator.recoverTo(CheckoutDestination.Form)

        assertFlowIsEmpty()
        assertEquals(listOf<CheckoutDestination>(CheckoutDestination.Form), navigationHost.poppedTo)
        assertEquals(listOf<CheckoutDestination>(CheckoutDestination.Form), navigationHost.navigated)
    }

    private fun makeNavigator(
        notifyResult: (MercadoPagoCheckoutResult<*, *>) -> Unit = {},
        dismissCheckout: () -> Unit = {},
    ) = CheckoutNavigator(
        navigationHost = navigationHost,
        flowViewModel = flowViewModel,
        notifyResult = notifyResult,
        dismissCheckout = dismissCheckout,
    )

    private fun assertFlowIsEmpty() {
        assertTransientPayloadsAreEmpty()
        assertNull(flowViewModel.paymentFeedback.event.value)
    }

    private fun assertTransientPayloadsAreEmpty() {
        val state = flowViewModel.state.value
        assertNull(state.installmentContext)
        assertNull(state.securityCodeContext)
        assertNull(state.reviewContext)
        assertNull(state.offlineMethodSelectorContext)
    }

    private fun processOrderParams() = ProcessOrderParams(
        orderId = "order-id",
        clientToken = "client-token",
        paymentMethodId = "visa",
        paymentMethodType = "credit_card",
        token = "payment-token",
        installments = 1,
        amount = "100.00",
    )

    private class FakeCheckoutNavigationHost : CheckoutNavigationHost {
        val navigated = mutableListOf<CheckoutDestination>()
        val poppedTo = mutableListOf<CheckoutDestination>()
        var popToResult = true
        var popResult = true
        var popCalls = 0

        override fun navigate(
            destination: CheckoutDestination,
        ) {
            navigated += destination
        }

        override fun pop(): Boolean {
            popCalls++
            return popResult
        }

        override fun popTo(
            destination: CheckoutDestination,
        ): Boolean {
            poppedTo += destination
            return popToResult
        }
    }
}
