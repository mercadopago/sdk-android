package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

internal class CardFormInputAnalyticsTest {
    @Test
    fun `when metricCardFormInputValidation called then returns correct path`() {
        val metric = metricCardFormInputValidation(field = "card_number", isInputValid = true)

        assertEquals("/checkout_api_native/checkout/card_form/input_validation", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
    }

    @Test
    fun `when metricCardFormInputValidation called with valid input then data reflects it`() {
        val metric = metricCardFormInputValidation(field = "cvv", isInputValid = true)
        val data = assertIs<CardFormInputValidationEventData>(metric.data)

        assertEquals("cvv", data.field)
        assertTrue(data.isInputValid)
    }

    @Test
    fun `when metricCardFormInputValidation called with invalid input then data reflects it`() {
        val metric = metricCardFormInputValidation(field = "expiration_date", isInputValid = false)
        val data = assertIs<CardFormInputValidationEventData>(metric.data)

        assertEquals("expiration_date", data.field)
        assertEquals(false, data.isInputValid)
    }

    @Test
    fun `when metricCardFormDropdownSelection called then returns correct path and data`() {
        val metric = metricCardFormDropdownSelection(dropdownSelectionType = "document_type")

        assertEquals("/checkout_api_native/checkout/card_form/dropdown_selection", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        val data = assertIs<CardFormDropdownSelectionEventData>(metric.data)
        assertEquals("document_type", data.dropdownSelectionType)
    }
}
