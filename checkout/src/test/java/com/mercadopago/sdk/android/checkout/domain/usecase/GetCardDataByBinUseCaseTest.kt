package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.CardData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.CardModel
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.SecurityCodeModel
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

internal class GetCardDataByBinUseCaseTest {
    private val getPaymentMethodsUseCase = mockk<GetPaymentMethodsUseCase>()
    private val getCardIssuersUseCase = mockk<GetCardIssuersUseCase>()
    private val getInstallmentsUseCase = mockk<GetInstallmentsUseCase>()
    private val stringProvider = mockk<StringProvider>()

    private val useCase = GetCardDataByBinUseCase(
        getPaymentMethodsUseCase = getPaymentMethodsUseCase,
        getCardIssuersUseCase = getCardIssuersUseCase,
        getInstallmentsUseCase = getInstallmentsUseCase,
        stringProvider = stringProvider,
    )

    private val bin = "123456"
    private val amount = BigDecimal("100.00")

    private val paymentMethodWithIssuers = PaymentMethod(
        id = "visa",
        paymentTypeId = "credit_card",
        additionalInfoNeeded = listOf("issuer_id"),
        card = CardModel(
            securityCode = SecurityCodeModel(length = 3, mode = "mandatory", location = "back"),
        ),
    )

    private val paymentMethodWithoutIssuers = PaymentMethod(
        id = "visa",
        paymentTypeId = "credit_card",
        additionalInfoNeeded = emptyList(),
        card = CardModel(
            securityCode = SecurityCodeModel(length = 3, mode = "mandatory", location = "back"),
        ),
    )

    private val cardIssuer = CardIssuer(id = "issuer-1", status = "active")
    private val installments = listOf(mockk<Installment>())
    private val serviceError = MercadoPagoCheckoutError.ServiceError(
        code = ErrorCode.SERVICE_ERROR,
        messageError = "Error",
        localized = ErrorLocalized.PAYMENT_METHODS.name,
        throwable = null,
    )

    @Before
    fun setUp() {
        every { stringProvider.getString(R.string.card_form_error_card_number_invalid) } returns "Invalid card number"
    }

    @Test
    fun `given getPaymentMethods returns error then returns that error`() = runTest {
        coEvery { getPaymentMethodsUseCase(bin) } returns Result.Error(serviceError)

        val result = useCase(bin, amount, paymentMethods = null)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertEquals(serviceError, result.error)
    }

    @Test
    fun `given getPaymentMethods returns empty list then returns service error`() = runTest {
        coEvery { getPaymentMethodsUseCase(bin) } returns Result.Success(emptyList())

        val result = useCase(bin, amount, paymentMethods = null)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        val error = result.error
        assertIs<MercadoPagoCheckoutError.ServiceError>(error)
        assertEquals(ErrorCode.SERVICE_ERROR, error.errorCode)
        assertEquals(ErrorLocalized.PAYMENT_METHODS.name, error.errorLocalized)
    }

    @Test
    fun `given no issuers and no amount then returns CardData without issuer and installments`() = runTest {
        coEvery { getPaymentMethodsUseCase(bin) } returns Result.Success(listOf(paymentMethodWithoutIssuers))

        val result = useCase(bin, amount = null, paymentMethods = null)

        assertIs<Result.Success<CardData>>(result)
        val cardData = result.data
        assertEquals(paymentMethodWithoutIssuers, cardData.paymentMethod)
        assertNull(cardData.cardIssuer)
        assertNull(cardData.installments)
        coVerify(exactly = 0) { getCardIssuersUseCase(any(), any()) }
        coVerify(exactly = 0) { getInstallmentsUseCase(any(), any()) }
    }

    @Test
    fun `given payment method with issuers then calls getCardIssuers and returns first issuer`() = runTest {
        coEvery { getPaymentMethodsUseCase(bin) } returns Result.Success(listOf(paymentMethodWithIssuers))
        coEvery { getCardIssuersUseCase(bin, "visa") } returns Result.Success(listOf(cardIssuer))

        val result = useCase(bin, amount = null, paymentMethods = null)

        assertIs<Result.Success<CardData>>(result)
        assertEquals(cardIssuer, result.data.cardIssuer)
    }

