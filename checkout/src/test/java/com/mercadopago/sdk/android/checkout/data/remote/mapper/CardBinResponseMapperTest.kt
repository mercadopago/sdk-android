package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberConfigResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.FieldErrorTranslationResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.FieldTranslationResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentConfigResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsSelectorResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsTranslationResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.IssuerResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.QuotaResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeConfigResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeTranslationResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.TranslationsResponse
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class CardBinResponseMapperTest {

    @Test
    fun `toDomain maps all fields from full response`() {
        val response = CardBinResponse(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = CardNumberConfigResponse(length = 16, validation = "standard", mask = null),
            securityCode = SecurityCodeConfigResponse(mode = "mandatory", length = 3, cardLocation = "back"),
            issuers = listOf(IssuerResponse(id = 1234L, name = "VISA", secureThumbnail = "https://img")),
            installment = InstallmentConfigResponse(
                quotas = listOf(
                    QuotaResponse(
                        quantity = 1,
                        installmentAmount = "10.00",
                        totalAmount = "10.00",
                        label = "1 cuota",
                        discountRate = 0.0,
                    ),
                ),
            ),
            translations = TranslationsResponse(
                cardNumber = FieldTranslationResponse(
                    label = "Número de tarjeta",
                    placeholder = "•••• ••••",
                    helper = "",
                    error = FieldErrorTranslationResponse(invalid = "Inválido", incomplete = "Incompleto"),
                ),
                cardHolderName = FieldTranslationResponse(label = "Titular", placeholder = "Nome", helper = "", error = null),
                expirationDate = FieldTranslationResponse(label = "Vencimento", placeholder = "MM/YY", helper = "", error = null),
                securityCode = SecurityCodeTranslationResponse(
                    label = "CVV",
                    placeholder = "123",
                    helper = "",
                    tooltip = "3 dígitos no verso",
                    error = FieldErrorTranslationResponse(invalid = "Inválido", incomplete = "Incompleto"),
                ),
                identification = FieldTranslationResponse(label = "CPF", placeholder = "000.000.000-00", helper = "", error = null),
                installments = InstallmentsTranslationResponse(
                    label = "Parcelas",
                    installmentsSelector = InstallmentsSelectorResponse(placeholder = "Selecione"),
                ),
            ),
        )

        val domain = response.toDomain()

        assertEquals("visa", domain.id)
        assertEquals("credit_card", domain.paymentTypeId)

        assertEquals(16, domain.cardNumber?.length)
        assertEquals("standard", domain.cardNumber?.validation)
        assertNull(domain.cardNumber?.mask)

        assertEquals("mandatory", domain.securityCode?.mode)
        assertEquals(3, domain.securityCode?.length)
        assertEquals("back", domain.securityCode?.cardLocation)

        assertEquals(1, domain.issuers.size)
        assertEquals(1234L, domain.issuers[0].id)
        assertEquals("VISA", domain.issuers[0].name)
        assertEquals("https://img", domain.issuers[0].secureThumbnail)

        assertEquals(1, domain.quotas.size)
        assertEquals(1, domain.quotas[0].quantity)
        assertEquals("10.00", domain.quotas[0].installmentAmount)
        assertEquals("10.00", domain.quotas[0].totalAmount)
        assertEquals("1 cuota", domain.quotas[0].label)
        assertEquals(0.0, domain.quotas[0].discountRate)

        val translations = domain.translations!!
        assertEquals("Número de tarjeta", translations.cardNumber?.label)
        assertEquals("•••• ••••", translations.cardNumber?.placeholder)
        assertEquals("Inválido", translations.cardNumber?.error?.invalid)
        assertEquals("Incompleto", translations.cardNumber?.error?.incomplete)

        assertEquals("Titular", translations.cardHolderName?.label)
        assertEquals("Vencimento", translations.expirationDate?.label)

        assertEquals("CVV", translations.securityCode?.label)
        assertEquals("3 dígitos no verso", translations.securityCode?.tooltip)
        assertEquals("Inválido", translations.securityCode?.error?.invalid)

        assertEquals("CPF", translations.identification?.label)

        assertEquals("Parcelas", translations.installments?.label)
        assertEquals("Selecione", translations.installments?.installmentsSelectorPlaceholder)
    }

    @Test
    fun `toDomain returns empty issuers list when issuers is null`() {
        val response = CardBinResponse(
            id = "visa",
            paymentTypeId = null,
            cardNumber = null,
            securityCode = null,
            issuers = null,
            installment = null,
            translations = null,
        )

        val domain = response.toDomain()

        assertTrue(domain.issuers.isEmpty())
        assertTrue(domain.quotas.isEmpty())
        assertNull(domain.cardNumber)
        assertNull(domain.securityCode)
        assertNull(domain.translations)
    }

    @Test
    fun `toDomain returns empty quotas when installment is null`() {
        val response = CardBinResponse(
            id = "master",
            paymentTypeId = "debit_card",
            cardNumber = null,
            securityCode = null,
            issuers = emptyList(),
            installment = null,
            translations = null,
        )

        val domain = response.toDomain()

        assertTrue(domain.quotas.isEmpty())
    }

    @Test
    fun `toDomain returns empty quotas when installment quotas is null`() {
        val response = CardBinResponse(
            id = "master",
            paymentTypeId = "debit_card",
            cardNumber = null,
            securityCode = null,
            issuers = emptyList(),
            installment = InstallmentConfigResponse(quotas = null),
            translations = null,
        )

        val domain = response.toDomain()

        assertTrue(domain.quotas.isEmpty())
    }

    @Test
    fun `toDomain maps installments selector placeholder from nested object`() {
        val response = CardBinResponse(
            id = "visa",
            paymentTypeId = null,
            cardNumber = null,
            securityCode = null,
            issuers = null,
            installment = null,
            translations = TranslationsResponse(
                cardNumber = null,
                cardHolderName = null,
                expirationDate = null,
                securityCode = null,
                identification = null,
                installments = InstallmentsTranslationResponse(
                    label = "Cuotas",
                    installmentsSelector = InstallmentsSelectorResponse(placeholder = "Selecione as parcelas"),
                ),
            ),
        )

        val domain = response.toDomain()

        assertEquals("Cuotas", domain.translations?.installments?.label)
        assertEquals("Selecione as parcelas", domain.translations?.installments?.installmentsSelectorPlaceholder)
    }

    @Test
    fun `toDomain maps null installments selector placeholder when selector is null`() {
        val response = CardBinResponse(
            id = "visa",
            paymentTypeId = null,
            cardNumber = null,
            securityCode = null,
            issuers = null,
            installment = null,
            translations = TranslationsResponse(
                cardNumber = null,
                cardHolderName = null,
                expirationDate = null,
                securityCode = null,
                identification = null,
                installments = InstallmentsTranslationResponse(label = "Cuotas", installmentsSelector = null),
            ),
        )

        val domain = response.toDomain()

        assertEquals("Cuotas", domain.translations?.installments?.label)
        assertNull(domain.translations?.installments?.installmentsSelectorPlaceholder)
    }

    @Test
    fun `toDomain maps security code translation with null error`() {
        val response = CardBinResponse(
            id = "visa",
            paymentTypeId = null,
            cardNumber = null,
            securityCode = null,
            issuers = null,
            installment = null,
            translations = TranslationsResponse(
                cardNumber = null,
                cardHolderName = null,
                expirationDate = null,
                securityCode = SecurityCodeTranslationResponse(
                    label = "CVV",
                    placeholder = "123",
                    helper = "",
                    tooltip = null,
                    error = null,
                ),
                identification = null,
                installments = null,
            ),
        )

        val domain = response.toDomain()

        assertEquals("CVV", domain.translations?.securityCode?.label)
        assertNull(domain.translations?.securityCode?.tooltip)
        assertNull(domain.translations?.securityCode?.error)
    }
}
