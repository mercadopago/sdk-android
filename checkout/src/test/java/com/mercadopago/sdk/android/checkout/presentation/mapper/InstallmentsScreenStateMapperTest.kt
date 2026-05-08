package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class InstallmentsScreenStateMapperTest {
    private val basePayment = MPPaymentData(
        transactionAmount = BigDecimal("100.00"),
        token = "token",
        installment = 1,
        paymentMethodId = "visa",
        paymentTypeId = "credit_card",
        issuerId = "1",
        payer = null,
    )

    private val defaultQuotas = listOf(
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

    private fun installmentData(
        displayType: InstallmentsDisplayType = InstallmentsDisplayType.RadioButton,
        quotas: List<Quota> = defaultQuotas,
        brand: String = "visa",
        lastFourDigits: String = "1234",
        interestFreeLabel: String = "",
        totalLabel: String = "",
        payButtonLabel: String = "",
        headerChevron: String = "",
        headerRadio: String = "",
    ) = MPInstallmentData(
        brand = brand,
        lastFourDigits = lastFourDigits,
        paymentMethodId = brand,
        paymentTypeId = "credit_card",
        quotas = quotas,
        display = MPInstallmentData.Display(
            displayType = displayType,
            interestFreeLabel = interestFreeLabel,
            totalLabel = totalLabel,
            payButtonLabel = payButtonLabel,
            headerChevron = headerChevron,
            headerRadio = headerRadio,
        ),
    )

    @Test
    fun `RadioButton mode auto-selects first item when no selection passed`() {
        val state = toInstallmentsScreenState(basePayment, installmentData())

        assertTrue(state.installmentsState.first().isSelected)
        assertFalse(state.installmentsState.drop(1).any { it.isSelected })
    }

    @Test
    fun `RadioButton mode honors explicit selection`() {
        val state = toInstallmentsScreenState(basePayment, installmentData(), selectedNumber = 3)

        val three = state.installmentsState.first { it.number == 3 }
        assertTrue(three.isSelected)
    }

    @Test
    fun `Chevron mode does not pre-select any item`() {
        val state = toInstallmentsScreenState(
            basePayment,
            installmentData(displayType = InstallmentsDisplayType.Chevron),
        )

        assertTrue(state.installmentsState.none { it.isSelected })
    }

    @Test
    fun `Chevron mode footer has no buttonLabel`() {
        val state = toInstallmentsScreenState(
            basePayment,
            installmentData(
                displayType = InstallmentsDisplayType.Chevron,
                payButtonLabel = "Pagar",
            ),
        )

        assertNull(state.footerState?.buttonLabel)
    }

    @Test
    fun `RadioButton mode footer keeps payButtonLabel`() {
        val state = toInstallmentsScreenState(
            basePayment,
            installmentData(payButtonLabel = "Pagar"),
        )

        assertEquals("Pagar", state.footerState?.buttonLabel)
    }

    @Test
    fun `subtitle joins capitalized brand parts and last four digits`() {
        val state = toInstallmentsScreenState(
            basePayment,
            installmentData(brand = "visa_credit", lastFourDigits = "1234"),
        )

        assertEquals("Visa Credit **** 1234", state.footerState?.subtitle)
    }

    @Test
    fun `first installment trailing is empty`() {
        val state = toInstallmentsScreenState(basePayment, installmentData())

        val first = state.installmentsState.first { it.number == 1 }
        assertEquals("", first.trailing)
    }

    @Test
    fun `interest-free non-first installment shows interest free label`() {
        val state = toInstallmentsScreenState(
            basePayment,
            installmentData(
                quotas = listOf(
                    Quota(
                        installments = 3,
                        installmentAmount = BigDecimal("50.00"),
                        totalAmount = BigDecimal("50.00"),
                    ),
                ),
                interestFreeLabel = "Sem juros",
            ),
        )

        val three = state.installmentsState.first { it.number == 3 }
        assertTrue(three.interestFree)
        assertEquals("Sem juros", three.trailing)
    }

    @Test
    fun `non-interest-free non-first installment shows total amount in trailing`() {
        val state = toInstallmentsScreenState(
            basePayment,
            installmentData(
                quotas = listOf(
                    Quota(
                        installments = 6,
                        installmentAmount = BigDecimal("20.00"),
                        totalAmount = BigDecimal("120.00"),
                    ),
                ),
            ),
        )

        val six = state.installmentsState.first { it.number == 6 }
        assertFalse(six.interestFree)
        assertTrue(six.trailing.isNotEmpty())
    }

    @Test
    fun `Chevron mode title comes from headerChevron`() {
        val state = toInstallmentsScreenState(
            basePayment,
            installmentData(
                displayType = InstallmentsDisplayType.Chevron,
                headerChevron = "Escolha (chevron)",
                headerRadio = "Escolha (radio)",
            ),
        )

        assertEquals("Escolha (chevron)", state.title)
    }

    @Test
    fun `RadioButton mode title comes from headerRadio`() {
        val state = toInstallmentsScreenState(
            basePayment,
            installmentData(
                headerChevron = "Escolha (chevron)",
                headerRadio = "Escolha (radio)",
            ),
        )

        assertEquals("Escolha (radio)", state.title)
    }
}
