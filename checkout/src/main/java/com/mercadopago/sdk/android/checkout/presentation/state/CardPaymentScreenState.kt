package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost

internal const val CARD_NUMBER_BIN_LENGTH = 6
internal const val DEFAULT_MAX_CARD_LENGTH = 19
internal const val DEFAULT_CARD_MASK = "#### #### #### ####"

internal data class CardPaymentScreenState(
    val expirationDateState: ExpirationDateState = ExpirationDateState(),
    val secureCodeState: SecurityCodeState = SecurityCodeState(),
    val cardNumberState: CardNumberState = CardNumberState(),
    val cardHolderState: CardHolderState = CardHolderState(),
    val identificationTypeState: IdentificationTypeState = IdentificationTypeState(),
    val installmentsState: InstallmentsState = InstallmentsState(),
    val fixedFooterState: FixedFooterState = FixedFooterState(),
    val cardIssuers: List<CardIssuer> = emptyList(),
    val dialogState: CardPaymentDialogState = CardPaymentDialogState.Hidden,
    val isLoading: Boolean = false,
)

internal data class SecurityCodeState(
    val isFocused: Boolean = false,
    val filled: Boolean = false,
    val enabled: Boolean = true,
    val helper: String = "",
    val placeHolder: String = "123",
    val showPlaceHolder: Boolean = true,
    val label: String = "Security code",
    val length: Int = 0,
    val error: Pair<Boolean, String> = Pair(false, ""),
    val secureCodeLength: Int = 3,
)

internal data class ExpirationDateState(
    val isFocused: Boolean = false,
    val filled: Boolean = false,
    val enabled: Boolean = true,
    val helper: String = "",
    val placeHolder: String = "MM/YY",
    val showPlaceHolder: Boolean = true,
    val label: String = "Expiration date",
    val length: Int = 0,
    val error: Pair<Boolean, String> = Pair(false, ""),
    val valid: Boolean = true,
)

internal data class CardNumberState(
    val image: String? = null,
    val isFocused: Boolean = false,
    val filled: Boolean = false,
    val enabled: Boolean = true,
    val helper: String = "",
    val placeHolder: String = "0000 0000 0000 0000",
    val showPlaceHolder: Boolean = true,
    val label: String = "Card number",
    val length: Int = 0,
    val maxLength: Int = DEFAULT_MAX_CARD_LENGTH,
    val mask: String = DEFAULT_CARD_MASK,
    val error: Pair<Boolean, String> = Pair(false, ""),
    val isValid: Boolean = false,
    val lastFourDigits: String = "",
    val cardBin: String? = null,
)

internal data class CardHolderState(
    val show: Boolean = true,
    val isFocused: Boolean = false,
    val filled: Boolean = false,
    val enabled: Boolean = true,
    val error: Boolean = false,
    val helper: String = "",
    val placeHolder: String = "As it appears on the card",
    val showPlaceHolder: Boolean = true,
    val label: String = "Cardholder name",
    val value: String = "",
)

internal data class IdentificationTypeState(
    val show: Boolean = true,
    val identificationTypes: List<IdentificationType>? = null,
    val selected: IdentificationType? = null,
    val isFocused: Boolean = false,
    val filled: Boolean = false,
    val enabled: Boolean = true,
    val error: Boolean = false,
    val helper: String = "",
    val placeHolder: String = "Identification number",
    val showPlaceHolder: Boolean = true,
    val label: String = "Identification",
    val value: String = "",
)

internal data class InstallmentsState(
    val showList: Boolean = false,
    val installments: List<PayerCost> = emptyList(),
    val selectedInstallment: PayerCost? = null,
)

internal sealed interface CardPaymentDialogState {
    data object Hidden : CardPaymentDialogState

    data class CardToken(val token: String) : CardPaymentDialogState

    data class Error(val title: String, val description: String) : CardPaymentDialogState
}

internal data class FixedFooterState(
    val title: String = "Total",
    val currencySymbol: String = "$",
    val amountIntegerPart: String = "0",
    val amountDecimalPart: String = "00",
    val subtitle: String? = null,
    val buttonText: String = "Pagar",
    val buttonEnabled: Boolean = true,
)
