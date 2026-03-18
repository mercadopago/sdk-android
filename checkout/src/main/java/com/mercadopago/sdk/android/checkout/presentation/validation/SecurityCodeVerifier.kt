package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState

internal class SecurityCodeVerifier(
    private val stringProvider: StringProvider,
) {
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
            stringProvider.getString(R.string.card_form_error_required_field)
        } else {
            null
        }
    }

    private fun checkIncomplete(
        state: SecurityCodeState,
    ): String? {
        return if (state.length > 0 && state.length < state.maxLength) {
            stringProvider.getString(R.string.card_form_error_cvv_incomplete)
        } else {
            null
        }
    }
}
