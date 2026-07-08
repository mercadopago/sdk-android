package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.analytics.InstallmentsCancelReason
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertFalse

internal class InstallmentsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val quotas = listOf(
        Quota(
            installments = 1,
            installmentAmount = BigDecimal("100.00"),
            totalAmount = BigDecimal("100.00"),
        ),
        Quota(
            installments = 3,
            installmentAmount = BigDecimal("34.00"),
            totalAmount = BigDecimal("102.00"),
        ),
    )

    private val paymentData = MPPaymentData.CardTransaction(
        orderId = "123",
        orderStatus = "approved",
        paymentMethodId = "visa",
        paymentTypeId = "credit_card",
    )

    private fun makeData(
        displayType: InstallmentsDisplayType = InstallmentsDisplayType.RadioButton,
    ) = MPInstallmentData(
        quotas = quotas,
        display = MPInstallmentData.InstallmentDisplay(
            displayType = displayType,
            footer = MPInstallmentData.InstallmentFooterDisplay(
                brand = "visa",
                lastFourDigits = "1234",
            ),
        ),
    )

    private fun makeViewModel(
        installmentData: MPInstallmentData = makeData(),
        tracker: InstallmentsAnalyticsTracker = mockk(relaxed = true),
    ) = InstallmentsViewModel(
        installmentData = installmentData,
        paymentData = paymentData,
        checkoutType = "card_form",
        orderId = "order_123",
        analyticsTracker = tracker,
    )

    @Test
    fun `viewState reflects quotas from installmentData`() = runTest {
        val viewModel = makeViewModel()

        kotlin.test.assertEquals(2, viewModel.viewState.value.items.size)
    }

    @Test
    fun `RadioButton mode auto-selects first item`() = runTest {
        val viewModel = makeViewModel()

        kotlin.test.assertEquals(
            true,
            viewModel.viewState.value.items.first().isSelected,
        )
    }

    @Test
    fun `Chevron mode does not pre-select`() = runTest {
        val viewModel = makeViewModel(makeData(InstallmentsDisplayType.Chevron))

        assertFalse(viewModel.viewState.value.items.any { it.isSelected })
    }

    @Test
    fun `onInstallmentSelected updates selection in RadioButton mode`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onInstallmentSelected(installment = 3)

        val states = viewModel.viewState.value.items
        kotlin.test.assertEquals(true, states.first { it.number == 3 }.isSelected)
        kotlin.test.assertEquals(false, states.first { it.number == 1 }.isSelected)
    }

    @Test
    fun `onInstallmentSelected ignored in Chevron mode keeps no selection`() = runTest {
        val viewModel = makeViewModel(makeData(InstallmentsDisplayType.Chevron))

        viewModel.onInstallmentSelected(installment = 3)

        assertFalse(viewModel.viewState.value.items.any { it.isSelected })
    }

    @Test
    fun `init tracks initialize event`() = runTest {
        val tracker = mockk<InstallmentsAnalyticsTracker>(relaxed = true)

        makeViewModel(tracker = tracker)

        verify { tracker.trackInitialize() }
    }

    @Test
    fun `RadioButton onInstallmentSelected tracks selected`() = runTest {
        val tracker = mockk<InstallmentsAnalyticsTracker>(relaxed = true)
        val viewModel = makeViewModel(tracker = tracker)

        viewModel.onInstallmentSelected(installment = 3)

        verify { tracker.trackSelected(3) }
    }

    @Test
    fun `Chevron onInstallmentSelected tracks submit`() = runTest {
        val tracker = mockk<InstallmentsAnalyticsTracker>(relaxed = true)
        val viewModel = makeViewModel(
            installmentData = makeData(InstallmentsDisplayType.Chevron),
            tracker = tracker,
        )

        viewModel.onInstallmentSelected(installment = 3)

        verify { tracker.trackSubmit(quotas.first { it.installments == 3 }) }
    }

    @Test
    fun `onPayClicked tracks submit in RadioButton mode`() = runTest {
        val tracker = mockk<InstallmentsAnalyticsTracker>(relaxed = true)
        val viewModel = makeViewModel(tracker = tracker)
        viewModel.onInstallmentSelected(installment = 3)

        viewModel.onPayClicked()

        verify { tracker.trackSubmit(quotas.first { it.installments == 3 }) }
    }

    @Test
    fun `onBackPressed tracks user canceled with back_pressed`() = runTest {
        val tracker = mockk<InstallmentsAnalyticsTracker>(relaxed = true)
        val viewModel = makeViewModel(tracker = tracker)

        viewModel.onBackPressed()

        verify { tracker.trackUserCanceled(InstallmentsCancelReason.BackPressed) }
    }

    @Test
    fun `onPayClicked sets isButtonLoading true before delay`() = runTest {
        val viewModel = makeViewModel()
        viewModel.onInstallmentSelected(installment = 1)

        viewModel.onPayClicked()

        kotlin.test.assertTrue(viewModel.viewState.value.footerState.buttonState?.isLoading == true)
    }

    @Test
    fun `onPayClicked emits OnSuccess only after delay`() = runTest {
        val viewModel = makeViewModel()
        viewModel.onInstallmentSelected(installment = 1)

        viewModel.onPayClicked()

        kotlin.test.assertNull(viewModel.viewEvent.value)
        testScheduler.advanceTimeBy(400)
        kotlin.test.assertTrue(viewModel.viewEvent.value is InstallmentViewEvent.OnSuccess)
    }

    @Test
    fun `isButtonLoading is false before onPayClicked`() = runTest {
        val viewModel = makeViewModel()

        assertFalse(viewModel.viewState.value.footerState.buttonState?.isLoading == true)
    }

    @Test
    fun `onViewEventConsumed resets isButtonLoading to false`() = runTest {
        val viewModel = makeViewModel()
        viewModel.onInstallmentSelected(installment = 1)
        viewModel.onPayClicked()
        testScheduler.advanceTimeBy(400)

        viewModel.onViewEventConsumed()

        assertFalse(viewModel.viewState.value.footerState.buttonState?.isLoading == true)
    }
}
