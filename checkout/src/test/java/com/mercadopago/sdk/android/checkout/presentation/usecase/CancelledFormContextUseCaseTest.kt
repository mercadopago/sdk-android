package com.mercadopago.sdk.android.checkout.presentation.usecase

import com.mercadopago.sdk.android.checkout.core.model.MPCardBrand
import com.mercadopago.sdk.android.checkout.core.model.MPCardType
import com.mercadopago.sdk.android.checkout.domain.model.Field
import com.mercadopago.sdk.android.checkout.domain.model.MPCancelledFieldState
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.State
import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberErrorType
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState
import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CancelledFormContextUseCaseTest {
    private val useCase = CancelledFormContextUseCase()

    private fun makeState(
        cardNumberState: CardNumberState = CardNumberState(),
        cardHolderState: CardHolderState = CardHolderState(),
        expirationDateState: ExpirationDateState = ExpirationDateState(),
        secureCodeState: SecurityCodeState = SecurityCodeState(),
        identificationTypeState: IdentificationTypeState = IdentificationTypeState(),
    ) = CardPaymentScreenState(
        cardNumberState = cardNumberState,
        cardHolderState = cardHolderState,
        expirationDateState = expirationDateState,
        secureCodeState = secureCodeState,
        identificationTypeState = identificationTypeState,
    )

    private fun invoke(
        state: CardPaymentScreenState,
    ): List<MPCancelledFieldState> {
        val result = useCase(state)
        assertIs<MPUserCancelledContext.CardForm>(result)
        return result.context.fields
    }

    @Test
    fun `given card number is empty then cardNumber state is Empty`() {
        val state = makeState(
            cardNumberState = CardNumberState(
                length = 0,
                errorTypes = listOf(CardNumberErrorType.FieldValidation("")),
            ),
        )

        val fields = invoke(state)

        val cardNumberField = fields.first { it.field == Field.CARD_NUMBER }
        assertEquals(State.Empty, cardNumberField.state)
    }

    @Test
    fun `given card number length is less than maxLength then cardNumber state is Incomplete`() {
        val state = makeState(
            cardNumberState = CardNumberState(
                length = 10,
                maxLength = 16,
                errorTypes = listOf(CardNumberErrorType.FieldValidation("")),
            ),
        )

        val fields = invoke(state)

        val cardNumberField = fields.first { it.field == Field.CARD_NUMBER }
        assertEquals(State.Incomplete, cardNumberField.state)
    }

    @Test
    fun `given card number has FieldValidation error and is complete then cardNumber state is Invalid`() {
        val state = makeState(
            cardNumberState = CardNumberState(
                length = 16,
                maxLength = 16,
                errorTypes = listOf(CardNumberErrorType.FieldValidation("Invalid number")),
            ),
        )

        val fields = invoke(state)

        val cardNumberField = fields.first { it.field == Field.CARD_NUMBER }
        assertEquals(State.Invalid, cardNumberField.state)
    }

    @Test
    fun `given card number has CardBrandNotAccepted error then state is CardBrandNotAccepted`() {
        val errorType = CardNumberErrorType.CardBrandNotAccepted(MPCardBrand.Visa)
        val state = makeState(cardNumberState = CardNumberState(errorTypes = listOf(errorType)))

        val fields = invoke(state)

        val cardNumberField = fields.first { it.field == Field.CARD_NUMBER }
        assertIs<State.CardBrandNotAccepted>(cardNumberField.state)
        assertEquals(MPCardBrand.Visa, cardNumberField.state.brand)
    }

    @Test
    fun `given card number has CardTypeNotAccepted error then state is CardTypeNotAccepted`() {
        val errorType = CardNumberErrorType.CardTypeNotAccepted(MPCardType.CREDIT)
        val state = makeState(cardNumberState = CardNumberState(errorTypes = listOf(errorType)))

        val fields = invoke(state)

        val cardNumberField = fields.first { it.field == Field.CARD_NUMBER }
        assertIs<State.CardTypeNotAccepted>(cardNumberField.state)
        assertEquals(MPCardType.CREDIT, cardNumberField.state.cardType)
    }

    @Test
    fun `given card number is complete and valid then cardNumber state is Valid`() {
        val state = makeState(cardNumberState = CardNumberState(length = 16, maxLength = 16))

        val fields = invoke(state)

        val cardNumberField = fields.first { it.field == Field.CARD_NUMBER }
        assertEquals(State.Valid, cardNumberField.state)
    }

    @Test
    fun `given cardHolder show is true then cardHolder field is included`() {
        val state = makeState(cardHolderState = CardHolderState(show = true))

        val fields = invoke(state)

        assertEquals(true, fields.any { it.field == Field.CARD_HOLDER })
    }

    @Test
    fun `given cardHolder show is false then cardHolder field is not included`() {
        val state = makeState(cardHolderState = CardHolderState(show = false))

        val fields = invoke(state)

        assertEquals(false, fields.any { it.field == Field.CARD_HOLDER })
    }

    @Test
    fun `given cardHolder is empty then cardHolder state is Empty`() {
        val state = makeState(cardHolderState = CardHolderState(show = true, value = ""))

        val fields = invoke(state)

        val cardHolderField = fields.first { it.field == Field.CARD_HOLDER }
        assertEquals(State.Empty, cardHolderField.state)
    }

    @Test
    fun `given cardHolder has invalid format then cardHolder state is Invalid`() {
        val state = makeState(
            cardHolderState = CardHolderState(
                show = true,
                value = "Jo1n",
                validation = ValidationState(errorInvalid = "Invalid format"),
            ),
        )

        val fields = invoke(state)

        val cardHolderField = fields.first { it.field == Field.CARD_HOLDER }
        assertEquals(State.Invalid, cardHolderField.state)
    }

    @Test
    fun `given cardHolder has value and no error then cardHolder state is Valid`() {
        val state = makeState(cardHolderState = CardHolderState(show = true, value = "John Doe"))

        val fields = invoke(state)

        val cardHolderField = fields.first { it.field == Field.CARD_HOLDER }
        assertEquals(State.Valid, cardHolderField.state)
    }

    @Test
    fun `given expiration date is empty then expirationDate state is Empty`() {
        val state = makeState(expirationDateState = ExpirationDateState(length = 0))

        val fields = invoke(state)

        val field = fields.first { it.field == Field.EXPIRATION_DATE }
        assertEquals(State.Empty, field.state)
    }

    @Test
    fun `given expiration date is not filled then expirationDate state is Incomplete`() {
        val state = makeState(expirationDateState = ExpirationDateState(length = 2, filled = false))

        val fields = invoke(state)

        val field = fields.first { it.field == Field.EXPIRATION_DATE }
        assertEquals(State.Incomplete, field.state)
    }

    @Test
    fun `given expiration date is filled but invalid then expirationDate state is Invalid`() {
        val state = makeState(
            expirationDateState = ExpirationDateState(
                filled = true,
                isValid = false,
                length = 4,
                validation = ValidationState(errorInvalid = "Expired"),
            ),
        )

        val fields = invoke(state)

        val field = fields.first { it.field == Field.EXPIRATION_DATE }
        assertEquals(State.Invalid, field.state)
    }

    @Test
    fun `given expiration date is filled and valid then expirationDate state is Valid`() {
        val state = makeState(expirationDateState = ExpirationDateState(filled = true, length = 4))

        val fields = invoke(state)

        val field = fields.first { it.field == Field.EXPIRATION_DATE }
        assertEquals(State.Valid, field.state)
    }

    @Test
    fun `given security code is not optional then securityCode field is included`() {
        val state = makeState(secureCodeState = SecurityCodeState(optional = false))

        val fields = invoke(state)

        assertEquals(true, fields.any { it.field == Field.SECURITY_CODE })
    }

    @Test
    fun `given security code is optional then securityCode field is not included`() {
        val state = makeState(secureCodeState = SecurityCodeState(optional = true))

        val fields = invoke(state)

        assertEquals(false, fields.any { it.field == Field.SECURITY_CODE })
    }

    @Test
    fun `given security code is empty then securityCode state is Empty`() {
        val state = makeState(secureCodeState = SecurityCodeState(optional = false, length = 0))

        val fields = invoke(state)

        val field = fields.first { it.field == Field.SECURITY_CODE }
        assertEquals(State.Empty, field.state)
    }

    @Test
    fun `given security code length is less than maxLength then securityCode state is Incomplete`() {
        val state = makeState(secureCodeState = SecurityCodeState(optional = false, length = 2, maxLength = 3))

        val fields = invoke(state)

        val field = fields.first { it.field == Field.SECURITY_CODE }
        assertEquals(State.Incomplete, field.state)
    }

    @Test
    fun `given security code length equals maxLength then securityCode state is Valid`() {
        val state = makeState(secureCodeState = SecurityCodeState(optional = false, length = 3, maxLength = 3))

        val fields = invoke(state)

        val field = fields.first { it.field == Field.SECURITY_CODE }
        assertEquals(State.Valid, field.state)
    }

    @Test
    fun `given identification type show is true then document field is included`() {
        val state = makeState(identificationTypeState = IdentificationTypeState(show = true))

        val fields = invoke(state)

        assertEquals(true, fields.any { it.field == Field.DOCUMENT })
    }

    @Test
    fun `given identification type show is false then document field is not included`() {
        val state = makeState(identificationTypeState = IdentificationTypeState(show = false))

        val fields = invoke(state)

        assertEquals(false, fields.any { it.field == Field.DOCUMENT })
    }

    @Test
    fun `given identification type is empty then document state is Empty`() {
        val state = makeState(identificationTypeState = IdentificationTypeState(show = true, value = ""))

        val fields = invoke(state)

        val field = fields.first { it.field == Field.DOCUMENT }
        assertEquals(State.Empty, field.state)
    }

    @Test
    fun `given identification type has all zeros value then document state is Invalid`() {
        val state = makeState(
            identificationTypeState = IdentificationTypeState(
                show = true,
                value = "000",
                validation = ValidationState(errorInvalid = "Invalid"),
            ),
        )

        val fields = invoke(state)

        val field = fields.first { it.field == Field.DOCUMENT }
        assertEquals(State.Invalid, field.state)
    }

    @Test
    fun `given default state then result contains cardNumber and expirationDate fields`() {
        val result = useCase(makeState())

        assertIs<MPUserCancelledContext.CardForm>(result)
        val fields = result.context.fields
        assertEquals(true, fields.any { it.field == Field.CARD_NUMBER })
        assertEquals(true, fields.any { it.field == Field.EXPIRATION_DATE })
    }
}
