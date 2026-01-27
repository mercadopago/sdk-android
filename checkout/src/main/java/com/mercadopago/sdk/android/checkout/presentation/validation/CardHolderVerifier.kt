package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState

internal object CardHolderVerifier {
    private val SPECIAL_CHARACTERS_REGEX = Regex("[^a-zA-Z\\s]")

    fun verify(state: CardHolderState): String {
        var error = ""
        error = checkEmpty(state) ?: error
        error = checkIncomplete(state) ?: error
        error = checkFormat(state) ?: error
        return error
    }

    private fun checkEmpty(state: CardHolderState): String? {
        return if (state.value.isEmpty()) {
            "Please, fill the cardholder name"
        } else {
            null
        }
    }

    private fun checkIncomplete(state: CardHolderState): String? {
        return if (state.value.isNotEmpty() && state.value.length > 12) {
            "Please, complete the cardholder name"
        } else {
            null
        }
    }

    private fun checkFormat(state: CardHolderState): String? {
        return if (state.value.isNotEmpty() && SPECIAL_CHARACTERS_REGEX.containsMatchIn(state.value)) {
            "Invalid format. Use only letters and spaces"
        } else {
            null
        }
    }
}
