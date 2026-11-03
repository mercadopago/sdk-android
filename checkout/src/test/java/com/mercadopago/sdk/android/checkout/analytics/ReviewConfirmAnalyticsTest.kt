package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.domain.models.TrackType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

internal class ReviewConfirmAnalyticsTest {
    private fun eventData(
        type: String = "credit_card",
        paymentMethodId: String = "master",
        paymentTypeId: String = "credit_card",
        issuerId: String = "123",
        cardId: String = "card-1",
        transactionAmount: Double = 300.0,
        installments: Int = 3,
    ) = ReviewConfirmPaymentMethodEventData(
        type = type,
        paymentMethodId = paymentMethodId,
        paymentTypeId = paymentTypeId,
        issuerId = issuerId,
        cardId = cardId,
        transactionAmount = transactionAmount,
        installments = installments,
    )

    @Test
    fun `when metricReviewConfirmImpression called then returns correct path and type`() {
        val metric = metricReviewConfirmImpression(eventData())

        assertEquals("/checkout_api_native/checkout/review_confirm", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
    }

    @Test
    fun `when metricReviewConfirmImpression called then event data contains all fields`() {
        val metric = metricReviewConfirmImpression(eventData())

        val data = assertIs<ReviewConfirmPaymentMethodEventData>(metric.data)
        assertEquals("credit_card", data.type)
        assertEquals("master", data.paymentMethodId)
        assertEquals("credit_card", data.paymentTypeId)
        assertEquals("123", data.issuerId)
        assertEquals("card-1", data.cardId)
        assertEquals(300.0, data.transactionAmount)
        assertEquals(3, data.installments)
    }

    @Test
    fun `when metricReviewConfirmContinue called then returns correct path type and no data`() {
        val metric = metricReviewConfirmContinue()

        assertEquals("/checkout_api_native/checkout/review_confirm_continue", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        assertNull(metric.data)
    }

    @Test
    fun `when metricReviewConfirmBack called then returns correct path type and no data`() {
        val metric = metricReviewConfirmBack()

        assertEquals("/checkout_api_native/checkout/review_confirm_back", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        assertNull(metric.data)
    }

    @Test
    fun `when metricReviewConfirmPaymentMethodChanged called then returns correct path and type`() {
        val metric = metricReviewConfirmPaymentMethodChanged(eventData(type = "ticket"))

        assertEquals("/checkout_api_native/checkout/review_confirm_payment_method_changed", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        val data = assertIs<ReviewConfirmPaymentMethodEventData>(metric.data)
        assertEquals("ticket", data.type)
    }

    @Test
    fun `when metricReviewConfirmPayerFieldChanged called then returns correct path type and changed field`() {
        val metric = metricReviewConfirmPayerFieldChanged(changedField = CHANGED_FIELD_EMAIL)

        assertEquals("/checkout_api_native/checkout/review_confirm_payer_field_changed", metric.path)
        assertEquals(TrackType.EVENT, metric.type)
        val data = assertIs<ReviewConfirmPayerFieldChangedEventData>(metric.data)
        assertEquals("email", data.changedField)
    }
}
