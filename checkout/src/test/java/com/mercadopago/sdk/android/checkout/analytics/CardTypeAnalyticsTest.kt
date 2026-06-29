package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.checkout.core.model.MPCardType
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CardTypeAnalyticsTest {
    @Test
    fun `when CardType CREDIT then toAnalyticsString returns credit`() {
        assertEquals("credit", MPCardType.CREDIT.toAnalyticsString())
    }

    @Test
    fun `when CardType DEBIT then toAnalyticsString returns debit`() {
        assertEquals("debit", MPCardType.DEBIT.toAnalyticsString())
    }

    @Test
    fun `when CardType PREPAID then toAnalyticsString returns prepaid`() {
        assertEquals("prepaid", MPCardType.PREPAID.toAnalyticsString())
    }
}