    @Test
    fun `given payment method with issuers when getCardIssuers fails then returns that error`() = runTest {
        coEvery { getPaymentMethodsUseCase(bin) } returns Result.Success(listOf(paymentMethodWithIssuers))
        coEvery { getCardIssuersUseCase(bin, "visa") } returns Result.Error(serviceError)

        val result = useCase(bin, amount = null, paymentMethods = null)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertEquals(serviceError, result.error)
    }

    @Test
    fun `given payment method with issuers returning empty list then cardIssuer is null`() = runTest {
        coEvery { getPaymentMethodsUseCase(bin) } returns Result.Success(listOf(paymentMethodWithIssuers))
        coEvery { getCardIssuersUseCase(bin, "visa") } returns Result.Success(emptyList())

        val result = useCase(bin, amount = null, paymentMethods = null)

        assertIs<Result.Success<CardData>>(result)
        assertNull(result.data.cardIssuer)
    }

    @Test
    fun `given amount is provided then calls getInstallments and returns installments`() = runTest {
        coEvery { getPaymentMethodsUseCase(bin) } returns Result.Success(listOf(paymentMethodWithoutIssuers))
        coEvery { getInstallmentsUseCase(bin, amount) } returns Result.Success(installments)

        val result = useCase(bin, amount, paymentMethods = null)

        assertIs<Result.Success<CardData>>(result)
        assertEquals(installments, result.data.installments)
    }

    @Test
    fun `given amount is provided when getInstallments fails then returns that error`() = runTest {
        coEvery { getPaymentMethodsUseCase(bin) } returns Result.Success(listOf(paymentMethodWithoutIssuers))
        coEvery { getInstallmentsUseCase(bin, amount) } returns Result.Error(serviceError)

        val result = useCase(bin, amount, paymentMethods = null)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertEquals(serviceError, result.error)
    }

    @Test
    fun `given no amount then does not call getInstallments`() = runTest {
        coEvery { getPaymentMethodsUseCase(bin) } returns Result.Success(listOf(paymentMethodWithoutIssuers))

        useCase(bin, amount = null, paymentMethods = null)

        coVerify(exactly = 0) { getInstallmentsUseCase(any(), any()) }
    }

    @Test
    fun `given payment method with issuers and amount then returns complete CardData`() = runTest {
        coEvery { getPaymentMethodsUseCase(bin) } returns Result.Success(listOf(paymentMethodWithIssuers))
        coEvery { getCardIssuersUseCase(bin, "visa") } returns Result.Success(listOf(cardIssuer))
        coEvery { getInstallmentsUseCase(bin, amount) } returns Result.Success(installments)

        val result = useCase(bin, amount, paymentMethods = null)

        assertIs<Result.Success<CardData>>(result)
        with(result.data) {
            assertEquals(paymentMethodWithIssuers, paymentMethod)
            assertEquals(cardIssuer, cardIssuer)
            assertEquals(installments, this.installments)
            assertEquals(3, securityCode.length)
            assertEquals("mandatory", securityCode.mode)
            assertEquals("back", securityCode.location)
        }
    }

    @Test
    fun `given payment method with no card info then security code uses defaults`() = runTest {
        val paymentMethodNoCard = PaymentMethod(
            id = "visa",
            additionalInfoNeeded = emptyList(),
            card = null,
        )
        coEvery { getPaymentMethodsUseCase(bin) } returns Result.Success(listOf(paymentMethodNoCard))

        val result = useCase(bin, amount = null, paymentMethods = null)

        assertIs<Result.Success<CardData>>(result)
        with(result.data.securityCode) {
            assertEquals(3, length)
            assertEquals("mandatory", mode)
            assertEquals("back", location)
        }
    }
}
