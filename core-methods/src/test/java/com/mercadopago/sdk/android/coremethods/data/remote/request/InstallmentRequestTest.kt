package com.mercadopago.sdk.android.coremethods.data.remote.request

import org.junit.Assert.assertEquals
import kotlin.test.Test

internal class InstallmentRequestTest {
    @Test
    fun testDefaultValues() {
        val request = InstallmentsRequest()

        assertEquals(null, request.productId)
        assertEquals(null, request.bin)
        assertEquals(null, request.processingMode)
        assertEquals(null, request.amount)
    }

    @Test
    fun testAllProperties() {
        val amount = 10000.0.toBigDecimal()
        val request = InstallmentsRequest(
            productId = "1234",
            bin = 5678,
            processingMode = "some_mode",
            amount = amount,
        )

        assertEquals("1234", request.productId)
        assertEquals(5678, request.bin)
        assertEquals("some_mode", request.processingMode)
        assertEquals(amount, request.amount)
    }

    @Test
    fun testNullableProperties() {
        val request = InstallmentsRequest(
            productId = null,
            bin = null,
            processingMode = null,
            amount = null,
        )

        assertEquals(null, request.productId)
        assertEquals(null, request.bin)
        assertEquals(null, request.processingMode)
        assertEquals(null, request.amount)
    }

    @Test
    fun testCopyFunctionality() {
        val originalRequest = InstallmentsRequest(
            productId = "5678",
            bin = 1234,
            processingMode = "another_mode",
            amount = 20000.0.toBigDecimal(),
        )

        val copiedRequest = originalRequest.copy(amount = 30000.0.toBigDecimal())

        assertEquals(20000.0.toBigDecimal(), originalRequest.amount)
        assertEquals("5678", originalRequest.productId)

        assertEquals(30000.0.toBigDecimal(), copiedRequest.amount)
        assertEquals("5678", copiedRequest.productId)
    }
}
