package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.data.remote.service.CardFormService
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
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

internal class CardFormRemoteDataSourceImplTest {
    private val service = mockk<CardFormService>()
    private val dataSource = CardFormRemoteDataSourceImpl(service)

    private val amount = "100.00"
    private val checkoutType = "card_form"

    @Test
    fun `given service returns successful response then fetchInitialization returns Success`() = runTest {
        val body = mockk<CardFormInitResponse>(relaxed = true)
        coEvery { service.initialization(any(), any(), any(), any()) } returns Response.success(body)

        val result = dataSource.fetchInitialization(amount, checkoutType)

        assertIs<Result.Success<CardFormInitResponse>>(result)
        assertEquals(body, result.data)
    }

    @Test
    fun `given service returns error response then fetchInitialization returns Error`() = runTest {
        val errorBody = """{"message":"Not Found","code":"404"}""".toResponseBody()
        coEvery { service.initialization(any(), any(), any(), any()) } returns Response.error(404, errorBody)

        val result = dataSource.fetchInitialization(amount, checkoutType)

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("404", error.error.code)
        assertEquals(404, error.error.httpStatus)
    }

    @Test
    fun `given invoke is called then passes amount and checkoutType to service`() = runTest {
        val body = mockk<CardFormInitResponse>(relaxed = true)
        coEvery { service.initialization(any(), any(), any(), any()) } returns Response.success(body)

        dataSource.fetchInitialization(amount = amount, checkoutType = checkoutType)

        coVerify { service.initialization(any(), amount = amount, checkoutType = checkoutType) }
    }
}
