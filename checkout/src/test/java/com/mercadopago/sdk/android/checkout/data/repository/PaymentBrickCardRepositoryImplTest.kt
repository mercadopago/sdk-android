package com.mercadopago.sdk.android.checkout.data.repository

import com.mercadopago.sdk.android.checkout.data.remote.datasource.PaymentBrickCardRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.response.CardFieldTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.CardHolderNameTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.CardInstallmentsHeaderTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.CardInstallmentsTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.CardSecurityCodeTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.CardTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentBrickCardResponse
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickCardOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickCardParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class PaymentBrickCardRepositoryImplTest {
    private val dataSource = mockk<PaymentBrickCardRemoteDataSource>()
    private val repository = PaymentBrickCardRepositoryImpl(dataSource)

    private val params = FetchPaymentBrickCardParams(orderId = "ORDER_123", bin = "503143")

    private fun minimalResponse() = PaymentBrickCardResponse(
        translations = CardTranslations(
            cardFormTitle = "Ingresá tu tarjeta",
            cardFormFooterButtonLabel = "Pagar",
            cardNumber = CardFieldTranslations(
                label = "Número",
                placeholder = "1234",
                errorEmptyField = "Completá",
                errorIncompleteField = "Incompleto",
            ),
            securityCode = CardSecurityCodeTranslations(
                label = "CVV",
                placeholder = "Ej.: 123",
                errorEmptyField = "Completá",
                errorIncompleteField = "Incompleto",
            ),
            expirationDate = CardFieldTranslations(
                label = "Vencimiento",
                placeholder = "MM/AA",
                errorEmptyField = "Completá",
                errorIncompleteField = "Incompleto",
            ),
            holderName = CardHolderNameTranslations(label = "Titular", placeholder = "Nombre"),
            installments = CardInstallmentsTranslations(
                header = CardInstallmentsHeaderTranslations(
                    chevron = "Elegí",
                    radio = "Elegí",
                    title = "Elegí las cuotas",
                ),
                interestFreeLabel = "Sin interés",
                totalLabel = "Total",
            ),
        ),
        installment = null,
        paymentMethods = emptyList(),
    )

    @Test
    fun `given dataSource returns success then returns mapped output`() = runTest {
        coEvery { dataSource.fetch(params) } returns Result.Success(minimalResponse())

        val result = repository.fetch(params)

        val success = assertIs<Result.Success<PaymentBrickCardOutput>>(result)
        assertEquals("Ingresá tu tarjeta", success.data.translations.cardFormTitle)
    }

    @Test
    fun `given dataSource returns error then propagates error`() = runTest {
        val error = ResponseError(code = "500", message = "Server error", httpStatus = 500)
        coEvery { dataSource.fetch(params) } returns Result.Error(error)

        val result = repository.fetch(params)

        val resultError = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("500", resultError.error.code)
    }

    @Test
    fun `given dataSource throws then withErrorHandling catches it`() = runTest {
        coEvery { dataSource.fetch(params) } throws RuntimeException("Network failure")

        val result = repository.fetch(params)

        assertIs<Result.Error<ResponseError>>(result)
    }

    @Test
    fun `given fetch is called then delegates to dataSource`() = runTest {
        coEvery { dataSource.fetch(params) } returns Result.Success(minimalResponse())

        repository.fetch(params)

        coVerify(exactly = 1) { dataSource.fetch(params) }
    }
}
