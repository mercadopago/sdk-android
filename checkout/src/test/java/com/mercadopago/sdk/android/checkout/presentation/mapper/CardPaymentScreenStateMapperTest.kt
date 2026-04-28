package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.CardFieldConfig
import com.mercadopago.sdk.android.checkout.domain.model.CardFormFields
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardHolderField
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberField
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberValidation
import com.mercadopago.sdk.android.checkout.domain.model.DocumentField
import com.mercadopago.sdk.android.checkout.domain.model.ExpirationDateField
import com.mercadopago.sdk.android.checkout.domain.model.IdentificationTypeItem
import com.mercadopago.sdk.android.checkout.domain.model.LengthRange
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeField
import com.mercadopago.sdk.android.checkout.domain.model.Validation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class CardPaymentScreenStateMapperTest {
    private val cardNumberValidation = CardNumberValidation(
        errorEmpty = "Número obrigatório",
        errorIncomplete = "Número incompleto",
        errorInvalid = "Número inválido",
        errorMethodNotAllowed = "",
        errorTypeNotAllowed = "",
    )

    private val holderValidation = Validation(
        errorEmpty = "Nome obrigatório",
        errorIncomplete = "Nome incompleto",
        errorInvalid = "Nome inválido",
    )

    private val expirationValidation = Validation(
        errorEmpty = "Validade obrigatória",
        errorIncomplete = "Validade incompleta",
        errorInvalid = "Validade inválida",
    )

    private val securityValidation = Validation(
        errorEmpty = "CVV obrigatório",
        errorIncomplete = "CVV incompleto",
        errorInvalid = "CVV inválido",
    )

    private val documentValidation = Validation(
        errorEmpty = "CPF obrigatório",
        errorIncomplete = "CPF incompleto",
        errorInvalid = "CPF inválido",
    )

    private val fieldConfig = CardFieldConfig(type = "number", length = LengthRange(min = 16, max = 16))

    private val cardNumberField = CardNumberField(
        label = "Número do cartão",
        placeholder = "0000 0000 0000 0000",
        validation = cardNumberValidation,
        config = fieldConfig,
    )

    private val holderNameField = CardHolderField(
        label = "Nome no cartão",
        placeholder = "Como aparece no cartão",
        validation = holderValidation,
        config = fieldConfig,
    )

    private val expirationDateField = ExpirationDateField(
        label = "Validade",
        placeholder = "MM/AA",
        validation = expirationValidation,
        config = fieldConfig,
    )

    private val securityCodeField = SecurityCodeField(
        label = "CVV",
        placeholder = "000",
        helper = "3 dígitos no verso",
        tooltip = "Código de segurança",
        validation = securityValidation,
        config = fieldConfig,
    )

    private val documentField = DocumentField(
        label = "CPF",
        validation = documentValidation,
    )

    private val identificationType = IdentificationTypeItem(
        id = "CPF",
        name = "CPF",
        minLength = 11,
        maxLength = 11,
        placeholder = "000.000.000-00",
        mask = "###.###.###-##",
        type = "number",
        sequence = "1",
    )

    private fun buildOutput(
        identificationTypes: List<IdentificationTypeItem> = listOf(identificationType),
    ) = CardFormInitializationOutput(
        title = "Adicionar cartão",
        button = "Continuar",
        fields = CardFormFields(
            cardNumber = cardNumberField,
            holderName = holderNameField,
            expirationDate = expirationDateField,
            securityCode = securityCodeField,
            document = documentField,
        ),
        identificationTypes = identificationTypes,
    )

    @Test
    fun `given output then maps title`() {
        val result = buildOutput().toCardPaymentScreenState()

        assertEquals("Adicionar cartão", result.title)
    }

    @Test
    fun `given output then maps button to fixedFooterState buttonText`() {
        val result = buildOutput().toCardPaymentScreenState()

        assertEquals("Continuar", result.fixedFooterState.buttonText)
    }

    @Test
    fun `given output then maps cardNumberState label and placeholder`() {
        val result = buildOutput().toCardPaymentScreenState()

        assertEquals("Número do cartão", result.cardNumberState.label)
        assertEquals("0000 0000 0000 0000", result.cardNumberState.placeHolder)
    }

    @Test
    fun `given output then maps cardNumberState validation`() {
        val result = buildOutput().toCardPaymentScreenState()

        with(result.cardNumberState.validation) {
            assertEquals("Número obrigatório", errorEmpty)
            assertEquals("Número incompleto", errorIncomplete)
            assertEquals("Número inválido", errorInvalid)
        }
    }

    @Test
    fun `given output then maps cardHolderState label and placeholder`() {
        val result = buildOutput().toCardPaymentScreenState()

        assertEquals("Nome no cartão", result.cardHolderState.label)
        assertEquals("Como aparece no cartão", result.cardHolderState.placeHolder)
    }

    @Test
    fun `given output then maps cardHolderState validation`() {
        val result = buildOutput().toCardPaymentScreenState()

        with(result.cardHolderState.validation) {
            assertEquals("Nome obrigatório", errorEmpty)
            assertEquals("Nome incompleto", errorIncomplete)
            assertEquals("Nome inválido", errorInvalid)
        }
    }

    @Test
    fun `given output then maps expirationDateState label and placeholder`() {
        val result = buildOutput().toCardPaymentScreenState()

        assertEquals("Validade", result.expirationDateState.label)
        assertEquals("MM/AA", result.expirationDateState.placeHolder)
    }

    @Test
    fun `given output then maps expirationDateState validation`() {
        val result = buildOutput().toCardPaymentScreenState()

        with(result.expirationDateState.validation) {
            assertEquals("Validade obrigatória", errorEmpty)
            assertEquals("Validade incompleta", errorIncomplete)
            assertEquals("Validade inválida", errorInvalid)
        }
    }

    @Test
    fun `given output then maps secureCodeState label placeholder helper and tooltip`() {
        val result = buildOutput().toCardPaymentScreenState()

        with(result.secureCodeState) {
            assertEquals("CVV", label)
            assertEquals("000", placeHolder)
            assertEquals("3 dígitos no verso", helper)
            assertEquals("Código de segurança", messageTooltip)
        }
    }

    @Test
    fun `given output then maps secureCodeState validation`() {
        val result = buildOutput().toCardPaymentScreenState()

        with(result.secureCodeState.validation) {
            assertEquals("CVV obrigatório", errorEmpty)
            assertEquals("CVV incompleto", errorIncomplete)
            assertEquals("CVV inválido", errorInvalid)
        }
    }

    @Test
    fun `given output then maps identificationTypeState label`() {
        val result = buildOutput().toCardPaymentScreenState()

        assertEquals("CPF", result.identificationTypeState.label)
    }

    @Test
    fun `given non-empty identificationTypes then identificationTypeState show is true`() {
        val result = buildOutput(identificationTypes = listOf(identificationType)).toCardPaymentScreenState()

        assertTrue(result.identificationTypeState.show)
    }

    @Test
    fun `given empty identificationTypes then identificationTypeState show is false`() {
        val result = buildOutput(identificationTypes = emptyList()).toCardPaymentScreenState()

        assertFalse(result.identificationTypeState.show)
    }

    @Test
    fun `given identificationTypes then maps to core types correctly`() {
        val result = buildOutput().toCardPaymentScreenState()

        val coreType = result.identificationTypeState.identificationTypes?.first()
        assertEquals("CPF", coreType?.id)
        assertEquals("CPF", coreType?.name)
        assertEquals("number", coreType?.type)
        assertEquals(11, coreType?.minLength)
        assertEquals(11, coreType?.maxLength)
        assertEquals("###.###.###-##", coreType?.mask)
    }

    @Test
    fun `given identificationTypes then selected is the first item`() {
        val second = identificationType.copy(id = "CNH", name = "CNH")
        val result = buildOutput(identificationTypes = listOf(identificationType, second)).toCardPaymentScreenState()

        assertEquals("CPF", result.identificationTypeState.selected?.id)
    }

    @Test
    fun `given empty identificationTypes then selected is null`() {
        val result = buildOutput(identificationTypes = emptyList()).toCardPaymentScreenState()

        assertNull(result.identificationTypeState.selected)
    }

    @Test
    fun `given identificationTypes then placeHolder comes from first item`() {
        val result = buildOutput().toCardPaymentScreenState()

        assertEquals("000.000.000-00", result.identificationTypeState.placeHolder)
    }

    @Test
    fun `given empty identificationTypes then placeHolder is empty`() {
        val result = buildOutput(identificationTypes = emptyList()).toCardPaymentScreenState()

        assertEquals("", result.identificationTypeState.placeHolder)
    }

    @Test
    fun `given identificationTypeState then maps validation errors`() {
        val result = buildOutput().toCardPaymentScreenState()

        with(result.identificationTypeState.validation) {
            assertEquals("CPF obrigatório", errorEmpty)
            assertEquals("CPF incompleto", errorIncomplete)
            assertEquals("CPF inválido", errorInvalid)
        }
    }
}
