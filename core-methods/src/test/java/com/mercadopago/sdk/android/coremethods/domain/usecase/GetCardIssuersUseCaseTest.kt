package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class GetCardIssuersUseCaseTest {
    private val repository: CoreMethodsRepository = mockk()
    private val getCardIssuersUseCase = GetCardIssuersUseCase(repository)

    @Test
    fun `invoke should call repository with correct parameters`() =
        runBlocking {
            val bin = 12345
            val productId = "123123"
            val paymentMethodId = "credit"

            val expectedResult = Result.Success(listOf(CardIssuer()))
            coEvery { repository.getCardIssuers(any()) } returns expectedResult

            val result = getCardIssuersUseCase(productId, bin, paymentMethodId)

            assertEquals(expectedResult, result)
        }

    @Test
    fun `invoke should return error when repository fails`() =
        runBlocking {
            val bin = 12345
            val productId = "123123"
            val paymentMethodId = "credit"

            val expectedErrorResult = Result.Error(ResultError("Repository error"))
            coEvery { repository.getCardIssuers(any()) } returns expectedErrorResult

            val result = getCardIssuersUseCase(productId, bin, paymentMethodId)

            assertEquals(expectedErrorResult, result)
        }
}
