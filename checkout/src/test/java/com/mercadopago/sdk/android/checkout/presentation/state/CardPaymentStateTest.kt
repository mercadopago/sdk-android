package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.core.model.MPCardBrand
import com.mercadopago.sdk.android.checkout.core.model.MPCardType
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

internal class CardPaymentStateTest {
    @Test
    fun `given defaults then CardHolderState has expected values`() {
        val state = CardHolderState()

        assertEquals("", state.label)
        assertEquals("", state.helper)
        assertEquals("", state.placeHolder)
        assertEquals("", state.error)
        assertFalse(state.isFocused)
        assertFalse(state.filled)
        assertTrue(state.enabled)
        assertFalse(state.isValid)
        assertTrue(state.showPlaceHolder)
        assertTrue(state.show)
        assertEquals("", state.value)
        assertEquals(ValidationState(), state.validation)
    }

    @Test
    fun `given explicit values then CardHolderState properties are assigned`() {
        val validation = ValidationState(
            errorEmpty = "empty",
            errorIncomplete = "incomplete",
            errorInvalid = "invalid",
        )
        val state = CardHolderState(
            label = "Name",
            helper = "as on card",
            placeHolder = "John Doe",
            error = "required",
            isFocused = true,
            filled = true,
            enabled = false,
            isValid = true,
            showPlaceHolder = false,
            show = false,
            value = "John Doe",
            validation = validation,
        )

        assertEquals("Name", state.label)
        assertEquals("as on card", state.helper)
        assertEquals("John Doe", state.placeHolder)
        assertEquals("required", state.error)
        assertTrue(state.isFocused)
        assertTrue(state.filled)
        assertFalse(state.enabled)
        assertTrue(state.isValid)
        assertFalse(state.showPlaceHolder)
        assertFalse(state.show)
        assertEquals("John Doe", state.value)
        assertEquals(validation, state.validation)
    }

    @Test
    fun `given copy on CardHolderState then value changes`() {
        val state = CardHolderState()

        val copy = state.copy(value = "Jane", isValid = true)

        assertEquals("Jane", copy.value)
        assertTrue(copy.isValid)
        assertEquals(state.label, copy.label)
        assertNotEquals(state, copy)
    }

    @Test
    fun `given defaults then MessageError is empty`() {
        val error = MessageError()

        assertEquals("", error.title)
        assertEquals("", error.description)
    }

    @Test
    fun `given explicit values then MessageError properties are assigned and copy works`() {
        val error = MessageError(title = "Oops", description = "Try again")

        assertEquals("Oops", error.title)
        assertEquals("Try again", error.description)

        val copy = error.copy(description = "Failed")
        assertEquals("Failed", copy.description)
        assertEquals(error.title, copy.title)
        assertNotEquals(error, copy)
    }

    @Test
    fun `given ValidationState explicit values then properties are assigned`() {
        val validation = ValidationState(
            errorEmpty = "e1",
            errorIncomplete = "e2",
            errorInvalid = "e3",
        )

        assertEquals("e1", validation.errorEmpty)
        assertEquals("e2", validation.errorIncomplete)
        assertEquals("e3", validation.errorInvalid)

        val copy = validation.copy(errorEmpty = "changed")
        assertEquals("changed", copy.errorEmpty)
        assertNotEquals(validation, copy)
    }

    @Test
    fun `given default CardPaymentScreenState then nested states use defaults`() {
        val state = CardPaymentScreenState()

        assertEquals("", state.title)
        assertEquals(ExpirationDateState(), state.expirationDateState)
        assertEquals(SecurityCodeState(), state.secureCodeState)
        assertEquals(CardNumberState(), state.cardNumberState)
        assertEquals(CardHolderState(), state.cardHolderState)
        assertEquals(IdentificationTypeState(), state.identificationTypeState)
        assertEquals(InstallmentsState(), state.installmentsState)
        assertEquals(PaymentState(), state.paymentState)
        assertEquals(FixedFooterState(), state.fixedFooterState)
        assertEquals(emptyList(), state.cardIssuers)
        assertEquals(MessageError(), state.messageError)
        assertFalse(state.isLoading)
        assertFalse(state.showTooltip)
        assertFalse(state.showMessage)
    }

    @Test
    fun `given copy on CardPaymentScreenState then flags change`() {
        val state = CardPaymentScreenState()

        val copy = state.copy(isLoading = true, showTooltip = true, showMessage = true)

        assertTrue(copy.isLoading)
        assertTrue(copy.showTooltip)
        assertTrue(copy.showMessage)
        assertEquals(state.title, copy.title)
        assertNotEquals(state, copy)
    }

    @Test
    fun `given SecurityCodeState explicit values then properties are assigned`() {
        val state = SecurityCodeState(
            label = "CVV",
            length = 3,
            optional = true,
            maxLength = 4,
            messageTooltip = "back of card",
        )

        assertEquals("CVV", state.label)
        assertEquals(3, state.length)
        assertTrue(state.optional)
        assertEquals(4, state.maxLength)
        assertEquals("back of card", state.messageTooltip)
    }

