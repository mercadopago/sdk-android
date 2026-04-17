package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.CardHolderState
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.ExpirationDateState
import com.mercadopago.sdk.android.checkout.presentation.state.IdentificationTypeState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState

internal class CardPaymentValidator {
    private val cardHolderVerifier = CardHolderVerifier()
    private val cardNumberVerifier = CardNumberVerifier()
    private val expirationDateVerifier = ExpirationDateVerifier()
    private val securityCodeVerifier = SecurityCodeVerifier()
    private val identificationTypeVerifier = IdentificationTypeVerifier()

    fun validateCardHolder(
        state: CardHolderState,
    ): String = cardHolderVerifier.verify(state)

    fun validateCardNumber(
        state: CardNumberState,
    ): String = cardNumberVerifier.verify(state)

    fun validateExpirationDate(
        state: ExpirationDateState,
    ): String = expirationDateVerifier.verify(state)

    fun validateSecurityCode(
        state: SecurityCodeState,
    ): String = securityCodeVerifier.verify(state)

    fun validateIdentificationType(
        state: IdentificationTypeState,
    ): String = identificationTypeVerifier.verify(state)
}
