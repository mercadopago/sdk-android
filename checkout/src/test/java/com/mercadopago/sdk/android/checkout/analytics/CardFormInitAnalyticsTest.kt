package com.mercadopago.sdk.android.checkout.analytics

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
            excludedPaymentTypes = listOf("credit", "debit"),
            excludedPaymentMethods = listOf("visa", "master"),
            orderId = "123",
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
            excludedPaymentTypes = listOf("credit"),
            excludedPaymentMethods = listOf("visa"),
            orderId = "order-abc",
        )

        val data = assertIs<CardFormInitEventData>(metric.data)
        assertEquals("card_form", data.checkoutType)
        assertEquals("system", data.appearance)
        assertEquals(emptyList(), data.sellerCustomization)
        assertEquals(listOf("credit"), data.excludedPaymentTypes)
        assertEquals(listOf("visa"), data.excludedPaymentMethods)
        assertEquals("order-abc", data.orderId)
    }

    @Test
    fun `when metricCardFormInitialize called with empty orderId then orderId resolves to NOT_APPLY`() {
        val metric = metricCardFormInitialize(
            checkoutType = "card_form",
            appearance = "light",
            sellerCustomization = emptyList(),
            excludedPaymentTypes = emptyList(),
            excludedPaymentMethods = emptyList(),
            orderId = "",
        )

        val data = assertIs<CardFormInitEventData>(metric.data)
        assertEquals("NOT_APPLY", data.orderId)
    }

    @Test
    fun `when metricCardFormInitializeError called then returns error path with MetricErrorData`() {
        val metric = metricCardFormInitializeError(
            errorType = "network_error",
            observabilityEventId = "event-id",
        )

        assertEquals("/checkout_api_native/checkout/card_form/initialize_error", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        val data = assertIs<CheckoutErrorEventData>(metric.data)
        assertEquals("network_error", data.errorType)
        assertEquals("event-id", data.observabilityEventId)
    }
}
