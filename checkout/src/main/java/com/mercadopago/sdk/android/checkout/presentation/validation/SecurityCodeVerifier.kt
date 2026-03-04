package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState

internal object SecurityCodeVerifier {
    fun verify(
        state: SecurityCodeState,
    ): String =
        listOfNotNull(
            checkEmpty(state),
            checkIncomplete(state),
        ).firstOrNull().orEmpty()

    private fun checkEmpty(
        state: SecurityCodeState,
    ): String? {
        return if (state.length == 0) {
            "Preencha este campo"
        } else {
            null
        }
    }

    private fun checkIncomplete(
        state: SecurityCodeState,
    ): String? {
        return if (state.length > 0 && state.length < state.maxLength) {
            "Insira o código completo"
        } else {
            null
        }
    }
}
