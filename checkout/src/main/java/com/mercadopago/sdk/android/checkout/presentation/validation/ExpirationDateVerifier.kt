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

    fun checkEmpty(
        state: ExpirationDateState,
    ): String? = if (state.length == 0) state.validation.errorEmpty else null

    fun checkIncomplete(
        state: ExpirationDateState,
    ): String? = if (state.length > 0 && !state.filled) state.validation.errorIncomplete else null

    private fun checkInvalid(
        state: ExpirationDateState,
    ): String? = if (state.filled && !state.isValid) state.validation.errorInvalid else null
}
