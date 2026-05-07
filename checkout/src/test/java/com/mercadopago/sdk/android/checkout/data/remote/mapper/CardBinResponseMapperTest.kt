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
    ) = CardBinResponse(
        paymentMethods = listOf(paymentMethodResponse(id, paymentTypeId)),
        installment = null,
        translations = null,
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
    ) = Translations(
        cardFormTitle = "Card Payment",
        cardFormFooterButtonLabel = "Pay",
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
        installments = InstallmentsTranslations(
            header = InstallmentsHeaderTranslations(chevron = "", radio = "", title = ""),
            interestFreeLabel = "",
            totalLabel = "",
        ),
    )

    @Test
    fun `toDomain maps card number config`() {
        val response = minimalResponse().copy(
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

        assertEquals("standard", domain.cardNumber?.type)
        assertEquals(LengthConfig(min = 16, max = 16), domain.cardNumber?.length)
        assertEquals("0000 0000 0000 0000", domain.cardNumber?.mask)
    }

    @Test
    fun `toDomain maps security code config`() {
        val response = minimalResponse().copy(
            paymentMethods = listOf(
                paymentMethodResponse().copy(
                    securityCode = SecurityCodeConfig(
                        type = "mandatory",
                        length = 3,
                        mode = "mandatory",
                        cardLocation = "back",
                    ),
                ),
            ),
        )

        val domain = response.toDomain()

        assertEquals("mandatory", domain.securityCode?.mode)
        assertEquals(3, domain.securityCode?.length)
        assertEquals("back", domain.securityCode?.cardLocation)
    }

    @Test
    fun `toDomain maps issuers list`() {
        val response = minimalResponse().copy(
            paymentMethods = listOf(
                paymentMethodResponse().copy(
                    issuers = listOf(IssuerResponse(id = 1234L, name = "VISA", secureThumbnail = "https://img")),
                ),
            ),
        )

        val domain = response.toDomain()

        assertEquals(1, domain.issuers.size)
        assertEquals(1234L, domain.issuers[0].id)
        assertEquals("VISA", domain.issuers[0].name)
        assertEquals("https://img", domain.issuers[0].secureThumbnail)
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
                    ),
                ),
            ),
        )

        val domain = response.toDomain()

        assertEquals(1, domain.payerCosts.size)
        assertEquals(1, domain.payerCosts[0].instalments)
        assertEquals(0, BigDecimal.valueOf(10.0).compareTo(domain.payerCosts[0].installmentAmount))
        assertEquals(0, BigDecimal.valueOf(10.0).compareTo(domain.payerCosts[0].totalAmount))
    }

    @Test
    fun `toDomain maps installments selectionType`() {
        val response = minimalResponse().copy(
            installment = InstallmentConfigResponse(
                selectionType = "radio_button",
                quotas = null,
            ),
        )

        val domain = response.toDomain()

        assertEquals("radio_button", domain.installmentsSelectionType)
    }

    @Test
    fun `toDomain returns null selectionType when installment is null`() {
        val domain = minimalResponse().copy(installment = null).toDomain()

        assertNull(domain.installmentsSelectionType)
    }

    @Test
    fun `toDomain maps field translations`() {
        val response = minimalResponse().copy(
            translations = defaultTranslations(
                cardNumber = defaultFieldTranslations(
                    label = "Número de tarjeta",
                    placeholder = "•••• ••••",
                    errorIncompleteField = "Incompleto",
                    errorInvalidField = "Inválido",
                ),
                holderName = defaultFieldTranslations(
                    label = "Titular",
                    placeholder = "Nome",
                ),
                expirationDate = defaultFieldTranslations(
                    label = "Vencimento",
                    placeholder = "MM/YY",
                ),
            ),
        )

        val domain = response.toDomain()
        val translations = domain.translations!!

        assertEquals("Número de tarjeta", translations.cardNumber.label)
        assertEquals("•••• ••••", translations.cardNumber.placeholder)
        assertEquals("Inválido", translations.cardNumber.errorInvalidField)
        assertEquals("Incompleto", translations.cardNumber.errorIncompleteField)
        assertEquals("Titular", translations.holderName.label)
        assertEquals("Vencimento", translations.expirationDate.label)
    }

    @Test
    fun `toDomain maps security code translation with tooltip`() {
        val response = minimalResponse().copy(
            translations = defaultTranslations(
                securityCode = defaultSecurityCodeTranslations(
                    label = "CVV",
                    placeholder = "123",
                    tooltip = "3 dígitos no verso",
                    errorIncompleteField = "Incompleto",
                ),
            ),
        )

        val domain = response.toDomain()
        val translations = domain.translations!!

        assertEquals("CVV", translations.securityCode.label)
        assertEquals("3 dígitos no verso", translations.securityCode.tooltip)
        assertEquals("Incompleto", translations.securityCode.errorIncompleteField)
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
        assertTrue(domain.payerCosts.isEmpty())
        assertNull(domain.cardNumber)
        assertNull(domain.securityCode)
        assertNull(domain.translations)
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

        assertTrue(domain.payerCosts.isEmpty())
    }

    @Test
    fun `toDomain returns empty quotas when installment quotas is null`() {
        val domain = minimalResponse().copy(
            installment = InstallmentConfigResponse(selectionType = null, quotas = null),
        ).toDomain()

        assertTrue(domain.payerCosts.isEmpty())
    }

    @Test
    fun `toDomain maps security code translation with empty tooltip`() {
        val response = minimalResponse().copy(
            translations = defaultTranslations(
                securityCode = defaultSecurityCodeTranslations(
                    label = "CVV",
                    placeholder = "123",
                    tooltip = "",
                ),
            ),
        )

        val domain = response.toDomain()

        assertEquals("CVV", domain.translations?.securityCode?.label)
        assertEquals("", domain.translations?.securityCode?.tooltip)
    }

    @Test
    fun `toDomain extracts id and paymentTypeId from first payment method`() {
        val response = minimalResponse(id = "master", paymentTypeId = "debit_card")

        val domain = response.toDomain()

        assertEquals("master", domain.id)
        assertEquals("debit_card", domain.paymentTypeId)
    }
}
