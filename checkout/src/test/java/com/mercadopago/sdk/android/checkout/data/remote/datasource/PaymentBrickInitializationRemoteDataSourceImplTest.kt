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

    private val params = FetchPaymentBrickInitializationParams(orderId = "ORDER_123", clientToken = "token")

    @Test
    fun `given service returns successful response then fetch returns Result Success`() = runTest {
        val body = mockk<PaymentBrickInitializationResponse>(relaxed = true)
        coEvery { service.fetch(any(), any(), any(), any()) } returns Response.success(body)

        val result = dataSource.fetch(params)

        assertIs<Result.Success<PaymentBrickInitializationResponse>>(result)
        assertEquals(body, result.data)
    }

    @Test
    fun `given service returns error response then fetch returns Result Error with http status`() = runTest {
        val errorBody = """{"message":"Not Found","code":"404"}""".toResponseBody()
        coEvery { service.fetch(any(), any(), any(), any()) } returns Response.error(404, errorBody)

        val result = dataSource.fetch(params)

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("404", error.error.code)
        assertEquals(404, error.error.httpStatus)
    }

    @Test
    fun `given fetch is called then passes orderId and clientToken with Bearer prefix to service`() = runTest {
        coEvery { service.fetch(any(), any(), any(), any()) } returns Response.success(mockk(relaxed = true))

        dataSource.fetch(params)

        coVerify {
            service.fetch(
                orderId = params.orderId,
                screens = params.screens,
                clientToken = "Bearer ${params.clientToken}",
            )
        }
    }
}
