package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.request.OrderProcessRequest
import com.mercadopago.sdk.android.checkout.data.remote.response.OrderProcessResponse
import com.mercadopago.sdk.android.checkout.data.remote.service.OrderService
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class OrderRemoteDataSourceImplTest {
    private val service = mockk<OrderService>()
    private val dataSource = OrderRemoteDataSourceImpl(service)

    private val params = ProcessOrderParams(
        orderId = "ORD_789",
        clientToken = "test-order",
        amount = "300.00",
        paymentMethodId = "visa",
        paymentMethodType = "credit_card",
        token = "TOKEN_DEF",
        installments = 6,
    )

    @Test
    fun `given service returns successful response then process returns Result Success`() = runTest {
        val body = mockk<OrderProcessResponse>(relaxed = true)
        coEvery { service.process(any(), any(), any()) } returns Response.success(body)

        val result = dataSource.process(params)

        assertIs<Result.Success<OrderProcessResponse>>(result)
        assertEquals(body, result.data)
    }

    @Test
    fun `given service returns error response then process returns Result Error`() = runTest {
        val errorBody = """{"message":"Not Found","code":"404"}""".toResponseBody()
        coEvery { service.process(any(), any(), any()) } returns Response.error(404, errorBody)

        val result = dataSource.process(params)

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("404", error.error.code)
        assertEquals(404, error.error.httpStatus)
    }

    @Test
    fun `given process is called then passes orderId to service`() = runTest {
        coEvery { service.process(any(), any(), any()) } returns Response.success(mockk(relaxed = true))

        dataSource.process(params)

        coVerify { service.process(orderId = params.orderId, any(), body = any()) }
    }

    @Test
    fun `given process is called then passes correct body fields to service`() = runTest {
        val bodySlot = slot<OrderProcessRequest>()
        coEvery { service.process(any(), any(), capture(bodySlot)) } returns
            Response.success(mockk(relaxed = true))

        dataSource.process(params)

        assertEquals(params.amount, bodySlot.captured.amount)
        assertEquals(params.paymentMethodId, bodySlot.captured.paymentMethodId)
        assertEquals(params.paymentMethodType, bodySlot.captured.paymentMethodType)
        assertEquals(params.token, bodySlot.captured.token)
        assertEquals(params.installments, bodySlot.captured.installments)
    }
}
