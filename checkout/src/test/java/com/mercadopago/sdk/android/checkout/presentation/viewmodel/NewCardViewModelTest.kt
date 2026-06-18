package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.model.CardFieldTranslationsOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardHolderNameTranslationsOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardInstallmentsHeaderOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardInstallmentsTranslationsOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardSecurityCodeTranslationsOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardTranslationsOutput
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickCardOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchPaymentBrickCardUseCase
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class NewCardViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val useCase = mockk<FetchPaymentBrickCardUseCase>()
    private val viewModel = NewCardViewModel(useCase)

    private fun cardOutput(
        title: String = "Ingresá tu tarjeta",
        button: String = "Pagar",
    ) =
        PaymentBrickCardOutput(
            translations = CardTranslationsOutput(
                cardFormTitle = title,
                cardFormFooterButtonLabel = button,
                cardNumber = CardFieldTranslationsOutput(
                    label = "Número",
                    placeholder = "1234",
                    errorEmptyField = "Completá",
                    errorIncompleteField = "Incompleto",
                    errorInvalidField = null,
                    helper = null,
                ),
                securityCode = CardSecurityCodeTranslationsOutput(
                    label = "CVV",
                    placeholder = "Ej.: 123",
                    tooltip = null,
                    errorEmptyField = "Completá",
                    errorIncompleteField = "Incompleto",
                ),
                expirationDate = CardFieldTranslationsOutput(
                    label = "Vencimiento",
                    placeholder = "MM/AA",
                    errorEmptyField = "Completá",
                    errorIncompleteField = "Incompleto",
                    errorInvalidField = null,
                    helper = null,
                ),
                holderName = CardHolderNameTranslationsOutput(
                    label = "Titular",
                    placeholder = "Nombre",
                    helper = null,
                ),
                installments = CardInstallmentsTranslationsOutput(
                    header = CardInstallmentsHeaderOutput(title = "Elegí las cuotas"),
                    interestFreeLabel = "Sin interés",
                    totalLabel = "Total",
                ),
            ),
            installment = null,
            paymentMethods = emptyList(),
        )

    @Test
    fun `given successful fetch then state is populated with titles`() = runTest {
        coEvery { useCase(any()) } returns Result.Success(cardOutput())

        viewModel.loadCardData(orderId = "ORD", bin = "503143")
        advanceUntilIdle()

        assertEquals("Ingresá tu tarjeta", viewModel.viewState.value.cardFormTitle)
        assertEquals("Pagar", viewModel.viewState.value.continueButtonLabel)
        assertFalse(viewModel.viewState.value.isLoading)
        assertFalse(viewModel.viewState.value.isError)
    }

    @Test
    fun `given fetch error then state has isError true`() = runTest {
        val error = mockk<MercadoPagoCheckoutError>(relaxed = true)
        coEvery { useCase(any()) } returns Result.Error(error)

        viewModel.loadCardData(orderId = "ORD", bin = "503143")
        advanceUntilIdle()

        assertTrue(viewModel.viewState.value.isError)
        assertFalse(viewModel.viewState.value.isLoading)
    }

    @Test
    fun `given exception then isError is true`() = runTest {
        val error = ResponseError(code = "500", message = "err", httpStatus = 500)
        coEvery { useCase(any()) } returns Result.Error(mockk(relaxed = true))

        viewModel.loadCardData(orderId = "ORD", bin = "503143")
        advanceUntilIdle()

        assertTrue(viewModel.viewState.value.isError)
    }
}
