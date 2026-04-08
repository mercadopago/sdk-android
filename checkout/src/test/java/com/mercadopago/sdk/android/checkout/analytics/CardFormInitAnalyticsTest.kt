package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.constants.MetricErrorData
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class CardFormInitAnalyticsTest {
    @Test
    fun `when metricCardFormInitialize called then returns correct path and type`() {
        val metric = metricCardFormInitialize(
            checkoutType = "card_form",
            appearance = "light",
            sellerCustomization = listOf("customized_token"),
            allowedPaymentTypes = listOf("credit", "debit"),
            allowedPaymentMethods = listOf("visa", "master"),
        )

        assertEquals("/checkout_api_native/checkout/card_form/initialize", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
    }

    @Test
    fun `when metricCardFormInitialize called then event data contains all fields`() {
        val metric = metricCardFormInitialize(
            checkoutType = "card_form",
            appearance = "system",
            sellerCustomization = emptyList(),
            allowedPaymentTypes = listOf("credit"),
            allowedPaymentMethods = listOf("visa"),
        )

        val data = assertIs<CardFormInitEventData>(metric.data)
        assertEquals("card_form", data.checkoutType)
        assertEquals("system", data.appearance)
        assertEquals(emptyList(), data.sellerCustomization)
        assertEquals(listOf("credit"), data.allowedPaymentTypes)
        assertEquals(listOf("visa"), data.allowedPaymentMethods)
    }

    @Test
    fun `when metricCardFormInitializeError called then returns error path with MetricErrorData`() {
        val metric = metricCardFormInitializeError(errorType = "network_error")

        assertEquals("/checkout_api_native/checkout/card_form/initialize/error", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        val data = assertIs<MetricErrorData>(metric.data)
        assertEquals("network_error", data.errorType)
    }
}
