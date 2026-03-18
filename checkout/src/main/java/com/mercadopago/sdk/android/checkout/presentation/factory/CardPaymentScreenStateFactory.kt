package com.mercadopago.sdk.android.checkout.presentation.factory

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState
import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState

internal class CardPaymentScreenStateFactory(
    private val stringProvider: StringProvider,
) {
    fun createInitialState(): CardPaymentScreenState {
        return CardPaymentScreenState(
            title = stringProvider.getString(R.string.card_form_title),
            cardNumberState = createCardNumberState(),
            cardHolderState = createCardHolderState(),
            expirationDateState = createExpirationDateState(),
            secureCodeState = createSecurityCodeState(),
            identificationTypeState = createIdentificationTypeState(),
        )
    }

    private fun createCardNumberState() =
        CardNumberState(
            label = stringProvider.getString(R.string.card_form_number_label),
            placeHolder = stringProvider.getString(R.string.card_form_number_placeholder),
        )

    private fun createCardHolderState() =
        CardHolderState(
            label = stringProvider.getString(R.string.card_form_holder_label),
            placeHolder = stringProvider.getString(R.string.card_form_holder_placeholder),
        )

    private fun createExpirationDateState() =
        ExpirationDateState(
            label = stringProvider.getString(R.string.card_form_expiration_short_label),
            placeHolder = stringProvider.getString(R.string.card_form_expiration_placeholder),
        )

    private fun createSecurityCodeState() =
        SecurityCodeState(
            label = stringProvider.getString(R.string.card_form_security_label),
            placeHolder = stringProvider.getString(R.string.card_form_security_placeholder),
        )

    private fun createIdentificationTypeState() =
        IdentificationTypeState(
            label = stringProvider.getString(R.string.card_form_document_label),
        )

    fun getOptionalFieldText() = stringProvider.getString(R.string.card_form_optional_field)

    fun getGenericErrorMessage() = stringProvider.getString(R.string.card_form_generic_error)
}
