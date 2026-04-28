package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.mapper.EMPTY_BODY_ERROR
import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.data.remote.service.CardFormService
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class CardFormRemoteDataSourceImplTest {
    private val service = mockk<CardFormService>()
    private val dataSource = CardFormRemoteDataSourceImpl(service)

    private val amount = "100.00"
    private val checkoutType = "card_payment"

    @Test
    fun `given service returns success with body then returns Result Success`() = runTest {
        val responseBody = mockk<CardFormInitResponse>()
        coEvery {
            service.initialization(productId = any(), amount = amount, checkoutType = checkoutType)
        } returns Response.success(responseBody)

        val result = dataSource.fetchInitialization(amount, checkoutType)

        assertIs<Result.Success<CardFormInitResponse>>(result)
        assertEquals(responseBody, result.data)
    }

    @Test
    fun `given service returns success with null body then returns empty body error`() = runTest {
        coEvery {
            service.initialization(productId = any(), amount = amount, checkoutType = checkoutType)
        } returns Response.success(null)

        val result = dataSource.fetchInitialization(amount, checkoutType)

        assertEquals(EMPTY_BODY_ERROR, result)
    }

    @Test
    fun `given service returns error response then returns Result Error with parsed message`() = runTest {
        val errorJson = """{"message":"Unauthorized","code":"401"}"""
        val errorBody = errorJson.toResponseBody("application/json".toMediaType())
        coEvery {
            service.initialization(productId = any(), amount = amount, checkoutType = checkoutType)
        } returns Response.error(401, errorBody)

        val result = dataSource.fetchInitialization(amount, checkoutType)

        assertIs<Result.Error<ResultError>>(result)
        val error = result.error
        assertIs<ResultError.Request>(error)
        assertEquals("Unauthorized", error.message)
        assertEquals("401", error.code)
    }

    @Test
    fun `given service returns error with empty body then returns Result Error with unknown error`() = runTest {
        val errorBody = "".toResponseBody("application/json".toMediaType())
        coEvery {
            service.initialization(productId = any(), amount = amount, checkoutType = checkoutType)
        } returns Response.error(500, errorBody)

        val result = dataSource.fetchInitialization(amount, checkoutType)

        assertIs<Result.Error<ResultError>>(result)
        val error = result.error
        assertIs<ResultError.Request>(error)
        assertEquals("UNKNOWN_ERROR", error.message)
        assertEquals("UNKNOWN_ERROR", error.code)
    }

    @Test
    fun `given fetchInitialization then delegates to service with correct amount and checkoutType`() = runTest {
        val responseBody = mockk<CardFormInitResponse>()
        coEvery {
            service.initialization(productId = any(), amount = amount, checkoutType = checkoutType)
        } returns Response.success(responseBody)

        dataSource.fetchInitialization(amount, checkoutType)

        coVerify(exactly = 1) {
            service.initialization(productId = any(), amount = amount, checkoutType = checkoutType)
        }
    }
}
