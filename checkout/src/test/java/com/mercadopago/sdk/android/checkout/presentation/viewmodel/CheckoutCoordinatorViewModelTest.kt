package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertSame

internal class CheckoutCoordinatorViewModelTest {
    private val viewModel = CheckoutCoordinatorViewModel()

    @Test
    fun `when installment context is set then it is available until cleared`() {
        val installmentData = mockk<MPInstallmentData>()
        val paymentData = mockk<MPPaymentData>()

        viewModel.setInstallmentData(installmentData, paymentData)

        assertSame(installmentData, viewModel.state.value.installmentData)
        assertSame(paymentData, viewModel.state.value.paymentData)

        viewModel.clearInstallmentData()

        assertNull(viewModel.state.value.installmentData)
        assertNull(viewModel.state.value.paymentData)
    }

    @Test
    fun `when transient contexts are cleared then no payment-related data remains`() {
        val securityCodeConfig = mockk<SecurityCodeScreenConfig>()
        val reviewConfirmParams = mockk<ProcessOrderParams>()
        val methodSelectionData = mockk<MethodSelectionScreenData>()

        viewModel.setSecurityCodeConfig(securityCodeConfig)
        viewModel.setReviewConfirmParams(reviewConfirmParams)
        viewModel.setMethodSelectionData(methodSelectionData)

        viewModel.clearSecurityCodeConfig()
        viewModel.clearReviewConfirmParams()
        viewModel.clearMethodSelectionData()

        assertNull(viewModel.state.value.securityCodeConfig)
        assertNull(viewModel.state.value.reviewConfirmParams)
        assertNull(viewModel.state.value.methodSelectionData)
    }
}
