package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.MPPayer
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickFooterOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickInitializationParams
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchPaymentBrickInitializationUseCase
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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

    private fun minimalOutput() = PaymentBrickInitializationOutput(
        headerTitle = "Elegí cómo pagar",
        sections = emptyList(),
        footer = PaymentBrickFooterOutput(totalLabel = "Total", totalAmount = "$ 500"),
    )

    @Test
    fun `given valid config and success response then state is populated with title`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val viewModel = PaymentBrickViewModel(paymentConfig(), fetchUseCase)

        advanceUntilIdle()

        assertEquals("Elegí cómo pagar", viewModel.viewState.value.title)
        assertEquals(false, viewModel.viewState.value.isLoading)
        assertEquals(false, viewModel.viewState.value.isError)
    }

    @Test
    fun `given valid config and success then sections are mapped`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val viewModel = PaymentBrickViewModel(paymentConfig(), fetchUseCase)

        advanceUntilIdle()

        assertEquals(emptyList(), viewModel.viewState.value.sections)
    }

    @Test
    fun `given use case returns error then state has isError true`() = runTest {
        val error = mockk<MercadoPagoCheckoutError>(relaxed = true)
        coEvery { fetchUseCase(any()) } returns Result.Error(error)
        val viewModel = PaymentBrickViewModel(paymentConfig(), fetchUseCase)

        advanceUntilIdle()

        assertEquals(true, viewModel.viewState.value.isError)
        assertEquals(false, viewModel.viewState.value.isLoading)
    }

    @Test
    fun `given null checkoutConfiguration then state has isError true immediately`() = runTest {
        val viewModel = PaymentBrickViewModel(checkoutConfiguration = null, fetchInitializationUseCase = fetchUseCase)

        assertEquals(true, viewModel.viewState.value.isError)
        assertEquals(false, viewModel.viewState.value.isLoading)
        coVerify(exactly = 0) { fetchUseCase(any()) }
    }

    @Test
    fun `given non-payment checkout type then state has isError true immediately`() = runTest {
        val cardTransactionConfig = CheckoutConfiguration(
            checkoutType = MPCheckoutType.CardTransaction(
                MPOrder(orderId = "ORD", amount = BigDecimal.TEN, payer = MPPayer(email = "")),
            ),
            paymentMethodConfigs = emptyList(),
        )
        val viewModel = PaymentBrickViewModel(cardTransactionConfig, fetchUseCase)

        assertEquals(true, viewModel.viewState.value.isError)
        coVerify(exactly = 0) { fetchUseCase(any()) }
    }

    @Test
    fun `given valid config then fetch is called with correct orderId and totalAmount`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        PaymentBrickViewModel(paymentConfig(orderId = "ORD_XYZ", amount = BigDecimal("188.00")), fetchUseCase)

        advanceUntilIdle()

        coVerify {
            fetchUseCase(
                FetchPaymentBrickInitializationParams(
                    orderId = "ORD_XYZ",
                    totalAmount = "188.00",
                ),
            )
        }
    }

    @Test
    fun `given cardIds then fetch is called with joined cardIds string`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        PaymentBrickViewModel(paymentConfig(cardIds = listOf("C1", "C2")), fetchUseCase)

        advanceUntilIdle()

        coVerify {
            fetchUseCase(
                match { it.cardIds == "C1,C2" },
            )
        }
    }

    @Test
    fun `given onOptionSelected then emits OnOptionSelected event`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val viewModel = PaymentBrickViewModel(paymentConfig(), fetchUseCase)

        viewModel.onOptionSelected("saved_card_123")

        assertIs<PaymentBrickViewEvent.OnOptionSelected>(viewModel.viewEvent.value)
        assertEquals("saved_card_123", (viewModel.viewEvent.value as PaymentBrickViewEvent.OnOptionSelected).optionId)
    }

    @Test
    fun `given onViewEventConsumed then clears viewEvent`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val viewModel = PaymentBrickViewModel(paymentConfig(), fetchUseCase)
        viewModel.onOptionSelected("any")

        viewModel.onViewEventConsumed()

        assertEquals(null, viewModel.viewEvent.value)
    }

    @Test
    fun `given onBackPressed then does not emit any event`() = runTest {
        coEvery { fetchUseCase(any()) } returns Result.Success(minimalOutput())
        val viewModel = PaymentBrickViewModel(paymentConfig(), fetchUseCase)

        viewModel.onBackPressed()

        assertEquals(null, viewModel.viewEvent.value)
    }
}
