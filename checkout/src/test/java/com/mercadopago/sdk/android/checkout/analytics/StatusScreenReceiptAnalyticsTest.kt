package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.CHECKOUT_STATUS_SCREEN_PATH
import com.mercadopago.sdk.android.initializer.analytics.SDK_NATIVE_PATH
import kotlin.test.Test
import kotlin.test.assertEquals

internal class StatusScreenReceiptAnalyticsTest {
    @Test
    fun `given receipt requested then metric is an EVENT on the receipt path with outcome and status type`() {
        // Given
        val expectedPath = "$SDK_NATIVE_PATH$CHECKOUT_STATUS_SCREEN_PATH/receipt"

        // When
        val metric = metricStatusScreenReceipt(outcome = StatusScreenReceiptOutcome.Requested, statusType = "approved")

        // Then
        assertEquals(expectedPath, metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        val data = metric.data as StatusScreenLoadEventData
        assertEquals("requested", data.outcome)
        assertEquals("approved", data.statusType)
    }

    @Test
    fun `given receipt outcome then tracking value matches the catalog outcome`() {
        // Given
        val outcomes = StatusScreenReceiptOutcome.entries

        // When
        val values = outcomes.map { it.trackingValue }

        // Then
        assertEquals(listOf("requested", "success", "failure", "dismissed", "cancelled"), values)
    }
}
