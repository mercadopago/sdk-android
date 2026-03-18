package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.getCardFormAmount
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.mapper.getLength
import com.mercadopago.sdk.android.checkout.domain.mapper.getMessage
import com.mercadopago.sdk.android.checkout.domain.mapper.isComplete
import com.mercadopago.sdk.android.checkout.domain.mapper.isOptional
import com.mercadopago.sdk.android.checkout.domain.mapper.toMask
import com.mercadopago.sdk.android.checkout.domain.model.CardData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.Payer
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCode
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardDataByBinUseCase
import com.mercadopago.sdk.android.checkout.presentation.extensions.fold
import com.mercadopago.sdk.android.checkout.presentation.extensions.toCountStringPlaceholder
import com.mercadopago.sdk.android.checkout.presentation.state.CARD_NUMBER_BIN_LENGTH
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberErrorType
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.DEFAULT_MAX_CARD_LENGTH
import com.mercadopago.sdk.android.checkout.presentation.state.MessageError
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentState
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateCardTokenUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GetIdentificationTypesUseCase
import com.mercadopago.sdk.android.checkout.presentation.validation.CardHolderVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.CardNumberVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.ExpirationDateVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.IdentificationTypeVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.SecurityCodeVerifier
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield.IdentificationTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.simpletextfield.SimpleTextFieldEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

private const val HELPER_TEXT_OPTIONAL = "Dado opcional"
private const val GENERIC_ERROR_MESSAGE_FOR_CALLS = "Ocorreu um erro. Por favor, tente novamente."

