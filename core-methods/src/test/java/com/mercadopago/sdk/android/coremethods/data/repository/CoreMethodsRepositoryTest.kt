package com.mercadopago.sdk.android.coremethods.data.repository

import com.mercadopago.sdk.android.coremethods.data.datasource.remote.CoreMethodsRemoteDataSource
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.params.GenerateCardTokenParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetInstallmentParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import kotlin.test.Test

internal class CoreMethodsRepositoryTest {
    private val dataSource: CoreMethodsRemoteDataSource = mockk()
    private val repository = CoreMethodsRepositoryImpl(dataSource)

    @Test
    fun `test generateCardToken returns Success`() =
        runBlocking {
            val generateCardTokenParams = GenerateCardTokenParams(cardId = "card_123")

            val response = Result.Success(CardToken(token = "token_id"))
            coEvery { dataSource.generateCardToken(any()) } returns response

            val result = repository.generateCardToken(generateCardTokenParams)

            assertTrue(result is Result.Success)
            assertEquals("token_id", (result as Result.Success).data.token)
        }

    @Test
    fun `test generateCardToken returns Error`() =
        runBlocking {
            val generateCardTokenParams = GenerateCardTokenParams(cardId = "card_123")

            val errorResponse = ResultError(code = "400", message = "Bad Request")
            val response: Result<CardToken, ResultError> = Result.Error(errorResponse)
            coEvery { dataSource.generateCardToken(any()) } returns response

            val result = repository.generateCardToken(generateCardTokenParams)

            assertTrue(result is Result.Error)
            assertEquals("Bad Request", (result as Result.Error).error.message)
            assertEquals("400", result.error.code)
        }

    @Test
    fun `test getInstallment returns Success`() =
        runBlocking {
            val installmentParams = GetInstallmentParams(bin = 12345678)

            val response = Result.Success(Installment(paymentMethodId = "credit"))
            coEvery { dataSource.getInstallments(any()) } returns response

            val result = repository.getInstallment(installmentParams)

            assertTrue(result is Result.Success)
            assertEquals("credit", (result as Result.Success).data.paymentMethodId)
        }

    @Test
    fun `test getInstallment returns Error`() =
        runBlocking {
            val installmentParams = GetInstallmentParams(bin = 12345678)

            val errorResponse = ResultError(code = "400", message = "Bad Request")
            val response: Result<Installment, ResultError> = Result.Error(errorResponse)
            coEvery { dataSource.getInstallments(any()) } returns response

            val result = repository.getInstallment(installmentParams)

            assertTrue(result is Result.Error)
            assertEquals("Bad Request", (result as Result.Error).error.message)
            assertEquals("400", result.error.code)
        }

    @Test
    fun `test getIdentificationTypes returns Success`() =
        runBlocking {
            val response = Result.Success(
                listOf(IdentificationType(id = "0", name = "rg", type = "rg", minLength = 10, maxLength = 10)),
            )
            coEvery { dataSource.getIdentificationTypes() } returns response

            val result = repository.getIdentificationTypes()

            assertTrue(result is Result.Success)
            assertEquals("rg", (result as Result.Success).data.first().type)
        }

    @Test
    fun `test getIdentificationTypes returns Error`() =
        runBlocking {
            val errorResponse = ResultError(code = "400", message = "Bad Request")
            val response: Result<List<IdentificationType>, ResultError> = Result.Error(errorResponse)
            coEvery { dataSource.getIdentificationTypes() } returns response

            val result = repository.getIdentificationTypes()

            assertTrue(result is Result.Error)
            assertEquals("Bad Request", (result as Result.Error).error.message)
            assertEquals("400", result.error.code)
        }
}
