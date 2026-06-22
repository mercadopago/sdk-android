package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.domain.model.CardFieldTranslationsOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardHolderNameTranslationsOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardInstallmentsHeaderOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardInstallmentsTranslationsOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardQuotaOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardSecurityCodeTranslationsOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardTranslationsOutput
import com.mercadopago.sdk.android.checkout.domain.model.InstallmentConfigOutput
import com.mercadopago.sdk.android.checkout.domain.model.QuotaState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

internal class InstallmentConfigOutputMapperTest {
    private fun buildTranslations(
        installmentsTitle: String = "Elegí las cuotas",
        totalLabel: String = "Total",
        payButtonLabel: String = "Pagar",
    ) = CardTranslationsOutput(
        cardFormTitle = "Ingresá tu tarjeta",
        cardFormFooterButtonLabel = payButtonLabel,
        cardNumber = CardFieldTranslationsOutput(
            label = "N",
            placeholder = "P",
            errorEmptyField = "E",
            errorIncompleteField = "I",
            errorInvalidField = null,
            helper = null,
        ),
        securityCode = CardSecurityCodeTranslationsOutput(
            label = "CVV",
            placeholder = "P",
            tooltip = null,
            errorEmptyField = "E",
            errorIncompleteField = "I",
        ),
        expirationDate = CardFieldTranslationsOutput(
            label = "F",
            placeholder = "P",
            errorEmptyField = "E",
            errorIncompleteField = "I",
            errorInvalidField = null,
            helper = null,
        ),
        holderName = CardHolderNameTranslationsOutput(label = "H", placeholder = "P", helper = null),
        installments = CardInstallmentsTranslationsOutput(
            header = CardInstallmentsHeaderOutput(title = installmentsTitle),
            interestFreeLabel = "Sin interés",
            totalLabel = totalLabel,
        ),
    )

    private fun buildInstallmentConfig(
        selectionType: String = "radio_button",
    ) = InstallmentConfigOutput(
        selectionType = selectionType,
        quotas = listOf(
            CardQuotaOutput(
                installments = 1,
                installmentAmount = BigDecimal("500"),
                totalAmount = BigDecimal("500"),
                primaryLabel = "1x $ 500,00",
                secondaryLabel = "",
                state = "none",
                accessibilityLabel = "1 cuota",
            ),
            CardQuotaOutput(
                installments = 3,
                installmentAmount = BigDecimal("170"),
                totalAmount = BigDecimal("510"),
                primaryLabel = "3x $ 170,00",
                secondaryLabel = "$ 510,00",
                state = "interest_free",
                accessibilityLabel = null,
            ),
        ),
    )

    @Test
    fun `given installment config then quotas are mapped`() {
        val data = buildInstallmentConfig().toInstallmentData(buildTranslations())

        assertEquals(2, data.quotas.size)
    }

    @Test
    fun `given quota then all fields map correctly`() {
        val data = buildInstallmentConfig().toInstallmentData(buildTranslations())
        val quota = data.quotas[0]

        assertEquals(1, quota.installments)
        assertEquals(BigDecimal("500"), quota.installmentAmount)
        assertEquals("1x $ 500,00", quota.primaryLabel)
        assertEquals(QuotaState.None, quota.state)
    }

    @Test
    fun `given interest_free quota then maps to QuotaState Success`() {
        val data = buildInstallmentConfig().toInstallmentData(buildTranslations())

        assertEquals(QuotaState.Success, data.quotas[1].state)
    }

    @Test
    fun `given translations then display title and labels are set from translations`() {
        val data = buildInstallmentConfig().toInstallmentData(
            buildTranslations(installmentsTitle = "Cuotas", totalLabel = "Total", payButtonLabel = "Pagar"),
        )

        assertEquals("Cuotas", data.display.title)
        assertEquals("Total", data.display.footer.footerTitle)
        assertEquals("Pagar", data.display.footer.buttonLabel)
    }

    @Test
    fun `given radio_button selection type then displayType is RadioButton`() {
        val data = buildInstallmentConfig(selectionType = "radio_button").toInstallmentData(buildTranslations())

        assertEquals(InstallmentsDisplayType.RadioButton, data.display.displayType)
    }

    @Test
    fun `given chevron selection type then displayType is Chevron`() {
        val data = buildInstallmentConfig(selectionType = "chevron").toInstallmentData(buildTranslations())

        assertEquals(InstallmentsDisplayType.Chevron, data.display.displayType)
    }

    @Test
    fun `given recommended state then maps to QuotaState Success`() {
        val config = InstallmentConfigOutput(
            selectionType = "radio_button",
            quotas = listOf(
                CardQuotaOutput(
                    installments = 6,
                    installmentAmount = BigDecimal("100"),
                    totalAmount = BigDecimal("600"),
                    primaryLabel = "6x",
                    secondaryLabel = "",
                    state = "recommended",
                    accessibilityLabel = null,
                ),
            ),
        )
        val data = config.toInstallmentData(buildTranslations())

        assertEquals(QuotaState.Success, data.quotas.first().state)
    }
}
