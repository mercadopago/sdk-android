package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPOrder
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.ScreenConfig
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.model.CardDataOutput
import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsFooterOutput
import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsHeaderOutput
import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickFooterOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentMethodOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentSectionOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeFieldOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeScreenOutput
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
import kotlin.test.assertIs
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
internal class PaymentBrickEmptyInstallmentsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fetchUseCase = mockk<FetchPaymentBrickInitializationUseCase>()
    private val processUseCase = mockk<ProcessOrderUseCase>()
    private val generateTokenUseCase = mockk<GenerateTokenWithCardIdUseCase>()

    @Before
    fun setUp() {
        mockkObject(CheckoutCallbackHolder)
    }

    @After
    fun tearDown() {
        unmockkObject(CheckoutCallbackHolder)
    }

    @Test
    fun `given empty installments when card selected with review then emits unavailable error`() = runTest {
        val viewModel = createViewModel(withReviewAndConfirm = true)
        advanceUntilIdle()

        viewModel.onOptionSelected(CARD_ID)
        advanceUntilIdle()

        assertInstallmentsUnavailable(viewModel)
    }

    @Test
    fun `given empty installments when card selected without review then emits unavailable error`() = runTest {
        val viewModel = createViewModel(withReviewAndConfirm = false)
        advanceUntilIdle()

        viewModel.onOptionSelected(CARD_ID)
        advanceUntilIdle()

        assertInstallmentsUnavailable(viewModel)
    }

    @Test
    fun `given empty installments after CVV then does not process or notify seller`() = runTest {
        val viewModel = createViewModel(
            withReviewAndConfirm = true,
            requiresSecurityCode = true,
        )
        advanceUntilIdle()

        viewModel.processOrder(cardId = CARD_ID, token = "TOKEN_123")
        advanceUntilIdle()

        assertInstallmentsUnavailable(viewModel)
    }

    @Test
    fun `given empty installments after CVV without review then does not process or notify seller`() = runTest {
        val viewModel = createViewModel(
            withReviewAndConfirm = false,
            requiresSecurityCode = true,
        )
        advanceUntilIdle()

        viewModel.processOrder(cardId = CARD_ID, token = "TOKEN_123")
        advanceUntilIdle()

        assertInstallmentsUnavailable(viewModel)
    }

    private fun createViewModel(
        withReviewAndConfirm: Boolean,
        requiresSecurityCode: Boolean = false,
    ): PaymentBrickViewModel {
        coEvery { fetchUseCase(any()) } returns Result.Success(
            initializationOutput(requiresSecurityCode),
        )
        return PaymentBrickViewModel(
            checkoutConfiguration = paymentConfig(withReviewAndConfirm),
            fetchInitializationUseCase = fetchUseCase,
            processOrderUseCase = processUseCase,
            getSecurityCodeScreenUseCase = GetSecurityCodeScreenUseCase(),
            fetchMethodSelectionScreenUseCase = FetchMethodSelectionScreenUseCase(),
            generateTokenWithCardIdUseCase = generateTokenUseCase,
            cancelledPaymentContextUseCase = CancelledPaymentContextUseCase(),
        )
    }

    private fun assertInstallmentsUnavailable(
        viewModel: PaymentBrickViewModel,
    ) {
        assertIs<PaymentBrickViewEvent.OnInstallmentsUnavailable>(viewModel.viewEvent.value)
        assertNull(viewModel.viewState.value.pendingInstallmentData)
        coVerify(exactly = 0) { generateTokenUseCase(any()) }
        coVerify(exactly = 0) { processUseCase(any()) }
        verify(exactly = 0) { CheckoutCallbackHolder.notify(any()) }
    }

    private fun paymentConfig(
        withReviewAndConfirm: Boolean,
    ) = CheckoutConfiguration(
        checkoutType = MPCheckoutType.Payment(
            order = MPOrder(orderId = "ORDER_123", clientToken = "client_token"),
        ),
        paymentMethodConfigs = emptyList(),
        screenConfigs = if (withReviewAndConfirm) {
            listOf(ScreenConfig.ReviewAndConfirm())
        } else {
            emptyList()
        },
    )

    private fun initializationOutput(
        requiresSecurityCode: Boolean,
    ) = PaymentBrickInitializationOutput(
        headerTitle = "Elegí cómo pagar",
        sections = listOf(
            PaymentSectionOutput(
                title = "Tarjetas",
                methods = listOf(emptyInstallmentsMethod(requiresSecurityCode)),
            ),
        ),
        footer = PaymentBrickFooterOutput(totalLabel = "Total", totalAmount = "$ 500"),
    )

    private fun emptyInstallmentsMethod(
        requiresSecurityCode: Boolean,
    ) = PaymentMethodOutput(
        type = "saved_card",
        title = "Visa **** 1234",
        cardData = CardDataOutput(
            id = CARD_ID,
            bin = "503143",
            lastFourDigits = "1234",
            paymentMethodId = "visa",
            paymentTypeId = "credit_card",
            issuerId = 1,
            securityCode = SecurityCodeOutput(
                length = 3,
                screen = if (requiresSecurityCode) securityCodeScreen() else null,
            ),
            installments = InstallmentsOutput(
                header = InstallmentsHeaderOutput(title = "Elegí las cuotas"),
                footer = InstallmentsFooterOutput(
                    totalLabel = "Total",
                    buttonLabel = "Pagar",
                    currencySymbol = "R$",
                ),
                selectionType = "radio_button",
                quotas = emptyList(),
            ),
        ),
    )

    private fun securityCodeScreen() = SecurityCodeScreenOutput(
        headerTitle = "Código de seguridad",
        field = SecurityCodeFieldOutput(
            label = "CVV",
            placeholder = "***",
            helper = "Ingresá el código",
            error = null,
        ),
        buttonLabel = "Continuar",
    )

    private companion object {
        const val CARD_ID = "CARD_WITH_EMPTY_INSTALLMENTS"
    }
}
