package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.constants.MetricErrorData
import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

internal class CardFormSubmitAnalyticsTest {
    @Test
    fun `when metricCardFormSubmit called then returns correct path and type`() {
        val metric = metricCardFormSubmit(
            cardBrand = "visa",
            transactionAmount = 100.0,
            issuer = "288",
            paymentType = "credit",
        )

        assertEquals("/checkout_api_native/checkout/card_form/submit", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
    }

    @Test
    fun `when metricCardFormSubmit called with all fields then data contains them`() {
        val metric = metricCardFormSubmit(
            cardBrand = "master",
            transactionAmount = 250.50,
            issuer = "310",
            paymentType = "debit",
        )

        val data = assertIs<CardFormSubmitEventData>(metric.data)
        assertEquals("master", data.cardBrand)
        assertEquals(250.50, data.transactionAmount)
        assertEquals("310", data.issuer)
        assertEquals("debit", data.paymentType)
    }

    @Test
    fun `when metricCardFormSubmit called with zero transactionAmount and null paymentType then data reflects them`() {
        val metric = metricCardFormSubmit(
            cardBrand = "amex",
            transactionAmount = 0.0,
            issuer = "",
            paymentType = null,
        )

        val data = assertIs<CardFormSubmitEventData>(metric.data)
        assertEquals("amex", data.cardBrand)
        assertEquals(0.0, data.transactionAmount)
        assertEquals("", data.issuer)
        assertNull(data.paymentType)
    }

    @Test
    fun `when metricCardFormSubmitError called then returns error path with MetricErrorData`() {
        val metric = metricCardFormSubmitError(errorType = "network_error")

        assertEquals("/checkout_api_native/checkout/card_form/submit_error", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        val data = assertIs<MetricErrorData>(metric.data)
        assertEquals("network_error", data.errorType)
    }

    @Test
    fun `when metricCardFormUserCanceledError called then returns correct path`() {
        val metric = metricCardFormUserCanceledError()

        assertEquals("/checkout_api_native/checkout/card_form/user_canceled_error", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        val data = assertIs<MetricErrorData>(metric.data)
        assertEquals("", data.errorType)
    }

    @Test
    fun `when metricCardFormUserCanceledError called with error then data contains it`() {
        val metric = metricCardFormUserCanceledError(errorType = "network_error")

        val data = assertIs<MetricErrorData>(metric.data)
        assertEquals("network_error", data.errorType)
    }
}
