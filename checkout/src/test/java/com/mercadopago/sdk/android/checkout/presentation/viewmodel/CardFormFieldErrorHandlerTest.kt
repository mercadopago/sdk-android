package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberErrorType
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState
import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class CardFormFieldErrorHandlerTest {
    private val analyticsTracker = mockk<CardFormAnalyticsTracker>(relaxed = true)
    private val handler = CardFormFieldErrorHandler(analyticsTracker)

    private val cardValidation = ValidationState(
        errorEmpty = "Número obrigatório",
        errorIncomplete = "Número incompleto",
        errorInvalid = "Número inválido",
    )

    private val baseState = CardPaymentScreenState(
        cardNumberState = CardNumberState(
            length = 16,
            maxLength = 16,
            error = "",
            isValid = true,
            validation = cardValidation,
        ),
        cardHolderState = CardHolderState(error = "", isValid = true),
        expirationDateState = ExpirationDateState(error = "", isValid = true),
        secureCodeState = SecurityCodeState(
            length = 3,
            maxLength = 3,
            error = "",
            isValid = true,
        ),
        identificationTypeState = IdentificationTypeState(show = false),
    )

    @Test
    fun `given isValid false then LuhnValidation is added to errorTypes`() {
        val result = handler.applyLuhnValidation(baseState, isValid = false)

        assertTrue(result.cardNumberState.errorTypes.any { it is CardNumberErrorType.LuhnValidation })
    }

    @Test
    fun `given isValid true then LuhnValidation is removed from errorTypes`() {
        val stateWithLuhn = baseState.copy(
            cardNumberState = baseState.cardNumberState.copy(
                errorTypes = listOf(CardNumberErrorType.LuhnValidation),
            ),
        )

        val result = handler.applyLuhnValidation(stateWithLuhn, isValid = true)

        assertFalse(result.cardNumberState.errorTypes.any { it is CardNumberErrorType.LuhnValidation })
    }

    @Test
    fun `given isValid false with complete card then error is errorInvalid`() {
        val result = handler.applyLuhnValidation(baseState, isValid = false)

        assertEquals("Número inválido", result.cardNumberState.error)
    }

    @Test
    fun `given card length zero then FieldValidation error is added`() {
        val emptyCard = baseState.copy(
            cardNumberState = baseState.cardNumberState.copy(length = 0, isValid = false),
        )

        val result = handler.applyCardNumberFieldError(emptyCard)

        assertTrue(result.cardNumberState.errorTypes.any { it is CardNumberErrorType.FieldValidation })
    }

    @Test
    fun `given card length equals maxLength then no FieldValidation added`() {
        val result = handler.applyCardNumberFieldError(baseState) // length=16, maxLength=16

        assertFalse(result.cardNumberState.errorTypes.any { it is CardNumberErrorType.FieldValidation })
    }

    @Test
    fun `given empty errors then cardNumber isValid and error is empty`() {
        val result = handler.applyCardNumberErrorState(
            state = baseState,
            errors = emptyList(),
            isValid = true,
        )

        assertTrue(result.cardNumberState.isValid)
        assertEquals("", result.cardNumberState.error)
    }

    @Test
    fun `given LuhnValidation with complete card then error is errorInvalid`() {
        val result = handler.applyCardNumberErrorState(
            state = baseState,
            errors = listOf(CardNumberErrorType.LuhnValidation),
            isValid = true,
        )

        assertEquals("Número inválido", result.cardNumberState.error)
    }

    @Test
    fun `given PaymentMethodNotFound then error is errorInvalid`() {
        val result = handler.applyCardNumberErrorState(
            state = baseState,
            errors = listOf(CardNumberErrorType.PaymentMethodNotFound(message = "Número inválido")),
            isValid = true,
        )

        assertEquals("Número inválido", result.cardNumberState.error)
    }

    @Test
    fun `given FieldValidation then error is the validation message`() {
        val result = handler.applyCardNumberErrorState(
            state = baseState,
            errors = listOf(CardNumberErrorType.FieldValidation("Número obrigatório")),
            isValid = true,
        )

        assertEquals("Número obrigatório", result.cardNumberState.error)
    }

    @Test
    fun `given empty errors then isValid is true`() {
        val result = handler.applyCardNumberErrorState(
            state = baseState,
            errors = emptyList(),
            isValid = true,
        )

        assertTrue(result.cardNumberState.isValid)
    }

    @Test
    fun `given errors then isValid is false`() {
        val result = handler.applyCardNumberErrorState(
            state = baseState,
            errors = listOf(CardNumberErrorType.LuhnValidation),
            isValid = false,
        )

        assertFalse(result.cardNumberState.isValid)
    }

    @Test
    fun `given invalid expiration and shouldUpdateError true then error is set`() {
        val invalidState = baseState.copy(
            expirationDateState = ExpirationDateState(
                length = 0,
                validation = ValidationState(errorEmpty = "Data obrigatória"),
            ),
        )

        val result = handler.applyExpirationDateError(invalidState, shouldUpdateError = true)

        assertEquals("Data obrigatória", result.expirationDateState.error)
        assertFalse(result.expirationDateState.isValid)
    }

    @Test
    fun `given invalid expiration and shouldUpdateError false then error is empty`() {
        val invalidState = baseState.copy(
            expirationDateState = ExpirationDateState(
                length = 0,
                validation = ValidationState(errorEmpty = "Data obrigatória"),
            ),
        )

        val result = handler.applyExpirationDateError(invalidState, shouldUpdateError = false)

        assertEquals("", result.expirationDateState.error)
        assertFalse(result.expirationDateState.isValid)
    }

    @Test
    fun `given secureCode is optional then validation is skipped`() {
        val optionalState = baseState.copy(
            secureCodeState = SecurityCodeState(optional = true, isValid = true, error = ""),
        )

        val result = handler.applySecurityCodeError(optionalState)

        assertEquals("", result.secureCodeState.error)
    }

    @Test
    fun `given invalid security code and shouldUpdateError true then error is set`() {
        val invalidState = baseState.copy(
            secureCodeState = SecurityCodeState(
                length = 0,
                maxLength = 3,
                optional = false,
                validation = ValidationState(errorEmpty = "CVV obrigatório"),
            ),
        )

        val result = handler.applySecurityCodeError(invalidState, shouldUpdateError = true)

        assertEquals("CVV obrigatório", result.secureCodeState.error)
        assertFalse(result.secureCodeState.isValid)
    }

    @Test
    fun `given invalid security code and shouldUpdateError false then error is empty`() {
        val invalidState = baseState.copy(
            secureCodeState = SecurityCodeState(
                length = 0,
                maxLength = 3,
                optional = false,
                validation = ValidationState(errorEmpty = "CVV obrigatório"),
            ),
        )

        val result = handler.applySecurityCodeError(invalidState, shouldUpdateError = false)

        assertEquals("", result.secureCodeState.error)
        assertFalse(result.secureCodeState.isValid)
    }

    @Test
    fun `given empty cardholder value then isValid is false`() {
        val invalidState = baseState.copy(
            cardHolderState = CardHolderState(
                value = "",
                validation = ValidationState(errorEmpty = "Nome obrigatório"),
            ),
        )

        val result = handler.applyCardHolderError(invalidState, shouldUpdateError = true)

        assertEquals("Nome obrigatório", result.cardHolderState.error)
        assertFalse(result.cardHolderState.isValid)
    }

    @Test
    fun `given valid cardholder value then isValid is true`() {
        val validState = baseState.copy(
            cardHolderState = CardHolderState(
                value = "John Doe",
                validation = ValidationState(errorEmpty = "Nome obrigatório"),
            ),
        )

        val result = handler.applyCardHolderError(validState, shouldUpdateError = true)

        assertEquals("", result.cardHolderState.error)
        assertTrue(result.cardHolderState.isValid)
    }

    @Test
    fun `given invalid cardholder and shouldUpdateError false then error is empty`() {
        val invalidState = baseState.copy(
            cardHolderState = CardHolderState(
                value = "",
                validation = ValidationState(errorEmpty = "Nome obrigatório"),
            ),
        )

        val result = handler.applyCardHolderError(invalidState, shouldUpdateError = false)

        assertEquals("", result.cardHolderState.error)
        assertFalse(result.cardHolderState.isValid)
    }

    @Test
    fun `given empty document value then isValid is false`() {
        val invalidState = baseState.copy(
            identificationTypeState = IdentificationTypeState(
                value = "",
                show = true,
                validation = ValidationState(errorEmpty = "CPF obrigatório"),
            ),
        )

        val result = handler.applyIdentificationTypeError(invalidState, shouldUpdateError = true)

        assertEquals("CPF obrigatório", result.identificationTypeState.error)
        assertFalse(result.identificationTypeState.isValid)
    }

    @Test
    fun `given valid document value then isValid is true`() {
        val validState = baseState.copy(
            identificationTypeState = IdentificationTypeState(
                value = "12345678",
                show = true,
                selected = IdentificationType(minLength = 7, maxLength = 11),
                validation = ValidationState(errorEmpty = "CPF obrigatório"),
            ),
        )

        val result = handler.applyIdentificationTypeError(validState, shouldUpdateError = true)

        assertEquals("", result.identificationTypeState.error)
        assertTrue(result.identificationTypeState.isValid)
    }

    @Test
    fun `given all fields valid then footer is visible`() {
        val result = handler.applyCardNumberErrorState(
            state = baseState,
            errors = emptyList(),
            isValid = true,
        )

        assertTrue(result.footerState.isVisible)
    }

    @Test
    fun `given cardNumber field invalid then footer is not visible`() {
        val result = handler.applyCardNumberErrorState(
            state = baseState,
            errors = listOf(CardNumberErrorType.FieldValidation("error")),
            isValid = true,
        )

        assertFalse(result.footerState.isVisible)
    }
}
