package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentViewEvent
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class InstallmentsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val paymentData = MPPaymentData(
        token = "token",
        transactionAmount = BigDecimal("100.00"),
        paymentMethodId = "visa",
        paymentTypeId = "credit_card",
        payer = null,
        installment = null,
        issuerId = "1",
    )

    private fun makeViewModel(
        paymentData: MPPaymentData = this.paymentData,
    ) = InstallmentsViewModel(paymentData = paymentData)

    @Test
    fun `onInstallmentSelected emits OnSuccess viewEvent carrying selected installment`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onInstallmentSelected(installment = 3)

        val event = viewModel.viewEvent.value
        assertTrue(event is InstallmentViewEvent.OnSuccess)
        assertEquals(3, event.payment.installment)
    }

    @Test
    fun `clearViewEvent resets viewEvent to null`() = runTest {
        val viewModel = makeViewModel()
        viewModel.onInstallmentSelected(installment = 2)

        viewModel.clearViewEvent()

        assertNull(viewModel.viewEvent.value)
    }
}
