package com.mercadopago.sdk.android.coremethods

import junit.framework.TestCase.assertEquals
import org.junit.Test

class TestClassTest {

    private val testClass = TestClass()

    @Test
    fun testSum() {
        val result = testClass.sum(2, 3)
        assertEquals(5, result)
    }
}
