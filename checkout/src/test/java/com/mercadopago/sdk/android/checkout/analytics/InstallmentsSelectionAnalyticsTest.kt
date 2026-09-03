package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class InstallmentsSelectionAnalyticsTest {
    @Test
    fun `when metricInstallmentsSelected called then returns correct path and data`() {
        val metric = metricInstallmentsSelected(installments = 3)

        assertEquals("/checkout_api_native/checkout/installments/selected", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        val data = assertIs<InstallmentsSelectedEventData>(metric.data)
        assertEquals(3, data.installments)
    }

    @Test
    fun `when metricInstallmentsSubmit called then returns correct path and data`() {
        val metric = metricInstallmentsSubmit(
            installments = 3,
            installmentAmount = 333.34,
            totalAmount = 1000.0,
        )

        assertEquals("/checkout_api_native/checkout/installments/submit", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        val data = assertIs<InstallmentsSubmitEventData>(metric.data)
        assertEquals(3, data.installments)
        assertEquals(333.34, data.installmentAmount)
        assertEquals(1000.0, data.totalAmount)
    }

    @Test
    fun `when metricInstallmentsUserCanceledError called with back_pressed then returns error data`() {
        val metric = metricInstallmentsUserCanceledError(
            errorType = "back_pressed",
            observabilityEventId = "event-id",
        )

        assertEquals("/checkout_api_native/checkout/installments/user_canceled_error", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        val data = assertIs<CheckoutErrorEventData>(metric.data)
        assertEquals("back_pressed", data.errorType)
    }

    @Test
    fun `when metricInstallmentsUserCanceledError called with user_dismissed then returns error data`() {
        val metric = metricInstallmentsUserCanceledError(
            errorType = "user_dismissed",
            observabilityEventId = "event-id",
        )

        val data = assertIs<CheckoutErrorEventData>(metric.data)
        assertEquals("user_dismissed", data.errorType)
    }

    @Test
    fun `InstallmentsCancelReason analytics values match contract`() {
        assertEquals("back_pressed", InstallmentsCancelReason.BackPressed.analyticsValue)
        assertEquals("user_dismissed", InstallmentsCancelReason.UserDismissed.analyticsValue)
    }
}
