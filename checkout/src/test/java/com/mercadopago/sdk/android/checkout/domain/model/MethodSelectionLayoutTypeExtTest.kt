package com.mercadopago.sdk.android.checkout.domain.model

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class MethodSelectionLayoutTypeExtTest {
    @Test
    fun `given CHEVRON then isArrowLayout returns true`() {
        assertTrue(MethodSelectionLayoutType.CHEVRON.isArrowLayout)
    }

    @Test
    fun `given RADIO_BUTTON then isArrowLayout returns false`() {
        assertFalse(MethodSelectionLayoutType.RADIO_BUTTON.isArrowLayout)
    }
}
