package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.MPResultError
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.MPResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class GenerateCardTokenUseCaseTest {
    private val repository: CoreMethodsRepository = mockk()
    private val generateCardTokenUseCase = GenerateCardTokenUseCase(repository)

    @Test
    fun `test invoke returns Result Success with valid data`() =
        runBlocking {
            val cardNumber = "4111111111111111"
            val expirationDate = "12/25"
            val securityCode = "123"
            val expectedCardToken = CardToken("sampleToken")
            val expectedMPResult = MPResult.Success(expectedCardToken)

            // Mock the repository behavior
            coEvery { repository.generateCardToken(any()) } returns (expectedMPResult)

            // Call the use case
            val result = generateCardTokenUseCase(cardNumber, expirationDate, securityCode)

            // Verify the result
            assertTrue(result is MPResult.Success)
            assertEquals(expectedCardToken, (result as MPResult.Success).data)
        }

    @Test
    fun `test invoke returns Result Error on failure`() =
        runBlocking {
            val cardNumber = "4111111111111111"
            val expirationDate = "12/25"
            val securityCode = "123"
            val expectedError = MPResultError.Request(code = "400", message = "Some error")
            val expectedMPResult = MPResult.Error(expectedError)

            // Mock the repository behavior
            coEvery { repository.generateCardToken(any()) } returns (expectedMPResult)

            // Call the use case
            val result = generateCardTokenUseCase(cardNumber, expirationDate, securityCode)

            // Verify the result
            assertTrue(result is MPResult.Error)
            assertEquals(expectedError, (result as MPResult.Error).error)
        }
}
