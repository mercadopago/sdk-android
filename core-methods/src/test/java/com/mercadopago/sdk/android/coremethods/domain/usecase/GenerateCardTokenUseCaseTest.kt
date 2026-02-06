package com.mercadopago.sdk.android.coremethods.domain.usecase

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
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class GenerateCardTokenUseCaseTest {
    private val repository: CoreMethodsRepository = mockk()
    private val sessionIdProvider: SessionIdProvider = mockk()
    private val paymentMethodsUseCase: GetPaymentMethodsUseCase = mockk()
    private val isSecurityCodeValidUseCase: IsSecurityCodeValidUseCase = mockk()
    private val generateCardTokenUseCase =
        GenerateCardTokenUseCase(repository, paymentMethodsUseCase, sessionIdProvider, isSecurityCodeValidUseCase)

    init {
        every { sessionIdProvider.getSessionId() } returns "test-session-id"
    }

    @Test
    fun `test invoke returns Result Success with valid data`() =
        runBlocking {
            val inputCardNumber = "4111111111111111"
            val inputExpirationDate = "12/25"
            val inputSecurityCode = "123"
            val expectedCardToken = CardToken("sampleToken")
            val expectedResult = Result.Success(expectedCardToken)
            val mockPaymentMethodWithSecurityCodeLengthThree = PaymentMethod(
                card = CardModel(securityCode = SecurityCodeModel(length = 3)),
            )
            every { isSecurityCodeValidUseCase(any(), any()) } returns true
            coEvery { paymentMethodsUseCase(any()) } returns
                Result.Success(listOf(mockPaymentMethodWithSecurityCodeLengthThree))
            coEvery { repository.generateCardToken(any()) } returns expectedResult
            val actualResult = generateCardTokenUseCase(inputCardNumber, inputSecurityCode, inputExpirationDate)
            assertTrue(actualResult is Result.Success)
            assertEquals(expectedCardToken, (actualResult as Result.Success).data)
        }

    @Test
    fun `test invoke returns Result Error on repository failure`() =
        runBlocking {
            val inputCardNumber = "4111111111111111"
            val inputExpirationDate = "12/25"
            val inputSecurityCode = "123"
            val expectedError = ResultError.Request(code = "400", message = "Some error")
            val expectedResult = Result.Error(expectedError)
            val mockPaymentMethodWithSecurityCodeLengthThree = PaymentMethod(
                card = CardModel(securityCode = SecurityCodeModel(length = 3)),
            )
            every { isSecurityCodeValidUseCase(any(), any()) } returns true
            coEvery { paymentMethodsUseCase(any()) } returns
                Result.Success(listOf(mockPaymentMethodWithSecurityCodeLengthThree))
            coEvery { repository.generateCardToken(any()) } returns expectedResult
            val actualResult = generateCardTokenUseCase(inputCardNumber, inputSecurityCode, inputExpirationDate)
            assertTrue(actualResult is Result.Error)
            assertEquals(expectedError, (actualResult as Result.Error).error)
        }

    @Test
    fun `test invoke returns Result Error when security code is invalid`() =
        runBlocking {
            val inputCardNumber = "4111111111111111"
            val inputExpirationDate = "12/25"
            val inputSecurityCode = "12"
            val mockPaymentMethodWithSecurityCodeLengthThree = PaymentMethod(
                card = CardModel(securityCode = SecurityCodeModel(length = 3)),
            )
            every { isSecurityCodeValidUseCase(any(), any()) } returns false
            coEvery { paymentMethodsUseCase(any()) } returns
                Result.Success(listOf(mockPaymentMethodWithSecurityCodeLengthThree))
            val actualResult = generateCardTokenUseCase(inputCardNumber, inputSecurityCode, inputExpirationDate)
            assertTrue(actualResult is Result.Error)
            assertTrue((actualResult as Result.Error).error is ResultError.Validation)
        }
}
