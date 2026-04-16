package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState

internal class ExpirationDateVerifier {
    fun verify(
        state: ExpirationDateState,
    ): String =
        listOfNotNull(
            checkEmpty(state),
            checkIncomplete(state),
            checkInvalid(state),
        ).firstOrNull().orEmpty()

    private fun checkEmpty(
        state: ExpirationDateState,
    ): String? {
        return if (state.length == 0) {
            state.errorEmptyField
        } else {
            null
        }
    }

    private fun checkIncomplete(
        state: ExpirationDateState,
    ): String? {
        return if (state.length > 0 && !state.filled) {
            state.errorIncompleteField
        } else {
            null
        }
    }

    private fun checkInvalid(
        state: ExpirationDateState,
    ): String? {
        return if (state.filled && !state.isValid) {
            state.errorInvalidField
        } else {
            null
        }
    }
}
