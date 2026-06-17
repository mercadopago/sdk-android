package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.DocumentTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.FieldTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentConfigResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsHeaderTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.IssuerResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.LengthConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentMethodResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.QuotaResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.Translations
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class CardBinResponseMapperTest {
    private fun paymentMethodResponse(
        id: String? = "visa",
        paymentTypeId: String? = null,
    ) = PaymentMethodResponse(
        id = id,
        paymentTypeId = paymentTypeId,
        cardNumber = null,
        securityCode = null,
        issuers = null,
    )

    private fun minimalResponse(
        id: String? = "visa",
        paymentTypeId: String? = null,
        translations: Translations? = defaultTranslations(),
    ) = CardBinResponse(
        paymentMethods = listOf(paymentMethodResponse(id, paymentTypeId)),
        installment = null,
        translations = translations,
    )

    private fun defaultFieldTranslations(
        label: String = "",
        placeholder: String = "",
        errorEmptyField: String = "",
        errorIncompleteField: String = "",
        errorInvalidField: String = "",
    ) = FieldTranslations(
        label = label,
        placeholder = placeholder,
        errorEmptyField = errorEmptyField,
        errorIncompleteField = errorIncompleteField,
        errorInvalidField = errorInvalidField,
    )

    private fun defaultSecurityCodeTranslations(
        label: String = "",
        placeholder: String = "",
        tooltip: String = "",
        errorEmptyField: String = "",
        errorIncompleteField: String = "",
    ) = SecurityCodeTranslations(
        label = label,
        placeholder = placeholder,
        tooltip = tooltip,
        errorEmptyField = errorEmptyField,
        errorIncompleteField = errorIncompleteField,
    )

    private fun defaultTranslations(
        cardNumber: FieldTranslations = defaultFieldTranslations(),
        holderName: FieldTranslations = defaultFieldTranslations(),
        expirationDate: FieldTranslations = defaultFieldTranslations(),
        securityCode: SecurityCodeTranslations = defaultSecurityCodeTranslations(),
        currencySymbol: String = "",
        installments: InstallmentsTranslations = InstallmentsTranslations(
            header = InstallmentsHeaderTranslations(title = ""),
            totalLabel = "",
            payButtonLabel = "",
        ),
    ) = Translations(
        cardFormTitle = "Card Payment",
        cardFormFooterButtonLabel = "Pay",
        currencySymbol = currencySymbol,
        cardNumber = cardNumber,
        holderName = holderName,
        expirationDate = expirationDate,
        securityCode = securityCode,
        document = DocumentTranslations(
            label = "",
            errorEmptyField = "",
            errorIncompleteField = "",
            errorInvalidField = "",
        ),
        installments = installments,
    )

    @Test
    fun `toDomain maps card number config with translations into CardNumberField`() {
        val response = minimalResponse(
            translations = defaultTranslations(
                cardNumber = defaultFieldTranslations(label = "Número", placeholder = "0000"),
            ),
        ).copy(
            paymentMethods = listOf(
                paymentMethodResponse().copy(
                    cardNumber = CardNumberConfig(
                        type = "standard",
                        length = LengthConfig(min = 16, max = 16),
                        mask = "0000 0000 0000 0000",
                    ),
                ),
            ),
        )

        val domain = response.toDomain()

        assertEquals("Número", domain.cardNumber?.label)
        assertEquals("0000", domain.cardNumber?.placeholder)
        assertEquals("standard", domain.cardNumber?.config?.type)
        assertEquals(16, domain.cardNumber?.config?.length?.max)
    }

    @Test
    fun `toDomain returns null cardNumber when translations are missing`() {
        val response = minimalResponse(translations = null).copy(
            paymentMethods = listOf(
                paymentMethodResponse().copy(
                    cardNumber = CardNumberConfig(
                        type = "standard",
                        length = LengthConfig(min = 16, max = 16),
                        mask = "",
                    ),
                ),
            ),
        )

        val domain = response.toDomain()

        assertNull(domain.cardNumber)
    }

    @Test
    fun `toDomain maps security code config with translations into SecurityCodeField`() {
        val response = minimalResponse(
            translations = defaultTranslations(
                securityCode = defaultSecurityCodeTranslations(label = "CVV", tooltip = "3 dígitos"),
            ),
        ).copy(
            paymentMethods = listOf(
                paymentMethodResponse().copy(
                    securityCode = SecurityCodeConfig(
                        type = "mandatory",
                        length = 3,
                        cardLocation = "back",
                    ),
                ),
            ),
        )

        val domain = response.toDomain()

        assertEquals("CVV", domain.securityCode?.label)
        assertEquals("3 dígitos", domain.securityCode?.tooltip)
        assertEquals(3, domain.securityCode?.config?.length?.max)
    }

    @Test
    fun `toDomain maps issuers list`() {
        val response = minimalResponse().copy(
            paymentMethods = listOf(
                paymentMethodResponse().copy(
                    issuers = listOf(IssuerResponse(id = "1234", name = "VISA")),
                ),
            ),
        )

        val domain = response.toDomain()

        assertEquals(1, domain.issuers.size)
        assertEquals("1234", domain.issuers[0].id)
        assertEquals("VISA", domain.issuers[0].name)
    }

    @Test
    fun `toDomain maps quotas`() {
        val response = minimalResponse().copy(
            installment = InstallmentConfigResponse(
                selectionType = null,
                quotas = listOf(
                    QuotaResponse(
                        installments = 1,
                        installmentAmount = 10.0f,
                        totalAmount = 10.0f,
                        primaryLabel = null,
                        secondaryLabel = null,
                        tertiaryLabel = null,
                        state = null,
                        accessibilityLabel = null,
                    ),
                ),
            ),
        )

        val domain = response.toDomain()

        assertEquals(1, domain.installmentData.quotas.size)
        assertEquals(1, domain.installmentData.quotas[0].installments)
        assertEquals(0, BigDecimal.valueOf(10.0).compareTo(domain.installmentData.quotas[0].installmentAmount))
        assertEquals(0, BigDecimal.valueOf(10.0).compareTo(domain.installmentData.quotas[0].totalAmount))
    }

    @Test
    fun `toDomain maps quota accessibilityLabel`() {
        val response = minimalResponse().copy(
            installment = InstallmentConfigResponse(
                selectionType = null,
                quotas = listOf(
                    QuotaResponse(
                        installments = 3,
                        installmentAmount = 33.34f,
                        totalAmount = 100.00f,
                        primaryLabel = "3x R$ 33,34",
                        secondaryLabel = "Sem juros",
                        tertiaryLabel = null,
                        state = "success",
                        accessibilityLabel = "3 parcelas de R$ 33,34, sem acréscimo",
                    ),
                ),
            ),
        )

        val domain = response.toDomain()

        assertEquals(
            "3 parcelas de R$ 33,34, sem acréscimo",
            domain.installmentData.quotas[0].accessibilityLabel,
        )
    }

    @Test
    fun `toDomain maps quota accessibilityLabel as null when absent`() {
        val response = minimalResponse().copy(
            installment = InstallmentConfigResponse(
                selectionType = null,
                quotas = listOf(
                    QuotaResponse(
                        installments = 1,
                        installmentAmount = 10.0f,
                        totalAmount = 10.0f,
                        primaryLabel = "1x R$ 10,00",
                        secondaryLabel = null,
                        tertiaryLabel = null,
                        state = null,
                        accessibilityLabel = null,
                    ),
                ),
            ),
        )

        val domain = response.toDomain()

        assertNull(domain.installmentData.quotas[0].accessibilityLabel)
    }

    @Test
    fun `toDomain maps radio_button selectionType to RadioButton displayType`() {
        val response = minimalResponse().copy(
            installment = InstallmentConfigResponse(selectionType = "radio_button", quotas = null),
        )

        val domain = response.toDomain()

        assertEquals(InstallmentsDisplayType.RadioButton, domain.installmentData.display.displayType)
    }

    @Test
    fun `toDomain maps chevron selectionType to Chevron displayType`() {
        val response = minimalResponse().copy(
            installment = InstallmentConfigResponse(selectionType = "chevron", quotas = null),
        )

        val domain = response.toDomain()

        assertEquals(InstallmentsDisplayType.Chevron, domain.installmentData.display.displayType)
    }

    @Test
    fun `toDomain defaults to RadioButton displayType when installment is null`() {
        val domain = minimalResponse().copy(installment = null).toDomain()

        assertEquals(InstallmentsDisplayType.RadioButton, domain.installmentData.display.displayType)
    }

    @Test
    fun `toDomain exposes currency symbol from translations`() {
        val response = minimalResponse(
            translations = defaultTranslations(currencySymbol = "R$"),
        )

        val domain = response.toDomain()

        assertEquals("R$", domain.installmentData.display.currencySymbol)
    }

    @Test
    fun `toDomain returns empty currency symbol when translations are missing`() {
        val domain = minimalResponse(translations = null).toDomain()

        assertEquals("", domain.installmentData.display.currencySymbol)
    }

    @Test
    fun `toDomain exposes installments labels from translations`() {
        val response = minimalResponse(
            translations = defaultTranslations(
                installments = InstallmentsTranslations(
                    header = InstallmentsHeaderTranslations(title = "Cuotas"),
                    totalLabel = "Total",
                    payButtonLabel = "Pagar",
                ),
            ),
        )

        val domain = response.toDomain()

        assertEquals("Cuotas", domain.installmentData.display.title)
        assertEquals("Total", domain.installmentData.display.footer.footerTitle)
        assertEquals("Pagar", domain.installmentData.display.footer.buttonLabel)
    }

    @Test
    fun `toDomain returns empty issuers and quotas when paymentMethods is null`() {
        val response = CardBinResponse(
            paymentMethods = null,
            installment = null,
            translations = null,
        )

        val domain = response.toDomain()

        assertTrue(domain.issuers.isEmpty())
        assertTrue(domain.installmentData.quotas.isEmpty())
        assertNull(domain.cardNumber)
        assertNull(domain.securityCode)
        assertNull(domain.id)
        assertNull(domain.paymentTypeId)
    }

    @Test
    fun `toDomain returns empty issuers when payment method issuers is null`() {
        val domain = minimalResponse().toDomain()

        assertTrue(domain.issuers.isEmpty())
    }

    @Test
    fun `toDomain returns empty quotas when installment is null`() {
        val domain = minimalResponse(id = "master", paymentTypeId = "debit_card")
            .copy(installment = null)
            .toDomain()

        assertTrue(domain.installmentData.quotas.isEmpty())
    }

    @Test
    fun `toDomain returns empty quotas when installment quotas is null`() {
        val domain = minimalResponse().copy(
            installment = InstallmentConfigResponse(selectionType = null, quotas = null),
        ).toDomain()

        assertTrue(domain.installmentData.quotas.isEmpty())
    }

    @Test
    fun `toDomain extracts id and paymentTypeId from first payment method`() {
        val response = minimalResponse(id = "master", paymentTypeId = "debit_card")

        val domain = response.toDomain()

        assertEquals("master", domain.id)
        assertEquals("debit_card", domain.paymentTypeId)
    }

    @Test
    fun `toDomain maps holderName translations into CardHolderField`() {
        val response = minimalResponse(
            translations = defaultTranslations(
                holderName = defaultFieldTranslations(label = "Titular", placeholder = "Maria"),
            ),
        )

        val domain = response.toDomain()

        assertEquals("Titular", domain.holderName?.label)
        assertEquals("Maria", domain.holderName?.placeholder)
    }
}
