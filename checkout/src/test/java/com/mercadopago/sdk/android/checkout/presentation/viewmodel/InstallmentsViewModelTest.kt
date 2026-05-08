package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.Payer
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.presentation.brick.InstallmentsCallbacks
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
internal class InstallmentsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val callbacks = mockk<InstallmentsCallbacks>(relaxed = true)

    private val paymentData = MPPaymentData(
        transactionAmount = BigDecimal("100.00"),
        token = "token_xyz",
        installment = 1,
        paymentMethodId = "visa",
        paymentTypeId = "credit_card",
        issuerId = "1",
        payer = Payer(documentType = "CPF", documentNumber = "12345678900"),
    )

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

    private fun makeData(
        displayType: InstallmentsDisplayType = InstallmentsDisplayType.RadioButton,
    ) = MPInstallmentData(
        brand = "visa",
        lastFourDigits = "1234",
        paymentMethodId = "visa",
        paymentTypeId = "credit_card",
        issuerId = "1",
        quotas = quotas,
        display = MPInstallmentData.Display(displayType = displayType),
    )

    private fun makeViewModel(
        installmentData: MPInstallmentData = makeData(),
    ) = InstallmentsViewModel(
        paymentData = paymentData,
        installmentData = installmentData,
        callbacks = callbacks,
    )

    @Test
    fun `viewState reflects quotas from installmentData`() = runTest {
        val viewModel = makeViewModel()

        kotlin.test.assertEquals(2, viewModel.viewState.value.installmentsState.size)
    }

    @Test
    fun `RadioButton mode auto-selects first item`() = runTest {
        val viewModel = makeViewModel()

        kotlin.test.assertEquals(
            true,
            viewModel.viewState.value.installmentsState.first().isSelected,
        )
    }

    @Test
    fun `Chevron mode does not pre-select`() = runTest {
        val viewModel = makeViewModel(makeData(InstallmentsDisplayType.Chevron))

        assertFalse(viewModel.viewState.value.installmentsState.any { it.isSelected })
    }

    @Test
    fun `onInstallmentSelected updates selection in RadioButton mode`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onInstallmentSelected(installment = 3)

        val states = viewModel.viewState.value.installmentsState
        kotlin.test.assertEquals(true, states.first { it.number == 3 }.isSelected)
        kotlin.test.assertEquals(false, states.first { it.number == 1 }.isSelected)
    }

    @Test
    fun `onInstallmentSelected ignored in Chevron mode`() = runTest {
        val viewModel = makeViewModel(makeData(InstallmentsDisplayType.Chevron))

        viewModel.onInstallmentSelected(installment = 3)

        assertFalse(viewModel.viewState.value.installmentsState.any { it.isSelected })
    }

    @Test
    fun `onPayClicked calls callbacks onSuccess with selected installment number`() = runTest {
        val viewModel = makeViewModel()
        viewModel.onInstallmentSelected(installment = 3)

        viewModel.onPayClicked()

        verify {
            callbacks.onSuccess(
                match { it.installment == 3 && it.token == "token_xyz" },
            )
        }
    }

    @Test
    fun `onPayClicked falls back to first installment when none explicitly selected in RadioButton mode`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onPayClicked()

        verify { callbacks.onSuccess(match { it.installment == 1 }) }
    }

    @Test
    fun `onPayClicked is no-op when no selection available in Chevron mode`() = runTest {
        val viewModel = makeViewModel(makeData(InstallmentsDisplayType.Chevron))

        viewModel.onPayClicked()

        verify(exactly = 0) { callbacks.onSuccess(any()) }
    }
}
