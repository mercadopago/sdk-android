package com.mercadopago.sdk.android.checkout.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

internal class SelectionDisplayTypeExtTest {
    @Test
    fun `Chevron toTrackingValue returns arrow`() {
        assertEquals("arrow", SelectionDisplayType.Chevron.toTrackingValue())
    }

    @Test
    fun `RadioButton toTrackingValue returns radio_button`() {
        assertEquals("radio_button", SelectionDisplayType.RadioButton.toTrackingValue())
    }
}
