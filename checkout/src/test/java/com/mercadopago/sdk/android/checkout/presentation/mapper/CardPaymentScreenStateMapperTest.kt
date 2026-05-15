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
    private val defaultConfig = CardFieldConfig(type = "text", length = LengthRange(min = 0, max = 0))
    private val defaultValidation = Validation(
        errorEmpty = "empty",
        errorIncomplete = "incomplete",
        errorInvalid = "invalid",
    )
    private val defaultCardNumberValidation = CardNumberValidation(
        errorEmpty = "empty",
        errorIncomplete = "incomplete",
        errorInvalid = "invalid",
        errorMethodNotAllowed = "",
        errorTypeNotAllowed = "",
    )

    private fun buildOutput(
        title: String = "Card Payment",
        button: String = "Pay",
        identificationTypes: List<IdentificationTypeItem> = emptyList(),
        cardNumber: CardNumberField = CardNumberField(
            label = "Card number",
            placeholder = "0000",
            validation = defaultCardNumberValidation,
            config = defaultConfig,
        ),
        holderName: CardHolderField = CardHolderField(
            label = "Holder",
            placeholder = "Name",
            validation = defaultValidation,
            helper = "helper",
            config = defaultConfig,
        ),
        expirationDate: ExpirationDateField = ExpirationDateField(
            label = "Expiry",
            placeholder = "MM/YY",
            validation = defaultValidation,
            config = defaultConfig,
        ),
        securityCode: SecurityCodeField = SecurityCodeField(
            label = "CVV",
            placeholder = "123",
            helper = "3 digits",
            tooltip = "Back of card",
            validation = defaultValidation,
            config = defaultConfig,
        ),
        document: DocumentField = DocumentField(
            label = "Document",
            validation = defaultValidation,
        ),
    ) = CardFormInitializationOutput(
        title = title,
        buttonLabel = button,
        fields = CardFormFields(
            cardNumber = cardNumber,
            holderName = holderName,
            expirationDate = expirationDate,
            securityCode = securityCode,
            document = document,
        ),
        identificationTypes = identificationTypes,
    )

    @Test
    fun `given output then title is mapped to screen state title`() {
        val output = buildOutput(title = "My Title")

        val state = output.toCardPaymentScreenState()

        assertEquals("My Title", state.title)
    }

    @Test
    fun `given output then buttonLabel is mapped to footerState buttonLabel`() {
        val output = buildOutput(button = "Confirm")

        val state = output.toCardPaymentScreenState()

        assertEquals("Confirm", state.footerState.buttonLabel)
    }

    @Test
    fun `given cardNumber field then label and placeholder are mapped to cardNumberState`() {
        val output = buildOutput(
            cardNumber = CardNumberField(
                label = "Número do cartão",
                placeholder = "#### #### #### ####",
                validation = defaultCardNumberValidation,
                config = defaultConfig,
            ),
        )

        val state = output.toCardPaymentScreenState().cardNumberState

        assertEquals("Número do cartão", state.label)
        assertEquals("#### #### #### ####", state.placeHolder)
    }

    @Test
    fun `given cardNumber field then validation errors are mapped to cardNumberState`() {
        val output = buildOutput(
            cardNumber = CardNumberField(
                label = "",
                placeholder = "",
                validation = CardNumberValidation(
                    errorEmpty = "req",
                    errorIncomplete = "inc",
                    errorInvalid = "inv",
                    errorMethodNotAllowed = "",
                    errorTypeNotAllowed = "",
                ),
                config = defaultConfig,
            ),
        )

        val validation = output.toCardPaymentScreenState().cardNumberState.validation

        assertEquals("req", validation.errorEmpty)
        assertEquals("inc", validation.errorIncomplete)
        assertEquals("inv", validation.errorInvalid)
    }

    @Test
    fun `given holderName field then label placeholder and validation are mapped to cardHolderState`() {
        val output = buildOutput(
            holderName = CardHolderField(
                label = "Titular",
                placeholder = "Nome",
                validation = Validation("empty", "incomplete", "invalid"),
                config = defaultConfig,
                helper = "helper",
            ),
        )

        val state = output.toCardPaymentScreenState().cardHolderState

        assertEquals("Titular", state.label)
        assertEquals("Nome", state.placeHolder)
        assertEquals("empty", state.validation.errorEmpty)
        assertEquals("incomplete", state.validation.errorIncomplete)
        assertEquals("invalid", state.validation.errorInvalid)
    }

    @Test
    fun `given expirationDate field then label placeholder and validation are mapped`() {
        val output = buildOutput(
            expirationDate = ExpirationDateField(
                label = "Validade",
                placeholder = "MM/AA",
                validation = Validation("empty", "incomplete", "invalid"),
                config = defaultConfig,
            ),
        )

        val state = output.toCardPaymentScreenState().expirationDateState

        assertEquals("Validade", state.label)
        assertEquals("MM/AA", state.placeHolder)
        assertEquals("empty", state.validation.errorEmpty)
        assertEquals("incomplete", state.validation.errorIncomplete)
        assertEquals("invalid", state.validation.errorInvalid)
    }

    @Test
    fun `given securityCode field then all fields are mapped to secureCodeState`() {
        val output = buildOutput(
            securityCode = SecurityCodeField(
                label = "CVV",
                placeholder = "000",
                helper = "3 digits on the back",
                tooltip = "Where to find",
                validation = Validation("empty", "incomplete", "invalid"),
                config = defaultConfig,
            ),
        )

        val state = output.toCardPaymentScreenState().secureCodeState

        assertEquals("CVV", state.label)
        assertEquals("000", state.placeHolder)
        assertEquals("3 digits on the back", state.helper)
        assertEquals("Where to find", state.messageTooltip)
        assertEquals("empty", state.validation.errorEmpty)
        assertEquals("incomplete", state.validation.errorIncomplete)
        assertEquals("invalid", state.validation.errorInvalid)
    }

    @Test
    fun `given empty identificationTypes then show is false and selected is null`() {
        val output = buildOutput(identificationTypes = emptyList())

        val state = output.toCardPaymentScreenState().identificationTypeState

        assertFalse(state.show)
        assertNull(state.selected)
        assertTrue(state.placeHolder.isEmpty())
    }

    @Test
    fun `given identificationTypes then show is true and first item is selected`() {
        val item = IdentificationTypeItem(
            id = "CPF",
            name = "CPF",
            minLength = 11,
            maxLength = 11,
            placeholder = "000.000.000-00",
            mask = "###.###.###-##",
            type = "number",
            sequence = "1",
        )
        val output = buildOutput(identificationTypes = listOf(item))

        val state = output.toCardPaymentScreenState().identificationTypeState

        assertTrue(state.show)
        assertEquals("CPF", state.selected?.id)
        assertEquals("000.000.000-00", state.placeHolder)
    }

    @Test
    fun `given identificationTypes then all items are mapped to core types`() {
        val items = listOf(
            IdentificationTypeItem("CPF", "CPF", 11, 11, "", "", "number", "1"),
            IdentificationTypeItem("CNPJ", "CNPJ", 14, 14, "", "", "number", "2"),
        )
        val output = buildOutput(identificationTypes = items)

        val types = output.toCardPaymentScreenState().identificationTypeState.identificationTypes

        assertEquals(2, types?.size)
        assertEquals("CPF", types?.get(0)?.id)
        assertEquals("CNPJ", types?.get(1)?.id)
    }

    @Test
    fun `given identificationTypeItem then all fields are mapped to IdentificationType`() {
        val item = IdentificationTypeItem(
            id = "RG",
            name = "RG",
            minLength = 7,
            maxLength = 9,
            placeholder = "00.000.000-0",
            mask = "##.###.###-#",
            type = "number",
            sequence = "2",
        )
        val output = buildOutput(identificationTypes = listOf(item))

        val coreType = output.toCardPaymentScreenState()
            .identificationTypeState.identificationTypes?.first()

        assertEquals("RG", coreType?.id)
        assertEquals("RG", coreType?.name)
        assertEquals(7, coreType?.minLength)
        assertEquals(9, coreType?.maxLength)
        assertEquals("##.###.###-#", coreType?.mask)
        assertEquals("number", coreType?.type)
    }

    @Test
    fun `given document field then label and validation are mapped to identificationTypeState`() {
        val output = buildOutput(
            document = DocumentField(
                label = "CPF",
                validation = Validation("empty", "incomplete", "invalid"),
            ),
        )

        val state = output.toCardPaymentScreenState().identificationTypeState

        assertEquals("CPF", state.label)
        assertEquals("empty", state.validation.errorEmpty)
        assertEquals("incomplete", state.validation.errorIncomplete)
        assertEquals("invalid", state.validation.errorInvalid)
    }
}
