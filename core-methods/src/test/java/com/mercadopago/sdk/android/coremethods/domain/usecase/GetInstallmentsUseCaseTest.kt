package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

internal class GetInstallmentsUseCaseTest {
    private val repository: CoreMethodsRepository = mockk()
    private val getInstallmentsUseCase = GetInstallmentsUseCase(repository)

    @Test
    fun `invoke should call repository with correct parameters`() =
        runBlocking {
            val bin = "123456"
            val amount = 1000L
            val processingMode = "gateway"

            // Setup mock response
            val expectedResult = Result.Success(Installment()) // Supondo que você tenha um resultado de sucesso
            coEvery { repository.getInstallment(any()) } returns expectedResult

            // Invoke the use case
            val result = getInstallmentsUseCase(bin, amount, processingMode)

            // Assert that the result is as expected
            assertEquals(expectedResult, result)
        }

    @Test
    fun `invoke should return error when repository fails`() =
        runBlocking {
            val bin = "123456"
            val amount = 1000L
            val processingMode = "gateway"

            // Setup mock to return an error
            val expectedErrorResult = Result.Error(ResultError.Request(code = 400, message = "Repository error"))
            coEvery { repository.getInstallment(any()) } returns expectedErrorResult

            // Invoke the use case
            val result = getInstallmentsUseCase(bin, amount, processingMode)

            // Assert that the result is the error that we expect
            assertEquals(expectedErrorResult, result)
        }
}
