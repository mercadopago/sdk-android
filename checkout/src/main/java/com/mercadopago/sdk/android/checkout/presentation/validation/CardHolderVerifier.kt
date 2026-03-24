package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState

internal class CardHolderVerifier(
    private val stringProvider: StringProvider,
) {
    fun verify(
        state: CardHolderState,
    ): String =
        listOfNotNull(
            checkEmpty(state),
            checkIncomplete(state),
            checkFormat(state),
        ).firstOrNull().orEmpty()

    private fun checkEmpty(
        state: CardHolderState,
    ): String? {
        return if (state.value.isEmpty()) {
            stringProvider.getString(R.string.card_form_error_required_field)
        } else {
            null
        }
    }

    private fun checkIncomplete(
        state: CardHolderState,
    ): String? {
        return if (state.value.isNotEmpty() && state.value.length < MIN_CHARACTERS) {
            stringProvider.getString(R.string.card_form_error_cardholder_incomplete)
        } else {
            null
        }
    }

    private fun checkFormat(
        state: CardHolderState,
    ): String? {
        return if (state.value.isNotEmpty() && SPECIAL_CHARACTERS_REGEX.containsMatchIn(state.value)) {
            stringProvider.getString(R.string.card_form_error_cardholder_format)
        } else {
            null
        }
    }

    companion object {
        private val SPECIAL_CHARACTERS_REGEX = Regex("[^a-zA-Z\\s]")
        private const val MIN_CHARACTERS = 3
    }
}
