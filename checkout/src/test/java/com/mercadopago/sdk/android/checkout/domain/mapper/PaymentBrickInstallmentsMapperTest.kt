package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsHeaderOutput
import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsOutput
import com.mercadopago.sdk.android.checkout.domain.model.QuotaOutput
import com.mercadopago.sdk.android.checkout.domain.model.QuotaState
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class PaymentBrickInstallmentsMapperTest {
    @Test
    fun `given installments output when mapped then maps display and quotas`() {
        val installments = installmentsOutput(
            selectionType = "CHEVRON",
            quotas = listOf(
                quotaOutput(
                    state = "SUCCESS",
                    accessibilityLabel = "1 cuota de 1000",
                ),
            ),
        )

        val result = installments.toMPInstallmentData()

        assertEquals("Elegí las cuotas", result.display.title)
        assertEquals(SelectionDisplayType.Chevron, result.display.displayType)
        assertEquals("Total", result.display.footer.footerTitle)
        assertEquals("Pagar", result.display.footer.buttonLabel)
        assertNull(result.selectedInstallment)
        with(result.quotas.single()) {
            assertEquals(1, this.installments)
            assertEquals(BigDecimal("1000.00"), installmentAmount)
            assertEquals(BigDecimal("1000.00"), totalAmount)
            assertEquals("1x 1000", primaryLabel)
            assertEquals("Sin interés", secondaryLabel)
            assertNull(tertiaryLabel)
            assertEquals(QuotaState.Success, state)
            assertEquals("1 cuota de 1000", accessibilityLabel)
        }
    }

    @Test
    fun `given unknown selection type when mapped then falls back to radio button`() {
        val installments = installmentsOutput(selectionType = "unknown")

        val result = installments.toMPInstallmentData()

        assertEquals(SelectionDisplayType.RadioButton, result.display.displayType)
    }

    @Test
    fun `given non success quota state when mapped then maps state to none`() {
        val installments = installmentsOutput(
            quotas = listOf(quotaOutput(state = "selected", accessibilityLabel = null)),
        )

        val result = installments.toMPInstallmentData()

        with(result.quotas.single()) {
            assertEquals(QuotaState.None, state)
            assertNull(accessibilityLabel)
        }
    }

    private fun installmentsOutput(
        selectionType: String = "radio_button",
        quotas: List<QuotaOutput> = emptyList(),
    ) = InstallmentsOutput(
        header = InstallmentsHeaderOutput(title = "Elegí las cuotas"),
        totalLabel = "Total",
        payButtonLabel = "Pagar",
        selectionType = selectionType,
        quotas = quotas,
    )

    private fun quotaOutput(
        state: String,
        accessibilityLabel: String?,
    ) = QuotaOutput(
        installments = 1,
        installmentAmount = BigDecimal("1000.00"),
        totalAmount = BigDecimal("1000.00"),
        primaryLabel = "1x 1000",
        secondaryLabel = "Sin interés",
        state = state,
        accessibilityLabel = accessibilityLabel,
    )
}
