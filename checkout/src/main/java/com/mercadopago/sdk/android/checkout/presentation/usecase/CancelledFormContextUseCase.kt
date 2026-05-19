package com.mercadopago.sdk.android.checkout.presentation.usecase

import com.mercadopago.sdk.android.checkout.domain.model.CancelledFieldState
import com.mercadopago.sdk.android.checkout.domain.model.CardFormUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.Field
import com.mercadopago.sdk.android.checkout.domain.model.State
import com.mercadopago.sdk.android.checkout.domain.model.UserCancelledContext
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberErrorType
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.validation.CardHolderVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.ExpirationDateVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.IdentificationTypeVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.SecurityCodeVerifier

internal class CancelledFormContextUseCase {
    operator fun invoke(
        screenState: CardPaymentScreenState,
        installmentsWasPresented: Boolean = false,
    ): UserCancelledContext.CardForm {
        val fields = buildCancelledFieldStates(screenState)
        return UserCancelledContext.CardForm(
            CardFormUserCancelledContext(
                fields = fields,
                installmentsWasPresented = installmentsWasPresented,
            ),
        )
    }

    private fun buildCancelledFieldStates(
        screenState: CardPaymentScreenState,
    ): List<CancelledFieldState> =
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

    private fun buildCardNumberFieldState(
        screenState: CardPaymentScreenState,
    ): CancelledFieldState {
        val cardNumberState = screenState.cardNumberState
        val state = cardNumberState.errorTypes.firstOrNull()?.let {
            when (it) {
                is CardNumberErrorType.CardBrandNotAccepted -> State.CardBrandNotAccepted(it.brand)
                is CardNumberErrorType.CardTypeNotAccepted -> State.CardTypeNotAccepted(it.cardType)
                is CardNumberErrorType.FieldValidation -> when {
                    cardNumberState.length == 0 -> State.Empty
                    cardNumberState.length < cardNumberState.maxLength -> State.Incomplete
                    else -> State.Invalid
                }
                else -> State.Invalid
            }
        } ?: State.Valid
        return CancelledFieldState(field = Field.CARD_NUMBER, state = state)
    }

    private fun buildCardHolderFieldState(
        screenState: CardPaymentScreenState,
    ): CancelledFieldState {
        val verifier = CardHolderVerifier()
        val cardHolderState = screenState.cardHolderState
        val state = when {
            verifier.checkEmpty(cardHolderState) != null -> State.Empty
            verifier.checkIncomplete(cardHolderState) != null -> State.Incomplete
            verifier.verify(cardHolderState).isNotEmpty() -> State.Invalid
            else -> State.Valid
        }
        return CancelledFieldState(field = Field.CARD_HOLDER, state = state)
    }

    private fun buildExpirationDateFieldState(
        screenState: CardPaymentScreenState,
    ): CancelledFieldState {
        val verifier = ExpirationDateVerifier()
        val expirationDateState = screenState.expirationDateState
        val state = when {
            verifier.checkEmpty(expirationDateState) != null -> State.Empty
            verifier.checkIncomplete(expirationDateState) != null -> State.Incomplete
            verifier.verify(expirationDateState).isNotEmpty() -> State.Invalid
            else -> State.Valid
        }
        return CancelledFieldState(field = Field.EXPIRATION_DATE, state = state)
    }

    private fun buildSecurityCodeFieldState(
        screenState: CardPaymentScreenState,
    ): CancelledFieldState {
        val verifier = SecurityCodeVerifier()
        val secureCodeState = screenState.secureCodeState
        val state = when {
            verifier.checkEmpty(secureCodeState) != null -> State.Empty
            verifier.checkIncomplete(secureCodeState) != null -> State.Incomplete
            else -> State.Valid
        }
        return CancelledFieldState(field = Field.SECURITY_CODE, state = state)
    }

    private fun buildDocumentFieldState(
        screenState: CardPaymentScreenState,
    ): CancelledFieldState {
        val verifier = IdentificationTypeVerifier()
        val identificationTypeState = screenState.identificationTypeState
        val state = when {
            verifier.checkEmpty(identificationTypeState) != null -> State.Empty
            verifier.checkIncomplete(identificationTypeState) != null -> State.Incomplete
            verifier.verify(identificationTypeState).isNotEmpty() -> State.Invalid
            else -> State.Valid
        }
        return CancelledFieldState(field = Field.DOCUMENT, state = state)
    }
}
