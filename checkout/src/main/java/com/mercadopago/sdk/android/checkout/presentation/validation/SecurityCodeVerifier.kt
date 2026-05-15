package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState

internal class SecurityCodeVerifier {
    fun verify(
        state: SecurityCodeState,
    ): String =
        listOfNotNull(
            checkEmpty(state),
            checkIncomplete(state),
        ).firstOrNull().orEmpty()

    fun checkEmpty(
        state: SecurityCodeState,
    ): String? = if (state.length == 0) state.validation.errorEmpty else null

    fun checkIncomplete(
        state: SecurityCodeState,
    ): String? =
        if (state.length > 0 && state.length < state.maxLength) {
            state.validation.errorIncomplete
        } else {
            null
        }
}
