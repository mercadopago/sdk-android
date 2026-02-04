package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState

internal object ExpirationDateVerifier {
    fun verify(
        state: ExpirationDateState,
    ): String =
        listOfNotNull(
            checkEmpty(state),
            checkIncomplete(state),
        ).firstOrNull().orEmpty()

    private fun checkEmpty(
        state: ExpirationDateState,
    ): String? {
        return if (state.length == 0) {
            "Please, fill the expiration date"
        } else {
            null
        }
    }

    private fun checkIncomplete(
        state: ExpirationDateState,
    ): String? {
        return if (state.length > 0 && !state.filled) {
            "Please, complete the expiration date"
        } else {
            null
        }
    }
}
