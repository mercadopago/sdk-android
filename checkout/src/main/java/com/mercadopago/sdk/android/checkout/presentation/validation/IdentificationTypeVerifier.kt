package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState

internal object IdentificationTypeVerifier {
    fun verify(
        state: IdentificationTypeState,
    ): String {
        var error = ""
        error = checkEmpty(state) ?: error
        error = checkIncomplete(state) ?: error
        return error
    }

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
}
