package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.mockk
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

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

        assertIs<Result.Error<ResponseError>>(result)
        val error = result.error
        assertEquals("EMPTY_BODY", error.code)
        assertEquals("empty body", error.message)
    }

    @Test
    fun `given error response with parseable body then returns Error with parsed ResponseError`() {
        val errorJson = """{"message":"Unauthorized","code":"401"}"""
        val errorBody = errorJson.toResponseBody("application/json".toMediaType())
        val response = Response.error<CardFormInitResponse>(401, errorBody)

        val result = response.toInternalResponse()

        assertIs<Result.Error<ResponseError>>(result)
        val error = result.error
        assertEquals("401", error.code)
        assertEquals("Unauthorized", error.message)
        assertEquals(401, error.httpStatus)
        assertNull(error.errorCode)
        assertNull(error.userErrorMessage)
    }

    @Test
    fun `given error response with null body then returns Error with UNKNOWN_ERROR`() {
        val response = Response.error<CardFormInitResponse>(
            500,
            "".toResponseBody("application/json".toMediaType()),
        )

        val result = response.toInternalResponse()

        assertIs<Result.Error<ResponseError>>(result)
        val error = result.error
        assertEquals(UNKNOWN_ERROR, error.code)
        assertEquals(UNKNOWN_ERROR, error.message)
        assertEquals(500, error.httpStatus)
    }

    @Test
    fun `given null ResponseBody then toResultError returns ResponseError with UNKNOWN_ERROR`() {
        val result = null.toResultError(httpStatus = 503)

        assertEquals(UNKNOWN_ERROR, result.code)
        assertEquals(UNKNOWN_ERROR, result.message)
        assertEquals(503, result.httpStatus)
        assertNull(result.errorCode)
        assertNull(result.userErrorMessage)
    }

    @Test
    fun `given valid JSON ResponseBody then parses all fields into ResponseError`() {
        val json = """{"message":"Not Found","code":"404"}"""
        val responseBody = json.toResponseBody("application/json".toMediaType())

        val result = responseBody.toResultError(httpStatus = 404)

        assertEquals("404", result.code)
        assertEquals("Not Found", result.message)
        assertEquals(404, result.httpStatus)
        assertNull(result.errorCode)
        assertNull(result.userErrorMessage)
    }

    @Test
    fun `given BFF error with error_code and user_error_message then all fields are parsed`() {
        val json = """{"code":"bad_request","error_code":"PAYMENT_METHOD_UNAVAILABLE",""" +
            """"message":"The store does not accept this payment method.",""" +
            """"user_error_message":"Este cartão não é aceito."}"""
        val responseBody = json.toResponseBody("application/json".toMediaType())

        val result = responseBody.toResultError(httpStatus = 400)

        assertEquals("bad_request", result.code)
        assertEquals("Este cartão não é aceito.", result.userErrorMessage)
        assertEquals(400, result.httpStatus)
    }

    @Test
    fun `given BFF error with only code and message then error_code and user_error_message are null`() {
        val json = """{"code":"bad_request","message":"The store does not accept this payment method."}"""
        val responseBody = json.toResponseBody("application/json".toMediaType())

        val result = responseBody.toResultError(httpStatus = 400)

        assertEquals("bad_request", result.code)
        assertEquals("The store does not accept this payment method.", result.message)
        assertEquals(400, result.httpStatus)
        assertNull(result.errorCode)
        assertNull(result.userErrorMessage)
    }
}
