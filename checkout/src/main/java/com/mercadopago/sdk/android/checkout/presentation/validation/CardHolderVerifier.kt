package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState

internal class CardHolderVerifier {
    fun verify(
        state: CardHolderState,
    ): String =
        listOfNotNull(
            checkEmpty(state),
            checkIncomplete(state),
            checkFormat(state),
        ).firstOrNull().orEmpty()

    private fun checkEmpty(
        state: CardHolderState,
    ): String? {
        return if (state.value.isEmpty()) {
            state.errorEmptyField
        } else {
            null
        }
    }

    private fun checkIncomplete(
        state: CardHolderState,
    ): String? {
        return if (state.value.isNotEmpty() && state.value.length < MIN_CHARACTERS) {
            state.errorIncompleteField
        } else {
            null
        }
    }

    private fun checkFormat(
        state: CardHolderState,
    ): String? {
        return if (state.value.isNotEmpty() && SPECIAL_CHARACTERS_REGEX.containsMatchIn(state.value)) {
            state.errorInvalidField
        } else {
            null
        }
    }

    companion object {
        private val SPECIAL_CHARACTERS_REGEX = Regex("[^a-zA-Z\\s]")
        private const val MIN_CHARACTERS = 3
    }
}
