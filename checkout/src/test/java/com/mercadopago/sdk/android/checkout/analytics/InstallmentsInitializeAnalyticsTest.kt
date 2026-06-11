package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class InstallmentsInitializeAnalyticsTest {
    private fun eventData(
        checkoutType: String = "card_form",
        paymentMethodId: String = "visa",
        paymentType: String = "credit_card",
        selectionType: String = "radio_button",
        quotasCount: Int = 6,
        transactionAmount: Double = 500.0,
    ) = InstallmentsInitializeEventData(
        checkoutType = checkoutType,
        paymentMethodId = paymentMethodId,
        paymentType = paymentType,
        selectionType = selectionType,
        quotasCount = quotasCount,
        transactionAmount = transactionAmount,
        orderId = "",
    )

    @Test
    fun `when metricInstallmentsInitialize called then returns correct path and type`() {
        val metric = metricInstallmentsInitialize(eventData())

        assertEquals("/checkout_api_native/checkout/installments/initialize", metric.path)
        assertEquals(TrackType.VIEW, metric.type)
    }

    @Test
    fun `when metricInstallmentsInitialize called then event data contains all fields`() {
        val metric = metricInstallmentsInitialize(eventData())

        val data = assertIs<InstallmentsInitializeEventData>(metric.data)
        assertEquals("card_form", data.checkoutType)
        assertEquals("visa", data.paymentMethodId)
        assertEquals("credit_card", data.paymentType)
        assertEquals("radio_button", data.selectionType)
        assertEquals(6, data.quotasCount)
        assertEquals(500.0, data.transactionAmount)
    }

    @Test
    fun `when metricInstallmentsInitialize called with zero transactionAmount then data reflects zero`() {
        val metric = metricInstallmentsInitialize(
            eventData(
                paymentMethodId = "master",
                paymentType = "debit_card",
                selectionType = "chevron",
                quotasCount = 3,
                transactionAmount = 0.0,
            ),
        )

        val data = assertIs<InstallmentsInitializeEventData>(metric.data)
        assertEquals(0.0, data.transactionAmount)
        assertEquals("chevron", data.selectionType)
        assertEquals(3, data.quotasCount)
    }

    @Test
    fun `InstallmentsDisplayType RadioButton maps to radio_button`() {
        assertEquals("radio_button", InstallmentsDisplayType.RadioButton.toAnalyticsString())
    }

    @Test
    fun `InstallmentsDisplayType Chevron maps to chevron`() {
        assertEquals("chevron", InstallmentsDisplayType.Chevron.toAnalyticsString())
    }
}
