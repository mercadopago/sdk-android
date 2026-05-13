package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.DocumentTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.FieldTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsHeaderTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.LengthConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.Translations
import com.mercadopago.sdk.android.checkout.domain.model.BinIssuer
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class CardBinStateMapperTest {
    private fun defaultTranslations(
        cardNumber: FieldTranslations = FieldTranslations(
            label = "",
            placeholder = "",
            errorEmptyField = "",
            errorIncompleteField = "",
            errorInvalidField = "",
        ),
        installments: InstallmentsTranslations = InstallmentsTranslations(
            header = InstallmentsHeaderTranslations(chevron = "", radio = "", title = ""),
            interestFreeLabel = "",
            totalLabel = "",
        ),
        cardFormFooterButtonLabel: String = "",
    ) = Translations(
        cardFormTitle = "",
        cardFormFooterButtonLabel = cardFormFooterButtonLabel,
        cardNumber = cardNumber,
        holderName = FieldTranslations(
            label = "",
            placeholder = "",
            errorEmptyField = "",
            errorIncompleteField = "",
            errorInvalidField = "",
        ),
        expirationDate = FieldTranslations(
            label = "",
            placeholder = "",
            errorEmptyField = "",
            errorIncompleteField = "",
            errorInvalidField = "",
        ),
        securityCode = SecurityCodeTranslations(
            label = "",
            placeholder = "",
            tooltip = "",
            errorEmptyField = "",
            errorIncompleteField = "",
        ),
        document = DocumentTranslations(
            label = "",
            errorEmptyField = "",
            errorIncompleteField = "",
            errorInvalidField = "",
        ),
        installments = installments,
    )

    private val baseState = CardPaymentScreenState(
        cardNumberState = CardNumberState(
            label = "Número",
            placeHolder = "0000",
            maxLength = 16,
        ),
        secureCodeState = SecurityCodeState(
            label = "CVV",
            maxLength = 3,
        ),
    )

    private val emptyBinData = CardBinData(
        id = null,
        paymentTypeId = null,
        cardNumber = null,
        securityCode = null,
        issuers = emptyList(),
        quotas = emptyList(),
        installmentsSelectionType = null,
        translations = null,
    )

    @Test
    fun `given cardNumber with length then updates maxLength`() {
        val data = emptyBinData.copy(
            cardNumber = CardNumberConfig(type = "text", length = LengthConfig(min = 13, max = 13), mask = ""),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals(13, result.cardNumberState.maxLength)
    }

    @Test
    fun `given null cardNumber then keeps current maxLength`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertEquals(16, result.cardNumberState.maxLength)
    }

    @Test
    fun `given issuers with thumbnail then sets image from first issuer`() {
        val data = emptyBinData.copy(
            issuers = listOf(
                BinIssuer(id = 1L, name = "Visa", secureThumbnail = "https://img.visa.com"),
                BinIssuer(id = 2L, name = "MC", secureThumbnail = "https://img.mc.com"),
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("https://img.visa.com", result.cardNumberState.image)
    }

    @Test
    fun `given empty issuers then image is null`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertNull(result.cardNumberState.image)
    }

    @Test
    fun `given translations with non-empty cardNumber label then updates label`() {
        val data = emptyBinData.copy(
            translations = defaultTranslations(
                cardNumber = FieldTranslations(
                    label = "Card number",
                    placeholder = "#### #### #### ####",
                    errorEmptyField = "",
                    errorIncompleteField = "",
                    errorInvalidField = "",
                ),
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("Card number", result.cardNumberState.label)
        assertEquals("#### #### #### ####", result.cardNumberState.placeHolder)
    }

    @Test
    fun `given null translations then keeps current cardNumber label`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertEquals("Número", result.cardNumberState.label)
        assertEquals("0000", result.cardNumberState.placeHolder)
    }

    @Test
    fun `given securityCode with length then updates secureCode maxLength`() {
        val data = emptyBinData.copy(
            securityCode = SecurityCodeConfig(type = "text", length = 4, mode = "mandatory", cardLocation = ""),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals(4, result.secureCodeState.maxLength)
    }

    @Test
    fun `given securityCode mode optional then optional is true`() {
        val data = emptyBinData.copy(
            securityCode = SecurityCodeConfig(type = "text", length = 3, mode = "optional", cardLocation = ""),
        )

        val result = baseState.applyCardBinData(data)

        assertTrue(result.secureCodeState.optional)
    }

    @Test
    fun `given securityCode mode mandatory then optional is false`() {
        val data = emptyBinData.copy(
            securityCode = SecurityCodeConfig(type = "text", length = 3, mode = "mandatory", cardLocation = ""),
        )

        val result = baseState.applyCardBinData(data)

        assertFalse(result.secureCodeState.optional)
    }

    @Test
    fun `given null securityCode then keeps current maxLength`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertEquals(3, result.secureCodeState.maxLength)
    }

    @Test
    fun `given issuers then maps to CardIssuer list with id and thumbnail`() {
        val data = emptyBinData.copy(
            issuers = listOf(
                BinIssuer(id = 42L, name = "Visa", secureThumbnail = "https://img.com/42"),
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals(1, result.cardIssuers.size)
        assertEquals("42", result.cardIssuers.first().id)
        assertEquals("https://img.com/42", result.cardIssuers.first().thumbnail)
    }

    @Test
    fun `given empty issuers then cardIssuers is empty`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertTrue(result.cardIssuers.isEmpty())
    }

    @Test
    fun `given non-empty payerCosts then showList is true`() {
        val data = emptyBinData.copy(
            quotas = listOf(
                Quota(
                    installments = 1,
                    installmentAmount = BigDecimal.valueOf(100.0),
                    totalAmount = BigDecimal.valueOf(100.0),
                ),
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertTrue(result.installmentsState.showList)
    }

    @Test
    fun `given empty payerCosts then showList is false`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertFalse(result.installmentsState.showList)
    }

    @Test
    fun `given payerCosts then exposes them on installments state`() {
        val data = emptyBinData.copy(
            quotas = listOf(
                Quota(
                    installments = 3,
                    installmentAmount = BigDecimal.valueOf(33.33),
                    totalAmount = BigDecimal.valueOf(100.0),
                ),
            ),
        )

        val result = baseState.applyCardBinData(data)

        val quota = result.installmentsState.installments.first()
        assertEquals(3, quota.installments)
        assertEquals(0, BigDecimal.valueOf(33.33).compareTo(quota.installmentAmount))
        assertEquals(0, BigDecimal.valueOf(100.0).compareTo(quota.totalAmount))
    }

    @Test
    fun `given binData with id and paymentTypeId then updates paymentState`() {
        val data = emptyBinData.copy(id = "visa", paymentTypeId = "credit_card")

        val result = baseState.applyCardBinData(data)

        assertEquals("visa", result.paymentState.paymentMethodId)
        assertEquals("credit_card", result.paymentState.paymentTypeId)
    }

    @Test
    fun `given selectionType radio_button then displayType is RadioButton`() {
        val data = emptyBinData.copy(installmentsSelectionType = "radio_button")

        val result = baseState.applyCardBinData(data)

        assertEquals(InstallmentsDisplayType.RadioButton, result.installmentsState.displayType)
    }

    @Test
    fun `given selectionType chevron then displayType is Chevron`() {
        val data = emptyBinData.copy(installmentsSelectionType = "chevron")

        val result = baseState.applyCardBinData(data)

        assertEquals(InstallmentsDisplayType.Chevron, result.installmentsState.displayType)
    }

    @Test
    fun `given selectionType chevron with uppercase then displayType is Chevron`() {
        val data = emptyBinData.copy(installmentsSelectionType = "CHEVRON")

        val result = baseState.applyCardBinData(data)

        assertEquals(InstallmentsDisplayType.Chevron, result.installmentsState.displayType)
    }

    @Test
    fun `given null selectionType then displayType defaults to RadioButton`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertEquals(InstallmentsDisplayType.RadioButton, result.installmentsState.displayType)
    }

    @Test
    fun `given unknown selectionType then displayType defaults to RadioButton`() {
        val data = emptyBinData.copy(installmentsSelectionType = "something_else")

        val result = baseState.applyCardBinData(data)

        assertEquals(InstallmentsDisplayType.RadioButton, result.installmentsState.displayType)
    }

    @Test
    fun `given installments translations then updates installments labels`() {
        val data = emptyBinData.copy(
            translations = defaultTranslations(
                installments = InstallmentsTranslations(
                    header = InstallmentsHeaderTranslations(
                        chevron = "Elegí las cuotas",
                        radio = "Elegí el plan",
                        title = "Cuotas",
                    ),
                    interestFreeLabel = "Sin interés",
                    totalLabel = "Total",
                ),
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("Sin interés", result.installmentsState.interestFreeLabel)
        assertEquals("Total", result.installmentsState.totalLabel)
    }

    @Test
    fun `given cardFormFooterButtonLabel then updates payButtonLabel`() {
        val data = emptyBinData.copy(
            translations = defaultTranslations(cardFormFooterButtonLabel = "Pagar"),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("Pagar", result.installmentsState.payButtonLabel)
    }
}
