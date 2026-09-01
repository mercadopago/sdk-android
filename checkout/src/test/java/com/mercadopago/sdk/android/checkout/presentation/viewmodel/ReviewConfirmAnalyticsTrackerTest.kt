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
        amount: String = "300.0",
    ) = ReviewConfirmAnalyticsTracker(
        processOrderParams = ProcessOrderParams(
            orderId = "order-123",
            clientToken = "token-abc",
            paymentMethodId = paymentMethodId,
            paymentMethodType = paymentMethodType,
            token = "card-token-xyz",
            installments = installments,
            amount = amount,
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
    fun `trackImpression does not throw with non numeric amount`() {
        makeTracker(amount = "not-a-number").trackImpression()
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
