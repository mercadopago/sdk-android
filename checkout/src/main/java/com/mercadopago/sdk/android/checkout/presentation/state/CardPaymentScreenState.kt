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
    val paymentState: PaymentState = PaymentState(),
    val fixedFooterState: FixedFooterState = FixedFooterState(),
    val cardIssuers: List<CardIssuer> = emptyList(),
    val messageError: MessageError = MessageError(),
    val isLoading: Boolean = false,
    val showTooltip: Boolean = false,
    val showMessage: Boolean = false,
)

internal data class SecurityCodeState(
    override val label: String = "Código de Segurança",
    override val helper: String = "",
    override val placeHolder: String = "Ex: 123",
    override val error: String = "",
    override val isFocused: Boolean = false,
    override val filled: Boolean = false,
    override val enabled: Boolean = true,
    override val isValid: Boolean = false,
    override val showPlaceHolder: Boolean = true,
    val length: Int = 0,
    val optional: Boolean = false,
    val maxLength: Int = 3,
    val messageTooltip: String = "É um número de 3 dígitos que está na parte da frente do seu cartão.",
) : FieldState

internal data class ExpirationDateState(
    override val label: String = "Validade",
    override val helper: String = "",
    override val placeHolder: String = "MM/AA",
    override val error: String = "",
    override val isFocused: Boolean = false,
    override val filled: Boolean = false,
    override val enabled: Boolean = true,
    override val isValid: Boolean = false,
    override val showPlaceHolder: Boolean = true,
    val length: Int = 0,
) : FieldState

internal data class CardNumberState(
    override val label: String = "Número do cartão",
    override val helper: String = "",
    override val placeHolder: String = "0000 0000 0000 0000",
    override val error: String = "",
    override val isFocused: Boolean = false,
    override val filled: Boolean = false,
    override val enabled: Boolean = true,
    override val isValid: Boolean = false,
    override val showPlaceHolder: Boolean = true,
    val image: String? = null,
    val length: Int = 0,
    val maxLength: Int = DEFAULT_MAX_CARD_LENGTH,
    val mask: String = DEFAULT_CARD_MASK,
    val lastFourDigits: String = "",
    val cardBin: String? = null,
    val errorType: CardNumberErrorType = CardNumberErrorType.NONE,
) : FieldState

internal data class CardHolderState(
    override val label: String = "Nome do titular",
    override val helper: String = "",
    override val placeHolder: String = "Nome completo",
    override val error: String = "",
    override val isFocused: Boolean = false,
    override val filled: Boolean = false,
    override val enabled: Boolean = true,
    override val isValid: Boolean = false,
    override val showPlaceHolder: Boolean = true,
    val show: Boolean = true,
    val value: String = "",
) : FieldState

internal data class IdentificationTypeState(
    override val label: String = "Tipo de documento",
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
) : FieldState

internal data class InstallmentsState(
    val showList: Boolean = false,
    val installments: List<PayerCost> = emptyList(),
    val selectedInstallment: PayerCost? = null,
)

internal data class MessageError(
    val title: String = "",
    val description: String = "",
)

internal data class FixedFooterState(
    val title: String = "Total",
    val currencySymbol: String = "$",
    val amountIntegerPart: String = "0",
    val amountDecimalPart: String = "00",
    val subtitle: String? = null,
    val buttonText: String = "Salvar Cartao",
    val buttonEnabled: Boolean = false,
)

internal data class PaymentState(
    val paymentMethodId: String? = null,
    val paymentTypeId: String? = null,
)
