package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import org.junit.Assert.assertEquals
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class GenerateCardTokenUseCaseTest {

    private val repository: CoreMethodsRepository = mock()
    private val generateCardTokenUseCase = GenerateCardTokenUseCase(repository)

    @Test
    fun `test invoke returns Result Success with valid data`() = runBlocking {
        val cardNumber = "4111111111111111"
        val expirationDate = "12/25"
        val securityCode = "123"
        val expectedCardToken = CardToken("sampleToken")
        val expectedResult = Result.Success(expectedCardToken)

        // Mock the repository behavior
        whenever(repository.generateCardToken(any())).thenReturn(expectedResult)

        // Call the use case
        val result = generateCardTokenUseCase(cardNumber, expirationDate, securityCode)

        // Verify the result
        assertTrue(result is Result.Success)
        assertEquals(expectedCardToken, (result as Result.Success).data)
    }

    @Test
    fun `test invoke returns Result Error on failure`() = runBlocking {
        val cardNumber = "4111111111111111"
        val expirationDate = "12/25"
        val securityCode = "123"
        val expectedError = ResultError("Some error")
        val expectedResult = Result.Error(expectedError)

        // Mock the repository behavior
        whenever(repository.generateCardToken(any())).thenReturn(expectedResult)

        // Call the use case
        val result = generateCardTokenUseCase(cardNumber, expirationDate, securityCode)

        // Verify the result
        assertTrue(result is Result.Error)
        assertEquals(expectedError, (result as Result.Error).error)
    }
}
