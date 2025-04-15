package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import kotlin.test.Test

class GetPaymentMethodUseCaseTest {
    private val repository: CoreMethodsRepository = mockk()
    private val useCase = GetPaymentMethodsUseCase(repository)

    @Test
    fun `getPaymentMethods should call repository with correct parameters`() =
        runBlocking {
            val bin = "12345"

            val expectedResult = Result.Success(listOf(PaymentMethod()))
            coEvery { repository.getPaymentMethods(any()) } returns expectedResult

            val result = useCase(bin)

            assertEquals(expectedResult, result)
        }

    @Test
    fun `getPaymentMethods should return error when repository fails`() =
        runBlocking {
            val bin = "12345"

            val expectedErrorResult = Result.Error(ResultError.Request(code = "400", message = "Repository error"))
            coEvery { repository.getPaymentMethods(any()) } returns expectedErrorResult

            val result = useCase(bin)

            assertEquals(expectedErrorResult, result)
        }
}
