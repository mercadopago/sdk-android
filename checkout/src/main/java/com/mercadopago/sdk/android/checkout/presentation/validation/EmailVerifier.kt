package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.domain.extensions.isValidEmailFormat
import com.mercadopago.sdk.android.checkout.presentation.state.EmailFieldState

internal class EmailVerifier {
    fun verify(
        value: String,
        state: EmailFieldState,
    ): String =
        listOfNotNull(
            checkEmpty(value, state),
            checkInvalid(value, state),
        ).firstOrNull().orEmpty()

    private fun checkEmpty(
        value: String,
        state: EmailFieldState,
    ): String? = if (value.isBlank()) state.validation.errorEmpty else null

    private fun checkInvalid(
        value: String,
        state: EmailFieldState,
    ): String? = if (!value.isValidEmailFormat()) state.validation.errorInvalid else null
}
