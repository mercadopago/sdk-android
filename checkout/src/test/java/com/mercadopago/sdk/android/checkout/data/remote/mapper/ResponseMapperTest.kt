package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.mockk
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class ResponseMapperTest {
    @Test
    fun `given successful response with body then returns Success with body`() {
        val body = mockk<CardFormInitResponse>(relaxed = true)
        val response = Response.success(body)

        val result = response.toInternalResponse()

        assertIs<Result.Success<CardFormInitResponse>>(result)
        assertEquals(body, result.data)
    }

    @Test
    fun `given successful response with null body then returns EMPTY_BODY_ERROR`() {
        val response = Response.success<CardFormInitResponse>(null)

        val result = response.toInternalResponse()

        assertIs<Result.Error<ResultError>>(result)
        val error = result.error
        assertIs<ResultError.Request>(error)
        assertEquals("200", error.code)
        assertEquals("empty body", error.message)
    }

    @Test
    fun `given error response with parseable body then returns Error with parsed ResultError`() {
        val errorJson = """{"message":"Unauthorized","code":"401"}"""
        val errorBody = errorJson.toResponseBody("application/json".toMediaType())
        val response = Response.error<CardFormInitResponse>(401, errorBody)

        val result = response.toInternalResponse()

        assertIs<Result.Error<ResultError>>(result)
        val error = result.error
        assertIs<ResultError.Request>(error)
        assertEquals("401", error.code)
        assertEquals("Unauthorized", error.message)
    }

    @Test
    fun `given error response with null body then returns Error with UNKNOWN_ERROR`() {
        val response = Response.error<CardFormInitResponse>(
            500,
            "".toResponseBody("application/json".toMediaType()),
        )

        val result = response.toInternalResponse()

        assertIs<Result.Error<ResultError>>(result)
        val error = result.error
        assertIs<ResultError.Request>(error)
        assertEquals(UNKNOWN_ERROR, error.code)
        assertEquals(UNKNOWN_ERROR, error.message)
    }

    @Test
    fun `given null ResponseBody then returns ResultError with UNKNOWN_ERROR`() {
        val result = null.toResultError()

        assertEquals(UNKNOWN_ERROR, result.code)
        assertEquals(UNKNOWN_ERROR, result.message)
    }

    @Test
    fun `given valid JSON ResponseBody then parses message and code`() {
        val json = """{"message":"Not Found","code":"404"}"""
        val responseBody = json.toResponseBody("application/json".toMediaType())

        val result = responseBody.toResultError()

        assertEquals("404", result.code)
        assertEquals("Not Found", result.message)
    }
}
