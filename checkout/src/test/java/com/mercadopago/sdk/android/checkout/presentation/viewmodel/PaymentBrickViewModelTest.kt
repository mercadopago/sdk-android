package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.MPPayer
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.CardDataOutput
import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsHeaderOutput
import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsOutput
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickFooterOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentMethodOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentSectionOutput
import com.mercadopago.sdk.android.checkout.domain.model.QuotaOutput
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeFieldOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeScreenOutput
import com.mercadopago.sdk.android.checkout.domain.model.TicketOptionOutput
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickInitializationParams
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchPaymentBrickInitializationUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.ProcessOrderUseCase
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateCardTokenForPaymentBrickUseCase
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
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
internal class PaymentBrickViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fetchUseCase = mockk<FetchPaymentBrickInitializationUseCase>()
    private val processUseCase = mockk<ProcessOrderUseCase>()
    private val tokenizeUseCase = mockk<GenerateCardTokenForPaymentBrickUseCase>()

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
        amount: BigDecimal = BigDecimal("500.00"),
        cardIds: List<String>? = null,
    ) = CheckoutConfiguration(
        checkoutType = MPCheckoutType.Payment(
            order = MPOrder(orderId = orderId, amount = amount, payer = MPPayer(email = "")),
            cardIds = cardIds,
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
            generateCardTokenUseCase = tokenizeUseCase,
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
        assertEquals(false, vm.viewState.value.isError)
    }

    @Test
    fun `given valid config and success then sections are mapped`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()

        advanceUntilIdle()

        assertEquals(emptyList(), vm.viewState.value.sections)
    }

    @Test
    fun `given use case returns error then state has isError true`() = runTest {
        val error = mockk<MercadoPagoCheckoutError>(relaxed = true)
        coEvery { fetchUseCase(any()) } returns Result.Error(error)
        val vm = viewModel()

        advanceUntilIdle()

        assertEquals(true, vm.viewState.value.isError)
        assertEquals(false, vm.viewState.value.isLoading)
    }

    @Test
    fun `given null checkoutConfiguration then state has isError true immediately`() = runTest {
        val vm = PaymentBrickViewModel(null, fetchUseCase, processUseCase, tokenizeUseCase)

        assertEquals(true, vm.viewState.value.isError)
        assertEquals(false, vm.viewState.value.isLoading)
        coVerify(exactly = 0) { fetchUseCase(any()) }
    }

    @Test
    fun `given non-payment checkout type then state has isError true immediately`() = runTest {
        val config = CheckoutConfiguration(
            checkoutType = MPCheckoutType.CardTransaction(
                MPOrder(orderId = "ORD", amount = BigDecimal.TEN, payer = MPPayer(email = "")),
            ),
            paymentMethodConfigs = emptyList(),
        )
        val vm = viewModel(config)

        assertEquals(true, vm.viewState.value.isError)
        coVerify(exactly = 0) { fetchUseCase(any()) }
    }

    @Test
    fun `given valid config then fetch is called with correct orderId and totalAmount`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        viewModel(paymentConfig(orderId = "ORD_XYZ", amount = BigDecimal("188.00")))

        advanceUntilIdle()

        coVerify {
            fetchUseCase(
                FetchPaymentBrickInitializationParams(orderId = "ORD_XYZ", totalAmount = "188.00"),
            )
        }
    }

    @Test
    fun `given cardIds then fetch is called with joined cardIds string`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        viewModel(paymentConfig(cardIds = listOf("C1", "C2")))

        advanceUntilIdle()

        coVerify { fetchUseCase(match { it.cardIds == "C1,C2" }) }
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
        val vm = viewModel(paymentConfig(orderId = "ORD_ID", amount = BigDecimal("500.00")))
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
                                payment.transactionAmount == BigDecimal("500.00") &&
                                payment.paymentMethodId == "visa" &&
                                payment.paymentTypeId == "credit_card" &&
                                payment.issuerId == "1"
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
        assertEquals(true, vm.viewState.value.isError)
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

    // ─── A21 – Routing on option selected ────────────────────────────────────

    @Test
    fun `given saved card with cvv screen then NavigateToCVV is emitted`() = runTest {
        val cvvScreen = SecurityCodeScreenOutput(
            headerTitle = "CVV",
            field = SecurityCodeFieldOutput(label = "CVV", placeholder = "123", helper = ""),
            continueButtonLabel = "Continue",
        )
        val card = savedCardMethod(cardId = "CARD_CVV").copy(
            cardData = savedCardMethod(cardId = "CARD_CVV").cardData?.copy(
                securityCode = SecurityCodeOutput(length = 3, screen = cvvScreen),
            ),
        )
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onOptionSelected("CARD_CVV")

        assertIs<PaymentBrickViewEvent.NavigateToCVV>(vm.viewEvent.value)
        val event = vm.viewEvent.value as PaymentBrickViewEvent.NavigateToCVV
        assertEquals("CARD_CVV", event.optionId)
        assertEquals(3, event.cvvExpectedLength)
    }

    @Test
    fun `given saved card without cvv screen then processPaymentMethod is called directly`() = runTest {
        val cardNoCVV = savedCardMethod(cardId = "CARD_NOCVV")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(cardNoCVV)))
        coEvery { processUseCase(any()) } returns
            Result.Success(OrderProcessOutput(id = "ORD", status = "processed"))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onOptionSelected("CARD_NOCVV")
        advanceUntilIdle()

        assertEquals(null, vm.viewEvent.value)
        coVerify(exactly = 1) { processUseCase(any()) }
    }

    @Test
    fun `given new card option then OnOptionSelected is emitted`() = runTest {
        val newCard = PaymentMethodOutput(type = "new_card", title = "New card")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(newCard)))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onOptionSelected("new_card")

        assertIs<PaymentBrickViewEvent.OnOptionSelected>(vm.viewEvent.value)
    }

    // ─── A22 – Ticket routing ─────────────────────────────────────────────────

    @Test
    fun `given ticket with multiple options then NavigateToOfflineSelector is emitted`() = runTest {
        val ticket = PaymentMethodOutput(
            type = "ticket",
            title = "Efectivo",
            options = listOf(
                TicketOptionOutput(id = "pagofacil", name = "Pago Fácil", iconUrl = "url1"),
                TicketOptionOutput(id = "rapipago", name = "Rapipago", iconUrl = "url2"),
            ),
        )
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(ticket)))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onOptionSelected("ticket")

        val event = assertIs<PaymentBrickViewEvent.NavigateToOfflineSelector>(vm.viewEvent.value)
        assertEquals(2, event.options.size)
    }

    @Test
    fun `given ticket with single option then processPaymentMethod is called directly`() = runTest {
        val ticket = PaymentMethodOutput(
            type = "ticket",
            title = "Rapipago",
            options = listOf(TicketOptionOutput(id = "rapipago", name = "Rapipago", iconUrl = "url")),
        )
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(ticket)))
        coEvery { processUseCase(any()) } returns
            Result.Success(OrderProcessOutput(id = "ORD", status = "processed"))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onOptionSelected("ticket")
        advanceUntilIdle()

        assertEquals(null, vm.viewEvent.value)
        coVerify(exactly = 1) { processUseCase(any()) }
    }

    @Test
    fun `given ticket with empty options then NavigateToOfflineSelector with empty list is emitted`() = runTest {
        val ticket = PaymentMethodOutput(type = "ticket", title = "Efectivo", options = emptyList())
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(ticket)))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onOptionSelected("ticket")

        val event = assertIs<PaymentBrickViewEvent.NavigateToOfflineSelector>(vm.viewEvent.value)
        assertEquals(0, event.options.size)
    }

    // ─── Events ───────────────────────────────────────────────────────────────

    @Test
    fun `given onOptionSelected with unknown id then emits OnOptionSelected event`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()

        vm.onOptionSelected("unknown_id")

        assertIs<PaymentBrickViewEvent.OnOptionSelected>(vm.viewEvent.value)
    }

    @Test
    fun `given onViewEventConsumed then clears viewEvent`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        vm.onOptionSelected("any")

        vm.onViewEventConsumed()

        assertEquals(null, vm.viewEvent.value)
    }

    // ─── A23 – Installments routing (saved card, no CVV) ─────────────────────

    @Test
    fun `given saved card no cvv and with installments then NavigateToInstallmentsFromCard emitted`() = runTest {
        val installments = InstallmentsOutput(
            header = InstallmentsHeaderOutput(title = "Elegí"),
            totalLabel = "Total",
            payButtonLabel = "Pagar",
            selectionType = "radio_button",
            quotas = listOf(
                QuotaOutput(
                    installments = 1,
                    installmentAmount = BigDecimal("500"),
                    totalAmount = BigDecimal("500"),
                    primaryLabel = "1x",
                    secondaryLabel = "",
                    state = "none",
                ),
            ),
        )
        val card = savedCardMethod(cardId = "CARD_INST").copy(
            cardData = savedCardMethod(cardId = "CARD_INST").cardData?.copy(
                securityCode = SecurityCodeOutput(length = 3, screen = null),
                installments = installments,
            ),
        )
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onOptionSelected("CARD_INST")

        val event = assertIs<PaymentBrickViewEvent.NavigateToInstallmentsFromCard>(vm.viewEvent.value)
        assertEquals("CARD_INST", event.optionId)
        assertEquals(1, event.installmentData.quotas.size)
    }

    // ─── A25 – CVV confirmed routing ─────────────────────────────────────────

    @Test
    fun `given cvv confirmed for card with installments then NavigateToInstallmentsFromCard emitted`() = runTest {
        val installments = InstallmentsOutput(
            header = InstallmentsHeaderOutput(title = "Elegí"),
            totalLabel = "Total",
            payButtonLabel = "Pagar",
            selectionType = "radio_button",
            quotas = listOf(
                QuotaOutput(
                    installments = 3,
                    installmentAmount = BigDecimal("170"),
                    totalAmount = BigDecimal("510"),
                    primaryLabel = "3x",
                    secondaryLabel = "",
                    state = "none",
                ),
            ),
        )
        val cvvScreen = SecurityCodeScreenOutput(
            headerTitle = "CVV",
            field = SecurityCodeFieldOutput(label = "CVV", placeholder = "123", helper = ""),
            continueButtonLabel = "Continuar",
        )
        val card = savedCardMethod(cardId = "CARD_CVV_INST").copy(
            cardData = savedCardMethod(cardId = "CARD_CVV_INST").cardData?.copy(
                securityCode = SecurityCodeOutput(length = 3, screen = cvvScreen),
                installments = installments,
            ),
        )
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onCVVConfirmed("CARD_CVV_INST")

        assertIs<PaymentBrickViewEvent.NavigateToInstallmentsFromCard>(vm.viewEvent.value)
    }

    @Test
    fun `given cvv confirmed for card without installments then process is called`() = runTest {
        val cvvScreen = SecurityCodeScreenOutput(
            headerTitle = "CVV",
            field = SecurityCodeFieldOutput(label = "CVV", placeholder = "123", helper = ""),
            continueButtonLabel = "Continuar",
        )
        val card = savedCardMethod(cardId = "CARD_CVV_NOINST").copy(
            cardData = savedCardMethod(cardId = "CARD_CVV_NOINST").cardData?.copy(
                securityCode = SecurityCodeOutput(length = 3, screen = cvvScreen),
                installments = null,
            ),
        )
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        coEvery { processUseCase(any()) } returns
            Result.Success(OrderProcessOutput(id = "ORD", status = "processed"))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onCVVConfirmed("CARD_CVV_NOINST")
        advanceUntilIdle()

        coVerify(exactly = 1) { processUseCase(any()) }
    }

    // ─── A11 – onUserCancelled ────────────────────────────────────────────────

    @Test
    fun `given onBackPressed then notifies UserCancelled with Payment context`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        advanceUntilIdle()

        vm.onBackPressed()

        verify {
            CheckoutCallbackHolder.notify(
                match { it is MercadoPagoCheckoutResult.UserCancelled<*> },
            )
        }
    }

    @Test
    fun `given onBackPressed then screens list contains PAYMENT_METHOD_SELECTOR`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        advanceUntilIdle()

        vm.onBackPressed()

        verify {
            CheckoutCallbackHolder.notify(
                match { result ->
                    result is MercadoPagoCheckoutResult.UserCancelled<*> &&
                        (result.cancelledData as? MPUserCancelledContext.Payment)
                            ?.screens?.contains(Screen.PAYMENT_METHOD_SELECTOR) == true
                },
            )
        }
    }

    @Test
    fun `given markScreenPresented then screen is added to visited list`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        advanceUntilIdle()

        vm.markScreenPresented(Screen.INSTALLMENTS)
        vm.onBackPressed()

        verify {
            CheckoutCallbackHolder.notify(
                match { result ->
                    result is MercadoPagoCheckoutResult.UserCancelled<*> &&
                        (result.cancelledData as? MPUserCancelledContext.Payment)?.screens?.let { screens ->
                            screens.contains(Screen.PAYMENT_METHOD_SELECTOR) &&
                                screens.contains(Screen.INSTALLMENTS)
                        } == true
                },
            )
        }
    }

    @Test
    fun `given markScreenPresented called twice with same screen then screen is not duplicated`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        advanceUntilIdle()

        vm.markScreenPresented(Screen.INSTALLMENTS)
        vm.markScreenPresented(Screen.INSTALLMENTS)
        vm.onBackPressed()

        verify {
            CheckoutCallbackHolder.notify(
                match { result ->
                    result is MercadoPagoCheckoutResult.UserCancelled<*> &&
                        (result.cancelledData as? MPUserCancelledContext.Payment)
                            ?.screens?.count { it == Screen.INSTALLMENTS } == 1
                },
            )
        }
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
}
