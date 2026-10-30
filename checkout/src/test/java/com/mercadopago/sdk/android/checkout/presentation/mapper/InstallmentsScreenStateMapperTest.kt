package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.model.QuotaState
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentTextColor
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
    fun `footer falls back to totalAmount when secondaryLabel is a non-monetary text`() {
        val state = installmentDataWith(
            currencySymbol = "$",
            quotas = listOf(
                quota(
                    installments = 2,
                    totalAmount = BigDecimal("1167.50"),
                    secondary = "Sin interés",
                    state = QuotaState.Success,
                ),
            ),
            selectedInstallment = null,
        ).toInstallmentsScreenState()

        assertEquals("1167", state.footerState.amountIntegerPart)
        assertEquals("50", state.footerState.amountDecimalPart)
    }

    @Test
    fun `footer falls back to totalAmount when secondaryLabel has digits but is not monetary`() {
        val state = installmentDataWith(
            currencySymbol = "$",
            quotas = listOf(
                quota(
                    installments = 2,
                    totalAmount = BigDecimal("1167.50"),
                    secondary = "Hasta 12 cuotas sin interés",
                    state = QuotaState.Success,
                ),
            ),
            selectedInstallment = null,
        ).toInstallmentsScreenState()

        assertEquals("1167", state.footerState.amountIntegerPart)
        assertEquals("50", state.footerState.amountDecimalPart)
    }

    @Test
    fun `effectiveSelectedQuota ignores quotas with null installments when nothing is selected`() {
        val state = installmentDataWith(
            currencySymbol = "$",
            quotas = listOf(
                Quota(installments = null, totalAmount = BigDecimal("1.00")),
                quota(installments = 3, totalAmount = BigDecimal("1112.30"), state = QuotaState.Success),
            ),
            selectedInstallment = null,
        ).toInstallmentsScreenState()

        assertEquals("1112", state.footerState.amountIntegerPart)
        assertEquals("30", state.footerState.amountDecimalPart)
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
            displayType = SelectionDisplayType.RadioButton,
        ).toInstallmentsScreenState()

        assertEquals("1000", state.footerState.amountIntegerPart)
        assertEquals("00", state.footerState.amountDecimalPart)
    }

    @Test
    fun `footer falls back to first quota when no selection and Chevron`() {
        val state = installmentDataWith(
            currencySymbol = "R$",
            quotas = listOf(
                quota(installments = 1, totalAmount = BigDecimal("1000.00"), primary = "1x R$ 1.000,00"),
                quota(installments = 2, totalAmount = BigDecimal("1096.40")),
            ),
            selectedInstallment = null,
            displayType = SelectionDisplayType.Chevron,
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
                quota(installments = 3, totalAmount = BigDecimal("1112.30"), state = QuotaState.Success),
                quota(installments = 6, totalAmount = BigDecimal("1143.20")),
            ),
            selectedInstallment = null,
        ).toInstallmentsScreenState()

        assertEquals("1112", state.footerState.amountIntegerPart)
        assertEquals("30", state.footerState.amountDecimalPart)
    }

    @Test
    fun `footer pay button label is null in Chevron mode`() {
        val state = installmentDataWith(
            quotas = listOf(quota(installments = 1, totalAmount = BigDecimal("100"))),
            displayType = SelectionDisplayType.Chevron,
            buttonLabel = "Pagar",
        ).toInstallmentsScreenState()

        assertNull(state.footerState.buttonLabel)
    }

    @Test
    fun `footer pay button label present in RadioButton mode`() {
        val state = installmentDataWith(
            quotas = listOf(quota(installments = 1, totalAmount = BigDecimal("100"))),
            displayType = SelectionDisplayType.RadioButton,
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
    fun `given success quota when mapped then secondary label color is positive`() {
        val installmentData = installmentDataWith(
            quotas = listOf(
                quota(
                    secondary = "Sem juros",
                    state = QuotaState.Success,
                ),
            ),
        )

        val state = installmentData.toInstallmentsScreenState()

        assertEquals(InstallmentTextColor.Positive, state.items.first().secondaryLabelColor)
    }

    @Test
    fun `given default quota when mapped then secondary label color is null`() {
        val installmentData = installmentDataWith(
            quotas = listOf(quota(secondary = "Com juros")),
        )

        val state = installmentData.toInstallmentsScreenState()

        assertNull(state.items.first().secondaryLabelColor)
    }

    @Test
    fun `given default installments matching a quota when mapped then preselects it`() {
        // Given
        val data = installmentDataWith(
            quotas = listOf(
                quota(installments = 1, primary = "1x"),
                quota(installments = 3, primary = "3x", state = QuotaState.Success),
            ),
            defaultInstallments = 1,
        )

        // When
        val state = data.toInstallmentsScreenState()

        // Then
        assertEquals(listOf(true, false), state.items.map { it.isSelected })
    }

    @Test
    fun `given default installments not in quotas when mapped then falls back to success quota`() {
        // Given
        val data = installmentDataWith(
            quotas = listOf(
                quota(installments = 1, primary = "1x"),
                quota(installments = 3, primary = "3x", state = QuotaState.Success),
            ),
            defaultInstallments = 6,
        )

        // When
        val state = data.toInstallmentsScreenState()

        // Then
        assertEquals(listOf(false, true), state.items.map { it.isSelected })
    }

    @Test
    fun `given selected installment and default when mapped then selected installment wins`() {
        // Given
        val data = installmentDataWith(
            quotas = listOf(
                quota(installments = 1, primary = "1x"),
                quota(installments = 3, primary = "3x"),
            ),
            selectedInstallment = 3,
            defaultInstallments = 1,
        )

        // When
        val state = data.toInstallmentsScreenState()

        // Then
        assertEquals(listOf(false, true), state.items.map { it.isSelected })
    }

    @Test
    fun `installment item uses accessibilityLabel from quota when present`() {
        val state = installmentDataWith(
            quotas = listOf(
                quota(
                    installments = 3,
                    primary = "3x R$ 33,34",
                    accessibilityLabel = "3 parcelas de R$ 33,34, sem acréscimo",
                ),
            ),
        ).toInstallmentsScreenState()

        assertEquals(
            "3 parcelas de R$ 33,34, sem acréscimo",
            state.items.first().accessibilityLabel,
        )
    }

    @Test
    fun `installment item falls back to primaryLabel when accessibilityLabel is null`() {
        val state = installmentDataWith(
            quotas = listOf(
                quota(installments = 1, primary = "1x R$ 1.000,00", accessibilityLabel = null),
            ),
        ).toInstallmentsScreenState()

        assertEquals("1x R$ 1.000,00", state.items.first().accessibilityLabel)
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

    @Test
    fun `secondaryLabel with BRL thousands dot and no decimal keeps full integer part`() {
        val state = installmentDataWith(
            currencySymbol = "R$",
            quotas = listOf(
                quota(installments = 1, totalAmount = BigDecimal("1000"), secondary = "R$ 1.000"),
            ),
            selectedInstallment = 1,
        ).toInstallmentsScreenState()

        assertEquals("R$", state.footerState.currencySymbol)
        assertEquals("1.000", state.footerState.amountIntegerPart)
        assertEquals("", state.footerState.amountDecimalPart)
    }

    private fun quota(
        installments: Int = 1,
        installmentAmount: BigDecimal = BigDecimal("100"),
        totalAmount: BigDecimal = BigDecimal("100"),
        primary: String = "",
        secondary: String = "",
        state: QuotaState = QuotaState.None,
        accessibilityLabel: String? = null,
    ) = Quota(
        installments = installments,
        installmentAmount = installmentAmount,
        totalAmount = totalAmount,
        primaryLabel = primary,
        secondaryLabel = secondary,
        state = state,
        accessibilityLabel = accessibilityLabel,
    )

    private fun installmentDataWith(
        quotas: List<Quota>,
        selectedInstallment: Int? = null,
        defaultInstallments: Int? = null,
        displayType: SelectionDisplayType = SelectionDisplayType.RadioButton,
        buttonLabel: String = "",
        currencySymbol: String = "",
    ): MPInstallmentData = MPInstallmentData(
        quotas = quotas,
        selectedInstallment = selectedInstallment,
        defaultInstallments = defaultInstallments,
        display = MPInstallmentData.InstallmentDisplay(
            title = "Escolha o parcelamento",
            currencySymbol = currencySymbol,
            displayType = displayType,
            footer = MPInstallmentData.InstallmentFooterDisplay(
                footerTitle = "Total",
                brand = "visa",
                lastFourDigits = "1234",
                buttonLabel = buttonLabel,
            ),
        ),
    )
}
