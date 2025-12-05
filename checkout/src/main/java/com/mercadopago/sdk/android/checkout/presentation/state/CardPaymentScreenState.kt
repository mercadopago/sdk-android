package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType


internal data class CardPaymentScreenState(
    val expirationDateState: ExpirationDateState = ExpirationDateState(),
    val secureCodeState: SecurityCodeState = SecurityCodeState(),
    val cardNumberState: CardNumberState = CardNumberState(),
    val cardHolderState: CardHolderState = CardHolderState(),
    val identificationTypeState: IdentificationTypeState = IdentificationTypeState()
)

internal data class SecurityCodeState(
    val isFocused: Boolean = false,
    val filled: Boolean = false,
    val enabled: Boolean = true,
    val helper: String = "",
    val placeHolder: String = "",
    val showPlaceHolder: Boolean =true,
    val label: String = "",
    val length: Int = 0,
    val error: Pair<Boolean, String> = Pair(false, ""),
    val secureCodeLength: Int = 3
)

internal data class ExpirationDateState(
    val isFocused: Boolean = false,
    val filled: Boolean = false,
    val enabled: Boolean = true,
    val helper: String = "",
    val placeHolder: String = "",
    val showPlaceHolder: Boolean =true,
    val label: String = "",
    val length: Int = 0,
    val error: Pair<Boolean, String> = Pair(false, ""),
    val valid: Boolean = true
)

internal data class CardNumberState(
    val image: String? = null,
    var isFocused: Boolean = false,
    var filled: Boolean = false,
    val enabled: Boolean = true,
    val helper: String = "",
    val placeHolder: String = "",
    val showPlaceHolder: Boolean =true,
    val label: String = "",
    var length: Int = 0,
    var maxLength: Int = 16,
    var mask: String = "",
    val error: Pair<Boolean, String> = Pair(false, ""),
    val isValid: Boolean = false,
    val lastFourDigits: String = "",
    val cardBin: String? = null,
)

internal data class CardHolderState (
    val show: Boolean = true,
    val isFocused: Boolean = false,
    val filled: Boolean = false,
    val enabled: Boolean = true,
    val error: Boolean = false,
    val helper: String = "",
    val placeHolder: String = "",
    val showPlaceHolder: Boolean =true,
    val label: String = "",
)

internal data class IdentificationTypeState (
    val show: Boolean = true,
    val identificationTypes: List<IdentificationType>? = null,
    val selected: IdentificationType? = null,
    val isFocused: Boolean = false,
    val filled: Boolean = false,
    val enabled: Boolean = true,
    val error: Boolean = false,
    val helper: String = "",
    val placeHolder: String = "",
    val showPlaceHolder: Boolean =true,
    val label: String = "",
)
