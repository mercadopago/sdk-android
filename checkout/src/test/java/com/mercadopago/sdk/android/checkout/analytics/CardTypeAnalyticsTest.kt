package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.checkout.core.model.CardType
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CardTypeAnalyticsTest {
    @Test
    fun `when CardType CREDIT then toAnalyticsString returns credit`() {
        assertEquals("credit", CardType.CREDIT.toAnalyticsString())
    }

    @Test
    fun `when CardType DEBIT then toAnalyticsString returns debit`() {
        assertEquals("debit", CardType.DEBIT.toAnalyticsString())
    }

    @Test
    fun `when CardType PREPAID then toAnalyticsString returns prepaid`() {
        assertEquals("prepaid", CardType.PREPAID.toAnalyticsString())
    }
}
