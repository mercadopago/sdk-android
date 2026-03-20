package com.mercadopago.sdk.android.checkout.presentation.usecase

import com.mercadopago.sdk.android.checkout.domain.extensions.isComplete
import com.mercadopago.sdk.android.checkout.domain.model.CancelledFieldState
import com.mercadopago.sdk.android.checkout.domain.model.CardFormUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.Field
import com.mercadopago.sdk.android.checkout.domain.model.State
import com.mercadopago.sdk.android.checkout.domain.model.UserCancelledContext
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberErrorType
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState

internal class CancelledFormContextUseCase {
    operator fun invoke(
        screenState: CardPaymentScreenState,
    ): UserCancelledContext.CardForm {
        val fields = buildCancelledFieldStates(screenState)
        return UserCancelledContext.CardForm(CardFormUserCancelledContext(fields))
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
    ): CancelledFieldState =
        CancelledFieldState(
            field = Field.CARD_NUMBER,
            state = when (val errorType = screenState.cardNumberState.errorType) {
                is CardNumberErrorType.CardBrandNotAccepted -> State.CardBrandNotAccepted(errorType.brand)
                is CardNumberErrorType.CardTypeNotAccepted -> State.CardTypeNotAccepted(errorType.cardType)
                else -> when {
                    screenState.cardNumberState.error.isNotEmpty() -> State.Invalid
                    screenState.cardNumberState.length == 0 -> State.Empty
                    screenState.cardNumberState.length < screenState.cardNumberState.maxLength -> State.Incomplete
                    else -> State.Valid
                }
            },
        )

    private fun buildCardHolderFieldState(
        screenState: CardPaymentScreenState,
    ): CancelledFieldState =
        CancelledFieldState(
            field = Field.CARD_HOLDER,
            state = when {
                screenState.cardHolderState.error.isNotEmpty() -> State.Invalid
                screenState.cardHolderState.value.isEmpty() -> State.Empty
                else -> State.Valid
            },
        )

    private fun buildExpirationDateFieldState(
        screenState: CardPaymentScreenState,
    ): CancelledFieldState =
        CancelledFieldState(
            field = Field.EXPIRATION_DATE,
            state = when {
                screenState.expirationDateState.error.isNotEmpty() -> State.Invalid
                screenState.expirationDateState.length == 0 -> State.Empty
                !screenState.expirationDateState.filled -> State.Incomplete
                else -> State.Valid
            },
        )

    private fun buildSecurityCodeFieldState(
        screenState: CardPaymentScreenState,
    ): CancelledFieldState =
        CancelledFieldState(
            field = Field.SECURITY_CODE,
            state = when {
                screenState.secureCodeState.error.isNotEmpty() -> State.Invalid
                screenState.secureCodeState.length == 0 -> State.Empty
                screenState.secureCodeState.length < screenState.secureCodeState.maxLength -> State.Incomplete
                else -> State.Valid
            },
        )

    private fun buildDocumentFieldState(
        screenState: CardPaymentScreenState,
    ): CancelledFieldState =
        CancelledFieldState(
            field = Field.DOCUMENT,
            state = when {
                screenState.identificationTypeState.error.isNotEmpty() -> State.Invalid
                screenState.identificationTypeState.value.isEmpty() -> State.Empty
                !screenState.identificationTypeState.isComplete(
                    screenState.identificationTypeState.value.length,
                ) -> State.Incomplete
                else -> State.Valid
            },
        )
}
