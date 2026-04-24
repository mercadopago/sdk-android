package com.mercadopago.sdk.android.checkout.domain.extensions

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ErrorMessagesExtensionsTest {
    @Test
    fun `given message contains not found then isPaymentMethodNotFound returns true`() {
        assertTrue("Payment method not found".isPaymentMethodNotFound())
    }

    @Test
    fun `given message contains NOT FOUND uppercase then isPaymentMethodNotFound returns true`() {
        assertTrue("Error: NOT FOUND".isPaymentMethodNotFound())
    }

    @Test
    fun `given message contains unable resource text then isPaymentMethodNotFound returns true`() {
        assertTrue(UNABLE_RESOURCE.isPaymentMethodNotFound())
    }

    @Test
    fun `given message does not match any pattern then isPaymentMethodNotFound returns false`() {
        assertFalse("Service unavailable".isPaymentMethodNotFound())
    }

    @Test
    fun `given empty message then isPaymentMethodNotFound returns false`() {
        assertFalse("".isPaymentMethodNotFound())
    }
}
