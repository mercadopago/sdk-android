package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState

internal class IdentificationTypeVerifier(
    private val stringProvider: StringProvider,
) {
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
            stringProvider.getString(R.string.card_form_error_required_field)
        } else {
            null
        }
    }

    private fun checkIncomplete(
        state: IdentificationTypeState,
    ): String? {
        val minLength = state.selected?.minLength ?: 0
        val maxLength = state.selected?.maxLength

        return when {
            maxLength == null -> null
            state.value.length !in minLength..maxLength ->
                stringProvider.getString(R.string.card_form_error_required_field)
            else -> null
        }
    }

    private fun checkAllZeros(
        state: IdentificationTypeState,
    ): String? {
        return if (state.value.isNotEmpty() && state.value.all { it == '0' }) {
            stringProvider.getString(R.string.card_form_error_document_all_zeros)
        } else {
            null
        }
    }
}
