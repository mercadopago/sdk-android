package com.mercadopago.sdk.android.example.presentation.coremethods.state

import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.example.data.model.Installment
import com.mercadopago.sdk.android.example.presentation.coremethods.DEFAULT_CARD_MASK
import com.mercadopago.sdk.android.example.presentation.coremethods.DEFAULT_MAX_CARD_LENGTH
import java.lang.Error

internal data class PaymentScreenViewState(
    val expirationDateState: ExpirationDateState = ExpirationDateState(),
    val secureCodeState: SecurityCodeState = SecurityCodeState(),
    val cardNumberState: CardNumberTextFieldState = CardNumberTextFieldState(),
    val installmentsState: InstallmentsState = InstallmentsState(),
    val identificationState: IdentificationState = IdentificationState(),
    val cardIssuers: List<CardIssuer> = emptyList(),
    val dialogState: PaymentScreenDialogState = PaymentScreenDialogState.Hidden,
)

internal data class SecurityCodeState(
    val isFocused: Boolean = false,
    val filled: Boolean = false,
    val length: Int = 0,
    val error: Pair<Boolean, String> = Pair(false, ""),
    val secureCodeLength: Int = 4
)

internal data class ExpirationDateState(
    val isFocused: Boolean = false,
    val filled: Boolean = false,
    val length: Int = 0,
    val error: Pair<Boolean, String> = Pair(false, ""),
    val valid: Boolean = true
)

internal data class CardNumberTextFieldState(
    val image: String? = null,
    var isFocused: Boolean = false,
    var filled: Boolean = false,
    var length: Int = 0,
    var maxLength: Int = DEFAULT_MAX_CARD_LENGTH,
    var mask: String = DEFAULT_CARD_MASK,
    val error: Pair<Boolean, String> = Pair(false, ""),
    val isValid: Boolean = false,
    val lastFourDigits: String = "",
    val cardBin: String? = null,
)

internal data class InstallmentsState(
    val showList: Boolean = false,
    val installments: List<Installment> = emptyList(),
    val selectedInstallment: Installment? = null
)

internal data class IdentificationState(
    val selectedIdentification: IdentificationType? = null,
    val identificationList: List<IdentificationType> = emptyList(),
    val identificationValue: String = "",
    val identificationNameValue: String = "",
)

internal sealed interface PaymentScreenDialogState {

    data object Hidden : PaymentScreenDialogState

    data class CardToken(val token: String) : PaymentScreenDialogState

    data class Error(
        val title: String,
        val description: String
    ) : PaymentScreenDialogState
}
