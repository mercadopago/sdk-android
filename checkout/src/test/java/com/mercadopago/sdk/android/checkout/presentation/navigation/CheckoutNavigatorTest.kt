package com.mercadopago.sdk.android.checkout.presentation.navigation

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.ScreenConfig
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig
import io.mockk.mockk
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

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
    fun `openInstallments stores the given origin in the installment context`() {
        // Given
        val installmentData = mockk<MPInstallmentData>()
        val paymentData = mockk<MPPaymentData>()
        val navigator = makeNavigator()

        // When
        navigator.openInstallments(installmentData, paymentData, ReviewOrigin.Payment)

        // Then
        assertEquals(ReviewOrigin.Payment, flowViewModel.state.value.installmentContext?.origin)
    }

    @Test
    fun `navigateUpFromInstallments clears context after destination is popped`() {
        val installmentData = mockk<MPInstallmentData>()
        val paymentData = mockk<MPPaymentData>()
        val navigator = makeNavigator()
        navigator.openInstallments(installmentData, paymentData, ReviewOrigin.Form)

        navigator.navigateUpFromInstallments()

        assertEquals(1, navigationHost.popCalls)
        assertNull(flowViewModel.state.value.installmentContext)
    }

    @Test
    fun `given installments opened from security code when navigating back then reopens security code`() {
        // Given
        val config = mockk<SecurityCodeScreenConfig>()
        val navigator = makeNavigator()
        navigator.openSecurityCode(config)
        navigator.completeSecurityCode { true }
        navigator.openInstallments(mockk(), mockk(), ReviewOrigin.Form)
        navigationHost.navigated.clear()
        navigationHost.popCalls = 0

        // When
        navigator.navigateUpFromInstallments()

        // Then
        assertEquals(1, navigationHost.popCalls)
        assertNull(flowViewModel.state.value.installmentContext)
        assertSame(config, flowViewModel.state.value.securityCodeContext)
        assertEquals(listOf<CheckoutDestination>(CheckoutDestination.SecurityCode), navigationHost.navigated)
    }

    @Test
    fun `given security code completed without installments when navigating up then clears context`() {
        // Given
        val config = mockk<SecurityCodeScreenConfig>()
        val navigator = makeNavigator()
        navigator.openSecurityCode(config)

        // When
        navigator.completeSecurityCode { false }

        // Then
        assertEquals(1, navigationHost.popCalls)
        assertNull(flowViewModel.state.value.securityCodeContext)
    }

    @Test
    fun `given security code when destination is not popped then does not process or clear context`() {
        // Given
        val config = mockk<SecurityCodeScreenConfig>()
        val navigator = makeNavigator()
        var wasProcessed = false
        navigator.openSecurityCode(config)
        navigationHost.popResult = false

        // When
        navigator.completeSecurityCode {
            wasProcessed = true
            true
        }

        // Then
        assertEquals(1, navigationHost.popCalls)
        assertFalse(wasProcessed)
        assertSame(config, flowViewModel.state.value.securityCodeContext)
    }

    @Test
    fun `navigateUpFromInstallments preserves context when destination is not popped`() {
        // Given
        val installmentData = mockk<MPInstallmentData>()
        val paymentData = mockk<MPPaymentData>()
        val config = mockk<SecurityCodeScreenConfig>()
        val navigator = makeNavigator()
        flowViewModel.setSecurityCodeContext(config)
        navigator.openInstallments(installmentData, paymentData, ReviewOrigin.Form)
        navigationHost.navigated.clear()
        navigationHost.popResult = false

        // When
        navigator.navigateUpFromInstallments()

        // Then
        assertEquals(1, navigationHost.popCalls)
        assertSame(installmentData, flowViewModel.state.value.installmentContext?.installmentData)
        assertSame(paymentData, flowViewModel.state.value.installmentContext?.paymentData)
        assertSame(config, flowViewModel.state.value.installmentContext?.securityCodeConfig)
        assertNull(flowViewModel.state.value.securityCodeContext)
        assertEquals(emptyList<CheckoutDestination>(), navigationHost.navigated)
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
    fun `given no status screen opt-in when finishCheckout succeeds then it notifies and finishes normally`() {
        // Given
        val result = MercadoPagoCheckoutResult.Success(
            MPPaymentData.Payment(
                orderId = "order-1",
                orderStatus = "approved",
                paymentMethodId = "",
                paymentTypeId = "",
            ),
        )
        var notifiedResult: MercadoPagoCheckoutResult<*, *>? = null
        val navigator = makeNavigator(notifyResult = { notifiedResult = it })

        // When
        navigator.finishCheckout(result, processOrderParams())

        // Then
        assertSame(result, notifiedResult)
        assertNull(flowViewModel.state.value.statusScreenContext)
        assertEquals(emptyList<CheckoutDestination>(), navigationHost.navigated)
    }

    @Test
    fun `given status screen opt-in when finishCheckout succeeds then it keeps the host open and opens it`() {
        // Given
        val result = MercadoPagoCheckoutResult.Success(
            MPPaymentData.Payment(
                orderId = "order-1",
                orderStatus = "approved",
                paymentMethodId = "",
                paymentTypeId = "",
            ),
        )
        var keptOpenResult: MercadoPagoCheckoutResult<*, *>? = null
        var finishResultCalled = false
        val navigator = makeNavigator(
            checkoutConfiguration = statusScreenConfiguration(),
            notifyResult = { finishResultCalled = true },
            notifyResultKeepingHostOpen = { keptOpenResult = it },
        )

        // When
        navigator.finishCheckout(result, processOrderParams().copy(checkoutType = "payment"))

        // Then
        assertSame(result, keptOpenResult)
        assertFalse(finishResultCalled)
        assertEquals("order-1", flowViewModel.state.value.statusScreenContext?.orderId)
        assertEquals("client-token", flowViewModel.state.value.statusScreenContext?.clientToken)
        assertEquals("payment", flowViewModel.state.value.statusScreenContext?.checkoutType)
        assertEquals(listOf<CheckoutDestination>(CheckoutDestination.StatusScreen), navigationHost.navigated)
    }

    @Test
    fun `given a processed payment when finishCheckout opens Status Screen then the context keeps it`() {
        // Given
        val paymentProcessed = OrderProcessOutput.PaymentProcessed(
            id = "PAY_1",
            status = "action_required",
            statusDetail = "waiting_payment",
            amount = BigDecimal("900.00"),
            paymentMethod = OrderProcessOutput.PaymentProcessedMethod(
                id = "rapipago",
                type = "ticket",
                installments = null,
                barcodeContent = "34191.75537",
                ticketUrl = "https://example.com/ticket",
                redirectUrl = null,
            ),
        )
        flowViewModel.setReviewContext(processOrderParams(), ReviewOrigin.Payment)
        val result = MercadoPagoCheckoutResult.Success(
            MPPaymentData.Payment(
                orderId = "order-1",
                orderStatus = "action_required",
                paymentMethodId = "",
                paymentTypeId = "",
            ),
        )
        val navigator = makeNavigator(checkoutConfiguration = statusScreenConfiguration())

        // When
        navigator.finishCheckout(result, processOrderParams(), paymentProcessed)

        // Then
        assertNull(flowViewModel.state.value.reviewContext)
        assertSame(paymentProcessed, flowViewModel.state.value.statusScreenContext?.paymentProcessed)
    }

    @Test
    fun `given no processed payment when finishCheckout opens Status Screen then the context carries none`() {
        // Given
        val result = MercadoPagoCheckoutResult.Success(
            MPPaymentData.Payment(
                orderId = "order-1",
                orderStatus = "approved",
                paymentMethodId = "",
                paymentTypeId = "",
            ),
        )
        val navigator = makeNavigator(checkoutConfiguration = statusScreenConfiguration())

        // When
        navigator.finishCheckout(result, processOrderParams())

        // Then
        assertNull(flowViewModel.state.value.statusScreenContext?.paymentProcessed)
    }

    @Test
    fun `given a processed order when finishCheckout opens Status Screen then the context carries the order status`() {
        // Given
        val result = MercadoPagoCheckoutResult.Success(
            MPPaymentData.Payment(
                orderId = "order-1",
                orderStatus = "action_required",
                paymentMethodId = "",
                paymentTypeId = "",
            ),
        )
        val navigator = makeNavigator(checkoutConfiguration = statusScreenConfiguration())

        // When
        navigator.finishCheckout(result, processOrderParams())

        // Then
        assertEquals("action_required", flowViewModel.state.value.statusScreenContext?.orderStatus)
    }

    @Test
    fun `given status screen opt-in when finishCheckout is another Error then it notifies and finishes normally`() {
        // Given
        val result = MercadoPagoCheckoutResult.Error(serviceError(ErrorCode.SERVICE_ERROR))
        var notifiedResult: MercadoPagoCheckoutResult<*, *>? = null
        val navigator = makeNavigator(
            checkoutConfiguration = statusScreenConfiguration(),
            notifyResult = { notifiedResult = it },
        )

        // When
        navigator.finishCheckout(result, processOrderParams())

        // Then
        assertSame(result, notifiedResult)
        assertNull(flowViewModel.state.value.statusScreenContext)
    }

    @Test
    fun `given status screen opt-in when the order is rejected then it reports the error and opens Status Screen`() {
        // Given
        val result = MercadoPagoCheckoutResult.Error(serviceError(ErrorCode.PAYMENT_REJECTED))
        var keptOpenResult: MercadoPagoCheckoutResult<*, *>? = null
        var finishResultCalled = false
        val navigator = makeNavigator(
            checkoutConfiguration = statusScreenConfiguration(),
            notifyResult = { finishResultCalled = true },
            notifyResultKeepingHostOpen = { keptOpenResult = it },
        )

        // When
        navigator.finishCheckout(result, processOrderParams())

        // Then
        assertSame(result, keptOpenResult)
        assertFalse(finishResultCalled)
        assertEquals("order-id", flowViewModel.state.value.statusScreenContext?.orderId)
        assertEquals(listOf<CheckoutDestination>(CheckoutDestination.StatusScreen), navigationHost.navigated)
    }

    @Test
    fun `given a process error then it reports the error before opening Status Screen`() {
        // Given
        val error = serviceError(ErrorCode.SERVICE_ERROR)
        val calls = mutableListOf<String>()
        var keptOpenResult: MercadoPagoCheckoutResult<*, *>? = null
        var finishResultCalled = false
        val navigator = makeNavigator(
            checkoutConfiguration = statusScreenConfiguration(),
            notifyResult = { finishResultCalled = true },
            notifyResultKeepingHostOpen = {
                keptOpenResult = it
                calls += "notify"
                assertTrue(navigationHost.navigated.isEmpty())
            },
        )

        // When
        navigator.navigateToStatusScreenForResolution(processOrderParams(), error)

        // Then
        assertEquals(MercadoPagoCheckoutResult.Error(error), keptOpenResult)
        assertFalse(finishResultCalled)
        assertEquals(listOf("notify"), calls)
        assertEquals(listOf<CheckoutDestination>(CheckoutDestination.StatusScreen), navigationHost.navigated)
    }

    @Test
    fun `navigateToStatusScreenForResolution clears the flow and opens Status Screen without notifying`() {
        // Given
        flowViewModel.setReviewContext(processOrderParams(), ReviewOrigin.Payment)
        var notified = false
        val navigator = makeNavigator(
            checkoutConfiguration = statusScreenConfiguration(),
            notifyResult = { notified = true },
        )

        // When
        navigator.navigateToStatusScreenForResolution(processOrderParams())

        // Then
        assertFalse(notified)
        assertNull(flowViewModel.state.value.reviewContext)
        assertEquals("order-id", flowViewModel.state.value.statusScreenContext?.orderId)
        assertEquals(listOf<CheckoutDestination>(CheckoutDestination.StatusScreen), navigationHost.navigated)
    }

    @Test
    fun `given a process status detail when navigating for resolution then Status Screen gets a rejected payment`() {
        // Given
        val navigator = makeNavigator(checkoutConfiguration = statusScreenConfiguration())

        // When
        navigator.navigateToStatusScreenForResolution(
            processOrderParams(),
            processStatusDetail = "cc_rejected_other_reason",
        )

        // Then
        val payment = flowViewModel.state.value.statusScreenContext?.paymentProcessed
        assertEquals("rejected", payment?.status)
        assertEquals("cc_rejected_other_reason", payment?.statusDetail)
    }

    @Test
    fun `given a process error without detail when resolving then Status Screen gets an error payment`() {
        // Given
        val navigator = makeNavigator(checkoutConfiguration = statusScreenConfiguration())

        // When
        navigator.navigateToStatusScreenForResolution(
            processOrderParams(),
            processError = serviceError(ErrorCode.SERVICE_ERROR),
        )

        // Then
        assertEquals("error", flowViewModel.state.value.statusScreenContext?.paymentProcessed?.status)
    }

    @Test
    fun `given a process error with detail when resolving then Status Screen gets a rejected payment`() {
        // Given
        val navigator = makeNavigator(checkoutConfiguration = statusScreenConfiguration())

        // When
        navigator.navigateToStatusScreenForResolution(
            processOrderParams(),
            processError = serviceError(ErrorCode.SERVICE_ERROR),
            processStatusDetail = "cc_rejected_other_reason",
        )

        // Then
        assertEquals("rejected", flowViewModel.state.value.statusScreenContext?.paymentProcessed?.status)
    }

    @Test
    fun `given no process status detail when navigating for resolution then Status Screen has no cached payment`() {
        // Given
        val navigator = makeNavigator(checkoutConfiguration = statusScreenConfiguration())

        // When
        navigator.navigateToStatusScreenForResolution(processOrderParams())

        // Then
        assertNull(flowViewModel.state.value.statusScreenContext?.paymentProcessed)
    }

    @Test
    fun `closeStatusScreen clears the status screen context dismisses and then invokes onExit`() {
        // Given
        flowViewModel.setStatusScreenContext(
            StatusScreenContext(
                orderId = "order-id",
                clientToken = "client-token",
                lastFourDigits = null,
                sellerInfo = null,
                checkoutType = null,
            ),
        )
        val calls = mutableListOf<String>()
        val navigator = makeNavigator(
            dismissCheckout = {
                assertNull(flowViewModel.state.value.statusScreenContext)
                calls += "dismiss"
                true
            },
        )

        // When
        navigator.closeStatusScreen { calls += "exit" }

        // Then
        assertEquals(listOf("dismiss", "exit"), calls)
    }

    @Test
    fun `closeStatusScreen tolerates a null onExit callback`() {
        val navigator = makeNavigator()

        navigator.closeStatusScreen(null)
    }

    @Test
    fun `given the host already finished when closeStatusScreen is called again then onExit is not invoked`() {
        // Given
        var exitInvokeCount = 0
        val navigator = makeNavigator(dismissCheckout = { false })

        // When
        navigator.closeStatusScreen { exitInvokeCount++ }

        // Then
        assertEquals(0, exitInvokeCount)
    }

    @Test
    fun `given status screen opt-in when finishCheckout succeeds then it notifies before opening Status Screen`() {
        // Given
        val result = MercadoPagoCheckoutResult.Success(
            MPPaymentData.Payment(
                orderId = "order-1",
                orderStatus = "approved",
                paymentMethodId = "",
                paymentTypeId = "",
            ),
        )
        val calls = mutableListOf<String>()
        val navigator = makeNavigator(
            checkoutConfiguration = statusScreenConfiguration(),
            notifyResultKeepingHostOpen = { calls += "notify" },
        )
        navigationHost.onNavigate = { calls += "navigate" }

        // When
        navigator.finishCheckout(result, processOrderParams())

        // Then
        assertEquals(listOf("notify", "navigate"), calls)
    }

    @Test
    fun `finishForEmailChange clears flow dismisses checkout and then invokes callback`() {
        flowViewModel.setReviewContext(processOrderParams(), ReviewOrigin.Payment)
        val calls = mutableListOf<String>()
        val navigator = makeNavigator(
            dismissCheckout = {
                assertFlowIsEmpty()
                calls += "dismiss"
                true
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
        checkoutConfiguration: CheckoutConfiguration? = null,
        notifyResult: (MercadoPagoCheckoutResult<*, *>) -> Unit = {},
        dismissCheckout: () -> Boolean = { true },
        notifyResultKeepingHostOpen: (MercadoPagoCheckoutResult<*, *>) -> Unit = {},
    ) = CheckoutNavigator(
        navigationHost = navigationHost,
        flowViewModel = flowViewModel,
        checkoutConfiguration = checkoutConfiguration,
        notifyResult = notifyResult,
        dismissCheckout = dismissCheckout,
        notifyResultKeepingHostOpen = notifyResultKeepingHostOpen,
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
        assertNull(state.statusScreenContext)
    }

    private fun serviceError(
        code: ErrorCode,
    ) = MercadoPagoCheckoutError.ServiceError(code = code, messageError = "", localized = "ORDER_PROCESS")

    private fun processOrderParams() = ProcessOrderParams(
        orderId = "order-id",
        clientToken = "client-token",
        paymentMethodId = "visa",
        paymentMethodType = "credit_card",
        token = "payment-token",
        installments = 1,
        amount = "100.00",
    )

    private fun statusScreenConfiguration() = CheckoutConfiguration(
        checkoutType = MPCheckoutType.Payment(MPOrder(orderId = "order-id", clientToken = "client-token")),
        paymentMethodConfigs = emptyList(),
        screenConfigs = listOf(ScreenConfig.StatusScreen()),
    )

    private class FakeCheckoutNavigationHost : CheckoutNavigationHost {
        val navigated = mutableListOf<CheckoutDestination>()
        val poppedTo = mutableListOf<CheckoutDestination>()
        var popToResult = true
        var popResult = true
        var popCalls = 0
        var onNavigate: (CheckoutDestination) -> Unit = {}

        override fun navigate(
            destination: CheckoutDestination,
        ) {
            navigated += destination
            onNavigate(destination)
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
