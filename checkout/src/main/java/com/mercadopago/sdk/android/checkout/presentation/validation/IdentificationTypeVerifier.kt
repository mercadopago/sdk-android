package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState

private val SPECIAL_CHARACTERS_REGEX = Regex("[^a-zA-Z\\s]")
internal object IdentificationTypeVerifier {
    fun verify(
        state: IdentificationTypeState,
    ): String =
        listOfNotNull(
            checkEmpty(state),
            checkIncomplete(state),
            checkFormat(state),
        ).firstOrNull().orEmpty()


    private fun checkEmpty(
        state: IdentificationTypeState,
    ): String? {
        return if (state.value.isEmpty()) {
            "Please, fill the identification"
        } else {
            null
        }
    }

    private fun checkIncomplete(
        state: IdentificationTypeState,
    ): String? {
        return if (state.value.isNotEmpty() && !state.filled) {
            "Please, complete the identification"
        } else {
            null
        }
    }

    private fun checkFormat(
        state: IdentificationTypeState,
    ): String? {
        return if (state.value.isNotEmpty() && SPECIAL_CHARACTERS_REGEX.containsMatchIn(state.value)) {
            "Invalid format. Use only letters and numbers"
        } else {
            null
        }
    }
}
