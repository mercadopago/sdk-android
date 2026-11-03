package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

internal class OffPaymentListAnalyticsTest {
    @Test
    fun `given Chevron when view metric then returns event path`() {
        // Given
        val optionsCount = 3
        val selectionType = SelectionDisplayType.Chevron

        // When
        val metric = metricOffPaymentListView(
            optionsCount = optionsCount,
            selectionType = selectionType,
        )

        // Then
        assertEquals("/checkout_api_native/checkout/payment_brick/off_payment_list", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
    }

    @Test
    fun `given Chevron when view metric then has options count and arrow`() {
        // Given
        val optionsCount = 2
        val selectionType = SelectionDisplayType.Chevron

        // When
        val metric = metricOffPaymentListView(
            optionsCount = optionsCount,
            selectionType = selectionType,
        )

        // Then
        val data = assertIs<OffPaymentListViewEventData>(metric.data)
        assertEquals(2, data.optionsCount)
        assertEquals("arrow", data.selectionType)
    }

    @Test
    fun `given RadioButton when view metric then has radio button`() {
        // Given
        val optionsCount = 1
        val selectionType = SelectionDisplayType.RadioButton

        // When
        val metric = metricOffPaymentListView(
            optionsCount = optionsCount,
            selectionType = selectionType,
        )

        // Then
        val data = assertIs<OffPaymentListViewEventData>(metric.data)
        assertEquals("radio_button", data.selectionType)
    }

    @Test
    fun `given Chevron when select metric then returns event path`() {
        // Given
        val paymentMethodId = "boleto"
        val selectionType = SelectionDisplayType.Chevron

        // When
        val metric = metricOffPaymentListSelect(
            paymentMethodId = paymentMethodId,
            selectionType = selectionType,
        )

        // Then
        assertEquals("/checkout_api_native/checkout/payment_brick/off_payment_list_select", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
    }

    @Test
    fun `given Chevron when select metric then has method id and arrow`() {
        // Given
        val paymentMethodId = "boleto"
        val selectionType = SelectionDisplayType.Chevron

        // When
        val metric = metricOffPaymentListSelect(
            paymentMethodId = paymentMethodId,
            selectionType = selectionType,
        )

        // Then
        val data = assertIs<OffPaymentListSelectEventData>(metric.data)
        assertEquals("boleto", data.paymentMethodId)
        assertEquals("arrow", data.selectionType)
    }

    @Test
    fun `given RadioButton when select metric then has radio button`() {
        // Given
        val paymentMethodId = "efecty"
        val selectionType = SelectionDisplayType.RadioButton

        // When
        val metric = metricOffPaymentListSelect(
            paymentMethodId = paymentMethodId,
            selectionType = selectionType,
        )

        // Then
        val data = assertIs<OffPaymentListSelectEventData>(metric.data)
        assertEquals("radio_button", data.selectionType)
    }

    @Test
    fun `when back metric then returns event path and null data`() {
        // When
        val metric = metricOffPaymentListBack()

        // Then
        assertEquals("/checkout_api_native/checkout/payment_brick/off_payment_list_back", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        assertNull(metric.data)
    }
}
