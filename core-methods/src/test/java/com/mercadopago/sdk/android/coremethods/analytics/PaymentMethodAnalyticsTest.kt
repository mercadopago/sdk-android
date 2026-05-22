package com.mercadopago.sdk.android.coremethods.analytics

import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

internal class PaymentMethodAnalyticsTest {
    @Test
    fun `success metric contains all four payment methods properties`() {
        val metric = metricPaymentMethodCallSuccess(
            issuer = "288",
            cardBrand = "visa",
            paymentType = "credit_card",
            securityLength = 3,
        )

        assertEquals("/checkout_api_native/core_methods/payment_methods", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        val data = assertIs<PaymentMethodEventData>(metric.data)
        assertEquals("288", data.issuer)
        assertEquals("visa", data.cardBrand)
        assertEquals("credit_card", data.paymentType)
        assertEquals(3, data.securityLength)
    }

    @Test
    fun `success metric accepts null optional fields`() {
        val metric = metricPaymentMethodCallSuccess(
            issuer = "",
            cardBrand = "",
            paymentType = null,
            securityLength = null,
        )

        val data = assertIs<PaymentMethodEventData>(metric.data)
        assertNull(data.paymentType)
        assertNull(data.securityLength)
    }

    @Test
    fun `error metric carries card_brand and issuer alongside error_type`() {
        val metric = metricPaymentMethodCallError(
            error = "NETWORK_ERROR",
            issuer = "24",
            cardBrand = "master",
        )

        assertEquals("/checkout_api_native/core_methods/payment_methods/error", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        val data = assertIs<PaymentMethodErrorData>(metric.data)
        assertEquals("NETWORK_ERROR", data.errorType)
        assertEquals("24", data.issuer)
        assertEquals("master", data.cardBrand)
    }

    @Test
    fun `error metric defaults card_brand and issuer to empty when omitted`() {
        val metric = metricPaymentMethodCallError(error = "NETWORK_ERROR")
        val data = assertIs<PaymentMethodErrorData>(metric.data)
        assertEquals("", data.issuer)
        assertEquals("", data.cardBrand)
    }
}
