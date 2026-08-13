package com.mercadopago.sdk.android.checkout.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ScreenTest {
    @Test
    fun `given Screen enum then contains all expected values`() {
        val values = Screen.entries

        assertTrue(values.contains(Screen.INSTALLMENTS))
        assertTrue(values.contains(Screen.CARD_FORM))
        assertTrue(values.contains(Screen.PAYMENT_METHOD_SELECTOR))
        assertTrue(values.contains(Screen.OFFLINE_METHOD_SELECTOR))
        assertTrue(values.contains(Screen.SECURITY_CODE))
        assertTrue(values.contains(Screen.REVIEW_AND_CONFIRM))
    }

    @Test
    fun `given Screen enum then has exactly six entries`() {
        assertEquals(6, Screen.entries.size)
    }

    @Test
    fun `given Screen valueOf then resolves each value by name`() {
        assertEquals(Screen.INSTALLMENTS, Screen.valueOf("INSTALLMENTS"))
        assertEquals(Screen.CARD_FORM, Screen.valueOf("CARD_FORM"))
        assertEquals(Screen.PAYMENT_METHOD_SELECTOR, Screen.valueOf("PAYMENT_METHOD_SELECTOR"))
        assertEquals(Screen.OFFLINE_METHOD_SELECTOR, Screen.valueOf("OFFLINE_METHOD_SELECTOR"))
        assertEquals(Screen.SECURITY_CODE, Screen.valueOf("SECURITY_CODE"))
        assertEquals(Screen.REVIEW_AND_CONFIRM, Screen.valueOf("REVIEW_AND_CONFIRM"))
    }
}
