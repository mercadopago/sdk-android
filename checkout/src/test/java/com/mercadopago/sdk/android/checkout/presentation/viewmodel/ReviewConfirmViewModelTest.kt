package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmFooter
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmHeader
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmItem
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmRequest
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmScreenState
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmViewData
import com.mercadopago.sdk.android.checkout.domain.model.SellerInfoRequest
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchReviewConfirmUseCase
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
internal class ReviewConfirmViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fetchReviewConfirmUseCase = mockk<FetchReviewConfirmUseCase>(relaxed = true)

    private val request = ReviewConfirmRequest(
        orderId = "order-123",
        paymentMethodType = "credit_card",
        paymentMethodId = "visa",
        issuerId = null,
        bin = "411111",
        productId = null,
        lastFourDigits = "1234",
        installments = 3,
        installmentAmount = "R$ 100,00",
        emailChangeEnabled = false,
        sellerInfo = SellerInfoRequest(name = "Loja Teste", iconUrl = null),
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
        request = request,
        fetchReviewConfirmUseCase = fetchReviewConfirmUseCase,
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
    fun `fetch use case is called with the provided request`() = runTest {
        // Given
        coEvery { fetchReviewConfirmUseCase(any()) } returns Result.Success(viewData)

        // When
        makeViewModel()

        // Then
        coVerify(exactly = 1) { fetchReviewConfirmUseCase(request) }
    }
}
