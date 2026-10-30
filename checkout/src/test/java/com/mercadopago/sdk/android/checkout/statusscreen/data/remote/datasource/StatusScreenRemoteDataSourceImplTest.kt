package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.datasource

import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.request.StatusScreenRequest
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.service.StatusScreenService
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

internal class StatusScreenRemoteDataSourceImplTest {
    private val service = mockk<StatusScreenService>()
    private val dataSource = StatusScreenRemoteDataSourceImpl(service)

    private val request = StatusScreenRequest(orderId = "order-123", lastFourDigits = "1234", sellerInfo = null)

    @Test
    fun `given service returns successful response then fetch returns Result Success`() = runTest {
        // Given
        val body = mockk<StatusScreenResponse>(relaxed = true)
        coEvery { service.fetch(any(), any(), any()) } returns Response.success(body)

        // When
        val result = dataSource.fetch(clientToken = "token-abc", statusScreenRequest = request)

        // Then
        assertEquals(body, assertIs<Result.Success<StatusScreenResponse>>(result).data)
    }

    @Test
    fun `given service returns error response then fetch returns Result Error with http status`() = runTest {
        // Given
        val errorBody = """{"message":"Not Found","code":"404"}""".toResponseBody()
        coEvery { service.fetch(any(), any(), any()) } returns Response.error(404, errorBody)

        // When
        val result = dataSource.fetch(clientToken = "token-abc", statusScreenRequest = request)

        // Then
        val error = assertIs<Result.Error<ResponseError>>(result).error
        assertEquals("404", error.code)
        assertEquals(404, error.httpStatus)
    }

    @Test
    fun `given fetch is called then clientToken is sent with the Bearer prefix`() = runTest {
        // Given
        coEvery { service.fetch(any(), any(), any()) } returns Response.success(mockk(relaxed = true))

        // When
        dataSource.fetch(clientToken = "token-abc", statusScreenRequest = request)

        // Then
        coVerify { service.fetch(clientToken = "Bearer token-abc", body = request) }
    }

    @Test
    fun `given a checkout type when fetch is called then it is forwarded as the checkout_type query`() = runTest {
        // Given
        coEvery { service.fetch(any(), any(), any()) } returns Response.success(mockk(relaxed = true))

        // When
        dataSource.fetch(clientToken = "token-abc", statusScreenRequest = request, checkoutType = "payment")

        // Then
        coVerify { service.fetch(clientToken = "Bearer token-abc", body = request, checkoutType = "payment") }
    }
}
