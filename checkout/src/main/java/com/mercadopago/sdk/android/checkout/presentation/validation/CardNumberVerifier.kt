package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState

internal class CardNumberVerifier {
    fun verify(
        state: CardNumberState,
    ): String =
        listOfNotNull(
            checkEmpty(state),
            checkIncomplete(state),
        ).firstOrNull().orEmpty()

    private fun checkEmpty(
        state: CardNumberState,
    ): String? = if (state.length == 0) state.validation.errorEmpty else null

    private fun checkIncomplete(
        state: CardNumberState,
    ): String? =
        if (state.length > 0 && state.length < state.maxLength) {
            state.validation.errorIncomplete
        } else {
            null
        }
}
