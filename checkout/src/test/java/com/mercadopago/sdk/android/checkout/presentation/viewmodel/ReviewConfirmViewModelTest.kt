package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmButton
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmFooter
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmHeader
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmItem
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmItemButton
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmViewData
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchReviewConfirmUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.ProcessOrderUseCase
import com.mercadopago.sdk.android.checkout.presentation.state.ITEM_TYPE_PAYER_EMAIL
import com.mercadopago.sdk.android.checkout.presentation.state.ReviewConfirmScreenConfig
import com.mercadopago.sdk.android.checkout.presentation.state.ReviewConfirmScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.ReviewConfirmViewEvent
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledPaymentContextUseCase
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
internal class ReviewConfirmViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fetchReviewConfirmUseCase = mockk<FetchReviewConfirmUseCase>(relaxed = true)
    private val processOrderUseCase = mockk<ProcessOrderUseCase>(relaxed = true)
    private val cancelledPaymentContextUseCase = mockk<CancelledPaymentContextUseCase>(relaxed = true)

    private val params = ProcessOrderParams(
        orderId = "order-123",
        clientToken = "token-abc",
        paymentMethodId = "visa",
        paymentMethodType = "credit_card",
        token = "card-token-xyz",
        installments = 3,
        amount = "30000",
        bin = "411111",
    )

    private val viewData = ReviewConfirmViewData(
        header = ReviewConfirmHeader(
            title = "Revisa e confirma",
            sellerName = "Loja Teste",
            sellerIconUrl = null,
        ),
        items = listOf(
            ReviewConfirmItem(
                type = "payment_method",
                label = "Forma de pagamento",
                value = "Visa •••• 1234",
                button = ReviewConfirmItemButton(label = "Mudar"),
            ),
        ),
        footerSummary = null,
        footer = ReviewConfirmFooter(
            button = ReviewConfirmButton(label = "Confirmar e pagar"),
            currencySymbol = "R$",
            totalAmount = "R$ 300,00",
            totalLabel = "Total",
            installments = null,
            description = null,
            interestLabel = null,
        ),
    )

    private val viewDataWithEmailItem = viewData.copy(
        items = viewData.items + ReviewConfirmItem(
            type = ITEM_TYPE_PAYER_EMAIL,
            label = "E-mail",
            value = "buyer@test.com",
            button = ReviewConfirmItemButton(label = "Mudar"),
        ),
    )

    private fun makeViewModel(
        emailChangeEnabled: Boolean = false,
    ) = ReviewConfirmViewModel(
        processOrderParams = params,
        config = ReviewConfirmScreenConfig(sellerInfo = null, emailChangeEnabled = emailChangeEnabled),
        fetchReviewConfirmUseCase = fetchReviewConfirmUseCase,
        processOrderUseCase = processOrderUseCase,
        cancelledPaymentContextUseCase = cancelledPaymentContextUseCase,
    )

    @Test
    fun `initial state is Loading before fetch completes`() = runTest {
        // Given
        val deferred = CompletableDeferred<Result<ReviewConfirmViewData, MercadoPagoCheckoutError>>()
        coEvery { fetchReviewConfirmUseCase(any()) } coAnswers { deferred.await() }

        // When
        val viewModel = makeViewModel()

        // Then
        assertIs<ReviewConfirmScreenState.Loading>(viewModel.state.value)
        deferred.complete(Result.Success(viewData))
    }

    @Test
    fun `state becomes Success when fetch returns Success`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)

        // When
        val viewModel = makeViewModel()

        // Then
        assertIs<ReviewConfirmScreenState.Success>(viewModel.state.value)
    }

    @Test
    fun `Success state maps header correctly`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)

        // When
        val viewModel = makeViewModel()

        // Then
        val state = viewModel.state.value as ReviewConfirmScreenState.Success
        assertEquals("Revisa e confirma", state.header.title)
        assertEquals("Loja Teste", state.header.sellerName)
    }

    @Test
    fun `Success state maps items correctly`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)

        // When
        val viewModel = makeViewModel()

        // Then
        val state = viewModel.state.value as ReviewConfirmScreenState.Success
        assertEquals(1, state.items.size)
        assertEquals("Forma de pagamento", state.items.first().label)
    }

    @Test
    fun `Success state maps footer correctly`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)

        // When
        val viewModel = makeViewModel()

        // Then
        val state = viewModel.state.value as ReviewConfirmScreenState.Success
        assertEquals("Confirmar e pagar", state.footer.buttonLabel)
        assertEquals("R$ 300,00", state.footer.totalAmount)
    }

    @Test
    fun `state becomes Error when fetch returns Error`() = runTest {
        // Given
        val error = MercadoPagoCheckoutError.NetworkError(
            code = ErrorCode.NETWORK_CONNECTION_FAILED,
            messageError = "Sem conexão com a internet",
            localized = "review_confirm",
            throwable = null,
        )
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Error(error)

        // When
        val viewModel = makeViewModel()

        // Then
        assertIs<ReviewConfirmScreenState.Error>(viewModel.state.value)
    }

    @Test
    fun `emits OnLoadFailure when fetch returns Error`() = runTest {
        // Given
        val error = MercadoPagoCheckoutError.NetworkError(
            code = ErrorCode.NETWORK_CONNECTION_FAILED,
            messageError = "Sem conexão com a internet",
            localized = "review_confirm",
            throwable = null,
        )
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Error(error)

        // When
        val viewModel = makeViewModel()

        // Then
        assertIs<ReviewConfirmViewEvent.OnLoadFailure>(viewModel.viewEvent.value)
    }

    @Test
    fun `fetch use case is called with params`() = runTest {
        // Given
        val paramsSlot = slot<ProcessOrderParams>()
        coEvery { fetchReviewConfirmUseCase(capture(paramsSlot)) } returns Result.Success(viewData)

        // When
        makeViewModel()

        // Then
        coVerify(exactly = 1) { fetchReviewConfirmUseCase(any(), emailChangeEnabled = false, sellerInfo = null) }
        assertEquals("order-123", paramsSlot.captured.orderId)
        assertEquals("credit_card", paramsSlot.captured.paymentMethodType)
        assertEquals("visa", paramsSlot.captured.paymentMethodId)
        assertEquals("411111", paramsSlot.captured.bin)
        assertEquals(3, paramsSlot.captured.installments)
        assertEquals("30000", paramsSlot.captured.amount)
    }

    @Test
    fun `fetch use case is called with emailChangeEnabled true when configured`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any(), emailChangeEnabled = true, sellerInfo = null) } returns
            Result.Success(viewData)

        // When
        makeViewModel(emailChangeEnabled = true)

        // Then
        coVerify(exactly = 1) { fetchReviewConfirmUseCase(any(), emailChangeEnabled = true, sellerInfo = null) }
    }

    @Test
    fun `payer_email button is shown when emailChangeEnabled is true`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any(), emailChangeEnabled = true, sellerInfo = null) } returns
            Result.Success(viewDataWithEmailItem)

        // When
        val viewModel = makeViewModel(emailChangeEnabled = true)

        // Then
        val state = viewModel.state.value as ReviewConfirmScreenState.Success
        val emailItem = state.items.first { it.type == ITEM_TYPE_PAYER_EMAIL }
        assertEquals("Mudar", emailItem.buttonLabel)
    }

    @Test
    fun `payer_email button is hidden when emailChangeEnabled is false even if backend returns it`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any(), emailChangeEnabled = false, sellerInfo = null) } returns
            Result.Success(viewDataWithEmailItem)

        // When
        val viewModel = makeViewModel(emailChangeEnabled = false)

        // Then
        val state = viewModel.state.value as ReviewConfirmScreenState.Success
        val emailItem = state.items.first { it.type == ITEM_TYPE_PAYER_EMAIL }
        assertNull(emailItem.buttonLabel)
    }

    @Test
    fun `payment_method button is unaffected by emailChangeEnabled`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any(), emailChangeEnabled = false, sellerInfo = null) } returns
            Result.Success(viewDataWithEmailItem)

        // When
        val viewModel = makeViewModel(emailChangeEnabled = false)

        // Then
        val state = viewModel.state.value as ReviewConfirmScreenState.Success
        val paymentMethodItem = state.items.first { it.type == "payment_method" }
        assertEquals("Mudar", paymentMethodItem.buttonLabel)
    }

    @Test
    fun `onConfirmClicked sets isLoading true while processing`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)
        val deferred = CompletableDeferred<Result<OrderProcessOutput, MercadoPagoCheckoutError>>()
        coEvery { processOrderUseCase(any()) } coAnswers { deferred.await() }

        // When
        val viewModel = makeViewModel()
        viewModel.onConfirmClicked()

        // Then
        assertEquals(true, (viewModel.state.value as ReviewConfirmScreenState.Success).isLoading)
        deferred.complete(Result.Success(OrderProcessOutput(id = "order-1", status = "approved")))
    }

    @Test
    fun `onConfirmClicked emits OnPaymentSuccess with process output on process success`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)
        coEvery { processOrderUseCase(any()) } returns Result.Success(
            OrderProcessOutput(id = "order-1", status = "approved"),
        )

        // When
        val viewModel = makeViewModel()
        viewModel.onConfirmClicked()

        // Then
        val event = viewModel.viewEvent.value
        assertIs<ReviewConfirmViewEvent.OnPaymentSuccess>(event)
        assertEquals("order-1", event.output.id)
        assertEquals("approved", event.output.status)
    }

    @Test
    fun `onConfirmClicked emits OnPaymentError on process failure`() = runTest {
        // Given
        val error = MercadoPagoCheckoutError.NetworkError(
            code = ErrorCode.NETWORK_CONNECTION_FAILED,
            messageError = "Pagamento rejeitado",
            localized = "review_confirm",
            throwable = null,
        )
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)
        coEvery { processOrderUseCase(any()) } returns Result.Error(error)

        // When
        val viewModel = makeViewModel()
        viewModel.onConfirmClicked()

        // Then
        assertIs<ReviewConfirmViewEvent.OnPaymentError>(viewModel.viewEvent.value)
    }

    @Test
    fun `onConfirmClicked resets isLoading false after success`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)
        coEvery { processOrderUseCase(any()) } returns Result.Success(
            OrderProcessOutput(id = "order-1", status = "approved"),
        )

        // When
        val viewModel = makeViewModel()
        viewModel.onConfirmClicked()

        // Then
        assertEquals(false, (viewModel.state.value as ReviewConfirmScreenState.Success).isLoading)
    }

    @Test
    fun `onConfirmClicked resets isLoading false after failure`() = runTest {
        // Given
        val error = MercadoPagoCheckoutError.NetworkError(
            code = ErrorCode.NETWORK_CONNECTION_FAILED,
            messageError = "Erro de rede",
            localized = "review_confirm",
            throwable = null,
        )
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)
        coEvery { processOrderUseCase(any()) } returns Result.Error(error)

        // When
        val viewModel = makeViewModel()
        viewModel.onConfirmClicked()

        // Then
        assertEquals(false, (viewModel.state.value as ReviewConfirmScreenState.Success).isLoading)
    }

    @Test
    fun `onConfirmClicked does nothing when state is not Success`() = runTest {
        // Given
        val deferred = CompletableDeferred<Result<ReviewConfirmViewData, MercadoPagoCheckoutError>>()
        coEvery { fetchReviewConfirmUseCase(any()) } coAnswers { deferred.await() }

        // When
        val viewModel = makeViewModel()
        viewModel.onConfirmClicked()

        // Then
        coVerify(exactly = 0) { processOrderUseCase(any()) }
        deferred.complete(Result.Success(viewData))
    }

    @Test
    fun `onViewEventConsumed clears the viewEvent`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)
        coEvery { processOrderUseCase(any()) } returns Result.Success(
            OrderProcessOutput(id = "order-1", status = "approved"),
        )
        val viewModel = makeViewModel()
        viewModel.onConfirmClicked()
        assertIs<ReviewConfirmViewEvent.OnPaymentSuccess>(viewModel.viewEvent.value)

        // When
        viewModel.onViewEventConsumed()

        // Then
        assertNull(viewModel.viewEvent.value)
    }

    @Test
    fun `processOrderUseCase is called with the provided params`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)
        coEvery { processOrderUseCase(any()) } returns Result.Success(
            OrderProcessOutput(id = "order-1", status = "approved"),
        )

        // When
        val viewModel = makeViewModel()
        viewModel.onConfirmClicked()

        // Then
        coVerify(exactly = 1) { processOrderUseCase(params) }
    }

    @Test
    fun `onConfirmClicked prevents double-tap when already loading`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)
        val deferred = CompletableDeferred<Result<OrderProcessOutput, MercadoPagoCheckoutError>>()
        coEvery { processOrderUseCase(any()) } coAnswers { deferred.await() }

        // When
        val viewModel = makeViewModel()
        viewModel.onConfirmClicked()
        viewModel.onConfirmClicked()

        // Then
        coVerify(exactly = 1) { processOrderUseCase(any()) }
        deferred.complete(Result.Success(OrderProcessOutput(id = "order-1", status = "approved")))
    }

    @Test
    fun `onConfirmClicked allows new call after previous completes`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)
        coEvery { processOrderUseCase(any()) } returns Result.Success(
            OrderProcessOutput(id = "order-1", status = "approved"),
        )

        // When
        val viewModel = makeViewModel()
        viewModel.onConfirmClicked()
        viewModel.onViewEventConsumed()
        viewModel.onConfirmClicked()

        // Then
        coVerify(exactly = 2) { processOrderUseCase(params) }
    }

    @Test
    fun `onModifyPaymentMethodClicked emits OnModifyPaymentMethod event`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)
        val viewModel = makeViewModel()

        // When
        viewModel.onModifyPaymentMethodClicked("payment_method")

        // Then
        val event = viewModel.viewEvent.value
        assertIs<ReviewConfirmViewEvent.OnModifyPaymentMethod>(event)
        assertEquals("payment_method", event.itemType)
    }

    @Test
    fun `onModifyPaymentMethodClicked preserves itemType correctly`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)
        val viewModel = makeViewModel()

        // When
        viewModel.onModifyPaymentMethodClicked("installments")

        // Then
        val event = viewModel.viewEvent.value
        assertIs<ReviewConfirmViewEvent.OnModifyPaymentMethod>(event)
        assertEquals("installments", event.itemType)
    }

    @Test
    fun `onModifyEmailClicked emits OnModifyEmail event`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)
        val viewModel = makeViewModel()

        // When
        viewModel.onModifyEmailClicked()

        // Then
        val event = viewModel.viewEvent.value
        assertIs<ReviewConfirmViewEvent.OnModifyEmail>(event)
    }
}
