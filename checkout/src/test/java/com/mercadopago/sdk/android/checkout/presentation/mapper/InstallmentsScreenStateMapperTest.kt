package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.model.QuotaState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class InstallmentsScreenStateMapperTest {
    @Test
    fun `footer uses secondaryLabel of selected quota when available`() {
        val state = installmentDataWith(
            currencySymbol = "R$",
            quotas = listOf(
                quota(installments = 1, totalAmount = BigDecimal("1000.00"), primary = "1x R$ 1.000,00"),
                quota(
                    installments = 2,
                    totalAmount = BigDecimal("1096.40"),
                    primary = "2x R$ 548,20",
                    secondary = "R$ 1.096,40",
                ),
            ),
            selectedInstallment = 2,
        ).toInstallmentsScreenState()

        assertEquals("R$", state.footerState.currencySymbol)
        assertEquals("1.096", state.footerState.amountIntegerPart)
        assertEquals("40", state.footerState.amountDecimalPart)
    }

    @Test
    fun `footer falls back to totalAmount with BFF currencySymbol when secondaryLabel is empty`() {
        val state = installmentDataWith(
            currencySymbol = "R$",
            quotas = listOf(
                quota(installments = 1, totalAmount = BigDecimal("1000.00"), primary = "1x R$ 1.000,00"),
                quota(installments = 2, totalAmount = BigDecimal("1096.40"), primary = "2x R$ 548,20"),
            ),
            selectedInstallment = 2,
        ).toInstallmentsScreenState()

        assertEquals("R$", state.footerState.currencySymbol)
        assertEquals("1096", state.footerState.amountIntegerPart)
        assertEquals("40", state.footerState.amountDecimalPart)
    }

    @Test
    fun `footer falls back to first quota when no selection and RadioButton`() {
        val state = installmentDataWith(
            currencySymbol = "R$",
            quotas = listOf(
                quota(installments = 1, totalAmount = BigDecimal("1000.00"), primary = "1x R$ 1.000,00"),
                quota(installments = 2, totalAmount = BigDecimal("1096.40")),
            ),
            selectedInstallment = null,
            displayType = InstallmentsDisplayType.RadioButton,
        ).toInstallmentsScreenState()

        assertEquals("1000", state.footerState.amountIntegerPart)
        assertEquals("00", state.footerState.amountDecimalPart)
    }

    @Test
    fun `footer uses preselected quota when state is Selected`() {
        val state = installmentDataWith(
            currencySymbol = "R$",
            quotas = listOf(
                quota(installments = 1, totalAmount = BigDecimal("1000.00")),
                quota(installments = 3, totalAmount = BigDecimal("1112.30"), state = QuotaState.Selected),
                quota(installments = 6, totalAmount = BigDecimal("1143.20")),
            ),
            selectedInstallment = null,
        ).toInstallmentsScreenState()

        assertEquals("1112", state.footerState.amountIntegerPart)
        assertEquals("30", state.footerState.amountDecimalPart)
    }

    @Test
    fun `footer falls back to transactionAmount when no quota matches`() {
        val state = installmentDataWith(
            currencySymbol = "R$",
            quotas = emptyList(),
            transactionAmount = BigDecimal("500.55"),
        ).toInstallmentsScreenState()

        assertEquals("R$", state.footerState.currencySymbol)
        assertEquals("500", state.footerState.amountIntegerPart)
        assertEquals("55", state.footerState.amountDecimalPart)
    }

    @Test
    fun `footer pay button label is null in Chevron mode`() {
        val state = installmentDataWith(
            quotas = listOf(quota(installments = 1, totalAmount = BigDecimal("100"))),
            displayType = InstallmentsDisplayType.Chevron,
            buttonLabel = "Pagar",
        ).toInstallmentsScreenState()

        assertNull(state.footerState.buttonLabel)
    }

    @Test
    fun `footer pay button label present in RadioButton mode`() {
        val state = installmentDataWith(
            quotas = listOf(quota(installments = 1, totalAmount = BigDecimal("100"))),
            displayType = InstallmentsDisplayType.RadioButton,
            buttonLabel = "Pagar",
        ).toInstallmentsScreenState()

        assertEquals("Pagar", state.footerState.buttonLabel)
    }

    @Test
    fun `installment item uses primaryLabel as text`() {
        val state = installmentDataWith(
            quotas = listOf(
                quota(installments = 2, primary = "2x R$ 548,20", secondary = "R$ 1.096,40"),
            ),
        ).toInstallmentsScreenState()

        val item = state.items.first()
        assertEquals("2x R$ 548,20", item.text)
        assertEquals("R$ 1.096,40", item.trailing)
    }

    @Test
    fun `secondaryLabel without decimal separator is treated as integer only`() {
        val state = installmentDataWith(
            currencySymbol = "¥",
            quotas = listOf(
                quota(installments = 1, totalAmount = BigDecimal("1000"), secondary = "¥ 1000"),
            ),
            selectedInstallment = 1,
        ).toInstallmentsScreenState()

        assertEquals("¥", state.footerState.currencySymbol)
        assertEquals("1000", state.footerState.amountIntegerPart)
        assertEquals("", state.footerState.amountDecimalPart)
    }

    private fun quota(
        installments: Int = 1,
        installmentAmount: BigDecimal = BigDecimal("100"),
        totalAmount: BigDecimal = BigDecimal("100"),
        primary: String = "",
        secondary: String = "",
        state: QuotaState = QuotaState.None,
    ) = Quota(
        installments = installments,
        installmentAmount = installmentAmount,
        totalAmount = totalAmount,
        primaryLabel = primary,
        secondaryLabel = secondary,
        state = state,
    )

    private fun installmentDataWith(
        quotas: List<Quota>,
        selectedInstallment: Int? = null,
        transactionAmount: BigDecimal? = BigDecimal("100"),
        displayType: InstallmentsDisplayType = InstallmentsDisplayType.RadioButton,
        buttonLabel: String = "",
        currencySymbol: String = "",
    ): MPInstallmentData = MPInstallmentData(
        brand = "visa",
        lastFourDigits = "1234",
        transactionAmount = transactionAmount,
        quotas = quotas,
        selectedInstallment = selectedInstallment,
        display = MPInstallmentData.Display(
            displayType = displayType,
            title = "Escolha o parcelamento",
            totalLabel = "Total",
            buttonLabel = buttonLabel,
            currencySymbol = currencySymbol,
        ),
    )
}
