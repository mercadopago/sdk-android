package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.DocumentTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.ExpirationDateConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.FieldTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.HolderNameConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.IdentificationType
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsHeaderTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.LengthConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.Translations
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class CardFormInitMapperTest {
    private fun buildTranslations(
        cardFormTitle: String = "Card Payment",
        cardFormFooterButtonLabel: String = "Pay",
        cardNumber: FieldTranslations = FieldTranslations(
            label = "Card number",
            placeholder = "0000 0000 0000 0000",
            errorEmptyField = "Required",
            errorIncompleteField = "Incomplete",
            errorInvalidField = "Invalid",
        ),
        holderName: FieldTranslations = FieldTranslations(
            label = "Cardholder name",
            placeholder = "Name as on card",
            errorEmptyField = "Required",
            errorIncompleteField = "Incomplete",
            errorInvalidField = "Invalid format",
        ),
        expirationDate: FieldTranslations = FieldTranslations(
            label = "Expiration date",
            placeholder = "MM/YY",
            errorEmptyField = "Required",
            errorIncompleteField = "Incomplete",
            errorInvalidField = "Invalid date",
        ),
        securityCode: SecurityCodeTranslations = SecurityCodeTranslations(
            label = "CVV",
            placeholder = "123",
            helper = "3 digits on the back",
            tooltip = "Security code",
            errorEmptyField = "Required",
            errorIncompleteField = "Incomplete",
            errorInvalidField = "Invalid",
        ),
        document: DocumentTranslations = DocumentTranslations(
            label = "Document",
            errorEmptyField = "Required",
            errorIncompleteField = "Incomplete",
            errorInvalidField = "Invalid",
        ),
    ) = Translations(
        cardFormTitle = cardFormTitle,
        cardFormFooterButtonLabel = cardFormFooterButtonLabel,
        cardNumber = cardNumber,
        holderName = holderName,
        expirationDate = expirationDate,
        securityCode = securityCode,
        document = document,
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

    private fun buildResponse(
        identificationTypes: List<IdentificationType> = emptyList(),
        translations: Translations = buildTranslations(),
        cardNumber: CardNumberConfig = CardNumberConfig(
            type = "text",
            length = LengthConfig(min = 13, max = 19),
            mask = "#### #### #### ####",
        ),
        holderName: HolderNameConfig = HolderNameConfig(
            type = "text",
            length = LengthConfig(min = 3, max = 26),
        ),
        expirationDate: ExpirationDateConfig = ExpirationDateConfig(
            type = "text",
            mask = "##/##",
            length = LengthConfig(min = 4, max = 4),
        ),
        securityCode: SecurityCodeConfig = SecurityCodeConfig(
            length = 3,
            type = "text",
        ),
    ) = CardFormInitResponse(
        identificationTypes = identificationTypes,
        cardNumber = cardNumber,
        holderName = holderName,
        expirationDate = expirationDate,
        securityCode = securityCode,
        translations = translations,
    )

    @Test
    fun `given response then title maps from cardFormTitle`() {
        val response = buildResponse(translations = buildTranslations(cardFormTitle = "My Title"))

        val output = response.toDomain()

        assertEquals("My Title", output.title)
    }

    @Test
    fun `given response then button maps from cardFormFooterButtonLabel`() {
        val response = buildResponse(
            translations = buildTranslations(cardFormFooterButtonLabel = "Confirm"),
        )

        val output = response.toDomain()

        assertEquals("Confirm", output.button)
    }

    @Test
    fun `given response then cardNumber label and placeholder are mapped`() {
        val translations = FieldTranslations(
            label = "Número",
            placeholder = "0000",
            errorEmptyField = "req",
            errorIncompleteField = "inc",
            errorInvalidField = "inv",
        )
        val response = buildResponse(translations = buildTranslations(cardNumber = translations))

        val output = response.toDomain()

        assertEquals("Número", output.fields.cardNumber.label)
        assertEquals("0000", output.fields.cardNumber.placeholder)
    }

    @Test
    fun `given response then cardNumber validation errors are mapped`() {
        val translations = FieldTranslations(
            label = "",
            placeholder = "",
            errorEmptyField = "empty",
            errorIncompleteField = "incomplete",
            errorInvalidField = "invalid",
        )
        val response = buildResponse(translations = buildTranslations(cardNumber = translations))

        val output = response.toDomain().fields.cardNumber.validation

        assertEquals("empty", output.errorEmpty)
        assertEquals("incomplete", output.errorIncomplete)
        assertEquals("invalid", output.errorInvalid)
    }

    @Test
    fun `given response then cardNumber config type and length are mapped`() {
        val config = CardNumberConfig(
            type = "number",
            length = LengthConfig(min = 13, max = 19),
            mask = "mask",
        )
        val response = buildResponse(cardNumber = config)

        val output = response.toDomain().fields.cardNumber.config

        assertEquals("number", output.type)
        assertEquals(13, output.length.min)
        assertEquals(19, output.length.max)
    }

    @Test
    fun `given response then holderName label placeholder and validation are mapped`() {
        val translations = FieldTranslations(
            label = "Titular",
            placeholder = "Nome",
            errorEmptyField = "empty",
            errorIncompleteField = "incomplete",
            errorInvalidField = "invalid",
        )
        val response = buildResponse(translations = buildTranslations(holderName = translations))

        val output = response.toDomain().fields.holderName

        assertEquals("Titular", output.label)
        assertEquals("Nome", output.placeholder)
        assertEquals("empty", output.validation.errorEmpty)
        assertEquals("incomplete", output.validation.errorIncomplete)
        assertEquals("invalid", output.validation.errorInvalid)
    }

    @Test
    fun `given response then expirationDate label placeholder and validation are mapped`() {
        val translations = FieldTranslations(
            label = "Validade",
            placeholder = "MM/AA",
            errorEmptyField = "empty",
            errorIncompleteField = "incomplete",
            errorInvalidField = "invalid",
        )
        val response = buildResponse(translations = buildTranslations(expirationDate = translations))

        val output = response.toDomain().fields.expirationDate

        assertEquals("Validade", output.label)
        assertEquals("MM/AA", output.placeholder)
        assertEquals("empty", output.validation.errorEmpty)
        assertEquals("incomplete", output.validation.errorIncomplete)
        assertEquals("invalid", output.validation.errorInvalid)
    }

    @Test
    fun `given response then securityCode label placeholder and validation are mapped`() {
        val translations = SecurityCodeTranslations(
            label = "CVV",
            placeholder = "000",
            helper = "3 digits",
            tooltip = "Where to find",
            errorEmptyField = "empty",
            errorIncompleteField = "incomplete",
            errorInvalidField = "invalid",
        )
        val response = buildResponse(translations = buildTranslations(securityCode = translations))

        val output = response.toDomain().fields.securityCode

        assertEquals("CVV", output.label)
        assertEquals("000", output.placeholder)
        assertEquals("3 digits", output.helper)
        assertEquals("Where to find", output.tooltip)
        assertEquals("empty", output.validation.errorEmpty)
        assertEquals("incomplete", output.validation.errorIncomplete)
        assertEquals("invalid", output.validation.errorInvalid)
    }

    @Test
    fun `given securityCode length then config min and max are both set to that length`() {
        val response = buildResponse(securityCode = SecurityCodeConfig(length = 4, type = "text"))

        val length = response.toDomain().fields.securityCode.config.length

        assertEquals(4, length.min)
        assertEquals(4, length.max)
    }

    @Test
    fun `given securityCode helper is null then helper is mapped to empty string`() {
        val translations = SecurityCodeTranslations(
            label = "",
            placeholder = "",
            helper = null,
            tooltip = "",
            errorEmptyField = "",
            errorIncompleteField = "",
        )
        val response = buildResponse(translations = buildTranslations(securityCode = translations))

        val helper = response.toDomain().fields.securityCode.helper

        assertTrue(helper.isEmpty())
    }

    @Test
    fun `given securityCode errorInvalidField is null then errorInvalid is mapped to empty string`() {
        val translations = SecurityCodeTranslations(
            label = "",
            placeholder = "",
            tooltip = "",
            errorEmptyField = "",
            errorIncompleteField = "",
            errorInvalidField = null,
        )
        val response = buildResponse(translations = buildTranslations(securityCode = translations))

        val errorInvalid = response.toDomain().fields.securityCode.validation.errorInvalid

        assertTrue(errorInvalid.isEmpty())
    }

    @Test
    fun `given response then document label and validation are mapped`() {
        val translations = DocumentTranslations(
            label = "CPF",
            errorEmptyField = "empty",
            errorIncompleteField = "incomplete",
            errorInvalidField = "invalid",
        )
        val response = buildResponse(translations = buildTranslations(document = translations))

        val output = response.toDomain().fields.document

        assertEquals("CPF", output.label)
        assertEquals("empty", output.validation.errorEmpty)
        assertEquals("incomplete", output.validation.errorIncomplete)
        assertEquals("invalid", output.validation.errorInvalid)
    }

    @Test
    fun `given empty identificationTypes then output list is empty`() {
        val response = buildResponse(identificationTypes = emptyList())

        val output = response.toDomain().identificationTypes

        assertTrue(output.isEmpty())
    }

    @Test
    fun `given identificationTypes then each item is mapped correctly`() {
        val idType = IdentificationType(
            id = "CPF",
            name = "CPF",
            minLength = 11,
            maxLength = 11,
            placeholder = "000.000.000-00",
            mask = "###.###.###-##",
            type = "number",
            sequence = "1",
        )
        val response = buildResponse(identificationTypes = listOf(idType))

        val output = response.toDomain().identificationTypes.first()

        assertEquals("CPF", output.id)
        assertEquals("CPF", output.name)
        assertEquals(11, output.minLength)
        assertEquals(11, output.maxLength)
        assertEquals("000.000.000-00", output.placeholder)
        assertEquals("###.###.###-##", output.mask)
        assertEquals("number", output.type)
        assertEquals("1", output.sequence)
    }

    @Test
    fun `given multiple identificationTypes then all items are mapped`() {
        val types = listOf(
            IdentificationType("CPF", "CPF", 11, 11, "", "", "number", "1"),
            IdentificationType("CNPJ", "CNPJ", 14, 14, "", "", "number", "2"),
        )
        val response = buildResponse(identificationTypes = types)

        val output = response.toDomain().identificationTypes

        assertEquals(2, output.size)
        assertEquals("CPF", output[0].id)
        assertEquals("CNPJ", output[1].id)
    }
}
