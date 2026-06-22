package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsHeaderOutput
import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsOutput
import com.mercadopago.sdk.android.checkout.domain.model.QuotaOutput
import com.mercadopago.sdk.android.checkout.domain.model.QuotaState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class InstallmentsOutputMapperTest {
    private fun buildInstallmentsOutput(
        selectionType: String = "radio_button",
        headerTitle: String = "Elegí las cuotas",
        totalLabel: String = "Total",
        payButtonLabel: String = "Pagar",
        quotas: List<QuotaOutput> = defaultQuotas(),
    ) = InstallmentsOutput(
        header = InstallmentsHeaderOutput(title = headerTitle),
        totalLabel = totalLabel,
        payButtonLabel = payButtonLabel,
        selectionType = selectionType,
        quotas = quotas,
    )

    private fun defaultQuotas() = listOf(
        QuotaOutput(
            installments = 1,
            installmentAmount = BigDecimal("500.00"),
            totalAmount = BigDecimal("500.00"),
            primaryLabel = "1x $ 500,00",
            secondaryLabel = "",
            state = "none",
        ),
        QuotaOutput(
            installments = 3,
            installmentAmount = BigDecimal("170.00"),
            totalAmount = BigDecimal("510.00"),
            primaryLabel = "3x $ 170,00",
            secondaryLabel = "$ 510,00",
            state = "interest_free",
        ),
    )

    @Test
    fun `given installments output then quotas count is preserved`() {
        val data = buildInstallmentsOutput().toInstallmentData()

        assertEquals(2, data.quotas.size)
    }

    @Test
    fun `given installments output then quota fields are mapped correctly`() {
        val data = buildInstallmentsOutput().toInstallmentData()
        val firstQuota = data.quotas[0]

        assertEquals(1, firstQuota.installments)
        assertEquals(BigDecimal("500.00"), firstQuota.installmentAmount)
        assertEquals("1x $ 500,00", firstQuota.primaryLabel)
        assertEquals("", firstQuota.secondaryLabel)
        assertEquals(QuotaState.None, firstQuota.state)
    }

    @Test
    fun `given interest_free state then maps to QuotaState Success`() {
        val data = buildInstallmentsOutput().toInstallmentData()

        assertEquals(QuotaState.Success, data.quotas[1].state)
    }

    @Test
    fun `given display fields then header title and button label are mapped`() {
        val data = buildInstallmentsOutput(
            headerTitle = "Elegí las cuotas",
            payButtonLabel = "Pagar",
            totalLabel = "Total",
        ).toInstallmentData()

        assertEquals("Elegí las cuotas", data.display.title)
        assertEquals("Pagar", data.display.footer.buttonLabel)
        assertEquals("Total", data.display.footer.footerTitle)
    }

    @Test
    fun `given radio_button selection type then display type is RadioButton`() {
        val data = buildInstallmentsOutput(selectionType = "radio_button").toInstallmentData()

        assertEquals(InstallmentsDisplayType.RadioButton, data.display.displayType)
    }

    @Test
    fun `given chevron selection type then display type is Chevron`() {
        val data = buildInstallmentsOutput(selectionType = "chevron").toInstallmentData()

        assertEquals(InstallmentsDisplayType.Chevron, data.display.displayType)
    }

    @Test
    fun `given empty string currency symbol then currency symbol is empty`() {
        val data = buildInstallmentsOutput().toInstallmentData()

        assertEquals("", data.display.currencySymbol)
    }

    @Test
    fun `given recommended state then maps to QuotaState Success`() {
        val quotaRecommended = QuotaOutput(
            installments = 6,
            installmentAmount = BigDecimal("100"),
            totalAmount = BigDecimal("600"),
            primaryLabel = "6x",
            secondaryLabel = "",
            state = "recommended",
        )
        val data = buildInstallmentsOutput(quotas = listOf(quotaRecommended)).toInstallmentData()

        assertEquals(QuotaState.Success, assertNotNull(data.quotas.firstOrNull()).state)
    }
}
