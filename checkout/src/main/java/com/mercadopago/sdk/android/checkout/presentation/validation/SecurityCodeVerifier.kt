package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState

internal object SecurityCodeVerifier {
    fun verify(
        state: SecurityCodeState,
    ): String {
        var error = ""
        error = checkEmpty(state) ?: error
        error = checkIncomplete(state) ?: error
        return error
    }

    private fun checkEmpty(
        state: SecurityCodeState,
    ): String? {
        return if (state.length == 0) {
            "Please, fill the security code"
        } else {
            null
        }
    }

    private fun checkIncomplete(
        state: SecurityCodeState,
    ): String? {
        return if (state.length > 0 && state.length < state.secureCodeLength) {
            "Please, complete the security code"
        } else {
            null
        }
    }
}
