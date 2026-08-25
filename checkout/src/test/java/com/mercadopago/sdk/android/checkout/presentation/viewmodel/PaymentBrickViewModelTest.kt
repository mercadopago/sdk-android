package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.ScreenConfig
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.CardDataOutput
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionOption
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenFooter
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickFooterOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentMethodOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentSectionOutput
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeFieldOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeScreenOutput
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickInitializationParams
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchMethodSelectionScreenUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchPaymentBrickInitializationUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetSecurityCodeScreenUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.ProcessOrderUseCase
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledPaymentContextUseCase
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
import kotlin.test.assertNull

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
        withReviewAndConfirm: Boolean = false,
    ) = CheckoutConfiguration(
        checkoutType = MPCheckoutType.Payment(
            order = MPOrder(orderId = orderId, clientToken = "client_token"),
        ),
        paymentMethodConfigs = emptyList(),
        screenConfigs = if (withReviewAndConfirm) {
            listOf(ScreenConfig.ReviewAndConfirm())
        } else {
            emptyList()
        },
    )

    private fun viewModel(
        config: CheckoutConfiguration? = paymentConfig(),
        fetchMethodSelectionScreenUseCase: FetchMethodSelectionScreenUseCase = FetchMethodSelectionScreenUseCase(),
    ) =
        PaymentBrickViewModel(
            checkoutConfiguration = config,
            fetchInitializationUseCase = fetchUseCase,
            processOrderUseCase = processUseCase,
            getSecurityCodeScreenUseCase = GetSecurityCodeScreenUseCase(),
            fetchMethodSelectionScreenUseCase = fetchMethodSelectionScreenUseCase,
            cancelledPaymentContextUseCase = CancelledPaymentContextUseCase(),
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
        // Given
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()

        // When
        advanceUntilIdle()

        // Then
        assertEquals("Elegí cómo pagar", vm.viewState.value.title)
        assertEquals(false, vm.viewState.value.isLoading)
    }

    @Test
    fun `given valid config and success then sections are mapped`() = runTest {
        // Given
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()

        // When
        advanceUntilIdle()

        // Then
        assertEquals(emptyList(), vm.viewState.value.sections)
    }

    @Test
    fun `given use case returns error then emits OnFailure view event`() = runTest {
        // Given
        val error = mockk<MercadoPagoCheckoutError>(relaxed = true)
        coEvery { fetchUseCase(any()) } returns Result.Error(error)
        val vm = viewModel()

        // When
        advanceUntilIdle()

        // Then
        assertIs<PaymentBrickViewEvent.OnFailure>(vm.viewEvent.value)
    }

    @Test
    fun `given null checkoutConfiguration then isLoading is false and no fetch is called`() = runTest {
        // Given / When
        val vm =
            PaymentBrickViewModel(
                null,
                fetchUseCase,
                processUseCase,
                GetSecurityCodeScreenUseCase(),
                FetchMethodSelectionScreenUseCase(),
                CancelledPaymentContextUseCase(),
            )

        // Then
        assertEquals(false, vm.viewState.value.isLoading)
        coVerify(exactly = 0) { fetchUseCase(any()) }
    }

    @Test
    fun `given non-payment checkout type then no fetch is called`() = runTest {
        // Given
        val config = CheckoutConfiguration(
            checkoutType = MPCheckoutType.CardTransaction(
                MPOrder(orderId = "ORD", clientToken = "client_token"),
            ),
            paymentMethodConfigs = emptyList(),
        )

        // When
        viewModel(config)

        // Then
        coVerify(exactly = 0) { fetchUseCase(any()) }
    }

    @Test
    fun `given valid config then fetch is called with correct orderId and clientToken`() = runTest {
        // Given
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())

        // When
        viewModel(paymentConfig(orderId = "ORD_XYZ"))
        advanceUntilIdle()

        // Then
        coVerify {
            fetchUseCase(FetchPaymentBrickInitializationParams(orderId = "ORD_XYZ", clientToken = "client_token"))
        }
    }

    // ─── processOrder — without ReviewAndConfirm ─────────────────────

    @Test
    fun `given no ReviewAndConfirm when processOrder then calls processOrderUseCase`() = runTest {
        // Given
        val card = savedCardMethod(cardId = "CARD_123")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        coEvery { processUseCase(any()) } returns Result.Success(OrderProcessOutput(id = "ORD", status = "processed"))
        val vm = viewModel(paymentConfig(withReviewAndConfirm = false))
        advanceUntilIdle()

        // When
        vm.processOrder("CARD_123")
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { processUseCase(any()) }
    }

    @Test
    fun `given no ReviewAndConfirm when process success then notifies CheckoutCallbackHolder`() = runTest {
        // Given
        val card = savedCardMethod(cardId = "CARD_123")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        coEvery { processUseCase(any()) } returns Result.Success(OrderProcessOutput(id = "ORD", status = "processed"))
        val vm = viewModel(paymentConfig(withReviewAndConfirm = false))
        advanceUntilIdle()

        // When
        vm.processOrder("CARD_123")
        advanceUntilIdle()

        // Then
        verify { CheckoutCallbackHolder.notify(match { it is MercadoPagoCheckoutResult.Success<*> }) }
    }

    @Test
    fun `given no ReviewAndConfirm when process error then notifies CheckoutCallbackHolder with Error`() = runTest {
        // Given
        val card = savedCardMethod(cardId = "CARD_123")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        coEvery { processUseCase(any()) } returns Result.Error(mockk(relaxed = true))
        val vm = viewModel(paymentConfig(withReviewAndConfirm = false))
        advanceUntilIdle()

        // When
        vm.processOrder("CARD_123")
        advanceUntilIdle()

        // Then
        verify { CheckoutCallbackHolder.notify(match { it is MercadoPagoCheckoutResult.Error }) }
    }

    // ─── processOrder — with ReviewAndConfirm ─────────────────────────

    @Test
    fun `given ReviewAndConfirm when processOrder then emits OnPaymentReadyForReview`() = runTest {
        // Given
        val card = savedCardMethod(cardId = "CARD_123")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel(paymentConfig(withReviewAndConfirm = true))
        advanceUntilIdle()

        // When
        vm.processOrder("CARD_123")

        // Then
        assertIs<PaymentBrickViewEvent.OnPaymentReadyForReview>(vm.viewEvent.value)
    }

    @Test
    fun `given ReviewAndConfirm when processOrder then does not call processOrderUseCase`() = runTest {
        // Given
        val card = savedCardMethod(cardId = "CARD_123")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel(paymentConfig(withReviewAndConfirm = true))
        advanceUntilIdle()

        // When
        vm.processOrder("CARD_123")
        advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { processUseCase(any()) }
    }

    @Test
    fun `given ReviewAndConfirm when processOrder then params contain bin`() = runTest {
        // Given
        val card = savedCardMethod(cardId = "CARD_123")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel(paymentConfig(withReviewAndConfirm = true))
        advanceUntilIdle()

        // When
        vm.processOrder("CARD_123")

        // Then
        val event = vm.viewEvent.value as? PaymentBrickViewEvent.OnPaymentReadyForReview
        assertEquals("503143", event?.params?.bin)
    }

    @Test
    fun `given unknown optionId when processOrder then no event is emitted`() = runTest {
        // Given
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(emptyList()))
        val vm = viewModel()
        advanceUntilIdle()

        // When
        vm.processOrder("UNKNOWN_ID")

        // Then
        assertNull(vm.viewEvent.value)
    }

    // ─── processOrder with token — with ReviewAndConfirm ────────────────

    @Test
    fun `given ReviewAndConfirm when processOrder with token then emits OnPaymentReadyForReview`() = runTest {
        // Given
        val card = savedCardMethod(cardId = "CARD_123")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel(paymentConfig(withReviewAndConfirm = true))
        advanceUntilIdle()

        // When
        vm.processOrder(cardId = "CARD_123", token = "tkn_abc")

        // Then
        assertIs<PaymentBrickViewEvent.OnPaymentReadyForReview>(vm.viewEvent.value)
    }

    @Test
    fun `given ReviewAndConfirm when processOrder with token then params contain token and bin`() = runTest {
        // Given
        val card = savedCardMethod(cardId = "CARD_123")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel(paymentConfig(withReviewAndConfirm = true))
        advanceUntilIdle()

        // When
        vm.processOrder(cardId = "CARD_123", token = "tkn_abc")

        // Then
        val event = vm.viewEvent.value as? PaymentBrickViewEvent.OnPaymentReadyForReview
        assertEquals("tkn_abc", event?.params?.token)
        assertEquals("503143", event?.params?.bin)
    }

    // ─── Events ───────────────────────────────────────────────────────────────

    @Test
    fun `given onOptionSelected then emits OnOptionSelected event`() = runTest {
        // Given
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()

        // When
        vm.onOptionSelected("saved_card_123")

        // Then
        assertIs<PaymentBrickViewEvent.OnOptionSelected>(vm.viewEvent.value)
        assertEquals("saved_card_123", (vm.viewEvent.value as PaymentBrickViewEvent.OnOptionSelected).optionId)
    }

    @Test
    fun `given onViewEventConsumed then clears viewEvent`() = runTest {
        // Given
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        vm.onOptionSelected("any")

        // When
        vm.onViewEventConsumed()

        // Then
        assertEquals(null, vm.viewEvent.value)
    }

    // ─── onUserCancelled ─────────────────────────────────────────────────────

    @Test
    fun `given onBackPressed then emits OnUserCancelled event`() = runTest {
        // Given
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        advanceUntilIdle()

        // When
        vm.onBackPressed()

        // Then
        assertIs<PaymentBrickViewEvent.OnUserCancelled>(vm.viewEvent.value)
    }

    @Test
    fun `given onBackPressed then screens list contains PAYMENT_METHOD_SELECTOR`() = runTest {
        // Given
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        advanceUntilIdle()

        // When
        vm.onBackPressed()

        // Then
        val event = vm.viewEvent.value as? PaymentBrickViewEvent.OnUserCancelled
        assertEquals(true, event?.context?.screens?.contains(Screen.PAYMENT_METHOD_SELECTOR))
    }

    @Test
    fun `given markScreenPresented then screen is added to visited list`() = runTest {
        // Given
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        advanceUntilIdle()
        vm.markScreenPresented(Screen.INSTALLMENTS)

        // When
        vm.onBackPressed()

        // Then
        val screens = (vm.viewEvent.value as? PaymentBrickViewEvent.OnUserCancelled)?.context?.screens
        assertEquals(true, screens?.contains(Screen.PAYMENT_METHOD_SELECTOR))
        assertEquals(true, screens?.contains(Screen.INSTALLMENTS))
    }

    @Test
    fun `given markScreenPresented called twice with same screen then screen is not duplicated`() = runTest {
        // Given
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        advanceUntilIdle()
        vm.markScreenPresented(Screen.INSTALLMENTS)

        // When
        vm.onBackPressed()

        // Then
        val screens = (vm.viewEvent.value as? PaymentBrickViewEvent.OnUserCancelled)?.context?.screens
        assertEquals(1, screens?.count { it == Screen.INSTALLMENTS })
    }

    // ─── CVV routing ─────────────────────────────────────────────────────────

    @Test
    fun `given card requiring CVV selected then emits OnSecurityCodeRequired with correct title`() = runTest {
        // Given
        val card = savedCardWithCvuMethod(cardId = "CVV_CARD")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel()
        advanceUntilIdle()

        // When
        vm.onOptionSelected("CVV_CARD")

        // Then
        val event = vm.viewEvent.value
        assertIs<PaymentBrickViewEvent.OnSecurityCodeRequired>(event)
        assertEquals("Código de seguridad", event.config.title)
    }

    @Test
    fun `given card requiring CVV selected then config has correct cardId`() = runTest {
        // Given
        val card = savedCardWithCvuMethod(cardId = "CVV_CARD")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel()
        advanceUntilIdle()

        // When
        vm.onOptionSelected("CVV_CARD")

        // Then
        val event = vm.viewEvent.value as? PaymentBrickViewEvent.OnSecurityCodeRequired
        assertEquals("CVV_CARD", event?.config?.cardId)
    }

    @Test
    fun `given card requiring CVV selected then config footerState has totalLabel as title`() = runTest {
        // Given
        val card = savedCardWithCvuMethod(cardId = "CVV_CARD")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel()
        advanceUntilIdle()

        // When
        vm.onOptionSelected("CVV_CARD")

        // Then
        val event = vm.viewEvent.value as? PaymentBrickViewEvent.OnSecurityCodeRequired
        assertEquals("Total", event?.config?.footerState?.title)
    }

    @Test
    fun `given card without CVV screen selected then emits OnOptionSelected`() = runTest {
        // Given
        val card = savedCardMethod(cardId = "CARD_NO_CVV")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel()
        advanceUntilIdle()

        // When
        vm.onOptionSelected("CARD_NO_CVV")

        // Then
        assertIs<PaymentBrickViewEvent.OnOptionSelected>(vm.viewEvent.value)
        assertEquals("CARD_NO_CVV", (vm.viewEvent.value as PaymentBrickViewEvent.OnOptionSelected).optionId)
    }

    @Test
    fun `given non-card method selected then emits OnOptionSelected`() = runTest {
        // Given
        val ticketMethod = PaymentMethodOutput(type = "ticket", title = "Boleto", cardData = null)
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(ticketMethod)))
        val vm = viewModel()
        advanceUntilIdle()

        // When
        vm.onOptionSelected("ticket")

        // Then
        assertIs<PaymentBrickViewEvent.OnOptionSelected>(vm.viewEvent.value)
        assertEquals("ticket", (vm.viewEvent.value as PaymentBrickViewEvent.OnOptionSelected).optionId)
    }

    @Test
    fun `given card requiring CVV but initialization not loaded yet then emits OnOptionSelected`() = runTest {
        // Given
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(emptyList()))
        val vm = viewModel()
        // Do NOT advance — initialization not loaded

        // When
        vm.onOptionSelected("CVV_CARD")

        // Then
        assertIs<PaymentBrickViewEvent.OnOptionSelected>(vm.viewEvent.value)
        assertEquals("CVV_CARD", (vm.viewEvent.value as PaymentBrickViewEvent.OnOptionSelected).optionId)
    }

    @Test
    fun `given ticket method with screen when onOptionSelected then emits OnOfflineMethodSelected`() = runTest {
        val screenData = MethodSelectionScreenData(
            headerTitle = "Escolha o boleto",
            selectionType = SelectionDisplayType.Chevron,
            footer = MethodSelectionScreenFooter(totalLabel = "Total", totalAmount = "R$ 100"),
            options = listOf(
                MethodSelectionOption(id = "boleto", name = "Boleto", subtitle = "3 dias", iconUrl = "url"),
            ),
        )
        val ticketMethod = PaymentMethodOutput(type = "ticket", title = "Boleto", screen = screenData)
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(ticketMethod)))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onOptionSelected("ticket")

        assertIs<PaymentBrickViewEvent.OnOfflineMethodSelected>(vm.viewEvent.value)
        assertEquals(screenData, (vm.viewEvent.value as PaymentBrickViewEvent.OnOfflineMethodSelected).screenData)
    }

    @Test
    fun `given ticket method without screen when onOptionSelected then emits OnOptionSelected`() = runTest {
        val ticketMethod = PaymentMethodOutput(type = "ticket", title = "Boleto", screen = null)
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(ticketMethod)))
        val vm = viewModel()
        advanceUntilIdle()

        vm.onOptionSelected("ticket")

        assertIs<PaymentBrickViewEvent.OnOptionSelected>(vm.viewEvent.value)
    }

    // ─── ReviewConfirm load failure ─────────────────────────────────────────────

    @Test
    fun `given onReviewConfirmLoadFailure then showMessage is true`() = runTest {
        // Given
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        advanceUntilIdle()

        // When
        vm.onReviewConfirmLoadFailure()

        // Then
        assertEquals(true, vm.viewState.value.showMessage)
    }

    @Test
    fun `given message shown when onMessageClick then showMessage is false`() = runTest {
        // Given
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val vm = viewModel()
        advanceUntilIdle()
        vm.onReviewConfirmLoadFailure()

        // When
        vm.onMessageClick()

        // Then
        assertEquals(false, vm.viewState.value.showMessage)
    }
}