    @Test
    fun `given ExpirationDateState explicit values then properties are assigned`() {
        val state = ExpirationDateState(label = "Expiry", length = 4, isValid = true)

        assertEquals("Expiry", state.label)
        assertEquals(4, state.length)
        assertTrue(state.isValid)
    }

    @Test
    fun `given CardNumberState explicit values then properties are assigned`() {
        val errorTypes = listOf<CardNumberErrorType>(CardNumberErrorType.LuhnValidation)
        val state = CardNumberState(
            label = "Card number",
            image = "https://img/visa.png",
            length = 16,
            maxLength = 16,
            mask = "#### #### #### ####",
            lastFourDigits = "1234",
            cardBin = "411111",
            errorTypes = errorTypes,
        )

        assertEquals("Card number", state.label)
        assertEquals("https://img/visa.png", state.image)
        assertEquals(16, state.length)
        assertEquals(16, state.maxLength)
        assertEquals("#### #### #### ####", state.mask)
        assertEquals("1234", state.lastFourDigits)
        assertEquals("411111", state.cardBin)
        assertEquals(errorTypes, state.errorTypes)
    }

    @Test
    fun `given IdentificationTypeState explicit values then properties are assigned`() {
        val type = IdentificationType(id = "CPF", name = "CPF")
        val state = IdentificationTypeState(
            label = "Document",
            show = false,
            identificationTypes = listOf(type),
            selected = type,
            value = "12345678900",
        )

        assertEquals("Document", state.label)
        assertFalse(state.show)
        assertEquals(listOf(type), state.identificationTypes)
        assertEquals(type, state.selected)
        assertEquals("12345678900", state.value)
    }

    @Test
    fun `given InstallmentsState explicit values then properties are assigned`() {
        val state = InstallmentsState(showList = true)

        assertTrue(state.showList)
        assertEquals(emptyList(), state.installments)

        val copy = state.copy(showList = false)
        assertFalse(copy.showList)
        assertNotEquals(state, copy)
    }

    @Test
    fun `given FixedFooterState explicit values then properties are assigned`() {
        val state = FixedFooterState(
            title = "Total",
            currencySymbol = "R$",
            amountIntegerPart = "100",
            amountDecimalPart = "00",
            subtitle = "subtitle",
            buttonText = "Pay",
            isVisible = true,
        )

        assertEquals("Total", state.title)
        assertEquals("R$", state.currencySymbol)
        assertEquals("100", state.amountIntegerPart)
        assertEquals("00", state.amountDecimalPart)
        assertEquals("subtitle", state.subtitle)
        assertEquals("Pay", state.buttonText)
        assertTrue(state.isVisible)
    }

    @Test
    fun `given PaymentState explicit values then properties are assigned`() {
        val state = PaymentState(paymentMethodId = "visa", paymentTypeId = "credit_card")

        assertEquals("visa", state.paymentMethodId)
        assertEquals("credit_card", state.paymentTypeId)
    }

    @Test
    fun `given CardNumberErrorType subtypes then properties are assigned`() {
        val fieldValidation = CardNumberErrorType.FieldValidation(message = "field")
        val notFound = CardNumberErrorType.PaymentMethodNotFound(message = "not found")
        val luhn = CardNumberErrorType.LuhnValidation
        val brandNotAccepted = CardNumberErrorType.CardBrandNotAccepted(brand = MPCardBrand.Visa)
        val typeNotAccepted = CardNumberErrorType.CardTypeNotAccepted(cardType = MPCardType.CREDIT)

        assertEquals("field", fieldValidation.message)
        assertEquals("not found", notFound.message)
        assertEquals(MPCardBrand.Visa, brandNotAccepted.brand)
        assertEquals(MPCardType.CREDIT, typeNotAccepted.cardType)

        val errors = listOf<CardNumberErrorType>(
            fieldValidation,
            notFound,
            luhn,
            brandNotAccepted,
            typeNotAccepted,
        )
        errors.forEach { error ->
            val described: String = when (error) {
                is CardNumberErrorType.FieldValidation -> error.message
                is CardNumberErrorType.PaymentMethodNotFound -> error.message
                CardNumberErrorType.LuhnValidation -> "luhn"
                is CardNumberErrorType.CardBrandNotAccepted -> error.brand.toString()
                is CardNumberErrorType.CardTypeNotAccepted -> error.cardType?.name.orEmpty()
            }
            assertTrue(described.isNotEmpty())
        }
    }

    @Test
    fun `given CardPaymentViewEvent OnBackPressed then it carries the user cancelled context`() {
        val context = MPUserCancelledContext.CardSave(emptyList())
        val event = CardPaymentViewEvent.OnBackPressed(context)

        assertEquals(context, event.context)
    }
}
