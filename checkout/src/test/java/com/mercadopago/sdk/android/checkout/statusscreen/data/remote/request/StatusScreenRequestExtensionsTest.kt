package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.request

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class StatusScreenRequestExtensionsTest {
    @Test
    fun `given exactly four ASCII digits when asLastFourDigitsOrNull then it returns the value unchanged`() {
        // Given / When / Then
        assertEquals("1234", "1234".asLastFourDigitsOrNull())
    }

    @Test
    fun `given null when asLastFourDigitsOrNull then it returns null`() {
        // Given / When / Then
        assertNull(null.asLastFourDigitsOrNull())
    }

    @Test
    fun `given blank string when asLastFourDigitsOrNull then it returns null`() {
        // Given / When / Then
        assertNull("   ".asLastFourDigitsOrNull())
    }

    @Test
    fun `given fewer than four digits when asLastFourDigitsOrNull then it returns null`() {
        // Given / When / Then
        assertNull("123".asLastFourDigitsOrNull())
    }

    @Test
    fun `given non-digit characters when asLastFourDigitsOrNull then it returns null`() {
        // Given / When / Then
        assertNull("12a4".asLastFourDigitsOrNull())
    }

    @Test
    fun `given an oversized token-like value when asLastFourDigitsOrNull then it returns null`() {
        // Given / When / Then
        assertNull("eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxMjM0In0.signature".asLastFourDigitsOrNull())
    }
}
