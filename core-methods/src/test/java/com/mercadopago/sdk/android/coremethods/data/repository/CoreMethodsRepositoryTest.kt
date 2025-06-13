package com.mercadopago.sdk.android.coremethods.data.repository

import com.mercadopago.sdk.android.coremethods.data.datasource.remote.CoreMethodsRemoteDataSource
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.MPError
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.params.GenerateCardTokenParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetCardIssuersParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetInstallmentParams
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetPaymentMethodsParams
import com.mercadopago.sdk.android.coremethods.domain.utils.MPResult
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

            val response = MPResult.Success(CardToken(token = "token_id"))
            coEvery { dataSource.generateCardToken(any()) } returns response

            val result = repository.generateCardToken(generateCardTokenParams)

            assertTrue(result is MPResult.Success)
            assertEquals("token_id", (result as MPResult.Success).data.token)
        }

    @Test
    fun `test generateCardToken returns Error`() =
        runBlocking {
            val generateCardTokenParams = GenerateCardTokenParams(cardId = "card_123")

            val errorResponse = MPError.Request(code = "400", message = "Bad Request")
            val response: MPResult<CardToken, MPError> = MPResult.Error(errorResponse)
            coEvery { dataSource.generateCardToken(any()) } returns response

            val result = repository.generateCardToken(generateCardTokenParams)

            assertTrue(result is MPResult.Error)
            assertEquals("Bad Request", ((result as MPResult.Error).error as MPError.Request).message)
        }

    @Test
    fun `test getInstallment returns Success`() =
        runBlocking {
            val installmentParams = GetInstallmentParams(bin = 12345678)

            val response = MPResult.Success(listOf(Installment(paymentMethodId = "credit")))
            coEvery { dataSource.getInstallments(any()) } returns response

            val result = repository.getInstallment(installmentParams)

            assertTrue(result is MPResult.Success)
            assertEquals("credit", (result as MPResult.Success).data[0].paymentMethodId)
        }

    @Test
    fun `test getInstallment returns Error`() =
        runBlocking {
            val installmentParams = GetInstallmentParams(bin = 12345678)

            val errorResponse = MPError.Request(code = "400", message = "Bad Request")
            val response: MPResult<List<Installment>, MPError> = MPResult.Error(errorResponse)
            coEvery { dataSource.getInstallments(any()) } returns response

            val result = repository.getInstallment(installmentParams)

            assertTrue(result is MPResult.Error)
            assertEquals("Bad Request", ((result as MPResult.Error).error as MPError.Request).message)
        }

    @Test
    fun `test getIdentificationTypes returns Success`() =
        runBlocking {
            val response = MPResult.Success(
                listOf(IdentificationType(id = "0", name = "rg", type = "rg", minLength = 10, maxLength = 10)),
            )
            coEvery { dataSource.getIdentificationTypes() } returns response

            val result = repository.getIdentificationTypes()

            assertTrue(result is MPResult.Success)
            assertEquals("rg", (result as MPResult.Success).data.first().type)
        }

    @Test
    fun `test getIdentificationTypes returns Error`() =
        runBlocking {
            val errorResponse = MPError.Request(code = "400", message = "Bad Request")
            val response: MPResult<List<IdentificationType>, MPError> = MPResult.Error(errorResponse)
            coEvery { dataSource.getIdentificationTypes() } returns response

            val result = repository.getIdentificationTypes()

            assertTrue(result is MPResult.Error)
            assertEquals("Bad Request", ((result as MPResult.Error).error as MPError.Request).message)
        }

    @Test
    fun `test getCardIssuers returns Success`() =
        runBlocking {
            val params = GetCardIssuersParams()
            val response = MPResult.Success(
                listOf(CardIssuer(thumbnail = "www")),
            )
            coEvery { dataSource.getCardIssuers(any()) } returns response

            val result = repository.getCardIssuers(params)

            assertTrue(result is MPResult.Success)
            assertEquals("www", (result as MPResult.Success).data.first().thumbnail)
        }

    @Test
    fun `test getCardIssuers returns Error`() =
        runBlocking {
            val params = GetCardIssuersParams()
            val errorResponse = MPError.Request(code = "400", message = "Bad Request")
            val response: MPResult<List<CardIssuer>, MPError> = MPResult.Error(errorResponse)
            coEvery { dataSource.getCardIssuers(any()) } returns response

            val result = repository.getCardIssuers(params)

            assertTrue(result is MPResult.Error)
            assertEquals("Bad Request", ((result as MPResult.Error).error as MPError.Request).message)
        }

    @Test
    fun `test getPaymentMethods returns Success`() =
        runBlocking {
            val params = GetPaymentMethodsParams()
            val response = MPResult.Success(
                listOf(PaymentMethod(thumbnail = "www")),
            )
            coEvery { dataSource.getPaymentMethods(any()) } returns response

            val result = repository.getPaymentMethods(params)

            assertTrue(result is MPResult.Success)
            assertEquals("www", (result as MPResult.Success).data.first().thumbnail)
        }

    @Test
    fun `test getPaymentMethods returns Error`() =
        runBlocking {
            val params = GetPaymentMethodsParams()
            val errorResponse = MPError.Request(code = "400", message = "Bad Request")
            val response: MPResult<List<PaymentMethod>, MPError> = MPResult.Error(errorResponse)
            coEvery { dataSource.getPaymentMethods(any()) } returns response

            val result = repository.getPaymentMethods(params)

            assertTrue(result is MPResult.Error)
            assertEquals("Bad Request", ((result as MPResult.Error).error as MPError.Request).message)
        }
}
