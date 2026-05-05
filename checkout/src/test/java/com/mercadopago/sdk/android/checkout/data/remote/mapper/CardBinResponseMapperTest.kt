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
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class CardBinResponseMapperTest {
    private fun defaultFieldTranslations() = FieldTranslations(
        label = "",
        placeholder = "",
        errorEmptyField = "",
        errorIncompleteField = "",
        errorInvalidField = "",
    )

    private fun defaultSecurityCodeTranslations() = SecurityCodeTranslations(
        label = "",
        placeholder = "",
        tooltip = "",
        errorEmptyField = "",
        errorIncompleteField = "",
    )

    private fun defaultTranslations(
        cardNumber: FieldTranslations = defaultFieldTranslations(),
        holderName: FieldTranslations = defaultFieldTranslations(),
        expirationDate: FieldTranslations = defaultFieldTranslations(),
        securityCode: SecurityCodeTranslations = defaultSecurityCodeTranslations(),
    ) = Translations(
        cardFormTitle = "",
        cardFormFooterButtonLabel = "",
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
            header = InstallmentsHeaderTranslations(
                chevron = "",
                radio = "",
                title = "",
            ),
            interestFreeLabel = "",
            totalLabel = "",
        ),
    )

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

    // ── card number ───────────────────────────────────────────────────────────

    @Test
    fun `toDomain maps card number config`() {
        val response = minimalResponse().copy(
            paymentMethods = listOf(
                paymentMethodResponse().copy(
                    cardNumber = CardNumberConfig(
                        type = "Number",
                        length = LengthConfig(min = 13, max = 16),
                        mask = "#### #### #### ####",
                    ),
                ),
            ),
        )

        val domain = response.toDomain()

        assertEquals("Number", domain.cardNumber?.type)
        assertEquals(13, domain.cardNumber?.length?.min)
        assertEquals(16, domain.cardNumber?.length?.max)
        assertEquals("#### #### #### ####", domain.cardNumber?.mask)
    }

    @Test
    fun `toDomain returns null cardNumber when not present`() {
        val domain = minimalResponse().toDomain()

        assertNull(domain.cardNumber)
    }

    // ── security code ─────────────────────────────────────────────────────────

    @Test
    fun `toDomain maps security code config`() {
        val response = minimalResponse().copy(
            paymentMethods = listOf(
                paymentMethodResponse().copy(
                    securityCode = SecurityCodeConfig(
                        type = "Number",
                        length = 3,
                        mode = "mandatory",
                        cardLocation = "back",
                        tooltip = "3 dígitos no verso",
                        placeholder = "123",
                    ),
                ),
            ),
        )

        val domain = response.toDomain()

        assertEquals("Number", domain.securityCode?.type)
        assertEquals(3, domain.securityCode?.length)
        assertEquals("mandatory", domain.securityCode?.mode)
        assertEquals("back", domain.securityCode?.cardLocation)
        assertEquals("3 dígitos no verso", domain.securityCode?.tooltip)
        assertEquals("123", domain.securityCode?.placeholder)
    }

    @Test
    fun `toDomain maps security code mode optional`() {
        val response = minimalResponse().copy(
            paymentMethods = listOf(
                paymentMethodResponse().copy(
                    securityCode = SecurityCodeConfig(type = "Number", length = 0, mode = "optional"),
                ),
            ),
        )

        val domain = response.toDomain()

        assertEquals("optional", domain.securityCode?.mode)
    }

    @Test
    fun `toDomain returns null securityCode when not present`() {
        val domain = minimalResponse().toDomain()

        assertNull(domain.securityCode)
    }

    // ── issuers ───────────────────────────────────────────────────────────────

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
    fun `toDomain returns empty issuers when payment method issuers is null`() {
        val domain = minimalResponse().toDomain()

        assertTrue(domain.issuers.isEmpty())
    }

    // ── quotas ────────────────────────────────────────────────────────────────

    @Test
    fun `toDomain maps quotas`() {
        val response = minimalResponse().copy(
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
        )

        val domain = response.toDomain()

        assertEquals(1, domain.quotas.size)
        assertEquals(1, domain.quotas[0].quantity)
        assertEquals("10.00", domain.quotas[0].installmentAmount)
        assertEquals("10.00", domain.quotas[0].totalAmount)
        assertEquals("1 cuota", domain.quotas[0].label)
        assertEquals(0.0, domain.quotas[0].discountRate)
    }

    @Test
    fun `toDomain returns empty quotas when installment is null`() {
        val domain = minimalResponse().copy(installment = null).toDomain()

        assertTrue(domain.quotas.isEmpty())
    }

    @Test
    fun `toDomain returns empty quotas when installment quotas is null`() {
        val domain = minimalResponse().copy(
            installment = InstallmentConfigResponse(quotas = null),
        ).toDomain()

        assertTrue(domain.quotas.isEmpty())
    }

    // ── translations ──────────────────────────────────────────────────────────

    @Test
    fun `toDomain passes translations through directly`() {
        val translations = defaultTranslations(
            cardNumber = FieldTranslations(
                label = "Número de tarjeta",
                placeholder = "•••• ••••",
                errorEmptyField = "",
                errorIncompleteField = "Incompleto",
                errorInvalidField = "Inválido",
            ),
            holderName = FieldTranslations(
                label = "Titular",
                placeholder = "Nome",
                errorEmptyField = "",
                errorIncompleteField = "",
                errorInvalidField = "",
            ),
            expirationDate = FieldTranslations(
                label = "Vencimento",
                placeholder = "MM/YY",
                errorEmptyField = "",
                errorIncompleteField = "",
                errorInvalidField = "",
            ),
            securityCode = SecurityCodeTranslations(
                label = "CVV",
                placeholder = "123",
                tooltip = "3 dígitos no verso",
                errorEmptyField = "",
                errorIncompleteField = "Incompleto",
            ),
        )
        val response = minimalResponse().copy(translations = translations)

        val domain = response.toDomain()

        assertEquals(translations, domain.translations)
    }

    @Test
    fun `toDomain returns null translations when not present`() {
        val domain = minimalResponse().toDomain()

        assertNull(domain.translations)
    }

    // ── identity ──────────────────────────────────────────────────────────────

    @Test
    fun `toDomain extracts id and paymentTypeId from first payment method`() {
        val response = minimalResponse(id = "master", paymentTypeId = "debit_card")

        val domain = response.toDomain()

        assertEquals("master", domain.id)
        assertEquals("debit_card", domain.paymentTypeId)
    }

    @Test
    fun `toDomain returns null fields when paymentMethods is null`() {
        val response = CardBinResponse(paymentMethods = null, installment = null, translations = null)

        val domain = response.toDomain()

        assertNull(domain.id)
        assertNull(domain.paymentTypeId)
        assertNull(domain.cardNumber)
        assertNull(domain.securityCode)
        assertTrue(domain.issuers.isEmpty())
        assertTrue(domain.quotas.isEmpty())
        assertNull(domain.translations)
    }
}
