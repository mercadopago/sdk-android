package com.mercadopago.sdk.android.coremethods.data.datasource.mappers

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import kotlin.test.Test

internal class ResponseMapperTest {
    @Test
    fun `given error body with message and code when toResultError then maps both fields`() {
        // Given
        val responseBody: ResponseBody = """{"message":"Card declined","code":"CARD_ERROR"}"""
            .toResponseBody("application/json".toMediaTypeOrNull())

        // When
        val result = responseBody.toResultError()

        // Then
        assertEquals(ResultError.Request(message = "Card declined", code = "CARD_ERROR"), result)
    }

    @Test
    fun `given error body without message field when toResultError then falls back to unknown error`() {
        // Given
        val responseBody: ResponseBody = """{"code":"SERVER_ERROR"}"""
            .toResponseBody("application/json".toMediaTypeOrNull())

        // When
        val result = responseBody.toResultError()

        // Then
        assertEquals(ResultError.Request(message = UNKNOWN_ERROR, code = "SERVER_ERROR"), result)
    }

    @Test
    fun `given error body that is not valid json when toResultError then falls back to unknown error`() {
        // Given
        val responseBody: ResponseBody = "<html>Internal Server Error</html>"
            .toResponseBody("text/html".toMediaTypeOrNull())

        // When
        val result = responseBody.toResultError()

        // Then
        assertEquals(ResultError.Request(message = UNKNOWN_ERROR, code = UNKNOWN_ERROR), result)
    }

    @Test
    fun `given null error body when toResultError then falls back to unknown error`() {
        // Given
        val responseBody: ResponseBody? = null

        // When
        val result = responseBody.toResultError()

        // Then
        assertEquals(ResultError.Request(message = UNKNOWN_ERROR, code = UNKNOWN_ERROR), result)
    }
}
