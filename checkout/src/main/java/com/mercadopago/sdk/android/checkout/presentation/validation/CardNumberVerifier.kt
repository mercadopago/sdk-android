package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState

internal object CardNumberVerifier {
    fun verify(state: CardNumberState): String {
        var error = ""
        error = checkEmpty(state) ?: error
        error = checkIncomplete(state) ?: error
        return error
    }

    private fun checkEmpty(state: CardNumberState): String? {
        return if (state.length == 0) {
            "Please, fill the card number"
        } else {
            null
        }
    }

    private fun checkIncomplete(state: CardNumberState): String? {
        return if (state.length > 0 && state.length < state.maxLength) {
            "Please, complete the card number"
        } else {
            null
        }
    }
}
