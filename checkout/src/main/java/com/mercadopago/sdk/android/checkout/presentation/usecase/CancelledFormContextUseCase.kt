package com.mercadopago.sdk.android.checkout.presentation.usecase

import com.mercadopago.sdk.android.checkout.domain.extensions.isComplete
import com.mercadopago.sdk.android.checkout.domain.model.CancelledFieldState
import com.mercadopago.sdk.android.checkout.domain.model.Field
import com.mercadopago.sdk.android.checkout.domain.model.MPCancelledFormContext
import com.mercadopago.sdk.android.checkout.domain.model.State
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState

internal class CancelledFormContextUseCase {
    operator fun invoke(
        screenState: CardPaymentScreenState,
    ): MPCancelledFormContext.CardForm {
        val fields = buildCancelledFieldStates(screenState)
        return MPCancelledFormContext.CardForm(fields)
    }

    @Suppress("LongMethod", "CyclomaticComplexMethod")
    private fun buildCancelledFieldStates(
        screenState: CardPaymentScreenState,
    ): List<CancelledFieldState> {
        val states = mutableListOf<CancelledFieldState>()

        states.add(
            CancelledFieldState(
                field = Field.CARD_NUMBER,
                state = when {
                    screenState.cardNumberState.error.isNotEmpty() -> State.Invalid
                    screenState.cardNumberState.length == 0 -> State.Empty
                    screenState.cardNumberState.length < screenState.cardNumberState.maxLength -> State.Incomplete
                    else -> State.Valid
                },
            ),
        )

        if (screenState.cardHolderState.show) {
            states.add(
                CancelledFieldState(
                    field = Field.CARD_HOLDER,
                    state = when {
                        screenState.cardHolderState.error.isNotEmpty() -> State.Invalid
                        screenState.cardHolderState.value.isEmpty() -> State.Empty
                        else -> State.Valid
                    },
                ),
            )
        }

        states.add(
            CancelledFieldState(
                field = Field.EXPIRATION_DATE,
                state = when {
                    screenState.expirationDateState.error.isNotEmpty() -> State.Invalid
                    screenState.expirationDateState.length == 0 -> State.Empty
                    !screenState.expirationDateState.filled -> State.Incomplete
                    else -> State.Valid
                },
            ),
        )

        if (!screenState.secureCodeState.optional) {
            states.add(
                CancelledFieldState(
                    field = Field.SECURITY_CODE,
                    state = when {
                        screenState.secureCodeState.error.isNotEmpty() -> State.Invalid
                        screenState.secureCodeState.length == 0 -> State.Empty
                        screenState.secureCodeState.length < screenState.secureCodeState.maxLength -> State.Incomplete
                        else -> State.Valid
                    },
                ),
            )
        }

        if (screenState.identificationTypeState.show) {
            states.add(
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
                ),
            )
        }

        return states
    }
}
