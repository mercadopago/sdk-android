package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

internal class SecurityCodeAnalyticsTest {
    @Test
    fun `metricSecurityCodeView returns VIEW type with cvv path`() {
        val metric = metricSecurityCodeView(
            paymentMethodId = "visa",
            paymentTypeId = "credit_card",
            issuerId = "1234",
            cardId = "card-abc",
        )

        assertEquals("/checkout_api_native/checkout/payment_brick/cvv", metric.path)
        assertEquals(TrackType.VIEW, metric.type)
    }

    @Test
    fun `metricSecurityCodeView data contains all fields`() {
        val metric = metricSecurityCodeView(
            paymentMethodId = "visa",
            paymentTypeId = "credit_card",
            issuerId = "1234",
            cardId = "card-abc",
        )

        val data = assertIs<SecurityCodeViewEventData>(metric.data)
        assertEquals("visa", data.paymentMethodId)
        assertEquals("credit_card", data.paymentTypeId)
        assertEquals("1234", data.issuerId)
        assertEquals("card-abc", data.cardId)
    }

    @Test
    fun `metricSecurityCodeView propagates empty issuerId without coercion`() {
        val metric = metricSecurityCodeView(
            paymentMethodId = "master",
            paymentTypeId = "debit_card",
            issuerId = "",
            cardId = "card-xyz",
        )

        val data = assertIs<SecurityCodeViewEventData>(metric.data)
        assertEquals("", data.issuerId)
    }

    @Test
    fun `metricSecurityCodeContinue returns EVENT type with cvv_continue path and no data`() {
        val metric = metricSecurityCodeContinue()

        assertEquals("/checkout_api_native/checkout/payment_brick/cvv_continue", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        assertNull(metric.data)
    }

    @Test
    fun `metricSecurityCodeBack returns EVENT type with cvv_back path and no data`() {
        val metric = metricSecurityCodeBack()

        assertEquals("/checkout_api_native/checkout/payment_brick/cvv_back", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        assertNull(metric.data)
    }
}
