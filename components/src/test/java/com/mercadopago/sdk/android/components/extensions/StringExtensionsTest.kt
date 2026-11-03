package com.mercadopago.sdk.android.components.extensions

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class StringExtensionsTest {
    @Test
    fun `given plain integer greater than zero when isGreaterThan then returns true`() {
        assertTrue("1000".isGreaterThan())
    }

    @Test
    fun `given formatted amount with separators when isGreaterThan then returns true`() {
        assertTrue("1.000,50".isGreaterThan())
    }

    @Test
    fun `given amount with currency symbol when isGreaterThan then returns true`() {
        assertTrue("R$ 2.500,00".isGreaterThan())
    }

    @Test
    fun `given zero when isGreaterThan then returns false`() {
        assertFalse("0".isGreaterThan())
    }

    @Test
    fun `given zero with decimals when isGreaterThan then returns false`() {
        assertFalse("0,00".isGreaterThan())
    }

    @Test
    fun `given empty string when isGreaterThan then returns false`() {
        assertFalse("".isGreaterThan())
    }

    @Test
    fun `given non numeric string when isGreaterThan then returns false`() {
        assertFalse("abc".isGreaterThan())
    }

    @Test
    fun `given string with only separators when isGreaterThan then returns false`() {
        assertFalse(".,".isGreaterThan())
    }

    @Test
    fun `given negative sign in string when isGreaterThan then ignores sign and uses digits only`() {
        assertTrue("-100".isGreaterThan())
    }

    @Test
    fun `given null image data then asBase64PngPayloadOrNull returns null`() {
        val imageDataUri: String? = null

        assertNull(imageDataUri.asBase64PngPayloadOrNull())
    }

    @Test
    fun `given blank image data then asBase64PngPayloadOrNull returns null`() {
        val imageDataUri = ""

        assertNull(imageDataUri.asBase64PngPayloadOrNull())
    }

    @Test
    fun `given image data without the base64 png prefix then asBase64PngPayloadOrNull returns null`() {
        val imageDataUri = "data:image/jpeg;base64,iVBORw0KGgo="

        assertNull(imageDataUri.asBase64PngPayloadOrNull())
    }

    @Test
    fun `given image data exceeding the max encoded length then asBase64PngPayloadOrNull returns null`() {
        val oversizedPayload = "a".repeat(200_001)
        val imageDataUri = "data:image/png;base64,$oversizedPayload"

        assertNull(imageDataUri.asBase64PngPayloadOrNull())
    }

    @Test
    fun `given valid base64 png data then asBase64PngPayloadOrNull returns the payload without the prefix`() {
        val imageDataUri = "data:image/png;base64,iVBORw0KGgo="

        val payload = imageDataUri.asBase64PngPayloadOrNull()

        assertEquals("iVBORw0KGgo=", payload)
    }
}
