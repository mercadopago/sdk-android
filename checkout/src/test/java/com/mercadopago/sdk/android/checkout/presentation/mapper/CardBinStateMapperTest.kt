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
import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class CardBinStateMapperTest {
    private val baseState = CardPaymentScreenState(
        cardNumberState = CardNumberState(
            label = "Número",
            placeHolder = "0000",
            helper = "Digite o número",
            maxLength = 16,
        ),
        secureCodeState = SecurityCodeState(
            label = "CVV",
            placeHolder = "000",
            messageTooltip = "3 dígitos",
            maxLength = 3,
        ),
        cardHolderState = CardHolderState(
            label = "Titular",
            placeHolder = "Nome no cartão",
        ),
        expirationDateState = ExpirationDateState(
            label = "Validade",
            placeHolder = "MM/AA",
        ),
    )

    private val emptyBinData = CardBinData(
        id = null,
        paymentTypeId = null,
        cardNumber = null,
        securityCode = null,
        issuers = emptyList(),
        quotas = emptyList(),
        translations = null,
    )

    private fun fieldTranslations(
        label: String = "",
        placeholder: String = "",
        helper: String? = null,
    ) = FieldTranslations(
        label = label,
        placeholder = placeholder,
        helper = helper,
        errorEmptyField = "",
        errorIncompleteField = "",
        errorInvalidField = "",
    )

    private fun securityCodeTranslations(
        label: String = "",
        placeholder: String = "",
        tooltip: String = "",
        helper: String? = null,
    ) = SecurityCodeTranslations(
        label = label,
        placeholder = placeholder,
        helper = helper,
        tooltip = tooltip,
        errorEmptyField = "",
        errorIncompleteField = "",
    )

    private fun defaultTranslations(
        cardNumber: FieldTranslations = fieldTranslations(),
        holderName: FieldTranslations = fieldTranslations(),
        expirationDate: FieldTranslations = fieldTranslations(),
        securityCode: SecurityCodeTranslations = securityCodeTranslations(),
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

    @Test
    fun `given cardNumber with length then updates maxLength`() {
        val data = emptyBinData.copy(
            cardNumber = CardNumberConfig(type = "Number", length = LengthConfig(min = 13, max = 13), mask = ""),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals(13, result.cardNumberState.maxLength)
    }

    @Test
    fun `given cardNumber with length then updates mask`() {
        val data = emptyBinData.copy(
            cardNumber = CardNumberConfig(type = "Number", length = LengthConfig(min = 16, max = 16), mask = ""),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("#### #### #### ####", result.cardNumberState.mask)
    }

    @Test
    fun `given null cardNumber then keeps current maxLength`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertEquals(16, result.cardNumberState.maxLength)
    }

    @Test
    fun `given translations with cardNumber label then updates label and placeholder`() {
        val data = emptyBinData.copy(
            translations = defaultTranslations(
                cardNumber = fieldTranslations(label = "Card number", placeholder = "#### #### #### ####"),
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("Card number", result.cardNumberState.label)
        assertEquals("#### #### #### ####", result.cardNumberState.placeHolder)
    }

    @Test
    fun `given translations with cardNumber helper then updates helper`() {
        val data = emptyBinData.copy(
            translations = defaultTranslations(
                cardNumber = fieldTranslations(helper = "16 dígitos"),
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("16 dígitos", result.cardNumberState.helper)
    }

    @Test
    fun `given empty cardNumber label in translations then keeps current label`() {
        val data = emptyBinData.copy(
            translations = defaultTranslations(cardNumber = fieldTranslations(label = "")),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("Número", result.cardNumberState.label)
    }

    @Test
    fun `given null translations then keeps current cardNumber label and placeholder`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertEquals("Número", result.cardNumberState.label)
        assertEquals("0000", result.cardNumberState.placeHolder)
    }

    @Test
    fun `given issuers then sets image from first issuer`() {
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
    fun `given securityCode with length then updates maxLength`() {
        val data = emptyBinData.copy(
            securityCode = SecurityCodeConfig(type = "Number", length = 4, mode = "mandatory"),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals(4, result.secureCodeState.maxLength)
    }

    @Test
    fun `given null securityCode then keeps current maxLength`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertEquals(3, result.secureCodeState.maxLength)
    }

    @Test
    fun `given securityCode mode optional then optional is true`() {
        val data = emptyBinData.copy(
            securityCode = SecurityCodeConfig(type = "Number", length = 3, mode = "optional"),
        )

        val result = baseState.applyCardBinData(data)

        assertTrue(result.secureCodeState.optional)
    }

    @Test
    fun `given securityCode mode mandatory then optional is false`() {
        val data = emptyBinData.copy(
            securityCode = SecurityCodeConfig(type = "Number", length = 3, mode = "mandatory"),
        )

        val result = baseState.applyCardBinData(data)

        assertFalse(result.secureCodeState.optional)
    }

    @Test
    fun `given securityCode placeholder then updates placeHolder`() {
        val data = emptyBinData.copy(
            securityCode = SecurityCodeConfig(type = "Number", length = 3, placeholder = "•••"),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("•••", result.secureCodeState.placeHolder)
    }

    @Test
    fun `given securityCode tooltip then updates messageTooltip`() {
        val data = emptyBinData.copy(
            securityCode = SecurityCodeConfig(type = "Number", length = 3, tooltip = "Verso do cartão"),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("Verso do cartão", result.secureCodeState.messageTooltip)
    }

    @Test
    fun `given translations with securityCode label then updates label`() {
        val data = emptyBinData.copy(
            translations = defaultTranslations(
                securityCode = securityCodeTranslations(label = "Código de segurança"),
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("Código de segurança", result.secureCodeState.label)
    }

    @Test
    fun `given securityCode placeholder overrides translations placeholder`() {
        val data = emptyBinData.copy(
            securityCode = SecurityCodeConfig(type = "Number", length = 3, placeholder = "from config"),
            translations = defaultTranslations(
                securityCode = securityCodeTranslations(placeholder = "from translations"),
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("from config", result.secureCodeState.placeHolder)
    }

    @Test
    fun `given securityCode tooltip overrides translations tooltip`() {
        val data = emptyBinData.copy(
            securityCode = SecurityCodeConfig(type = "Number", length = 3, tooltip = "from config"),
            translations = defaultTranslations(
                securityCode = securityCodeTranslations(tooltip = "from translations"),
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("from config", result.secureCodeState.messageTooltip)
    }

    @Test
    fun `given translations with holderName then updates cardHolderState`() {
        val data = emptyBinData.copy(
            translations = defaultTranslations(
                holderName = fieldTranslations(label = "Nome no cartão", placeholder = "Ex. Maria Silva"),
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("Nome no cartão", result.cardHolderState.label)
        assertEquals("Ex. Maria Silva", result.cardHolderState.placeHolder)
    }

    @Test
    fun `given empty holderName label in translations then keeps current label`() {
        val data = emptyBinData.copy(
            translations = defaultTranslations(holderName = fieldTranslations(label = "")),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("Titular", result.cardHolderState.label)
    }

    @Test
    fun `given null translations then keeps current cardHolderState`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertEquals("Titular", result.cardHolderState.label)
        assertEquals("Nome no cartão", result.cardHolderState.placeHolder)
    }

    @Test
    fun `given translations with expirationDate then updates expirationDateState`() {
        val data = emptyBinData.copy(
            translations = defaultTranslations(
                expirationDate = fieldTranslations(label = "Data de validade", placeholder = "MM/AA"),
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("Data de validade", result.expirationDateState.label)
        assertEquals("MM/AA", result.expirationDateState.placeHolder)
    }

    @Test
    fun `given null translations then keeps current expirationDateState`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertEquals("Validade", result.expirationDateState.label)
        assertEquals("MM/AA", result.expirationDateState.placeHolder)
    }

    @Test
    fun `given issuers then maps to CardIssuer list`() {
        val data = emptyBinData.copy(
            issuers = listOf(BinIssuer(id = 42L, name = "Visa", secureThumbnail = "https://img.com/42")),
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
    fun `given non-empty quotas then showList is true`() {
        val data = emptyBinData.copy(
            quotas = listOf(
                Quota(
                    quantity = 1,
                    installmentAmount = "100.00",
                    totalAmount = "100.00",
                    label = null,
                    discountRate = null,
                ),
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertTrue(result.installmentsState.showList)
    }

    @Test
    fun `given empty quotas then showList is false`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertFalse(result.installmentsState.showList)
    }

    @Test
    fun `given quotas then maps to PayerCost list`() {
        val data = emptyBinData.copy(
            quotas = listOf(
                Quota(
                    quantity = 3,
                    installmentAmount = "33.33",
                    totalAmount = "100.00",
                    label = "3x",
                    discountRate = 0.0,
                ),
            ),
        )

        val result = baseState.applyCardBinData(data)

        val payerCost = result.installmentsState.installments.first()
        assertEquals(3, payerCost.instalments)
        assertEquals(33.33f, payerCost.installmentAmount)
        assertEquals(100.00f, payerCost.totalAmount)
        assertEquals(listOf("3x"), payerCost.labels)
    }

    @Test
    fun `given binData with id and paymentTypeId then updates paymentState`() {
        val data = emptyBinData.copy(id = "visa", paymentTypeId = "credit_card")

        val result = baseState.applyCardBinData(data)

        assertEquals("visa", result.paymentState.paymentMethodId)
        assertEquals("credit_card", result.paymentState.paymentTypeId)
    }
}
