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

    fun checkEmpty(
        state: CardHolderState,
    ): String? = if (state.value.isEmpty()) state.validation.errorEmpty else null

    fun checkIncomplete(
        state: CardHolderState,
    ): String? =
        if (state.value.isNotEmpty() && state.value.length < MIN_CHARACTERS) {
            state.validation.errorIncomplete
        } else {
            null
        }

    private fun checkFormat(
        state: CardHolderState,
    ): String? =
        if (state.value.isNotEmpty() && SPECIAL_CHARACTERS_REGEX.containsMatchIn(state.value)) {
            state.validation.errorInvalid
        } else {
            null
        }

    companion object {
        private val SPECIAL_CHARACTERS_REGEX = Regex("[^a-zA-Z\\s]")
        private const val MIN_CHARACTERS = 3
    }
}
