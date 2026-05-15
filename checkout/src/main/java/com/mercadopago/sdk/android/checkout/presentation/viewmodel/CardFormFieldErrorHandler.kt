package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.extensions.isComplete
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberErrorType
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.MessageError
import com.mercadopago.sdk.android.checkout.presentation.validation.CardHolderVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.CardNumberVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.ExpirationDateVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.IdentificationTypeVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.SecurityCodeVerifier

@Suppress("TooManyFunctions")
internal class CardFormFieldErrorHandler(
    private val analyticsTracker: CardFormAnalyticsTracker,
) {
    fun applyLuhnValidation(
        state: CardPaymentScreenState,
        isValid: Boolean,
    ): CardPaymentScreenState =
        updateCardNumberError<CardNumberErrorType.LuhnValidation>(
            state = state,
            shouldShowError = state.cardNumberState.isComplete(),
        ) {
            if (!isValid) CardNumberErrorType.LuhnValidation else null
        }

    fun applyCardNumberFieldError(
        state: CardPaymentScreenState,
        shouldShowError: Boolean = true,
    ): CardPaymentScreenState =
        updateCardNumberError<CardNumberErrorType.FieldValidation>(state, shouldShowError) {
            val error = CardNumberVerifier().verify(state.cardNumberState)
            if (error.isNotEmpty()) CardNumberErrorType.FieldValidation(error) else null
        }

    fun applyCardNumberErrorState(
        state: CardPaymentScreenState,
        errors: List<CardNumberErrorType>,
        isValid: Boolean,
        shouldShowError: Boolean = true,
    ): CardPaymentScreenState {
        val cardNumberState = state.cardNumberState
        val errorMessage: String = when {
            errors.any { it is CardNumberErrorType.LuhnValidation && cardNumberState.isComplete() } ->
                cardNumberState.validation.errorInvalid

            errors.any { it is CardNumberErrorType.PaymentMethodNotFound } ->
                errors.filterIsInstance<CardNumberErrorType.PaymentMethodNotFound>().first().message
                    .ifEmpty { cardNumberState.validation.errorInvalid }

            errors.any { it is CardNumberErrorType.FieldValidation } ->
                errors.filterIsInstance<CardNumberErrorType.FieldValidation>().first().message

            else -> ""
        }

        return computeFooterVisibility(
            state.copy(
                cardNumberState = cardNumberState.copy(
                    error = if (shouldShowError) errorMessage else "",
                    isValid = isValid,
                    errorTypes = errors,
                ),
            ),
        )
    }

    fun clearCardNumberErrors(
        state: CardPaymentScreenState,
    ): CardPaymentScreenState =
        applyCardNumberErrorState(
            state = state,
            errors = emptyList(),
            isValid = false,
        )

    fun applyPaymentMethodNotFoundError(
        state: CardPaymentScreenState,
        message: String,
    ): CardPaymentScreenState =
        updateCardNumberError<CardNumberErrorType.PaymentMethodNotFound>(state, true) {
            CardNumberErrorType.PaymentMethodNotFound(message)
        }

    fun handleResultError(
        state: CardPaymentScreenState,
        message: String,
        genericErrorMessage: String,
    ): CardPaymentScreenState {
        val isCardNumberFocused = state.cardNumberState.isFocused
        return state.copy(
            messageError = MessageError(
                title = message,
                description = genericErrorMessage,
            ),
            showMessage = !isCardNumberFocused,
        )
    }

    fun applyExpirationDateError(
        state: CardPaymentScreenState,
        shouldUpdateError: Boolean = true,
    ): CardPaymentScreenState {
        val error = ExpirationDateVerifier().verify(state.expirationDateState)
        return computeFooterVisibility(
            state.copy(
                expirationDateState = state.expirationDateState.copy(
                    error = if (shouldUpdateError) error else "",
                    isValid = error.isEmpty(),
                ),
            ),
        )
    }

    fun applySecurityCodeError(
        state: CardPaymentScreenState,
        shouldUpdateError: Boolean = true,
    ): CardPaymentScreenState {
        if (state.secureCodeState.optional) return computeFooterVisibility(state)
        val error = SecurityCodeVerifier().verify(state.secureCodeState)
        if (shouldUpdateError) analyticsTracker.trackInputValidation("cvv", error.isEmpty())
        return computeFooterVisibility(
            state.copy(
                secureCodeState = state.secureCodeState.copy(
                    error = if (shouldUpdateError) error else "",
                    isValid = error.isEmpty(),
                ),
            ),
        )
    }

    fun applyCardHolderError(
        state: CardPaymentScreenState,
        shouldUpdateError: Boolean = true,
    ): CardPaymentScreenState {
        val error = CardHolderVerifier().verify(state.cardHolderState)
        if (shouldUpdateError) analyticsTracker.trackInputValidation("card_holder", error.isEmpty())
        return computeFooterVisibility(
            state.copy(
                cardHolderState = state.cardHolderState.copy(
                    error = if (shouldUpdateError) error else "",
                    isValid = error.isEmpty(),
                ),
            ),
        )
    }

    fun applyIdentificationTypeError(
        state: CardPaymentScreenState,
        shouldUpdateError: Boolean = true,
    ): CardPaymentScreenState {
        val error = IdentificationTypeVerifier().verify(state.identificationTypeState)
        if (shouldUpdateError) analyticsTracker.trackInputValidation("document", error.isEmpty())
        return computeFooterVisibility(
            state.copy(
                identificationTypeState = state.identificationTypeState.copy(
                    error = if (shouldUpdateError) error else "",
                    isValid = error.isEmpty(),
                ),
            ),
        )
    }

    private fun computeFooterVisibility(
        state: CardPaymentScreenState,
    ): CardPaymentScreenState {
        val isIdentificationValid = !state.identificationTypeState.show ||
            (state.identificationTypeState.error.isEmpty() && state.identificationTypeState.isValid)
        val isFormValid = state.cardNumberState.error.isEmpty() &&
            state.cardNumberState.errorTypes.isEmpty() &&
            state.cardNumberState.isValid &&
            state.expirationDateState.error.isEmpty() &&
            state.expirationDateState.isValid &&
            state.secureCodeState.error.isEmpty() &&
            state.secureCodeState.isValid &&
            state.cardHolderState.error.isEmpty() &&
            state.cardHolderState.isValid &&
            isIdentificationValid
        return state.copy(
            fixedFooterState = state.fixedFooterState.copy(isVisible = isFormValid),
        )
    }

    private inline fun <reified T : CardNumberErrorType> updateCardNumberError(
        state: CardPaymentScreenState,
        shouldShowError: Boolean,
        errorFactory: () -> T?,
    ): CardPaymentScreenState {
        val errors = state.cardNumberState.errorTypes.toMutableList()
        errors.removeAll { it is T }
        errorFactory()?.let { errors.add(it) }
        return applyCardNumberErrorState(state, errors, errors.isEmpty(), shouldShowError)
    }
}
