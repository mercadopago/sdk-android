package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentBrickInitializationResponse
import com.mercadopago.sdk.android.checkout.data.remote.service.PaymentBrickInitializationService
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickInitializationParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class PaymentBrickInitializationRemoteDataSourceImplTest {
    private val service = mockk<PaymentBrickInitializationService>()
    private val dataSource = PaymentBrickInitializationRemoteDataSourceImpl(service)

    private val params = FetchPaymentBrickInitializationParams(
        orderId = "ORDER_123",
        totalAmount = "500.00",
        customerId = "CUSTOMER_456",
        cardIds = "CARD_1,CARD_2",
    )

    @Test
    fun `given service returns successful response then fetch returns Result Success`() = runTest {
        val body = mockk<PaymentBrickInitializationResponse>(relaxed = true)
        coEvery { service.fetch(any(), any(), any(), any(), any()) } returns Response.success(body)

        val result = dataSource.fetch(params)

        assertIs<Result.Success<PaymentBrickInitializationResponse>>(result)
        assertEquals(body, result.data)
    }

    @Test
    fun `given service returns error response then fetch returns Result Error with http status`() = runTest {
        val errorBody = """{"message":"Not Found","code":"404"}""".toResponseBody()
        coEvery { service.fetch(any(), any(), any(), any(), any()) } returns Response.error(404, errorBody)

        val result = dataSource.fetch(params)

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("404", error.error.code)
        assertEquals(404, error.error.httpStatus)
    }

    @Test
    fun `given fetch is called then passes orderId and totalAmount to service`() = runTest {
        coEvery { service.fetch(any(), any(), any(), any()) } returns Response.success(mockk(relaxed = true))

        dataSource.fetch(params)

        coVerify {
            service.fetch(
                orderId = params.orderId,
                totalAmount = params.totalAmount,
                customerId = params.customerId,
                cardIds = params.cardIds,
            )
        }
    }

    @Test
    fun `given params without customer id then passes null customerId and cardIds to service`() = runTest {
        val paramsWithoutCustomer = FetchPaymentBrickInitializationParams(
            orderId = "ORDER_789",
            totalAmount = "100.00",
        )
        coEvery { service.fetch(any(), any(), any(), any()) } returns Response.success(mockk(relaxed = true))

        dataSource.fetch(paramsWithoutCustomer)

        coVerify {
            service.fetch(
                orderId = paramsWithoutCustomer.orderId,
                totalAmount = paramsWithoutCustomer.totalAmount,
                customerId = null,
                cardIds = null,
            )
        }
    }
}
