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
    }

    @Test
    fun `given Screen enum then has exactly two entries`() {
        assertEquals(2, Screen.entries.size)
    }

    @Test
    fun `given Screen valueOf then resolves each value by name`() {
        assertEquals(Screen.INSTALLMENTS, Screen.valueOf("INSTALLMENTS"))
        assertEquals(Screen.CARD_FORM, Screen.valueOf("CARD_FORM"))
    }
}
