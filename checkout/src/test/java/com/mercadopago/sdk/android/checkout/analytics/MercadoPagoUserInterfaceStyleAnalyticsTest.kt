package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.foundation.theme.MercadoPagoUserInterfaceStyle
import kotlin.test.Test
import kotlin.test.assertEquals

internal class MercadoPagoUserInterfaceStyleAnalyticsTest {
    @Test
    fun `when System then toAnalyticsString returns system`() {
        assertEquals("system", MercadoPagoUserInterfaceStyle.System.toAnalyticsString())
    }

    @Test
    fun `when Light then toAnalyticsString returns light`() {
        assertEquals("light", MercadoPagoUserInterfaceStyle.Light.toAnalyticsString())
    }

    @Test
    fun `when Dark then toAnalyticsString returns dark`() {
        assertEquals("dark", MercadoPagoUserInterfaceStyle.Dark.toAnalyticsString())
    }
}
