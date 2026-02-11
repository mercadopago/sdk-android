package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.domain.model.CardModel
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.SecurityCodeModel
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.usecase.validations.IsSecurityCodeValidUseCase
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.di.SessionIdProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class GenerateCardTokenWithSecurityCodeUseCaseTest {
    private val repository: CoreMethodsRepository = mockk()
    private val sessionIdProvider: SessionIdProvider = mockk()
    private val paymentMethodsUseCase: GetPaymentMethodsUseCase = mockk()
    private val isSecurityCodeValidUseCase: IsSecurityCodeValidUseCase = mockk()
    private val useCase: GenerateCardTokenWithSecurityCodeUseCase =
        GenerateCardTokenWithSecurityCodeUseCase(
            repository = repository,
            paymentMethodsUseCase = paymentMethodsUseCase,
            sessionIdProvider = sessionIdProvider,
            isSecurityCodeValidUseCase = isSecurityCodeValidUseCase,
        )

    init {
        every { sessionIdProvider.getSessionId() } returns "test-session-id"
    }

    @Test
    fun `invoke returns Result Success with valid data and no buyer identification`() =
        runBlocking {
            val inputCardNumber = "4111111111111111"
            val inputSecurityCode = "123"
            val inputExpirationDate = "1225"
            val expectedCardToken = CardToken("sampleToken")
            val expectedResult = Result.Success(expectedCardToken)
            val mockPaymentMethod = PaymentMethod(
                card = CardModel(securityCode = SecurityCodeModel(length = 3)),
            )
            every { isSecurityCodeValidUseCase(3, 3) } returns true
            coEvery { paymentMethodsUseCase(any()) } returns Result.Success(listOf(mockPaymentMethod))
            coEvery { repository.generateCardToken(any()) } returns expectedResult
            val actualResult = useCase(
                cardNumber = inputCardNumber,
                securityCode = inputSecurityCode,
                expirationDate = inputExpirationDate,
                buyerIdentification = null,
            )
            assertTrue(actualResult is Result.Success)
            assertEquals(expectedCardToken, (actualResult as Result.Success).data)
        }

    @Test
    fun `invoke returns Result Success with valid data and buyer identification`() =
        runBlocking {
            val inputCardNumber = "4111111111111111"
            val inputSecurityCode = "123"
            val inputExpirationDate = "1225"
            val inputBuyerIdentification = BuyerIdentification(
                name = "John Doe",
                number = "12345678",
                type = "CPF",
            )
            val expectedCardToken = CardToken("sampleToken")
            val expectedResult = Result.Success(expectedCardToken)
            val mockPaymentMethod = PaymentMethod(
                card = CardModel(securityCode = SecurityCodeModel(length = 3)),
            )
            every { isSecurityCodeValidUseCase(3, 3) } returns true
            coEvery { paymentMethodsUseCase(any()) } returns Result.Success(listOf(mockPaymentMethod))
            coEvery { repository.generateCardToken(any()) } returns expectedResult
            val actualResult = useCase(
                cardNumber = inputCardNumber,
                securityCode = inputSecurityCode,
                expirationDate = inputExpirationDate,
                buyerIdentification = inputBuyerIdentification,
            )
            assertTrue(actualResult is Result.Success)
            assertEquals(expectedCardToken, (actualResult as Result.Success).data)
        }

    @Test
    fun `invoke returns Result Error when card number is empty`() =
        runBlocking {
            val inputCardNumber = ""
            val inputSecurityCode = "123"
            val inputExpirationDate = "1225"
            val actualResult = useCase(
                cardNumber = inputCardNumber,
                securityCode = inputSecurityCode,
                expirationDate = inputExpirationDate,
            )
            assertTrue(actualResult is Result.Error)
            val error = (actualResult as Result.Error).error
            assertTrue(error is ResultError.Validation)
            assertEquals("card number cannot be empty", (error as ResultError.Validation).message)
        }

    @Test
    fun `invoke returns Result Error when security code is empty`() =
        runBlocking {
            val inputCardNumber = "4111111111111111"
            val inputSecurityCode = ""
            val inputExpirationDate = "1225"
            val actualResult = useCase(
                cardNumber = inputCardNumber,
                securityCode = inputSecurityCode,
                expirationDate = inputExpirationDate,
            )
            assertTrue(actualResult is Result.Error)
            val error = (actualResult as Result.Error).error
            assertTrue(error is ResultError.Validation)
        }

    @Test
    fun `invoke returns Result Error when security code is invalid`() =
        runBlocking {
            val inputCardNumber = "4111111111111111"
            val inputSecurityCode = "12"
            val inputExpirationDate = "1225"
            val mockPaymentMethod = PaymentMethod(
                card = CardModel(securityCode = SecurityCodeModel(length = 3)),
            )
            every { isSecurityCodeValidUseCase(2, 3) } returns false
            coEvery { paymentMethodsUseCase(any()) } returns Result.Success(listOf(mockPaymentMethod))
            val actualResult = useCase(
                cardNumber = inputCardNumber,
                securityCode = inputSecurityCode,
                expirationDate = inputExpirationDate,
            )
            assertTrue(actualResult is Result.Error)
            assertTrue((actualResult as Result.Error).error is ResultError.Validation)
        }

    @Test
    fun `invoke returns Result Error when expiration date is empty`() =
        runBlocking {
            val inputCardNumber = "4111111111111111"
            val inputSecurityCode = "123"
            val inputExpirationDate = ""
            val mockPaymentMethod = PaymentMethod(
                card = CardModel(securityCode = SecurityCodeModel(length = 3)),
            )
            every { isSecurityCodeValidUseCase(any<Int>(), any<Int>()) } returns true
            coEvery { paymentMethodsUseCase(any()) } returns Result.Success(listOf(mockPaymentMethod))
            val actualResult = useCase(
                cardNumber = inputCardNumber,
                securityCode = inputSecurityCode,
                expirationDate = inputExpirationDate,
            )
            assertTrue(actualResult is Result.Error)
            assertTrue((actualResult as Result.Error).error is ResultError.Validation)
        }

    @Test
    fun `invoke returns Result Error when expiration date length is smaller than two`() =
        runBlocking {
            val inputCardNumber = "4111111111111111"
            val inputSecurityCode = "123"
            val inputExpirationDate = "1"
            val mockPaymentMethod = PaymentMethod(
                card = CardModel(securityCode = SecurityCodeModel(length = 3)),
            )
            every { isSecurityCodeValidUseCase(any<Int>(), any<Int>()) } returns true
            coEvery { paymentMethodsUseCase(any()) } returns Result.Success(listOf(mockPaymentMethod))
            val actualResult = useCase(
                cardNumber = inputCardNumber,
                securityCode = inputSecurityCode,
                expirationDate = inputExpirationDate,
            )
            assertTrue(actualResult is Result.Error)
            assertTrue((actualResult as Result.Error).error is ResultError.Validation)
        }

    @Test
    fun `invoke returns Result Error on repository failure`() =
        runBlocking {
            val inputCardNumber = "4111111111111111"
            val inputSecurityCode = "123"
            val inputExpirationDate = "1225"
            val expectedError = ResultError.Request(code = "400", message = "Bad request")
            val expectedResult = Result.Error(expectedError)
            val mockPaymentMethod = PaymentMethod(
                card = CardModel(securityCode = SecurityCodeModel(length = 3)),
            )
            every { isSecurityCodeValidUseCase(any<Int>(), any<Int>()) } returns true
            coEvery { paymentMethodsUseCase(any()) } returns Result.Success(listOf(mockPaymentMethod))
            coEvery { repository.generateCardToken(any()) } returns expectedResult
            val actualResult = useCase(
                cardNumber = inputCardNumber,
                securityCode = inputSecurityCode,
                expirationDate = inputExpirationDate,
            )
            assertTrue(actualResult is Result.Error)
            assertEquals(expectedError, (actualResult as Result.Error).error)
        }

    @Test
    fun `invoke uses SECURITY_CODE_MIN_LENGTH when payment methods use case returns Error`() =
        runBlocking {
            val inputCardNumber = "4111111111111111"
            val inputSecurityCode = "123"
            val inputExpirationDate = "1225"
            val expectedCardToken = CardToken("sampleToken")
            every { isSecurityCodeValidUseCase(3, 3) } returns true
            coEvery { paymentMethodsUseCase(any()) } returns Result.Error(ResultError.Request("500", "Server error"))
            coEvery { repository.generateCardToken(any()) } returns Result.Success(expectedCardToken)
            val actualResult = useCase(
                cardNumber = inputCardNumber,
                securityCode = inputSecurityCode,
                expirationDate = inputExpirationDate,
            )
            assertTrue(actualResult is Result.Success)
            assertEquals(expectedCardToken, (actualResult as Result.Success).data)
        }

    @Test
    fun `invoke returns Result Error when payment methods returns Error and security code length is invalid`() =
        runBlocking {
            val inputCardNumber = "4111111111111111"
            val inputSecurityCode = "12"
            val inputExpirationDate = "1225"
            every { isSecurityCodeValidUseCase(2, 3) } returns false
            coEvery { paymentMethodsUseCase(any()) } returns Result.Error(ResultError.Request("500", "Server error"))
            val actualResult = useCase(
                cardNumber = inputCardNumber,
                securityCode = inputSecurityCode,
                expirationDate = inputExpirationDate,
            )
            assertTrue(actualResult is Result.Error)
            assertTrue((actualResult as Result.Error).error is ResultError.Validation)
        }

    @Test
    fun `invoke uses SECURITY_CODE_MIN_LENGTH when payment method has null security code length`() =
        runBlocking {
            val inputCardNumber = "4111111111111111"
            val inputSecurityCode = "123"
            val inputExpirationDate = "1225"
            val expectedCardToken = CardToken("sampleToken")
            val mockPaymentMethodWithNullSecurityCode = PaymentMethod(
                card = CardModel(securityCode = SecurityCodeModel(length = null)),
            )
            every { isSecurityCodeValidUseCase(3, 3) } returns true
            coEvery { paymentMethodsUseCase(any()) } returns
                Result.Success(listOf(mockPaymentMethodWithNullSecurityCode))
            coEvery { repository.generateCardToken(any()) } returns Result.Success(expectedCardToken)
            val actualResult = useCase(
                cardNumber = inputCardNumber,
                securityCode = inputSecurityCode,
                expirationDate = inputExpirationDate,
            )
            assertTrue(actualResult is Result.Success)
            assertEquals(expectedCardToken, (actualResult as Result.Success).data)
        }

    @Test
    fun `invoke uses SECURITY_CODE_MIN_LENGTH when payment methods returns empty list`() =
        runBlocking {
            val inputCardNumber = "4111111111111111"
            val inputSecurityCode = "123"
            val inputExpirationDate = "1225"
            val expectedCardToken = CardToken("sampleToken")
            every { isSecurityCodeValidUseCase(3, 3) } returns true
            coEvery { paymentMethodsUseCase(any()) } returns Result.Success(emptyList())
            coEvery { repository.generateCardToken(any()) } returns Result.Success(expectedCardToken)
            val actualResult = useCase(
                cardNumber = inputCardNumber,
                securityCode = inputSecurityCode,
                expirationDate = inputExpirationDate,
            )
            assertTrue(actualResult is Result.Success)
            assertEquals(expectedCardToken, (actualResult as Result.Success).data)
        }

    @Test
    fun `invoke calls payment methods use case with card bin from first CARD_BIN_LENGTH characters`() =
        runBlocking {
            val inputCardNumber = "4111111111111111"
            val inputSecurityCode = "123"
            val inputExpirationDate = "1225"
            val expectedCardToken = CardToken("sampleToken")
            val mockPaymentMethod = PaymentMethod(
                card = CardModel(securityCode = SecurityCodeModel(length = 3)),
            )
            every { isSecurityCodeValidUseCase(any<Int>(), any<Int>()) } returns true
            coEvery { paymentMethodsUseCase("411") } returns Result.Success(listOf(mockPaymentMethod))
            coEvery { repository.generateCardToken(any()) } returns Result.Success(expectedCardToken)
            useCase(
                cardNumber = inputCardNumber,
                securityCode = inputSecurityCode,
                expirationDate = inputExpirationDate,
            )
            coVerify { paymentMethodsUseCase("411") }
        }
}
