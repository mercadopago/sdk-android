package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.coremethods.domain.utils.Result
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
}
