package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState

internal object IdentificationTypeVerifier {
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
    ): String? {
        return if (state.value.isEmpty()) {
            "Preencha este campo"
        } else {
            null
        }
    }

    private fun checkIncomplete(
        state: IdentificationTypeState,
    ): String? {
        return if (state.value.isNotEmpty() && state.value.length < (state.selected?.maxLength ?: 0)
        ) {
            "Preencha este campo"
        } else {
            null
        }
    }

    private fun checkAllZeros(
        state: IdentificationTypeState,
    ): String? {
        return if (state.value.isNotEmpty() && state.value.all { it == '0' }) {
            "Insira-o conforme está no documento."
        } else {
            null
        }
    }
}
