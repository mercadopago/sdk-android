package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.DocumentTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.ExpirationDateConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.ExpirationDateTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.HolderNameConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.HolderNameTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.IdentificationType
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsHeaderTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.LengthConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.Translations
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CardFormInitMapperTest {
    private val cardNumberTranslations = CardNumberTranslations(
        label = "Número do cartão",
        placeholder = "0000 0000 0000 0000",
        errorEmptyField = "Campo obrigatório",
        errorIncompleteField = "Número incompleto",
        errorInvalidField = "Número inválido",
    )

    private val holderNameTranslations = HolderNameTranslations(
        label = "Nome no cartão",
        placeholder = "Como aparece no cartão",
        errorEmptyField = "Campo obrigatório",
        errorIncompleteField = "Nome incompleto",
        errorInvalidField = "Nome inválido",
    )

    private val expirationDateTranslations = ExpirationDateTranslations(
        label = "Validade",
        placeholder = "MM/AA",
        errorEmptyField = "Campo obrigatório",
        errorIncompleteField = "Data incompleta",
        errorInvalidField = "Data inválida",
    )

    private val securityCodeTranslations = SecurityCodeTranslations(
        label = "Código de segurança",
        placeholder = "000",
        helper = "3 dígitos no verso",
        tooltip = "Código CVV",
        errorEmptyField = "Campo obrigatório",
        errorIncompleteField = "Código incompleto",
        errorInvalidField = "Código inválido",
    )

    private val documentTranslations = DocumentTranslations(
        label = "CPF",
        errorEmptyField = "Campo obrigatório",
        errorIncompleteField = "CPF incompleto",
        errorInvalidField = "CPF inválido",
    )

    private val translations = Translations(
        cardFormTitle = "Adicionar cartão",
        cardFormFooterButtonLabel = "Continuar",
        cardNumber = cardNumberTranslations,
        holderName = holderNameTranslations,
        expirationDate = expirationDateTranslations,
        securityCode = securityCodeTranslations,
        document = documentTranslations,
        installments = InstallmentsTranslations(
            header = InstallmentsHeaderTranslations(chevron = "", radio = "", title = "Parcelas"),
            interestFreeLabel = "Sem juros",
            totalLabel = "Total",
        ),
    )

    private val cardNumberConfig = CardNumberConfig(
        type = "number",
        length = LengthConfig(min = 13, max = 19),
        mask = "#### #### #### ####",
    )

    private val holderNameConfig = HolderNameConfig(
        type = "text",
        length = LengthConfig(min = 2, max = 26),
    )

    private val expirationDateConfig = ExpirationDateConfig(
        type = "date",
        mask = "##/##",
        length = LengthConfig(min = 4, max = 4),
    )

    private val securityCodeConfig = SecurityCodeConfig(
        length = 3,
        type = "back_field",
    )

    private val identificationType = IdentificationType(
        id = "CPF",
        name = "CPF",
        minLength = 11,
        maxLength = 11,
        placeholder = "000.000.000-00",
        mask = "###.###.###-##",
        type = "number",
        sequence = "1",
    )

    private fun buildResponse(
        identificationTypes: List<IdentificationType> = listOf(identificationType),
    ) = CardFormInitResponse(
        identificationTypes = identificationTypes,
        cardNumber = cardNumberConfig,
        securityCode = securityCodeConfig,
        holderName = holderNameConfig,
        expirationDate = expirationDateConfig,
        translations = translations,
    )

    @Test
    fun `given response then maps title from translations`() {
        val result = buildResponse().toDomain()

        assertEquals("Adicionar cartão", result.title)
    }

    @Test
    fun `given response then maps button from translations`() {
        val result = buildResponse().toDomain()

        assertEquals("Continuar", result.button)
    }

    @Test
    fun `given response then maps cardNumber label and placeholder`() {
        val result = buildResponse().toDomain()

        assertEquals("Número do cartão", result.fields.cardNumber.label)
        assertEquals("0000 0000 0000 0000", result.fields.cardNumber.placeholder)
    }

    @Test
    fun `given response then maps cardNumber validation errors`() {
        val result = buildResponse().toDomain()

        with(result.fields.cardNumber.validation) {
            assertEquals("Campo obrigatório", errorEmpty)
            assertEquals("Número incompleto", errorIncomplete)
            assertEquals("Número inválido", errorInvalid)
        }
    }

    @Test
    fun `given response then maps cardNumber config type and length`() {
        val result = buildResponse().toDomain()

        with(result.fields.cardNumber.config) {
            assertEquals("number", type)
            assertEquals(13, length.min)
            assertEquals(19, length.max)
        }
    }

    @Test
    fun `given response then maps holderName label and placeholder`() {
        val result = buildResponse().toDomain()

        assertEquals("Nome no cartão", result.fields.holderName.label)
        assertEquals("Como aparece no cartão", result.fields.holderName.placeholder)
    }

    @Test
    fun `given response then maps holderName validation errors`() {
        val result = buildResponse().toDomain()

        with(result.fields.holderName.validation) {
            assertEquals("Campo obrigatório", errorEmpty)
            assertEquals("Nome incompleto", errorIncomplete)
            assertEquals("Nome inválido", errorInvalid)
        }
    }

    @Test
    fun `given response then maps holderName config type and length`() {
        val result = buildResponse().toDomain()

        with(result.fields.holderName.config) {
            assertEquals("text", type)
            assertEquals(2, length.min)
            assertEquals(26, length.max)
        }
    }

    @Test
    fun `given response then maps expirationDate label and placeholder`() {
        val result = buildResponse().toDomain()

        assertEquals("Validade", result.fields.expirationDate.label)
        assertEquals("MM/AA", result.fields.expirationDate.placeholder)
    }

    @Test
    fun `given response then maps expirationDate validation errors`() {
        val result = buildResponse().toDomain()

        with(result.fields.expirationDate.validation) {
            assertEquals("Campo obrigatório", errorEmpty)
            assertEquals("Data incompleta", errorIncomplete)
            assertEquals("Data inválida", errorInvalid)
        }
    }

    @Test
    fun `given response then maps securityCode label placeholder helper and tooltip`() {
        val result = buildResponse().toDomain()

        with(result.fields.securityCode) {
            assertEquals("Código de segurança", label)
            assertEquals("000", placeholder)
            assertEquals("3 dígitos no verso", helper)
            assertEquals("Código CVV", tooltip)
        }
    }

    @Test
    fun `given response then maps securityCode validation errors`() {
        val result = buildResponse().toDomain()

        with(result.fields.securityCode.validation) {
            assertEquals("Campo obrigatório", errorEmpty)
            assertEquals("Código incompleto", errorIncomplete)
            assertEquals("Código inválido", errorInvalid)
        }
    }

    @Test
    fun `given response then securityCode config length has same min and max`() {
        val result = buildResponse().toDomain()

        with(result.fields.securityCode.config.length) {
            assertEquals(3, min)
            assertEquals(3, max)
        }
    }

    @Test
    fun `given response then maps document label and validation errors`() {
        val result = buildResponse().toDomain()

        with(result.fields.document) {
            assertEquals("CPF", label)
            assertEquals("Campo obrigatório", validation.errorEmpty)
            assertEquals("CPF incompleto", validation.errorIncomplete)
            assertEquals("CPF inválido", validation.errorInvalid)
        }
    }

    @Test
    fun `given response then maps identificationTypes correctly`() {
        val result = buildResponse().toDomain()

        assertEquals(1, result.identificationTypes.size)
        with(result.identificationTypes.first()) {
            assertEquals("CPF", id)
            assertEquals("CPF", name)
            assertEquals(11, minLength)
            assertEquals(11, maxLength)
            assertEquals("000.000.000-00", placeholder)
            assertEquals("###.###.###-##", mask)
            assertEquals("number", type)
            assertEquals("1", sequence)
        }
    }

    @Test
    fun `given empty identificationTypes then returns empty list`() {
        val result = buildResponse(identificationTypes = emptyList()).toDomain()

        assertEquals(emptyList(), result.identificationTypes)
    }

    @Test
    fun `given multiple identificationTypes then maps all items`() {
        val cnh = identificationType.copy(id = "CNH", name = "CNH", sequence = "2")
        val result = buildResponse(identificationTypes = listOf(identificationType, cnh)).toDomain()

        assertEquals(2, result.identificationTypes.size)
        assertEquals("CPF", result.identificationTypes[0].id)
        assertEquals("CNH", result.identificationTypes[1].id)
    }

    @Test
    fun `given securityCode with null helper then helper is empty string`() {
        val responseWithNullHelper = buildResponse().copy(
            securityCode = securityCodeConfig,
            translations = translations.copy(
                securityCode = securityCodeTranslations.copy(helper = null),
            ),
        )

        val result = responseWithNullHelper.toDomain()

        assertEquals("", result.fields.securityCode.helper)
    }

    @Test
    fun `given securityCode with null errorInvalidField then validation errorInvalid is empty string`() {
        val responseWithNullError = buildResponse().copy(
            translations = translations.copy(
                securityCode = securityCodeTranslations.copy(errorInvalidField = null),
            ),
        )

        val result = responseWithNullError.toDomain()

        assertEquals("", result.fields.securityCode.validation.errorInvalid)
    }
}
