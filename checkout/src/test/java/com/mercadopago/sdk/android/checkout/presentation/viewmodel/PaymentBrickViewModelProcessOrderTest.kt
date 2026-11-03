package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.ScreenConfig
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.CardDataOutput
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.ObservedCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickFooterOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentMethodOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentSectionOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeOutput
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchMethodSelectionScreenUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchPaymentBrickInitializationUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GenerateTokenWithCardIdUseCase
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
internal class PaymentBrickViewModelProcessOrderTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fetchUseCase = mockk<FetchPaymentBrickInitializationUseCase>()
    private val processUseCase = mockk<ProcessOrderUseCase>()
    private val generateTokenWithCardIdUseCase = mockk<GenerateTokenWithCardIdUseCase>()

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
        withStatusScreen: Boolean = false,
    ) = CheckoutConfiguration(
        checkoutType = MPCheckoutType.Payment(
            order = MPOrder(orderId = orderId, clientToken = "client_token"),
        ),
        paymentMethodConfigs = emptyList(),
        screenConfigs = buildList {
            if (withReviewAndConfirm) add(ScreenConfig.ReviewAndConfirm())
            if (withStatusScreen) add(ScreenConfig.StatusScreen())
        },
    )

    private fun viewModel(
        config: CheckoutConfiguration? = paymentConfig(),
        fetchMethodSelectionScreenUseCase: FetchMethodSelectionScreenUseCase = FetchMethodSelectionScreenUseCase(),
        generateTokenUseCase: GenerateTokenWithCardIdUseCase = generateTokenWithCardIdUseCase,
    ) =
        PaymentBrickViewModel(
            checkoutConfiguration = config,
            fetchInitializationUseCase = fetchUseCase,
            processOrderUseCase = processUseCase,
            getSecurityCodeScreenUseCase = GetSecurityCodeScreenUseCase(),
            fetchMethodSelectionScreenUseCase = fetchMethodSelectionScreenUseCase,
            generateTokenWithCardIdUseCase = generateTokenUseCase,
            cancelledPaymentContextUseCase = CancelledPaymentContextUseCase(),
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
    fun `given ticket action_required process response then notifies Success with ticket data`() = runTest {
        // Given
        val card = savedCardMethod(cardId = "CARD_123")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        coEvery { processUseCase(any()) } returns Result.Success(
            OrderProcessOutput(
                id = "ORD",
                status = "action_required",
                paymentProcessed = OrderProcessOutput.PaymentProcessed(
                    id = "PAY_2",
                    status = "waiting_payment",
                    statusDetail = null,
                    amount = null,
                    paymentMethod = OrderProcessOutput.PaymentProcessedMethod(
                        id = "rapipago",
                        type = "ticket",
                        installments = null,
                        barcodeContent = "1234",
                        ticketUrl = "https://ticket",
                        redirectUrl = null,
                    ),
                ),
            ),
        )
        val vm = viewModel(paymentConfig(withReviewAndConfirm = false))
        advanceUntilIdle()

        // When
        vm.processOrder("CARD_123")
        advanceUntilIdle()

        // Then
        verify {
            CheckoutCallbackHolder.notify(
                match {
                    it is MercadoPagoCheckoutResult.Success<*> &&
                        (it.paymentData as MPPaymentData.Payment).paymentMethodId == "rapipago" &&
                        it.paymentData.paymentTypeId == "ticket"
                },
            )
        }
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
    fun `given ReviewAndConfirm when processOrder then params contain card id and bin`() = runTest {
        // Given
        val card = savedCardMethod(cardId = "CARD_123")
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        val vm = viewModel(paymentConfig(withReviewAndConfirm = true))
        advanceUntilIdle()

        // When
        vm.processOrder("CARD_123")

        // Then
        val event = vm.viewEvent.value as? PaymentBrickViewEvent.OnPaymentReadyForReview
        assertEquals("CARD_123", event?.params?.cardId)
        assertEquals("503143", event?.params?.bin)
        assertEquals("", event?.params?.amount)
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

    // ─── processOrder — with Status Screen, without ReviewAndConfirm ─────

    @Test
    fun `given status screen without ReviewAndConfirm when process success then emits OnOrderProcessed`() = runTest {
        // Given
        val card = savedCardMethod(cardId = "CARD_123")
        val paymentProcessed = OrderProcessOutput.PaymentProcessed(
            id = "PAY_1",
            status = "approved",
            statusDetail = "accredited",
            amount = null,
            paymentMethod = null,
        )
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        coEvery { processUseCase(any()) } returns Result.Success(
            OrderProcessOutput(id = "ORD", status = "processed", paymentProcessed = paymentProcessed),
        )
        val vm = viewModel(paymentConfig(withStatusScreen = true))
        advanceUntilIdle()

        // When
        vm.processOrder("CARD_123")
        advanceUntilIdle()

        // Then
        val event = vm.viewEvent.value as? PaymentBrickViewEvent.OnOrderProcessed
        assertEquals("ORD", (event?.payment as? MPPaymentData.Payment)?.orderId)
        assertEquals("CARD_123", event?.params?.cardId)
        assertEquals(paymentProcessed, event?.paymentProcessed)
    }

    @Test
    fun `given status screen without ReviewAndConfirm when process success then does not notify the callback`() =
        runTest {
            // Given
            val card = savedCardMethod(cardId = "CARD_123")
            coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
            coEvery { processUseCase(any()) } returns Result.Success(
                OrderProcessOutput(id = "ORD", status = "processed"),
            )
            val vm = viewModel(paymentConfig(withStatusScreen = true))
            advanceUntilIdle()

            // When
            vm.processOrder("CARD_123")
            advanceUntilIdle()

            // Then
            verify(exactly = 0) { CheckoutCallbackHolder.notify(any()) }
        }

    @Test
    fun `given status screen without ReviewAndConfirm when process error then emits OnOrderProcessFailed`() = runTest {
        // Given
        val card = savedCardMethod(cardId = "CARD_123")
        val publicError = mockk<MercadoPagoCheckoutError>()
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
        coEvery { processUseCase(any()) } returns Result.Error(
            ObservedCheckoutError(
                publicError = publicError,
                nativeErrorInput = mockk(relaxed = true),
                statusDetail = "cc_rejected_other_reason",
            ),
        )
        val vm = viewModel(paymentConfig(withStatusScreen = true))
        advanceUntilIdle()

        // When
        vm.processOrder("CARD_123")
        advanceUntilIdle()

        // Then
        val event = vm.viewEvent.value as? PaymentBrickViewEvent.OnOrderProcessFailed
        assertEquals(publicError, event?.error)
        assertEquals("cc_rejected_other_reason", event?.statusDetail)
        assertEquals("CARD_123", event?.params?.cardId)
        verify(exactly = 0) { CheckoutCallbackHolder.notify(any()) }
    }

    @Test
    fun `given status screen with ReviewAndConfirm when processOrder then still emits OnPaymentReadyForReview`() =
        runTest {
            // Given
            val card = savedCardMethod(cardId = "CARD_123")
            coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput(listOf(card)))
            val vm = viewModel(paymentConfig(withReviewAndConfirm = true, withStatusScreen = true))
            advanceUntilIdle()

            // When
            vm.processOrder("CARD_123")

            // Then
            assertIs<PaymentBrickViewEvent.OnPaymentReadyForReview>(vm.viewEvent.value)
        }
}
