package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.checkout.analytics.InstallmentsCancelReason
import com.mercadopago.sdk.android.checkout.analytics.InstallmentsInitializeEventData
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.verify
import org.junit.After
import org.junit.Before
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class InstallmentsAnalyticsTrackerTest {
    private val analytics = mockk<MPAnalytics>(relaxed = true)
    private val cardTransactionPaymentData = MPPaymentData.CardTransaction(
        orderId = "123",
        orderStatus = "approved",
        paymentMethodId = "visa",
        paymentTypeId = "credit_card",
    )

    private val installmentData = MPInstallmentData(
        quotas = listOf(
            Quota(
                installments = 3,
                installmentAmount = BigDecimal("34.00"),
                totalAmount = BigDecimal("102.00"),
            ),
        ),
        display = MPInstallmentData.InstallmentDisplay(
            displayType = SelectionDisplayType.RadioButton,
        ),
    )

    @Before
    fun setUp() {
        mockkObject(MPAnalytics.Companion)
        every { MPAnalytics.tryGetInstance() } returns analytics
    }

    @After
    fun tearDown() {
        unmockkObject(MPAnalytics.Companion)
    }

    private fun makeTracker(
        paymentData: MPPaymentData = cardTransactionPaymentData,
    ) = InstallmentsAnalyticsTracker(
        checkoutType = "card_form",
        paymentData = paymentData,
        installmentData = installmentData,
        orderId = "order",
    )

    @Test
    fun `trackInitialize does not throw`() {
        makeTracker().trackInitialize()
    }

    @Test
    fun `trackInitialize with Payment includes payment method fields`() {
        val paymentData = MPPaymentData.Payment(
            orderId = "123",
            orderStatus = "approved",
            paymentMethodId = "master",
            paymentTypeId = "credit_card",
        )

        makeTracker(paymentData).trackInitialize()

        val metric = slot<Metric>()
        verify { analytics.trackMetric(capture(metric)) }
        val data = assertIs<InstallmentsInitializeEventData>(metric.captured.data)
        assertEquals("master", data.paymentMethodId)
        assertEquals("credit_card", data.paymentType)
    }

    @Test
    fun `trackSelected does not throw`() {
        makeTracker().trackSelected(3)
    }

    @Test
    fun `trackSubmit does not throw with valid quota`() {
        makeTracker().trackSubmit(installmentData.quotas.first())
    }

    @Test
    fun `trackSubmit silently returns when quota has null fields`() {
        makeTracker().trackSubmit(Quota())
    }

    @Test
    fun `trackUserCanceled does not throw for BackPressed`() {
        makeTracker().trackUserCanceled(InstallmentsCancelReason.BackPressed)
    }

    @Test
    fun `trackUserCanceled does not throw for UserDismissed`() {
        makeTracker().trackUserCanceled(InstallmentsCancelReason.UserDismissed)
    }

    @Test
    fun `trackSubmit then trackUserCanceled only tracks first terminal event`() {
        val tracker = makeTracker()

        tracker.trackSubmit(installmentData.quotas.first())
        tracker.trackUserCanceled(InstallmentsCancelReason.UserDismissed)
    }

    @Test
    fun `trackUserCanceled then trackSelected does not throw`() {
        val tracker = makeTracker()

        tracker.trackUserCanceled(InstallmentsCancelReason.BackPressed)
        tracker.trackSelected(3)
    }
}
