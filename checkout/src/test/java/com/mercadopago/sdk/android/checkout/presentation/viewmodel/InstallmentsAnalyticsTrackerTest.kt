package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.analytics.InstallmentsCancelReason
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import java.math.BigDecimal
import kotlin.test.Test

internal class InstallmentsAnalyticsTrackerTest {
    private val paymentData = MPPaymentData.CardTransaction(
        transactionAmount = BigDecimal("100.00"),
        paymentMethodId = "visa",
        paymentTypeId = "credit_card",
        payer = null,
        installment = null,
        issuerId = "1",
        orderId = "123",
        orderStatus = "approved",
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
            displayType = InstallmentsDisplayType.RadioButton,
        ),
    )

    private fun makeTracker() = InstallmentsAnalyticsTracker(
        checkoutType = "card_form",
        paymentData = paymentData,
        installmentData = installmentData,
    )

    @Test
    fun `trackInitialize does not throw`() {
        makeTracker().trackInitialize()
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
