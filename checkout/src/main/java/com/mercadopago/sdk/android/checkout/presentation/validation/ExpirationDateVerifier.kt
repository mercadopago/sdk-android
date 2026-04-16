package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState

internal class ExpirationDateVerifier(
    private val stringProvider: StringProvider,
) {
    fun verify(
        state: ExpirationDateState,
    ): String =
        listOfNotNull(
            checkEmpty(state),
            checkIncomplete(state),
            checkInvalid(state),
        ).firstOrNull().orEmpty()

    private fun checkEmpty(
        state: ExpirationDateState,
    ): String? {
        return if (state.length == 0) {
            state.errorEmptyField.ifEmpty {
                stringProvider.getString(R.string.card_form_error_required_field)
            }
        } else {
            null
        }
    }

    private fun checkIncomplete(
        state: ExpirationDateState,
    ): String? {
        return if (state.length > 0 && !state.filled) {
            state.errorIncompleteField.ifEmpty {
                stringProvider.getString(R.string.card_form_error_expiration_incomplete)
            }
        } else {
            null
        }
    }

    private fun checkInvalid(
        state: ExpirationDateState,
    ): String? {
        return if (state.filled && !state.isValid) {
            state.errorInvalidField.ifEmpty {
                stringProvider.getString(R.string.card_form_error_expiration_invalid)
            }
        } else {
            null
        }
    }
}
