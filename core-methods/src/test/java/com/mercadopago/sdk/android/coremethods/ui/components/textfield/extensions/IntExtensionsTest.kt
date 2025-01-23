package com.mercadopago.sdk.android.coremethods.ui.components.textfield.extensions

import com.mercadopago.sdk.android.coremethods.extensions.between
import com.mercadopago.sdk.android.coremethods.extensions.takeLast
import org.junit.Assert.assertEquals
import org.junit.Test

class IntExtensionsTest {

    @Test
    fun `when pass a great number should return max range value`() {
        val number = 100
        val expected = 10

        assertEquals(number.between(0, 10), expected)
    }

    @Test
    fun `when pass a minor number should return min range value`() {
        val number = 4
        val expected = 5

        assertEquals(number.between(expected, 100), expected)
    }

    @Test
    fun `when pass a number should return last digits by a range`() {
        val number = 12345
        val expected = 345
        val result = number.takeLast(3)

        assertEquals(result, expected)
    }
}
