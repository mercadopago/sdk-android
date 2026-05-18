package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
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

    private fun makeData(
        displayType: InstallmentsDisplayType = InstallmentsDisplayType.RadioButton,
    ) = MPInstallmentData(
        brand = "visa",
        lastFourDigits = "1234",
        quotas = quotas,
        display = MPInstallmentData.Display(displayType = displayType),
    )

    private fun makeViewModel(
        installmentData: MPInstallmentData = makeData(),
    ) = InstallmentsViewModel(installmentData = installmentData)

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
    fun `onInstallmentSelected ignored in Chevron mode`() = runTest {
        val viewModel = makeViewModel(makeData(InstallmentsDisplayType.Chevron))

        viewModel.onInstallmentSelected(installment = 3)

        assertFalse(viewModel.viewState.value.items.any { it.isSelected })
    }
}
