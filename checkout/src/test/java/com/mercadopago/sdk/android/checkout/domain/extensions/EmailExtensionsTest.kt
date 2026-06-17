package com.mercadopago.sdk.android.checkout.domain.extensions

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
internal class EmailExtensionsTest {
    @Test
    fun `given valid email then isValidEmailFormat is true`() {
        assertTrue("user@example.com".isValidEmailFormat())
    }

    @Test
    fun `given email with subdomain then isValidEmailFormat is true`() {
        assertTrue("user@mail.example.com".isValidEmailFormat())
    }

    @Test
    fun `given malformed email then isValidEmailFormat is false`() {
        assertFalse("user@".isValidEmailFormat())
    }

    @Test
    fun `given blank value then isValidEmailFormat is false`() {
        assertFalse("   ".isValidEmailFormat())
    }

    @Test
    fun `given empty value then isValidEmailFormat is false`() {
        assertFalse("".isValidEmailFormat())
    }
}