@Suppress(
    "TooManyFunctions",
)
internal class CardPaymentViewModel(
    private val checkoutConfiguration: CheckoutConfiguration?,
    private val getCardDataByBinUseCase: GetCardDataByBinUseCase,
    private val getIdentificationTypesUseCase: GetIdentificationTypesUseCase,
    private val generateCardTokenUseCase: GenerateCardTokenUseCase,
) : ViewModel() {
    private val _viewState = MutableStateFlow(CardPaymentScreenState())
    val viewState: StateFlow<CardPaymentScreenState> = _viewState

    fun getIdentificationTypes() {
        viewModelScope.launch {
            updateLoadingState(true)
            getIdentificationTypesUseCase().fold(
                onSuccess = { data ->
                    _viewState.value = _viewState.value.copy(
                        identificationTypeState = _viewState.value.identificationTypeState.copy(
                            identificationTypes = data,
                            selected = data.firstOrNull(),
                        ),
                    )
                },
                onError = { error ->
                    CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(error))
                },
            ).apply {
                updateLoadingState(false)
            }
        }
    }

    fun getPaymentMethods(
        bin: String,
        amount: BigDecimal?,
    ) {
        viewModelScope.launch {
            getCardDataByBinUseCase(
                bin = bin,
                amount = amount,
                paymentMethods = checkoutConfiguration?.paymentMethods,
            ).fold(
                onSuccess = { cardData ->
                    updateStateWithCardData(cardData)
                    updateCardMaskState(cardData.getLength())
                },
                onError = { error ->
                    if (error is MercadoPagoCheckoutError.ServiceError) {
                        updateError(error.errorMessage) { errorMsg, isValid ->
                            copy(
                                cardNumberState = cardNumberState.copy(
                                    error = errorMsg,
                                    isValid = isValid,
                                    errorType = CardNumberErrorType.BIN_VALIDATION,
                                ),
                            )
                        }
                    } else {
                        if (_viewState.value.cardNumberState.isComplete()) {
                            handleResultError(error)
                        }
                    }
                },
            )
        }
    }

    fun onExpirationDateEvent(
        event: ExpirationDateTextFieldEvent,
    ) {
        when (event) {
            is ExpirationDateTextFieldEvent.OnInputFilled -> {
                _viewState.value = _viewState.value.copy(
                    expirationDateState = _viewState.value.expirationDateState.copy(
                        filled = event.isFilled,
                    ),
                )
                if (event.isFilled) {
                    handleExpirationDateInputError()
                }
            }

            is ExpirationDateTextFieldEvent.IsValid -> {
                _viewState.value = _viewState.value.copy(
                    expirationDateState = _viewState.value.expirationDateState.copy(
                        isValid = event.isValid,
                    ),
                )
            }

            is ExpirationDateTextFieldEvent.OnFocusChanged -> {
                _viewState.value = _viewState.value.copy(
                    expirationDateState = _viewState.value.expirationDateState.copy(
                        isFocused = event.isFocused,
                    ),
                )
                if (!event.isFocused) {
                    handleExpirationDateInputError()
                }
            }

            is ExpirationDateTextFieldEvent.OnLengthChanged -> {
                _viewState.value = _viewState.value.copy(
                    expirationDateState = _viewState.value.expirationDateState.copy(
                        length = event.length,
                    ),
                )
            }
        }
    }

    fun onSecurityCodeEvent(
        event: SecurityCodeTextFieldEvent,
    ) {
        when (event) {
            is SecurityCodeTextFieldEvent.OnFocusChanged -> {
                _viewState.value = _viewState.value.copy(
                    secureCodeState = _viewState.value.secureCodeState.copy(
                        isFocused = event.isFocused,
                    ),
                )
                if (!event.isFocused) {
                    handleSecurityCodeInputError()
                }
            }

            is SecurityCodeTextFieldEvent.OnLengthChanged -> {
                _viewState.value = _viewState.value.copy(
                    secureCodeState = _viewState.value.secureCodeState.copy(
                        length = event.length,
                    ),
                )
                if (event.length == _viewState.value.secureCodeState.maxLength) {
                    handleSecurityCodeInputError()
                }
            }

            is SecurityCodeTextFieldEvent.OnInputFilled -> {
                _viewState.value = _viewState.value.copy(
                    secureCodeState = _viewState.value.secureCodeState.copy(
                        filled = event.isFilled,
                    ),
                )
            }
        }
    }

    fun onCardNumberEvent(
        event: CardNumberTextFieldEvent,
    ) {
        when (event) {
            is CardNumberTextFieldEvent.OnFocusChanged -> {
                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        isFocused = event.isFocused,
                    ),
                )
                if (!event.isFocused) {
                    handleCardNumberInputError()
                    val cardNumberState = _viewState.value.cardNumberState
                    checkoutConfiguration?.takeIf { cardNumberState.isComplete() }?.let {
                        getPaymentMethods(cardNumberState.cardBin.orEmpty(), it.getCardFormAmount())
                    }
                }
            }

            is CardNumberTextFieldEvent.OnLengthChanged -> {
                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        length = event.length,
                    ),
                )
            }

            is CardNumberTextFieldEvent.OnLastFourDigitsFilled -> {
                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        lastFourDigits = event.lastFourDigits,
                    ),
                )
                if (_viewState.value.cardNumberState.isComplete()) {
                    handleCardNumberInputError()
                }
            }

            is CardNumberTextFieldEvent.OnBinChanged -> {
                handleBinChanged(event.cardBin)
            }
        }
    }

    fun onIdentificationTypeValueChanged(
        value: String,
    ) {
        _viewState.value = _viewState.value.copy(
            identificationTypeState = _viewState.value.identificationTypeState.copy(
                value = value,
            ),
        )
    }

    fun onIdentificationTypeChanged(
        identificationType: IdentificationType,
    ) {
        _viewState.value = _viewState.value.copy(
            identificationTypeState = _viewState.value.identificationTypeState.copy(
                selected = identificationType,
            ),
        )
    }

    fun onCardHolderNameChanged(
        value: String,
    ) {
        _viewState.value = _viewState.value.copy(
            cardHolderState = _viewState.value.cardHolderState.copy(
                value = value,
            ),
        )
    }

    fun onTooltipClick() {
        _viewState.value = _viewState.value.copy(
            showTooltip = !_viewState.value.showTooltip,
        )
    }

    fun onMessageClick() {
        _viewState.value = _viewState.value.copy(
            showMessage = false,
        )
    }

    fun onCardHolderEvent(
        event: SimpleTextFieldEvent,
    ) {
        when (event) {
            is SimpleTextFieldEvent.OnValueChanged -> {
                _viewState.value = _viewState.value.copy(
                    cardHolderState = _viewState.value.cardHolderState.copy(
                        value = event.value,
                    ),
                )
            }

            is SimpleTextFieldEvent.OnFocusChanged -> {
                _viewState.value = _viewState.value.copy(
                    cardHolderState = _viewState.value.cardHolderState.copy(
                        isFocused = event.isFocused,
                    ),
                )
                if (!event.isFocused) {
                    handleCardHolderInputError()
                }
            }
        }
    }

    fun onIdentificationEvent(
        event: IdentificationTextFieldEvent,
    ) {
        when (event) {
            is IdentificationTextFieldEvent.OnValueChanged -> {
                _viewState.value = _viewState.value.copy(
                    identificationTypeState = _viewState.value.identificationTypeState.copy(
                        value = event.value,
                    ),
                )
                if (event.value.length == _viewState.value.identificationTypeState.selected?.maxLength) {
                    handleIdentificationTypeInputError()
                }
            }

            is IdentificationTextFieldEvent.OnFocusChanged -> {
                _viewState.value = _viewState.value.copy(
                    identificationTypeState = _viewState.value.identificationTypeState.copy(
                        isFocused = event.isFocused,
                    ),
                )
                if (!event.isFocused) {
                    handleIdentificationTypeInputError()
                }
            }

            is IdentificationTextFieldEvent.OnTypeSelected -> {
                _viewState.value = _viewState.value.copy(
                    identificationTypeState = _viewState.value.identificationTypeState.copy(
                        selected = event.identificationType,
                    ),
                )
            }
        }
    }

    private fun updateStateWithCardData(
        cardData: CardData,
    ) {
        _viewState.value = _viewState.value.copy(
            secureCodeState = buildSecurityCodeState(cardData.securityCode),
            cardIssuers = listOfNotNull(cardData.cardIssuer),
            cardNumberState = _viewState.value.cardNumberState.copy(
                image = cardData.cardIssuer?.thumbnail,
                error = "",
                errorType = CardNumberErrorType.NONE,
            ),
            installmentsState = buildInstallmentsState(cardData.installments),
            paymentState = PaymentState(
                paymentTypeId = cardData.paymentMethod.paymentTypeId,
                paymentMethodId = cardData.paymentMethod.id,
            ),
        )
    }

    private fun buildSecurityCodeState(
        securityCode: SecurityCode,
    ) = _viewState.value.secureCodeState.copy(
        maxLength = securityCode.length,
        placeHolder = securityCode.length.toCountStringPlaceholder("Ex:"),
        optional = securityCode.isOptional(),
        helper = if (securityCode.isOptional()) HELPER_TEXT_OPTIONAL else "",
        messageTooltip = securityCode.getMessage(),
    )

    private fun buildInstallmentsState(
        installments: List<com.mercadopago.sdk.android.coremethods.domain.model.Installment>?,
    ) = _viewState.value.installmentsState.copy(
        showList = installments.isNullOrEmpty().not(),
        installments = installments?.firstOrNull()?.payerCost.orEmpty(),
    )

    fun validateFieldsAndTokenize(
        cardNumberState: PCIFieldState,
        expirationDateState: PCIFieldState,
        securityCodeState: PCIFieldState,
    ) {
        _viewState.value.let { state ->
            val hasErrors = state.cardNumberState.error.isNotEmpty() ||
                state.expirationDateState.error.isNotEmpty() ||
                state.secureCodeState.error.isNotEmpty() ||
                state.cardHolderState.error.isNotEmpty() ||
                state.identificationTypeState.error.isNotEmpty()
            if (!hasErrors) {
                generateToken(
                    cardNumberState = cardNumberState,
                    expirationDateState = expirationDateState,
                    securityCodeState = securityCodeState,
                    buyerIdentification = BuyerIdentification(
                        name = state.cardHolderState.value,
                        number = state.identificationTypeState.value,
                        type = state.identificationTypeState.selected?.name,
                    ),
                )
            }
        }
    }

    private fun generateToken(
        cardNumberState: PCIFieldState,
        expirationDateState: PCIFieldState,
        securityCodeState: PCIFieldState,
        buyerIdentification: BuyerIdentification,
    ) {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            updateLoadingState(true)
            generateCardTokenUseCase(
                cardNumberState = cardNumberState,
                expirationDateState = expirationDateState,
                securityCodeState = securityCodeState,
                buyerIdentification = buyerIdentification,
            ).fold(
                onSuccess = { cardToken ->
                    val paymentData = MPPaymentData(
                        transactionAmount = checkoutConfiguration?.getCardFormAmount()?.toInt() ?: 0,
                        token = cardToken.token,
                        installment = 1,
                        paymentMethodId = viewState.value.paymentState.paymentMethodId.orEmpty(),
                        paymentTypeId = viewState.value.paymentState.paymentTypeId.orEmpty(),
                        issuerId = viewState.value.cardIssuers.firstOrNull()?.id,
                        payer = Payer(
                            documentType = buyerIdentification.type,
                            documentNumber = buyerIdentification.number,
                        ),
                    )
                    CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Success(paymentData))
                },
                onError = { checkoutError ->
                    CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(checkoutError))
                },
            ).apply {
                updateLoadingState(false)
            }
        }
    }

    private fun handleBinChanged(
        cardBin: String?,
    ) {
        if ((cardBin?.length ?: 0) < CARD_NUMBER_BIN_LENGTH) {
            val currentState = _viewState.value.cardNumberState
            _viewState.value = _viewState.value.copy(
                cardNumberState = currentState.copy(
                    image = null,
                    error = if (currentState.errorType == CardNumberErrorType.BIN_VALIDATION) {
                        ""
                    } else {
                        currentState.error
                    },
                    errorType = CardNumberErrorType.NONE,
                ),
                installmentsState = _viewState.value.installmentsState.copy(showList = false),
            )
            updateCardMaskState(DEFAULT_MAX_CARD_LENGTH)
        } else {
            if (checkoutConfiguration?.checkoutType is CheckoutType.CardForm) {
                getPaymentMethods(
                    bin = cardBin.orEmpty(),
                    amount = checkoutConfiguration.checkoutType.cardFormConfiguration?.amount,
                )
            }
        }
        _viewState.value = _viewState.value.copy(
            cardNumberState = _viewState.value.cardNumberState.copy(
                cardBin = cardBin,
            ),
        )
    }

    private fun handleCardNumberInputError() {
        val currentState = _viewState.value
        val cardNumberError = CardNumberVerifier.verify(currentState.cardNumberState)
        if (currentState.cardNumberState.errorType != CardNumberErrorType.BIN_VALIDATION) {
            updateError(cardNumberError) { error, isValid ->
                copy(
                    cardNumberState = cardNumberState.copy(
                        error = error,
                        isValid = isValid,
                        errorType = if (error.isEmpty()) {
                            CardNumberErrorType.NONE
                        } else {
                            CardNumberErrorType.FIELD_VALIDATION
                        },
                    ),
                )
            }
        }
        hasFormErrors()
    }

    private fun updateError(
        error: String,
        updateState: CardPaymentScreenState.(String, Boolean) -> CardPaymentScreenState,
    ) {
        val isValid = error.isEmpty()
        _viewState.value = _viewState.value.updateState(error, isValid)
    }

    private fun handleExpirationDateInputError() {
        val currentState = _viewState.value
        val expirationDateError = ExpirationDateVerifier.verify(currentState.expirationDateState)
        updateError(expirationDateError) { error, isValid ->
            copy(
                expirationDateState = expirationDateState.copy(
                    error = error,
                    isValid = isValid,
                ),
            )
        }
        hasFormErrors()
    }

    private fun handleSecurityCodeInputError() {
        val currentState = _viewState.value
        if (!currentState.secureCodeState.optional) {
            val securityCodeError = SecurityCodeVerifier.verify(currentState.secureCodeState)
            updateError(securityCodeError) { error, isValid ->
                copy(
                    secureCodeState = secureCodeState.copy(
                        error = error,
                        isValid = isValid,
                    ),
                )
            }
        }
        hasFormErrors()
    }

    private fun handleCardHolderInputError() {
        val currentState = _viewState.value
        val cardHolderError = CardHolderVerifier.verify(currentState.cardHolderState)
        updateError(cardHolderError) { error, isValid ->
            copy(
                cardHolderState = cardHolderState.copy(
                    error = error,
                    isValid = isValid,
                ),
            )
        }
        hasFormErrors()
    }

    private fun handleIdentificationTypeInputError() {
        val currentState = _viewState.value
        val identificationError =
            IdentificationTypeVerifier.verify(currentState.identificationTypeState).orEmpty()
        updateError(identificationError) { error, isValid ->
            copy(
                identificationTypeState = identificationTypeState.copy(
                    error = error,
                    isValid = isValid,
                ),
            )
        }
        hasFormErrors()
    }

    private fun handleResultError(
        error: MercadoPagoCheckoutError,
    ) {
        _viewState.value = _viewState.value.copy(
            messageError = MessageError(
                title = error.message.orEmpty(),
                description = GENERIC_ERROR_MESSAGE_FOR_CALLS,
            ),
            showMessage = true,
        )
    }

    private fun updateCardMaskState(
        cardLength: Int,
    ) {
        _viewState.value = _viewState.value.copy(
            cardNumberState = _viewState.value.cardNumberState.copy(
                maxLength = cardLength,
                mask = cardLength.toMask(),
            ),
        )
    }

    private fun hasFormErrors() {
        _viewState.value.let { state ->
            val isFormValid = state.cardNumberState.error.isEmpty() &&
                state.cardNumberState.isValid &&
                state.expirationDateState.error.isEmpty() &&
                state.expirationDateState.isValid &&
                state.secureCodeState.error.isEmpty() &&
                state.secureCodeState.isValid &&
                state.cardHolderState.error.isEmpty() &&
                state.identificationTypeState.error.isEmpty() &&
                state.identificationTypeState.isValid

            _viewState.value = _viewState.value.copy(
                fixedFooterState = state.fixedFooterState.copy(
                    buttonEnabled = isFormValid,
                ),
            )
        }
    }

    private fun updateLoadingState(
        isLoading: Boolean,
    ) {
        _viewState.value = _viewState.value.copy(isLoading = isLoading)
    }
}
