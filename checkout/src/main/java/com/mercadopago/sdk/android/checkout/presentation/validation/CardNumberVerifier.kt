package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.extensions.checkAllSameDigits
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState

internal object CardNumberVerifier {
    fun verify(
        state: CardNumberState,
    ): String? =
        listOfNotNull(
            checkEmpty(state),
            checkIncomplete(state),
            verifyAllSameDigits(state),
        ).firstOrNull()

    private fun verifyAllSameDigits(
        state: CardNumberState,
    ): String? {
        return if (state.checkAllSameDigits()) {
            "Insira-o conforme está no cartão."
        } else {
            null
        }
    }

    private fun checkEmpty(
        state: CardNumberState,
    ): String? {
        return if (state.length == 0) {
            "Preencha esse campo."
        } else {
            null
        }
    }

    private fun checkIncomplete(
        state: CardNumberState,
    ): String? {
        return if (state.length > 0 && state.length < state.maxLength) {
            "Insira o número completo"
        } else {
            null
        }
    }
}
