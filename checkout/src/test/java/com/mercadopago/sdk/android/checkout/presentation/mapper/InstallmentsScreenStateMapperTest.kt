package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.model.QuotaState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class InstallmentsScreenStateMapperTest {
    @Test
    fun `currency symbol BR with prefix Nx is extracted`() {
        val state = installmentDataWithLabels(
            primary = "1x R$ 1.000,00",
            secondary = "",
        ).toInstallmentsScreenState()

        assertEquals("R$", state.footerState?.currencySymbol)
    }

    @Test
    fun `currency symbol BR from secondary takes precedence over primary`() {
        val state = installmentDataWithLabels(
            primary = "1x US$ 50,00",
            secondary = "R$ 100,00",
        ).toInstallmentsScreenState()

        assertEquals("R$", state.footerState?.currencySymbol)
    }

    @Test
    fun `currency symbol AR with prefix Nx is extracted`() {
        val state = installmentDataWithLabels(
            primary = "12x AR$ 101,76",
            secondary = "AR$ 1.221,10",
        ).toInstallmentsScreenState()

        assertEquals("AR$", state.footerState?.currencySymbol)
    }

    @Test
    fun `currency symbol US with prefix Nx is extracted`() {
        val state = installmentDataWithLabels(
            primary = "3x US$ 33.33",
            secondary = "US$ 100.00",
        ).toInstallmentsScreenState()

        assertEquals("US$", state.footerState?.currencySymbol)
    }

    @Test
    fun `currency symbol single dollar sign is extracted`() {
        val state = installmentDataWithLabels(
            primary = "1x $ 100,00",
            secondary = "",
        ).toInstallmentsScreenState()

        assertEquals("$", state.footerState?.currencySymbol)
    }

    @Test
    fun `currency symbol euro is extracted`() {
        val state = installmentDataWithLabels(
            primary = "1x € 100,00",
            secondary = "€ 100,00",
        ).toInstallmentsScreenState()

        assertEquals("€", state.footerState?.currencySymbol)
    }

    @Test
    fun `currency symbol pound is extracted`() {
        val state = installmentDataWithLabels(
            primary = "",
            secondary = "£ 250.00",
        ).toInstallmentsScreenState()

        assertEquals("£", state.footerState?.currencySymbol)
    }

    @Test
    fun `currency symbol yen is extracted`() {
        val state = installmentDataWithLabels(
            primary = "1x ¥ 1000",
            secondary = "",
        ).toInstallmentsScreenState()

        assertEquals("¥", state.footerState?.currencySymbol)
    }

    @Test
    fun `currency symbol rupee is extracted`() {
        val state = installmentDataWithLabels(
            primary = "1x ₹ 100",
            secondary = "₹ 100,00",
        ).toInstallmentsScreenState()

        assertEquals("₹", state.footerState?.currencySymbol)
    }

    @Test
    fun `currency symbol without Nx prefix is extracted`() {
        val state = installmentDataWithLabels(
            primary = "R$ 1.000,00",
            secondary = "",
        ).toInstallmentsScreenState()

        assertEquals("R$", state.footerState?.currencySymbol)
    }

    @Test
    fun `currency symbol falls back to primary when secondary is empty`() {
        val state = installmentDataWithLabels(
            primary = "1x R$ 100,00",
            secondary = "",
        ).toInstallmentsScreenState()

        assertEquals("R$", state.footerState?.currencySymbol)
    }

    @Test
    fun `currency symbol is searched in next quota when first quota has empty labels`() {
        val data = installmentDataWith(
            quotas = listOf(
                quota(primary = "", secondary = ""),
                quota(primary = "2x R$ 50,00", secondary = "R$ 100,00"),
            ),
        )

        val state = data.toInstallmentsScreenState()

        assertEquals("R$", state.footerState?.currencySymbol)
    }

    @Test
    fun `currency symbol label without symbol returns locale default fallback`() {
        val state = installmentDataWithLabels(
            primary = "1x 100,00",
            secondary = "",
        ).toInstallmentsScreenState()

        // sem símbolo na string → cai no fallback do locale (não é vazio nem "x")
        val symbol = state.footerState?.currencySymbol
        assertNotNull(symbol)
        assertTrue(symbol != "x", "Não deve capturar a letra 'x' do prefixo Nx como moeda")
    }

    @Test
    fun `currency symbol pure number labels do not capture letters`() {
        val state = installmentDataWithLabels(
            primary = "100,00",
            secondary = "200,00",
        ).toInstallmentsScreenState()

        val symbol = state.footerState?.currencySymbol
        assertNotNull(symbol)
        // garante que não capturou texto sem símbolo de moeda
        assertTrue(symbol.any { !it.isLetter() } || symbol.isEmpty() || symbol == ",", "Símbolo inesperado: $symbol")
    }

    @Test
    fun `currency symbol label glued to digits without space falls back`() {
        // BFF que envia "R$1.000,00" (sem espaço) — regex exige espaço entre símbolo e dígito
        val state = installmentDataWithLabels(
            primary = "R$1.000,00",
            secondary = "",
        ).toInstallmentsScreenState()

        // não casa o padrão, mas o fallback de locale ainda produz algo
        val symbol = state.footerState?.currencySymbol
        assertNotNull(symbol)
    }

    @Test
    fun `footer amount integer part uses selected quota totalAmount`() {
        val state = installmentDataWith(
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

        assertEquals("1096", state.footerState?.amountIntegerPart)
        assertEquals("40", state.footerState?.amountDecimalPart)
    }

    @Test
    fun `footer amount falls back to first quota when no selection and radio button`() {
        val state = installmentDataWith(
            quotas = listOf(
                quota(installments = 1, totalAmount = BigDecimal("1000.00"), primary = "1x R$ 1.000,00"),
                quota(installments = 2, totalAmount = BigDecimal("1096.40")),
            ),
            selectedInstallment = null,
            displayType = InstallmentsDisplayType.RadioButton,
        ).toInstallmentsScreenState()

        assertEquals("1000", state.footerState?.amountIntegerPart)
        assertEquals("00", state.footerState?.amountDecimalPart)
    }

    @Test
    fun `footer amount uses preselected quota when state is Selected`() {
        val state = installmentDataWith(
            quotas = listOf(
                quota(installments = 1, totalAmount = BigDecimal("1000.00")),
                quota(installments = 3, totalAmount = BigDecimal("1112.30"), state = QuotaState.Selected),
                quota(installments = 6, totalAmount = BigDecimal("1143.20")),
            ),
            selectedInstallment = null,
        ).toInstallmentsScreenState()

        assertEquals("1112", state.footerState?.amountIntegerPart)
        assertEquals("30", state.footerState?.amountDecimalPart)
    }

    @Test
    fun `footer amount falls back to transactionAmount when no quota matches`() {
        val state = installmentDataWith(
            quotas = emptyList(),
            transactionAmount = BigDecimal("500.55"),
        ).toInstallmentsScreenState()

        assertEquals("500", state.footerState?.amountIntegerPart)
        assertEquals("55", state.footerState?.amountDecimalPart)
    }

    @Test
    fun `footer pay button label only present in radio button mode`() {
        val state = installmentDataWith(
            quotas = listOf(quota(installments = 1, totalAmount = BigDecimal("100"))),
            displayType = InstallmentsDisplayType.Chevron,
            payButtonLabel = "Pagar",
        ).toInstallmentsScreenState()

        assertEquals(null, state.footerState?.buttonLabel)
    }

    @Test
    fun `footer pay button label present when display is radio button`() {
        val state = installmentDataWith(
            quotas = listOf(quota(installments = 1, totalAmount = BigDecimal("100"))),
            displayType = InstallmentsDisplayType.RadioButton,
            payButtonLabel = "Pagar",
        ).toInstallmentsScreenState()

        assertEquals("Pagar", state.footerState?.buttonLabel)
    }

    // ---------- helpers ----------

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

    private fun installmentDataWithLabels(
        primary: String,
        secondary: String,
    ): MPInstallmentData = installmentDataWith(
        quotas = listOf(quota(primary = primary, secondary = secondary)),
    )

    private fun installmentDataWith(
        quotas: List<Quota>,
        selectedInstallment: Int? = null,
        transactionAmount: BigDecimal? = BigDecimal("100"),
        displayType: InstallmentsDisplayType = InstallmentsDisplayType.RadioButton,
        payButtonLabel: String = "",
    ): MPInstallmentData = MPInstallmentData(
        brand = "visa",
        lastFourDigits = "1234",
        paymentMethodId = "visa",
        paymentTypeId = "credit_card",
        transactionAmount = transactionAmount,
        quotas = quotas,
        selectedInstallment = selectedInstallment,
        display = MPInstallmentData.Display(
            displayType = displayType,
            title = "Escolha o parcelamento",
            totalLabel = "Total",
            payButtonLabel = payButtonLabel,
        ),
    )
}
