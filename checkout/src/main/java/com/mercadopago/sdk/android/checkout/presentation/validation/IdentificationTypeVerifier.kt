package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState

internal class IdentificationTypeVerifier {
    fun verify(
        state: IdentificationTypeState,
    ): String =
        listOfNotNull(
            checkEmpty(state),
            checkIncomplete(state),
            checkAllZeros(state),
        ).firstOrNull().orEmpty()

    private fun checkEmpty(
        state: IdentificationTypeState,
    ): String? = if (state.value.isEmpty()) state.validation.errorEmpty else null

    private fun checkIncomplete(
        state: IdentificationTypeState,
    ): String? {
        val minLength = state.selected?.minLength ?: 0
        val maxLength = state.selected?.maxLength

        return when {
            maxLength == null -> null
            state.value.length !in minLength..maxLength -> state.validation.errorIncomplete
            else -> null
        }
    }

    private fun checkAllZeros(
        state: IdentificationTypeState,
    ): String? =
        if (state.value.isNotEmpty() && state.value.all { it == '0' }) {
            state.validation.errorInvalid
        } else {
            null
        }
}
