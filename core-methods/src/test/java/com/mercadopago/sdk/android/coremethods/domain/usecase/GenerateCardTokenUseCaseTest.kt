package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.CardModel
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.SecurityCodeModel
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
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
    private val generateCardTokenUseCase = GenerateCardTokenUseCase(repository, paymentMethodsUseCase, sessionIdProvider)

    init {
        every { sessionIdProvider.getSessionId() } returns "test-session-id"
    }

    @Test
    fun `test invoke returns Result Success with valid data`() =
        runBlocking {
            val cardNumber = "4111111111111111"
            val expirationDate = "12/25"
            val securityCode = "123"
            val expectedCardToken = CardToken("sampleToken")
            val expectedResult = Result.Success(expectedCardToken)
            val paymentMethodWithSecurityCodeLengthThree = PaymentMethod(
                card = CardModel(securityCode = SecurityCodeModel(length = 3)),
            )

            coEvery { paymentMethodsUseCase(any()) } returns Result.Success(listOf(paymentMethodWithSecurityCodeLengthThree))
            coEvery { repository.generateCardToken(any()) } returns expectedResult

            val result = generateCardTokenUseCase(cardNumber, securityCode, expirationDate)

            assertTrue(result is Result.Success)
            assertEquals(expectedCardToken, (result as Result.Success).data)
        }

    @Test
    fun `test invoke returns Result Error on failure`() =
        runBlocking {
            val cardNumber = "4111111111111111"
            val expirationDate = "12/25"
            val securityCode = "123"
            val expectedError = ResultError.Request(code = "400", message = "Some error")
            val expectedResult = Result.Error(expectedError)
            val paymentMethodWithSecurityCodeLengthThree = PaymentMethod(
                card = CardModel(securityCode = SecurityCodeModel(length = 3)),
            )

            coEvery { paymentMethodsUseCase(any()) } returns Result.Success(listOf(paymentMethodWithSecurityCodeLengthThree))
            coEvery { repository.generateCardToken(any()) } returns expectedResult

            val result = generateCardTokenUseCase(cardNumber, securityCode, expirationDate)

            assertTrue(result is Result.Error)
            assertEquals(expectedError, (result as Result.Error).error)
        }
}
