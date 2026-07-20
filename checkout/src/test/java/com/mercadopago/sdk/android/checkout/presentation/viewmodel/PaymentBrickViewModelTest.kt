package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.CardDataOutput
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickFooterOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentMethodOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentSectionOutput
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeFieldOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeScreenOutput
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickInitializationParams
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchPaymentBrickInitializationUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetSecurityCodeScreenUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.ProcessOrderUseCase
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
internal class PaymentBrickViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fetchUseCase = mockk<FetchPaymentBrickInitializationUseCase>()
    private val processUseCase = mockk<ProcessOrderUseCase>()

    @Before
    fun setUp() {
        mockkObject(CheckoutCallbackHolder)
    }

    @After
    fun tearDown() {
        unmockkObject(CheckoutCallbackHolder)
    }

    private fun paymentConfig(
        orderId: String = "ORDER_123",
    ) = CheckoutConfiguration(
        checkoutType = MPCheckoutType.Payment(
            order = MPOrder(
                orderId = orderId,
                clientToken = "client_token",
            ),
        ),
        paymentMethodConfigs = emptyList(),
    )

    private fun viewModel(
        config: CheckoutConfiguration? = paymentConfig(),
    ) =
        PaymentBrickViewModel(
            checkoutConfiguration = config,
            fetchInitializationUseCase = fetchUseCase,
            processOrderUseCase = processUseCase,
            getSecurityCodeScreenUseCase = GetSecurityCodeScreenUseCase(),
        )

    private fun savedCardWithCvuMethod(
        cardId: String = "CVV_CARD",
    ) = PaymentMethodOutput(
        type = "saved_card",
        title = "Visa **** 5678",
        cardData = CardDataOutput(
            id = cardId,
            bin = "503143",
            lastFourDigits = "5678",
            paymentMethodId = "visa",
            paymentTypeId = "credit_card",
            issuerId = 1,
            securityCode = SecurityCodeOutput(
                length = 3,
                screen = SecurityCodeScreenOutput(
                    headerTitle = "Código de seguridad",
                    field = SecurityCodeFieldOutput(
                        label = "CVV",
                        placeholder = "***",
                        helper = "Ingresá el código",
                        error = null,
                    ),
                    buttonLabel = "Continuar",
                ),
            ),
            installments = null,
        ),
    )

    private fun minimalOutput(
        methods: List<PaymentMethodOutput> = emptyList(),
    ) =
        PaymentBrickInitializationOutput(
            headerTitle = "Elegí cómo pagar",
            sections = if (methods.isEmpty()) {
                emptyList()
            } else {
                listOf(
                    PaymentSectionOutput(title = "Section", methods = methods),
                )
            },
            footer = PaymentBrickFooterOutput(totalLabel = "Total", totalAmount = "$ 500"),
        )

    private fun savedCardMethod(
        cardId: String = "CARD_123",
    ) = PaymentMethodOutput(
        type = "saved_card",
        title = "Visa **** 1234",
        cardData = CardDataOutput(
            id = cardId,
            bin = "503143",
            lastFourDigits = "1234",
            paymentMethodId = "visa",
            paymentTypeId = "credit_card",
            issuerId = 1,
            securityCode = SecurityCodeOutput(length = 3, screen = null),
            installments = null,
        ),
    )

    // ─── Initialization ───────────────────────────────────────────────────────

    @Test
    fun `given valid config and success response then state is populated with title`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()

        advanceUntilIdle()

        assertEquals("Elegí cómo pagar", vm.viewState.value.title)
        assertEquals(false, vm.viewState.value.isLoading)
    }

    @Test
    fun `given valid config and success then sections are mapped`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()

        advanceUntilIdle()

        assertEquals(emptyList(), vm.viewState.value.sections)
    }

    @Test
    fun `given use case returns error then emits OnFailure view event`() = runTest {
        val error = mockk<MercadoPagoCheckoutError>(relaxed = true)
        coEvery { fetchUseCase(any()) } returns Result.Error(error)
        val vm = viewModel()

        advanceUntilIdle()

        assertIs<PaymentBrickViewEvent.OnFailure>(vm.viewEvent.value)
    }

    @Test
    fun `given null checkoutConfiguration then isLoading is false and no fetch is called`() = runTest {
        val vm = PaymentBrickViewModel(null, fetchUseCase, processUseCase, GetSecurityCodeScreenUseCase())

        assertEquals(false, vm.viewState.value.isLoading)
        coVerify(exactly = 0) { fetchUseCase(any()) }
    }

    @Test
    fun `given non-payment checkout type then no fetch is called`() = runTest {
        val config = CheckoutConfiguration(
            checkoutType = MPCheckoutType.CardTransaction(
                MPOrder(
                    orderId = "ORD",
                    clientToken = "client_token",
                ),
            ),
            paymentMethodConfigs = emptyList(),
        )
        val vm = viewModel(config)

        coVerify(exactly = 0) { fetchUseCase(any()) }
    }

    @Test
    fun `given valid config then fetch is called with correct orderId and clientToken`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        viewModel(paymentConfig(orderId = "ORD_XYZ"))

        advanceUntilIdle()

        coVerify {
            fetchUseCase(FetchPaymentBrickInitializationParams(orderId = "ORD_XYZ", clientToken = "client_token"))
        }
    }

    // ─── processPaymentMethod ────────────────────────────────────────────────

    @Test
    fun `given known saved card optionId then processPaymentMethod calls processOrderUseCase`() = runTest {
        val card = savedCardMethod(cardId = "CARD_123")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        coEvery { processUseCase(any()) } returns Result.Success(OrderProcessOutput(id = "ORD", status = "processed"))
        val vm = viewModel()
        advanceUntilIdle()

        vm.processPaymentMethod("CARD_123")
        advanceUntilIdle()

        coVerify(exactly = 1) { processUseCase(any()) }
    }

    @Test
    fun `given process success then notifies CheckoutCallbackHolder with Payment`() = runTest {
        val card = savedCardMethod(cardId = "CARD_123")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        coEvery { processUseCase(any()) } returns
            Result.Success(OrderProcessOutput(id = "ORD_SUCCESS", status = "processed"))
        val vm = viewModel()
        advanceUntilIdle()

        vm.processPaymentMethod("CARD_123")
        advanceUntilIdle()

        verify {
            CheckoutCallbackHolder.notify(
                match { it is MercadoPagoCheckoutResult.Success<*> },
            )
        }
    }

    @Test
    fun `given process success then payment data carries all required fields`() = runTest {
        val card = savedCardMethod(cardId = "CARD_123")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        coEvery { processUseCase(any()) } returns
            Result.Success(OrderProcessOutput(id = "ORD_ID", status = "processed"))
        val vm = viewModel(paymentConfig(orderId = "ORD_ID"))
        advanceUntilIdle()

        vm.processPaymentMethod("CARD_123")
        advanceUntilIdle()

        verify {
            CheckoutCallbackHolder.notify(
                match { result ->
                    result is MercadoPagoCheckoutResult.Success<*> &&
                        (result.paymentData as? MPPaymentData.Payment)?.let { payment ->
                            payment.orderId == "ORD_ID" &&
                                payment.orderStatus == "processed" &&
                                payment.paymentMethodId == "visa" &&
                                payment.paymentTypeId == "credit_card"
                        } == true
                },
            )
        }
    }

    @Test
    fun `given process error then notifies CheckoutCallbackHolder with Error`() = runTest {
        val card = savedCardMethod(cardId = "CARD_123")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val error = mockk<MercadoPagoCheckoutError>(relaxed = true)
        coEvery { processUseCase(any()) } returns Result.Error(error)
        val vm = viewModel()
        advanceUntilIdle()

        vm.processPaymentMethod("CARD_123")
        advanceUntilIdle()

        verify { CheckoutCallbackHolder.notify(match { it is MercadoPagoCheckoutResult.Error }) }
    }

    @Test
    fun `given unknown optionId then processPaymentMethod does not call processOrderUseCase`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(emptyList()))
        val vm = viewModel()
        advanceUntilIdle()

        vm.processPaymentMethod("UNKNOWN_ID")
        advanceUntilIdle()

        coVerify(exactly = 0) { processUseCase(any()) }
    }

    // ─── Events ───────────────────────────────────────────────────────────────

    @Test
    fun `given onOptionSelected then emits OnOptionSelected event`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()

        vm.onOptionSelected("saved_card_123")

        assertIs<PaymentBrickViewEvent.OnOptionSelected>(vm.viewEvent.value)
        assertEquals("saved_card_123", (vm.viewEvent.value as PaymentBrickViewEvent.OnOptionSelected).optionId)
    }

    @Test
    fun `given onViewEventConsumed then clears viewEvent`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        vm.onOptionSelected("any")

        vm.onViewEventConsumed()

        assertEquals(null, vm.viewEvent.value)
    }

    // ─── A11 – onUserCancelled ────────────────────────────────────────────────

    @Test
    fun `given onBackPressed then emits OnUserCancelled event`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        advanceUntilIdle()

        vm.onBackPressed()

        assertIs<PaymentBrickViewEvent.OnUserCancelled>(vm.viewEvent.value)
    }

    @Test
    fun `given onBackPressed then screens list contains PAYMENT_METHOD_SELECTOR`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        advanceUntilIdle()

        vm.onBackPressed()

        val event = vm.viewEvent.value as? PaymentBrickViewEvent.OnUserCancelled
        assertEquals(true, event?.context?.screens?.contains(Screen.PAYMENT_METHOD_SELECTOR))
    }

    @Test
    fun `given markScreenPresented then screen is added to visited list`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        advanceUntilIdle()

        vm.markScreenPresented(Screen.INSTALLMENTS)
        vm.onBackPressed()

        val screens = (vm.viewEvent.value as? PaymentBrickViewEvent.OnUserCancelled)?.context?.screens
        assertEquals(true, screens?.contains(Screen.PAYMENT_METHOD_SELECTOR))
        assertEquals(true, screens?.contains(Screen.INSTALLMENTS))
    }

    @Test
    fun `given markScreenPresented called twice with same screen then screen is not duplicated`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        advanceUntilIdle()

        vm.markScreenPresented(Screen.INSTALLMENTS)
        vm.markScreenPresented(Screen.INSTALLMENTS)
        vm.onBackPressed()

        val screens = (vm.viewEvent.value as? PaymentBrickViewEvent.OnUserCancelled)?.context?.screens
        assertEquals(1, screens?.count { it == Screen.INSTALLMENTS })
    }

    // ─── A12 – onError (process order) ───────────────────────────────────────

    @Test
    fun `given process error then emits Error result to CheckoutCallbackHolder`() = runTest {
        val card = savedCardMethod(cardId = "CARD_ERR")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val error = mockk<MercadoPagoCheckoutError>(relaxed = true)
        coEvery { processUseCase(any()) } returns Result.Error(error)
        val vm = viewModel()
        advanceUntilIdle()

        vm.processPaymentMethod("CARD_ERR")
        advanceUntilIdle()

        verify { CheckoutCallbackHolder.notify(match { it is MercadoPagoCheckoutResult.Error }) }
    }

    @Test
    fun `given process error then no retry is triggered`() = runTest {
        val card = savedCardMethod(cardId = "CARD_ERR")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        coEvery { processUseCase(any()) } returns Result.Error(mockk(relaxed = true))
        val vm = viewModel()
        advanceUntilIdle()

        vm.processPaymentMethod("CARD_ERR")
        advanceUntilIdle()

        coVerify(exactly = 1) { processUseCase(any()) }
    }

    // ─── A15 – onOptionSelected CVV routing ──────────────────────────────────

    @Test
    fun `given card requiring CVV selected then emits OnSecurityCodeRequired with correct title`() = runTest {
        val card = savedCardWithCvuMethod(cardId = "CVV_CARD")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onOptionSelected("CVV_CARD")

        val event = vm.viewEvent.value
        assertIs<PaymentBrickViewEvent.OnSecurityCodeRequired>(event)
        assertEquals("Código de seguridad", event.config.title)
    }

    @Test
    fun `given card requiring CVV selected then config has correct cardId`() = runTest {
        val card = savedCardWithCvuMethod(cardId = "CVV_CARD")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onOptionSelected("CVV_CARD")

        val event = vm.viewEvent.value as? PaymentBrickViewEvent.OnSecurityCodeRequired
        assertEquals("CVV_CARD", event?.config?.cardId)
    }

    @Test
    fun `given card requiring CVV selected then config footerState has totalLabel as title`() = runTest {
        val card = savedCardWithCvuMethod(cardId = "CVV_CARD")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onOptionSelected("CVV_CARD")

        val event = vm.viewEvent.value as? PaymentBrickViewEvent.OnSecurityCodeRequired
        assertEquals("Total", event?.config?.footerState?.title)
    }

    @Test
    fun `given card without CVV screen selected then emits OnOptionSelected`() = runTest {
        val card = savedCardMethod(cardId = "CARD_NO_CVV")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onOptionSelected("CARD_NO_CVV")

        assertIs<PaymentBrickViewEvent.OnOptionSelected>(vm.viewEvent.value)
        assertEquals("CARD_NO_CVV", (vm.viewEvent.value as PaymentBrickViewEvent.OnOptionSelected).optionId)
    }

    @Test
    fun `given non-card method selected then emits OnOptionSelected`() = runTest {
        val ticketMethod = PaymentMethodOutput(
            type = "ticket",
            title = "Boleto",
            cardData = null,
        )
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(ticketMethod)))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onOptionSelected("ticket")

        assertIs<PaymentBrickViewEvent.OnOptionSelected>(vm.viewEvent.value)
        assertEquals("ticket", (vm.viewEvent.value as PaymentBrickViewEvent.OnOptionSelected).optionId)
    }

    @Test
    fun `given card requiring CVV but initialization not loaded yet then emits OnOptionSelected`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(emptyList()))
        val vm = viewModel()
        // Do NOT advance - initialization not loaded

        vm.onOptionSelected("CVV_CARD")

        assertIs<PaymentBrickViewEvent.OnOptionSelected>(vm.viewEvent.value)
        assertEquals("CVV_CARD", (vm.viewEvent.value as PaymentBrickViewEvent.OnOptionSelected).optionId)
    }
}
