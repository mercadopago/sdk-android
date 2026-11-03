package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.checkout.analytics.CheckoutAnalyticsConstants.NOT_APPLY
import com.mercadopago.sdk.android.checkout.analytics.ReviewConfirmPaymentMethodEventData
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class ReviewConfirmAnalyticsTrackerTest {
    private val analytics = mockk<MPAnalytics>(relaxed = true)

    @Before
    fun setup() {
        mockkObject(MPAnalytics.Companion)
        every { MPAnalytics.tryGetInstance() } returns analytics
    }

    @After
    fun tearDown() {
        unmockkObject(MPAnalytics.Companion)
    }

    private fun makeTracker(
        paymentMethodId: String = "master",
        paymentMethodType: String = "credit_card",
        issuerId: String? = "123",
        cardId: String? = "card-1",
        installments: Int = 3,
    ) = ReviewConfirmAnalyticsTracker(
        processOrderParams = ProcessOrderParams(
            orderId = "order-123",
            clientToken = "token-abc",
            paymentMethodId = paymentMethodId,
            paymentMethodType = paymentMethodType,
            token = "card-token-xyz",
            installments = installments,
            issuerId = issuerId,
            cardId = cardId,
        ),
    )

    @Test
    fun `trackImpression includes card id`() {
        makeTracker().trackImpression()

        assertEquals("card-1", capturedPaymentMethodData().cardId)
    }

    @Test
    fun `trackImpression uses not apply when card id is null`() {
        makeTracker(issuerId = null, cardId = null).trackImpression()

        assertEquals(NOT_APPLY, capturedPaymentMethodData().cardId)
    }

    @Test
    fun `trackImpression reports zero transaction amount before it is updated`() {
        makeTracker().trackImpression()

        assertEquals(0.0, capturedPaymentMethodData().transactionAmount)
    }

    @Test
    fun `trackImpression reports the transaction amount set via updateTransactionAmount`() {
        val tracker = makeTracker()
        tracker.updateTransactionAmount(500.0)

        tracker.trackImpression()

        assertEquals(500.0, capturedPaymentMethodData().transactionAmount)
    }

    @Test
    fun `trackContinue does not throw`() {
        makeTracker().trackContinue()
    }

    @Test
    fun `trackBack does not throw`() {
        makeTracker().trackBack()
    }

    @Test
    fun `trackPaymentMethodChanged includes card id`() {
        makeTracker().trackPaymentMethodChanged()

        assertEquals("card-1", capturedPaymentMethodData().cardId)
    }

    @Test
    fun `trackPayerFieldChanged does not throw`() {
        makeTracker().trackPayerFieldChanged()
    }

    private fun capturedPaymentMethodData(): ReviewConfirmPaymentMethodEventData {
        val metric = slot<Metric>()
        verify { analytics.trackMetric(capture(metric)) }
        return assertIs(metric.captured.data)
    }
}
