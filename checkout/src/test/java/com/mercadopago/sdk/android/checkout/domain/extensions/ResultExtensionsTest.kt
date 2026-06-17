package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import kotlinx.coroutines.test.runTest
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class ResultExtensionsTest {
    @Test
    fun `given Success then fold calls onSuccess with data`() {
        val result = Result.Success("value")

        val folded = result.fold(onSuccess = { it }, onError = { "error" })

        assertEquals("value", folded)
    }

    @Test
    fun `given Error then fold calls onError with error`() {
        val result = Result.Error("failure")

        val folded = result.fold(onSuccess = { "success" }, onError = { it })

        assertEquals("failure", folded)
    }

    @Test
    fun `given Success then flatMap transforms data`() {
        val result = Result.Success(1)

        val mapped = result.flatMap { Result.Success(it * 2) }

        assertIs<Result.Success<Int>>(mapped)
        assertEquals(2, mapped.data)
    }

    @Test
    fun `given Error then flatMap propagates error without calling transform`() {
        val result: Result<Int, String> = Result.Error("error")

        val mapped = result.flatMap { Result.Success(it * 2) }

        assertIs<Result.Error<String>>(mapped)
        assertEquals("error", mapped.error)
    }

    @Test
    fun `given Success then map transforms data`() {
        val result = Result.Success(5)

        val mapped = result.map { it.toString() }

        assertIs<Result.Success<String>>(mapped)
        assertEquals("5", mapped.data)
    }

    @Test
    fun `given Error then map propagates error without calling transform`() {
        val result: Result<Int, String> = Result.Error("error")

        val mapped = result.map { it.toString() }

        assertIs<Result.Error<String>>(mapped)
        assertEquals("error", mapped.error)
    }

    @Test
    fun `given Success then onSuccess executes action and returns original result`() {
        val result = Result.Success("data")
        var captured = ""

        val returned = result.onSuccess { captured = it }

        assertEquals("data", captured)
        assertIs<Result.Success<String>>(returned)
    }

    @Test
    fun `given Error then onSuccess does not execute action`() {
        val result: Result<String, String> = Result.Error("error")
        var executed = false

        result.onSuccess { executed = true }

        assertEquals(false, executed)
    }

    @Test
    fun `given Error then onError executes action and returns original result`() {
        val result: Result<String, String> = Result.Error("error")
        var captured = ""

        val returned = result.onError { captured = it }

        assertEquals("error", captured)
        assertIs<Result.Error<String>>(returned)
    }

    @Test
    fun `given Success then onError does not execute action`() {
        val result = Result.Success("data")
        var executed = false

        result.onError { executed = true }

        assertEquals(false, executed)
    }

    @Test
    fun `given block returns success then withErrorHandling returns it`() = runTest {
        val result = withErrorHandling { Result.Success("ok") }

        assertIs<Result.Success<String>>(result)
        assertEquals("ok", result.data)
    }

    @Test
    fun `given SocketTimeoutException then withErrorHandling returns TIMEOUT response error`() = runTest {
        val result = withErrorHandling<String> { throw SocketTimeoutException("timeout") }

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("TIMEOUT", error.error.code)
        assertEquals("timeout", error.error.message)
    }

    @Test
    fun `given UnknownHostException then withErrorHandling returns NO_INTERNET response error`() = runTest {
        val result = withErrorHandling<String> { throw UnknownHostException("no host") }

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("NO_INTERNET", error.error.code)
    }

    @Test
    fun `given ConnectException then withErrorHandling returns CONNECTION response error`() = runTest {
        val result = withErrorHandling<String> { throw ConnectException("connect") }

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("CONNECTION", error.error.code)
    }

    @Test
    fun `given IOException then withErrorHandling returns NETWORK response error`() = runTest {
        val result = withErrorHandling<String> { throw IOException("io") }

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("NETWORK", error.error.code)
    }

    @Test
    fun `given generic Exception then withErrorHandling returns EXCEPTION response error`() = runTest {
        val result = withErrorHandling<String> { throw IllegalStateException("boom") }

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("EXCEPTION", error.error.code)
        assertEquals("boom", error.error.message)
    }

    @Test
    fun `given generic Exception without message then withErrorHandling uses default message`() = runTest {
        val result = withErrorHandling<String> { throw IllegalStateException() }

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("EXCEPTION", error.error.code)
        assertEquals("An error occurred", error.error.message)
    }

    @Test
    fun `given default shouldRetry and null httpStatus then withServiceRetry retries`() = runTest {
        var attempts = 0

        val result = withServiceRetry<String>(maxAttempts = 2) {
            attempts++
            Result.Error(ResponseError(code = "ERR", message = "no status", httpStatus = null))
        }

        assertIs<Result.Error<ResponseError>>(result)
        assertEquals(2, attempts)
    }

    @Test
    fun `given default shouldRetry and server error httpStatus then withServiceRetry retries`() = runTest {
        var attempts = 0

        val result = withServiceRetry<String>(maxAttempts = 2) {
            attempts++
            Result.Error(ResponseError(code = "500", message = "server", httpStatus = 503))
        }

        assertIs<Result.Error<ResponseError>>(result)
        assertEquals(2, attempts)
    }

    @Test
    fun `given default shouldRetry and client error httpStatus then withServiceRetry does not retry`() = runTest {
        var attempts = 0

        val result = withServiceRetry<String>(maxAttempts = 2) {
            attempts++
            Result.Error(ResponseError(code = "404", message = "not found", httpStatus = 404))
        }

        assertIs<Result.Error<ResponseError>>(result)
        assertEquals(1, attempts)
    }

    @Test
    fun `given zero maxAttempts then withServiceRetry returns RETRY_EXHAUSTED fallback`() = runTest {
        var attempts = 0

        val result = withServiceRetry<String>(maxAttempts = 0) {
            attempts++
            Result.Success("never")
        }

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("RETRY_EXHAUSTED", error.error.code)
        assertEquals(0, attempts)
    }

    @Test
    fun `given SocketTimeoutException then withResultErrorHandling returns TIMEOUT request error`() = runTest {
        val result = withResultErrorHandling<String> { throw SocketTimeoutException("timeout") }

        val error = assertIs<Result.Error<ResultError>>(result)
        val request = assertIs<ResultError.Request>(error.error)
        assertEquals("TIMEOUT", request.code)
        assertEquals("timeout", request.message)
    }

    @Test
    fun `given UnknownHostException then withResultErrorHandling returns NO_INTERNET request error`() = runTest {
        val result = withResultErrorHandling<String> { throw UnknownHostException("no host") }

        val error = assertIs<Result.Error<ResultError>>(result)
        val request = assertIs<ResultError.Request>(error.error)
        assertEquals("NO_INTERNET", request.code)
    }

    @Test
    fun `given ConnectException then withResultErrorHandling returns CONNECTION request error`() = runTest {
        val result = withResultErrorHandling<String> { throw ConnectException("connect") }

        val error = assertIs<Result.Error<ResultError>>(result)
        val request = assertIs<ResultError.Request>(error.error)
        assertEquals("CONNECTION", request.code)
    }

    @Test
    fun `given IOException then withResultErrorHandling returns NETWORK request error`() = runTest {
        val result = withResultErrorHandling<String> { throw IOException("io") }

        val error = assertIs<Result.Error<ResultError>>(result)
        val request = assertIs<ResultError.Request>(error.error)
        assertEquals("NETWORK", request.code)
    }

    @Test
    fun `given generic Exception then withResultErrorHandling returns EXCEPTION request error`() = runTest {
        val result = withResultErrorHandling<String> { throw IllegalStateException("boom") }

        val error = assertIs<Result.Error<ResultError>>(result)
        val request = assertIs<ResultError.Request>(error.error)
        assertEquals("EXCEPTION", request.code)
        assertEquals("boom", request.message)
    }

    @Test
    fun `given block returns success then withResultErrorHandling returns it`() = runTest {
        val result = withResultErrorHandling { Result.Success("ok") }

        assertIs<Result.Success<String>>(result)
        assertEquals("ok", result.data)
    }

    @Test
    fun `given success on first attempt then withServiceRetry does not retry`() = runTest {
        var attempts = 0

        val result = withServiceRetry {
            attempts++
            Result.Success("ok")
        }

        assertIs<Result.Success<String>>(result)
        assertEquals(1, attempts)
    }

    @Test
    fun `given retryable error then withServiceRetry retries up to maxAttempts and returns last error`() = runTest {
        var attempts = 0
        val responseError = ResponseError(code = "500", message = "server", httpStatus = 500)

        val result = withServiceRetry<String>(maxAttempts = 3) {
            attempts++
            Result.Error(responseError)
        }

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("500", error.error.code)
        assertEquals(3, attempts)
    }

    @Test
    fun `given non-retryable error then withServiceRetry returns immediately`() = runTest {
        var attempts = 0

        val result = withServiceRetry<String>(shouldRetry = { false }) {
            attempts++
            Result.Error(ResponseError(code = "400", message = "bad", httpStatus = 400))
        }

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("400", error.error.code)
        assertEquals(1, attempts)
    }

    @Test
    fun `given exception thrown inside block then withServiceRetry converts it via toResponseError`() = runTest {
        val result = withServiceRetry<String>(shouldRetry = { false }) {
            throw SocketTimeoutException("timeout")
        }

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("TIMEOUT", error.error.code)
    }
}
