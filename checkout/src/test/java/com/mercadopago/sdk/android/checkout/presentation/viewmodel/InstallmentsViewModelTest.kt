package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.presentation.event.InstallmentsScreenEvent
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsScreenState
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class InstallmentsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val coreMethods = mockk<CoreMethods>(relaxed = true)

    private val amount = BigDecimal("100.00")

    private fun makeViewModel() = InstallmentsViewModel(coreMethods = coreMethods)

    @Test
    fun `when initialized then viewState is default`() {
        val viewModel = makeViewModel()

        assertEquals(InstallmentsScreenState(), viewModel.viewState.value)
    }

    @Test
    fun `when initialized then viewEvent is Idle`() {
        val viewModel = makeViewModel()

        assertEquals(InstallmentsScreenEvent.Idle, viewModel.viewEvent.value)
    }

    @Test
    fun `when getInstallments succeeds then viewState title is updated`() = runTest {
        coEvery { coreMethods.getInstallments(bin = any(), amount = any()) } returns
            Result.Success(
                listOf(
                    Installment(
                        payerCost = listOf(
                            PayerCost(instalments = 1, installmentAmount = 100f, totalAmount = 100f),
                        ),
                    ),
                ),
            )
        val viewModel = makeViewModel()

        viewModel.getInstallments(bin = "123456", amount = amount)

        assertEquals("Escolha o parcelamento", viewModel.viewState.value.title)
    }

    @Test
    fun `when getInstallments succeeds then installmentsState is mapped from first installment`() = runTest {
        coEvery { coreMethods.getInstallments(bin = any(), amount = any()) } returns
            Result.Success(
                listOf(
                    Installment(
                        payerCost = listOf(
                            PayerCost(instalments = 1, installmentAmount = 100f, totalAmount = 100f),
                            PayerCost(instalments = 3, installmentAmount = 34f, totalAmount = 102f),
                        ),
                    ),
                ),
            )
        val viewModel = makeViewModel()

        viewModel.getInstallments(bin = "123456", amount = amount)

        val installmentsState = viewModel.viewState.value.installmentsState
        assertEquals(2, installmentsState.size)
        assertEquals(1, installmentsState.first().number)
        assertEquals(3, installmentsState[1].number)
    }

    @Test
    fun `when getInstallments succeeds then footerState is populated`() = runTest {
        coEvery { coreMethods.getInstallments(bin = any(), amount = any()) } returns
            Result.Success(
                listOf(
                    Installment(
                        payerCost = listOf(
                            PayerCost(instalments = 1, installmentAmount = 100f, totalAmount = 100f),
                        ),
                    ),
                ),
            )
        val viewModel = makeViewModel()

        viewModel.getInstallments(bin = "123456", amount = amount)

        val footerState = viewModel.viewState.value.footerState
        assertEquals("Total", footerState?.title)
        assertEquals("Santander Credito **** 1234", footerState?.subtitle)
    }

    @Test
    fun `when getInstallments succeeds with empty list then installmentsState is empty`() = runTest {
        coEvery { coreMethods.getInstallments(bin = any(), amount = any()) } returns
            Result.Success(emptyList())
        val viewModel = makeViewModel()

        viewModel.getInstallments(bin = "123456", amount = amount)

        assertEquals("Escolha o parcelamento", viewModel.viewState.value.title)
        assertTrue(viewModel.viewState.value.installmentsState.isEmpty())
    }

    @Test
    fun `when getInstallments fails then viewState stays default`() = runTest {
        coEvery { coreMethods.getInstallments(bin = any(), amount = any()) } returns
            Result.Error(ResultError.Request(message = "error", code = "CONNECTION_ERROR"))
        val viewModel = makeViewModel()

        viewModel.getInstallments(bin = "123456", amount = amount)

        assertEquals(InstallmentsScreenState(), viewModel.viewState.value)
    }

    @Test
    fun `when onInstallmentSelected then viewEvent is OnInstallmentsSelected with the value`() {
        val viewModel = makeViewModel()

        viewModel.onInstallmentSelected(installment = 6)

        assertEquals(
            InstallmentsScreenEvent.OnInstallmentsSelected(installment = 6),
            viewModel.viewEvent.value,
        )
    }
}
