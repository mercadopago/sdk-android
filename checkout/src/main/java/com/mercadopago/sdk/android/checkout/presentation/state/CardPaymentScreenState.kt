package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.domain.extensions.CARD_LENGTH_19
import com.mercadopago.sdk.android.checkout.domain.extensions.CARD_LENGTH_19_MASK
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost

internal const val CARD_NUMBER_BIN_LENGTH = 6

internal data class ValidationState(
    val errorEmpty: String = "",
    val errorIncomplete: String = "",
    val errorInvalid: String = "",
)

internal data class CardPaymentScreenState(
    val title: String = "",
    val expirationDateState: ExpirationDateState = ExpirationDateState(),
    val secureCodeState: SecurityCodeState = SecurityCodeState(),
    val cardNumberState: CardNumberState = CardNumberState(),
    val cardHolderState: CardHolderState = CardHolderState(),
    val identificationTypeState: IdentificationTypeState = IdentificationTypeState(),
    val installmentsState: InstallmentsState = InstallmentsState(),
    val installmentsScreen: InstallmentsScreenState = InstallmentsScreenState(),
    val paymentState: PaymentState = PaymentState(),
    val fixedFooterState: FixedFooterState = FixedFooterState(),
    val cardIssuers: List<CardIssuer> = emptyList(),
    val messageError: MessageError = MessageError(),
    val isLoading: Boolean = false,
    val showTooltip: Boolean = false,
    val showMessage: Boolean = false,
)

internal data class SecurityCodeState(
    override val label: String = "",
    override val helper: String = "",
    override val placeHolder: String = "",
    override val error: String = "",
    override val isFocused: Boolean = false,
    override val filled: Boolean = false,
    override val enabled: Boolean = true,
    override val isValid: Boolean = false,
    override val showPlaceHolder: Boolean = true,
    val length: Int = 0,
    val optional: Boolean = false,
    val maxLength: Int = 3,
    val messageTooltip: String = "",
    val validation: ValidationState = ValidationState(),
) : FieldState

internal data class ExpirationDateState(
    override val label: String = "",
    override val helper: String = "",
    override val placeHolder: String = "",
    override val error: String = "",
    override val isFocused: Boolean = false,
    override val filled: Boolean = false,
    override val enabled: Boolean = true,
    override val isValid: Boolean = false,
    override val showPlaceHolder: Boolean = true,
    val length: Int = 0,
    val validation: ValidationState = ValidationState(),
) : FieldState

internal data class CardNumberState(
    override val label: String = "",
    override val helper: String = "",
    override val placeHolder: String = "",
    override val error: String = "",
    override val isFocused: Boolean = false,
    override val filled: Boolean = false,
    override val enabled: Boolean = true,
    override val isValid: Boolean = false,
    override val showPlaceHolder: Boolean = true,
    val image: String? = null,
    val length: Int = 0,
    val maxLength: Int = CARD_LENGTH_19,
    val mask: String = CARD_LENGTH_19_MASK,
    val lastFourDigits: String = "",
    val cardBin: String? = null,
    val errorTypes: List<CardNumberErrorType> = listOf(),
    val validation: ValidationState = ValidationState(),
) : FieldState

internal data class CardHolderState(
    override val label: String = "",
    override val helper: String = "",
    override val placeHolder: String = "",
    override val error: String = "",
    override val isFocused: Boolean = false,
    override val filled: Boolean = false,
    override val enabled: Boolean = true,
    override val isValid: Boolean = false,
    override val showPlaceHolder: Boolean = true,
    val show: Boolean = true,
    val value: String = "",
    val validation: ValidationState = ValidationState(),
) : FieldState

internal data class IdentificationTypeState(
    override val label: String = "",
    override val helper: String = "",
    override val placeHolder: String = "",
    override val error: String = "",
    override val isFocused: Boolean = false,
    override val filled: Boolean = false,
    override val enabled: Boolean = true,
    override val isValid: Boolean = false,
    override val showPlaceHolder: Boolean = true,
    val show: Boolean = true,
    val identificationTypes: List<IdentificationType>? = null,
    val selected: IdentificationType? = null,
    val value: String = "",
    val validation: ValidationState = ValidationState(),
) : FieldState

internal data class InstallmentsState(
    val showList: Boolean = false,
    val installments: List<PayerCost> = emptyList(),
    val headerChevron: String = "",
    val headerRadio: String = "",
    val interestFreeLabel: String = "",
    val totalLabel: String = "",
    val payButtonLabel: String = "",
    val displayType: InstallmentsDisplayType = InstallmentsDisplayType.RadioButton,
)

internal data class MessageError(
    val title: String = "",
    val description: String = "",
)

internal data class FixedFooterState(
    val title: String = "",
    val currencySymbol: String = "",
    val amountIntegerPart: String = "",
    val amountDecimalPart: String = "",
    val subtitle: String? = null,
    val buttonText: String = "",
    val isVisible: Boolean = false,
)

internal data class PaymentState(
    val paymentMethodId: String? = null,
    val paymentTypeId: String? = null,
)
