package com.mercadopago.sdk.android.checkout.data.repository

import com.mercadopago.sdk.android.checkout.data.remote.datasource.OrderRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.response.OrderProcessResponse
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class OrderRepositoryImplTest {
    private val dataSource = mockk<OrderRemoteDataSource>()
    private val repository = OrderRepositoryImpl(dataSource)

    private val params = ProcessOrderParams(
        orderId = "ORD_456",
        amount = "200.00",
        paymentMethodId = "master",
        paymentMethodType = "credit_card",
        token = "TOKEN_XYZ",
        installments = 1,
    )

    @Test
    fun `given dataSource returns success then returns Result Success with mapped OrderProcessOutput`() = runTest {
        val response = OrderProcessResponse(
            id = "ORD_456",
            status = "approved",
            productId = null, processingMode = null, externalReference = null,
            description = null, totalAmount = null, totalPaidAmount = null,
            expirationTime = null, checkoutAvailableAt = null, siteId = null,
            userId = null, createdDate = null, lastUpdatedDate = null, type = null,
            statusDetail = null, captureMode = null, currency = null, config = null,
            integrationData = null, payer = null, shipment = null, transactions = null,
            items = null,
        )
        coEvery { dataSource.process(params) } returns Result.Success(response)

        val result = repository.process(params)

        val success = assertIs<Result.Success<OrderProcessOutput>>(result)
        assertEquals("ORD_456", success.data.id)
        assertEquals("approved", success.data.status)
    }

    @Test
    fun `given dataSource returns success with null fields then maps to empty strings`() = runTest {
        val response = mockk<OrderProcessResponse>(relaxed = true) {
            every { id } returns null
            every { status } returns null
        }
        coEvery { dataSource.process(params) } returns Result.Success(response)

        val result = repository.process(params)

        val success = assertIs<Result.Success<OrderProcessOutput>>(result)
        assertEquals("", success.data.id)
        assertEquals("", success.data.status)
    }

    @Test
    fun `given dataSource returns error then returns Result Error with same ResponseError`() = runTest {
        val error = ResponseError(code = "404", message = "Not Found", httpStatus = 404)
        coEvery { dataSource.process(params) } returns Result.Error(error)

        val result = repository.process(params)

        val resultError = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("404", resultError.error.code)
        assertEquals("Not Found", resultError.error.message)
        assertEquals(404, resultError.error.httpStatus)
    }

    @Test
    fun `given dataSource throws exception then withErrorHandling catches it and returns Result Error`() = runTest {
        coEvery { dataSource.process(params) } throws RuntimeException("Network failure")

        val result = repository.process(params)

        assertIs<Result.Error<ResponseError>>(result)
    }

    @Test
    fun `given process is called then delegates params to dataSource`() = runTest {
        coEvery { dataSource.process(params) } returns Result.Success(mockk(relaxed = true))

        repository.process(params)

        coVerify(exactly = 1) { dataSource.process(params) }
    }
}
