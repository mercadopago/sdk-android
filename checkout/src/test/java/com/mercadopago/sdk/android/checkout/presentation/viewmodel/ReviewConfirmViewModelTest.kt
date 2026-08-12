package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.ScreenConfig
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmFooter
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmHeader
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmItem
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmScreenState
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmViewData
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchReviewConfirmUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.ProcessOrderUseCase
import com.mercadopago.sdk.android.checkout.presentation.state.ReviewConfirmViewEvent
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

    private val checkoutConfiguration = CheckoutConfiguration(
        checkoutType = MPCheckoutType.Payment(
            order = MPOrder(orderId = "order-123", clientToken = "token-abc"),
        ),
        paymentMethodConfigs = emptyList(),
        screenConfigs = listOf(
            ScreenConfig.ReviewAndConfirm(
                seller = MPSellerInfo(name = "Loja Teste", logoUrl = null),
            ),
        ),
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
                changeLabel = "Mudar",
            ),
        ),
        footerSummary = null,
        footer = ReviewConfirmFooter(
            buttonLabel = "Confirmar e pagar",
            totalAmount = "R$ 300,00",
            description = "Total",
            interestLabel = null,
        ),
    )

    private fun makeViewModel() = ReviewConfirmViewModel(
        params = params,
        checkoutConfiguration = checkoutConfiguration,
        fetchReviewConfirmUseCase = fetchReviewConfirmUseCase,
        processOrderUseCase = processOrderUseCase,
    )

    // region — fetch

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
    fun `fetch use case is called with request built from params and configuration`() = runTest {
        // Given
        val requestSlot = slot<com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmRequest>()
        coEvery { fetchReviewConfirmUseCase(capture(requestSlot)) } returns Result.Success(viewData)

        // When
        makeViewModel()

        // Then
        coVerify(exactly = 1) { fetchReviewConfirmUseCase(any()) }
        assertEquals("order-123", requestSlot.captured.orderId)
        assertEquals("credit_card", requestSlot.captured.paymentMethodType)
        assertEquals("visa", requestSlot.captured.paymentMethodId)
        assertEquals("411111", requestSlot.captured.bin)
        assertEquals(3, requestSlot.captured.installments)
        assertEquals("30000", requestSlot.captured.installmentAmount)
        assertEquals(true, requestSlot.captured.emailChangeEnabled)
        assertEquals("Loja Teste", requestSlot.captured.sellerInfo?.name)
        assertEquals(null, requestSlot.captured.sellerInfo?.iconUrl)
    }

    // endregion

    // region — onConfirmClicked

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
    fun `onConfirmClicked emits OnPaymentSuccess on process success`() = runTest {
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
        assertEquals("order-1", event.payment.orderId)
        assertEquals("approved", event.payment.orderStatus)
        assertEquals("visa", event.payment.paymentMethodId)
        assertEquals("credit_card", event.payment.paymentTypeId)
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
        viewModel.onConfirmClicked() // First click
        viewModel.onConfirmClicked() // Second click (double-tap)

        // Then - processOrderUseCase should only be called once
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
        viewModel.onConfirmClicked() // First call
        viewModel.onViewEventConsumed()
        viewModel.onConfirmClicked() // Second call after first completes

        // Then - processOrderUseCase should be called twice (two separate operations)
        coVerify(exactly = 2) { processOrderUseCase(params) }
    }

    // endregion
}
