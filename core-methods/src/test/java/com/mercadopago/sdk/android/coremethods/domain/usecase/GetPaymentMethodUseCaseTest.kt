package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.MPResultError
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.MPResult
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

            val expectedMPResult = MPResult.Success(listOf(PaymentMethod()))
            coEvery { repository.getPaymentMethods(any()) } returns expectedMPResult

            val result = useCase(bin)

            assertEquals(expectedMPResult, result)
        }

    @Test
    fun `getPaymentMethods should return error when repository fails`() =
        runBlocking {
            val bin = "12345"

            val expectedErrorMPResult = MPResult.Error(
                MPResultError.Request(code = "400", message = "Repository error")
            )
            coEvery { repository.getPaymentMethods(any()) } returns expectedErrorMPResult

            val result = useCase(bin)

            assertEquals(expectedErrorMPResult, result)
        }
}
