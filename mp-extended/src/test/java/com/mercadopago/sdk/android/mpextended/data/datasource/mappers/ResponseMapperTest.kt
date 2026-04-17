package com.mercadopago.sdk.android.mpextended.data.datasource.mappers

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

internal class ResponseMapperTest {

    @Test
    fun `when response body is null then toResultError returns UNKNOWN_ERROR`() {
        val error = null.toResultError()

        assertEquals(UNKNOWN_ERROR, error.code)
        assertEquals(UNKNOWN_ERROR, error.message)
    }

    @Test
    fun `when response body has valid JSON then toResultError returns parsed error`() {
        val json = """{"message":"Bad Request","code":"400"}"""
        val body = json.toResponseBody("application/json".toMediaTypeOrNull())

        val error = body.toResultError()

        assertEquals("400", error.code)
        assertEquals("Bad Request", error.message)
    }

    @Test
    fun `when response is successful with body then toInternalResponse returns Success`() {
        val response = Response.success("data")

        val result = response.toInternalResponse()

        assertTrue(result is Result.Success)
        assertEquals("data", (result as Result.Success).data)
    }

    @Test
    fun `when response is successful with null body then toInternalResponse returns EMPTY_BODY_ERROR`() {
        val response = Response.success<String>(null)

        val result = response.toInternalResponse()

        assertTrue(result is Result.Error)
        assertEquals("EMPTY_BODY", ((result as Result.Error).error as ResultError.Request).code)
    }

    @Test
    fun `when response is error then toInternalResponse returns Error`() {
        val json = """{"message":"Server Error","code":"500"}"""
        val body = json.toResponseBody("application/json".toMediaTypeOrNull())
        val response = Response.error<String>(500, body)

        val result = response.toInternalResponse()

        assertTrue(result is Result.Error)
        assertEquals("Server Error", ((result as Result.Error).error as ResultError.Request).message)
    }

    @Test
    fun `when result is Success then mapSuccess transforms data`() {
        val result: Result<String, ResultError> = Result.Success("hello")

        val mapped = result.mapSuccess { this.length }

        assertTrue(mapped is Result.Success)
        assertEquals(5, (mapped as Result.Success).data)
    }

    @Test
    fun `when result is Error then mapSuccess passes through error`() {
        val error = ResultError.Request(code = "400", message = "Bad Request")
        val result: Result<String, ResultError> = Result.Error(error)

        val mapped = result.mapSuccess { this.length }

        assertTrue(mapped is Result.Error)
        assertEquals(error, (mapped as Result.Error).error)
    }
}
