package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState

internal class CardNumberVerifier(
    private val stringProvider: StringProvider,
) {
    fun verify(
        state: CardNumberState,
    ): String =
        listOfNotNull(
            checkEmpty(state),
            checkIncomplete(state),
        ).firstOrNull().orEmpty()

    private fun checkEmpty(
        state: CardNumberState,
    ): String? {
        return if (state.length == 0) {
            stringProvider.getString(R.string.card_form_error_required_field)
        } else {
            null
        }
    }

    private fun checkIncomplete(
        state: CardNumberState,
    ): String? {
        return if (state.length > 0 && state.length < state.maxLength) {
            stringProvider.getString(R.string.card_form_error_card_number_incomplete)
        } else {
            null
        }
    }
}
