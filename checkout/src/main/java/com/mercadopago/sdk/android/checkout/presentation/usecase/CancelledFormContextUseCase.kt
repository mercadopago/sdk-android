package com.mercadopago.sdk.android.checkout.presentation.usecase

import com.mercadopago.sdk.android.checkout.domain.model.Field
import com.mercadopago.sdk.android.checkout.domain.model.MPCancelledFieldState
import com.mercadopago.sdk.android.checkout.domain.model.MPCardFormUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.State
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberErrorType
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.validation.CardHolderVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.ExpirationDateVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.IdentificationTypeVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.SecurityCodeVerifier

internal class CancelledFormContextUseCase {
    operator fun invoke(
        screenState: CardPaymentScreenState,
    ): MPUserCancelledContext.CardForm {
        val fields = buildCancelledFieldStates(screenState)
        return MPUserCancelledContext.CardForm(MPCardFormUserCancelledContext(fields))
    }

    private fun buildCancelledFieldStates(
        screenState: CardPaymentScreenState,
    ): List<MPCancelledFieldState> =
        buildList {
            add(buildCardNumberFieldState(screenState))
            if (screenState.cardHolderState.show) {
                add(buildCardHolderFieldState(screenState))
            }
            add(buildExpirationDateFieldState(screenState))
            if (!screenState.secureCodeState.optional) {
                add(buildSecurityCodeFieldState(screenState))
            }
            if (screenState.identificationTypeState.show) {
                add(buildDocumentFieldState(screenState))
            }
        }
        return CancelledFormContext(fields = fields, screens = presentedScreens.toList())
    }

    private fun buildCardNumberFieldState(
        screenState: CardPaymentScreenState,
    ): MPCancelledFieldState {
        val cardNumberState = screenState.cardNumberState
        val fieldValidationState = when {
            cardNumberState.length == 0 -> State.Empty
            cardNumberState.length < cardNumberState.maxLength -> State.Incomplete
            else -> State.Invalid
        }
        val state = cardNumberState.errorTypes.firstOrNull()?.let {
            when (it) {
                is CardNumberErrorType.CardBrandNotAccepted -> State.CardBrandNotAccepted(it.brand)
                is CardNumberErrorType.CardTypeNotAccepted -> State.CardTypeNotAccepted(it.cardType)
                is CardNumberErrorType.FieldValidation -> fieldValidationState
                is CardNumberErrorType.PaymentMethodNotFound -> State.Invalid
                is CardNumberErrorType.LuhnValidation -> State.Invalid
            }
        } ?: State.Valid
        return MPCancelledFieldState(field = Field.CARD_NUMBER, state = state)
    }

    private fun buildCardHolderFieldState(
        screenState: CardPaymentScreenState,
    ): MPCancelledFieldState {
        val verifier = CardHolderVerifier()
        val cardHolderState = screenState.cardHolderState
        val state = when {
            verifier.checkEmpty(cardHolderState) != null -> State.Empty
            verifier.checkIncomplete(cardHolderState) != null -> State.Incomplete
            verifier.verify(cardHolderState).isNotEmpty() -> State.Invalid
            else -> State.Valid
        }
        return MPCancelledFieldState(field = Field.CARD_HOLDER, state = state)
    }

    private fun buildExpirationDateFieldState(
        screenState: CardPaymentScreenState,
    ): MPCancelledFieldState {
        val verifier = ExpirationDateVerifier()
        val expirationDateState = screenState.expirationDateState
        val state = when {
            verifier.checkEmpty(expirationDateState) != null -> State.Empty
            verifier.checkIncomplete(expirationDateState) != null -> State.Incomplete
            verifier.verify(expirationDateState).isNotEmpty() -> State.Invalid
            else -> State.Valid
        }
        return MPCancelledFieldState(field = Field.EXPIRATION_DATE, state = state)
    }

    private fun buildSecurityCodeFieldState(
        screenState: CardPaymentScreenState,
    ): MPCancelledFieldState {
        val verifier = SecurityCodeVerifier()
        val secureCodeState = screenState.secureCodeState
        val state = when {
            verifier.checkEmpty(secureCodeState) != null -> State.Empty
            verifier.checkIncomplete(secureCodeState) != null -> State.Incomplete
            else -> State.Valid
        }
        return MPCancelledFieldState(field = Field.SECURITY_CODE, state = state)
    }

    private fun buildDocumentFieldState(
        screenState: CardPaymentScreenState,
    ): MPCancelledFieldState {
        val verifier = IdentificationTypeVerifier()
        val identificationTypeState = screenState.identificationTypeState
        val state = when {
            verifier.checkEmpty(identificationTypeState) != null -> State.Empty
            verifier.checkIncomplete(identificationTypeState) != null -> State.Incomplete
            verifier.verify(identificationTypeState).isNotEmpty() -> State.Invalid
            else -> State.Valid
        }
        return MPCancelledFieldState(field = Field.DOCUMENT, state = state)
    }
}
