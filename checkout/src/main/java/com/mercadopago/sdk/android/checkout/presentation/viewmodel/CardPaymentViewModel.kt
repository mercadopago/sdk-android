package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.getCardFormAmount
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.extensions.getCardNumberErrorInfo
import com.mercadopago.sdk.android.checkout.domain.extensions.getLength
import com.mercadopago.sdk.android.checkout.domain.extensions.getMessage
import com.mercadopago.sdk.android.checkout.domain.extensions.isComplete
import com.mercadopago.sdk.android.checkout.domain.extensions.isOptional
import com.mercadopago.sdk.android.checkout.domain.extensions.isPaymentMethodNotFound
import com.mercadopago.sdk.android.checkout.domain.extensions.toMask
import com.mercadopago.sdk.android.checkout.domain.model.CardData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.Payer
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCode
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardDataByBinUseCase
import com.mercadopago.sdk.android.checkout.presentation.extensions.fold
import com.mercadopago.sdk.android.checkout.presentation.extensions.isBeingCleared
import com.mercadopago.sdk.android.checkout.presentation.factory.CardPaymentScreenStateFactory
import com.mercadopago.sdk.android.checkout.presentation.state.CARD_NUMBER_BIN_LENGTH
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberErrorType
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.MessageError
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentState
import com.mercadopago.sdk.android.checkout.presentation.state.isFieldValidationOrNone
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledFormContextUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateCardTokenUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GetIdentificationTypesUseCase
import com.mercadopago.sdk.android.checkout.presentation.validation.CardPaymentValidator
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield.IdentificationTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.simpletextfield.SimpleTextFieldEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Suppress(
    "TooManyFunctions",
    "LongParameterList",
)
internal class CardPaymentViewModel(
    private val stateFactory: CardPaymentScreenStateFactory,
    private val checkoutConfiguration: CheckoutConfiguration?,
    private val getCardDataByBinUseCase: GetCardDataByBinUseCase,
    private val getIdentificationTypesUseCase: GetIdentificationTypesUseCase,
    private val generateCardTokenUseCase: GenerateCardTokenUseCase,
    private val cancelledFormContextUseCase: CancelledFormContextUseCase,
    private val validator: CardPaymentValidator,
) : ViewModel() {
    private val helperTextOptional: String
        get() = stateFactory.getOptionalFieldText()

    private val genericErrorMessage: String
        get() = stateFactory.getGenericErrorMessage()

    private val _viewState = MutableStateFlow(stateFactory.createInitialState())
    val viewState: StateFlow<CardPaymentScreenState> = _viewState

    fun getIdentificationTypes() {
        viewModelScope.launch {
            updateLoadingState(true)
            getIdentificationTypesUseCase().fold(
                onSuccess = { data ->
                    _viewState.value = _viewState.value.copy(
                        identificationTypeState = _viewState.value.identificationTypeState.copy(
                            show = data.isNotEmpty(),
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

    private fun getPaymentMethods() {
        viewModelScope.launch {
            val cardNumberState = _viewState.value.cardNumberState
            if (cardNumberState.isComplete() && cardNumberState.isValid) {
                getCardDataByBinUseCase(
                    bin = cardNumberState.cardBin.orEmpty(),
                    amount = checkoutConfiguration?.getCardFormAmount(),
                    paymentMethods = checkoutConfiguration?.paymentMethods,
                ).fold(
                    onSuccess = { cardData ->
                        updateStateWithCardData(cardData)
                    },
                    onError = { error ->
                        when {
                            error is MercadoPagoCheckoutError.ServiceError &&
                                error.message.orEmpty().isPaymentMethodNotFound() -> {
                                handleCardNumberServiceError(error)
                            }

                            error is MercadoPagoCheckoutError.ServiceError ||
                                _viewState.value.cardNumberState.isComplete() -> {
                                handleResultError(error)
                            }

                            else -> {
                                if (viewState.value.cardNumberState.isComplete()) {
                                    handleResultError(error)
                                }
                            }
                        }
                    },
                )
            }
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
                val previousLength = _viewState.value.expirationDateState.length
                _viewState.value = _viewState.value.copy(
                    expirationDateState = _viewState.value.expirationDateState.copy(
                        length = event.length,
                    ),
                )
                if (event.length.isBeingCleared(previousLength)) {
                    handleExpirationDateInputError(shouldUpdateError = false)
                }
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
                val previousLength = _viewState.value.secureCodeState.length
                _viewState.value = _viewState.value.copy(
                    secureCodeState = _viewState.value.secureCodeState.copy(
                        length = event.length,
                    ),
                )
                if (event.length == _viewState.value.secureCodeState.maxLength) {
                    handleSecurityCodeInputError()
                }
                if (event.length.isBeingCleared(previousLength)) {
                    handleSecurityCodeInputError(shouldUpdateError = false)
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
                    getPaymentMethods()
                }
            }

            is CardNumberTextFieldEvent.OnLengthChanged -> {
                val previousLength = _viewState.value.cardNumberState.length
                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        length = event.length,
                    ),
                )
                if (event.length.isBeingCleared(previousLength)) {
                    handleCardNumberInputError(shouldUpdateError = false)
                }
            }

            is CardNumberTextFieldEvent.OnLastFourDigitsFilled -> {
                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        lastFourDigits = event.lastFourDigits,
                    ),
                )
            }

            is CardNumberTextFieldEvent.IsValid -> {
                if (viewState.value.cardNumberState.isComplete() && !event.isValid) {
                    val errorMessage = stateFactory.getStringProvider()
                        .getString(R.string.card_form_error_card_number_repeated)
                    updateFieldState(
                        error = errorMessage,
                        shouldUpdateError = true,
                    ) { error, isValid ->
                        copy(
                            cardNumberState = cardNumberState.copy(
                                error = error,
                                isValid = isValid,
                                errorType = CardNumberErrorType.LuhnValidation,
                            ),
                        )
                    }
                }
            }

            is CardNumberTextFieldEvent.OnBinChanged -> {
                handleBinChanged(event.cardBin)
            }
        }
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
                val previousValue = _viewState.value.cardHolderState.value
                _viewState.value = _viewState.value.copy(
                    cardHolderState = _viewState.value.cardHolderState.copy(
                        value = event.value,
                    ),
                )
                if (event.value.isBeingCleared(previousValue)) {
                    handleCardHolderInputError(shouldUpdateError = false)
                }
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
                val previousValue = _viewState.value.identificationTypeState.value
                _viewState.value = _viewState.value.copy(
                    identificationTypeState = _viewState.value.identificationTypeState.copy(
                        value = event.value,
                    ),
                )
                if (event.value.isBeingCleared(previousValue)) {
                    handleIdentificationTypeInputError(shouldUpdateError = false)
                }
                if (viewState.value.identificationTypeState.isComplete(event.value.length)) {
                    handleIdentificationTypeInputError(shouldUpdateError = true)
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
                mask = cardData.getLength().toMask(),
                length = cardData.getLength(),
                errorType = CardNumberErrorType.None,
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
        optional = securityCode.isOptional(),
        helper = if (securityCode.isOptional()) helperTextOptional else "",
        messageTooltip = securityCode.getMessage(stateFactory.getStringProvider()),
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
            val hasIdentificationError = state.identificationTypeState.show &&
                state.identificationTypeState.error.isNotEmpty()
            val hasErrors = state.cardNumberState.error.isNotEmpty() ||
                state.expirationDateState.error.isNotEmpty() ||
                state.secureCodeState.error.isNotEmpty() ||
                state.cardHolderState.error.isNotEmpty() ||
                hasIdentificationError
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
        _viewState.value = _viewState.value.copy(
            cardNumberState = _viewState.value.cardNumberState.copy(
                cardBin = cardBin,
            ),
        )
        if ((cardBin?.length ?: 0) < CARD_NUMBER_BIN_LENGTH) {
            handleCardNumberInputError()
            val currentState = _viewState.value.cardNumberState
            _viewState.value = _viewState.value.copy(
                cardNumberState = currentState.copy(
                    image = null,
                    error = if (currentState.errorType == CardNumberErrorType.PaymentMethodNotFound) {
                        ""
                    } else {
                        currentState.error
                    },
                    mask = currentState.maxLength.toMask(),
                    errorType = CardNumberErrorType.None,
                ),
                installmentsState = _viewState.value.installmentsState.copy(showList = false),
            )
        } else {
            getPaymentMethods()
        }
    }

    private fun handleCardNumberInputError(
        shouldUpdateError: Boolean = true,
    ) {
        val currentState = _viewState.value
        val cardNumberError = validator.validateCardNumber(currentState.cardNumberState)
        if (currentState.cardNumberState.errorType.isFieldValidationOrNone()) {
            updateFieldState(cardNumberError, shouldUpdateError) { error, isValid ->
                copy(
                    cardNumberState = cardNumberState.copy(
                        error = error,
                        isValid = isValid,
                        errorType = if (error.isEmpty()) {
                            CardNumberErrorType.None
                        } else {
                            CardNumberErrorType.FieldValidation
                        },
                    ),
                )
            }
        }
        hasFormErrors()
    }

    private fun handleCardNumberServiceError(
        error: MercadoPagoCheckoutError.ServiceError,
    ) {
        val (errorType, message) = error.getCardNumberErrorInfo(
            stateFactory.getStringProvider(),
        )
        updateFieldState(message, shouldUpdateError = true) { message, isValid ->
            copy(
                cardNumberState = cardNumberState.copy(
                    error = message,
                    isValid = isValid,
                    errorType = errorType,
                ),
            )
        }
    }

    private fun updateFieldState(
        error: String,
        shouldUpdateError: Boolean,
        updateState: CardPaymentScreenState.(String, Boolean) -> CardPaymentScreenState,
    ) {
        val isValid = error.isEmpty()
        if (shouldUpdateError) {
            _viewState.value = _viewState.value.updateState(error, isValid)
        } else {
            _viewState.value = _viewState.value.updateState("", isValid)
        }
    }

    private fun handleExpirationDateInputError(
        shouldUpdateError: Boolean = true,
    ) {
        val currentState = _viewState.value
        val expirationDateError = validator.validateExpirationDate(currentState.expirationDateState)
        updateFieldState(expirationDateError, shouldUpdateError) { error, isValid ->
            copy(
                expirationDateState = expirationDateState.copy(
                    error = error,
                    isValid = isValid,
                ),
            )
        }
        hasFormErrors()
    }

    private fun handleSecurityCodeInputError(
        shouldUpdateError: Boolean = true,
    ) {
        val currentState = _viewState.value
        if (!currentState.secureCodeState.optional) {
            val securityCodeError = validator.validateSecurityCode(currentState.secureCodeState)
            updateFieldState(securityCodeError, shouldUpdateError) { error, isValid ->
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

    private fun handleCardHolderInputError(
        shouldUpdateError: Boolean = true,
    ) {
        val currentState = _viewState.value
        val cardHolderError = validator.validateCardHolder(currentState.cardHolderState)
        updateFieldState(cardHolderError, shouldUpdateError) { error, isValid ->
            copy(
                cardHolderState = cardHolderState.copy(
                    error = error,
                    isValid = isValid,
                ),
            )
        }
        hasFormErrors()
    }

    private fun handleIdentificationTypeInputError(
        shouldUpdateError: Boolean = true,
    ) {
        val currentState = _viewState.value
        val identificationError =
            validator.validateIdentificationType(currentState.identificationTypeState)
        updateFieldState(identificationError, shouldUpdateError) { error, isValid ->
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
                description = genericErrorMessage,
            ),
            showMessage = true,
        )
    }

    private fun hasFormErrors() {
        _viewState.value.let { state ->
            val isIdentificationValid = !state.identificationTypeState.show ||
                (state.identificationTypeState.error.isEmpty() && state.identificationTypeState.isValid)
            val isFormValid = state.cardNumberState.error.isEmpty() &&
                state.cardNumberState.isValid &&
                state.expirationDateState.error.isEmpty() &&
                state.expirationDateState.isValid &&
                state.secureCodeState.error.isEmpty() &&
                state.secureCodeState.isValid &&
                state.cardHolderState.error.isEmpty() &&
                state.cardHolderState.isValid &&
                isIdentificationValid

            _viewState.value = _viewState.value.copy(
                fixedFooterState = state.fixedFooterState.copy(
                    buttonVisible = isFormValid,
                ),
            )
        }
    }

    private fun updateLoadingState(
        isLoading: Boolean,
    ) {
        _viewState.value = _viewState.value.copy(isLoading = isLoading)
    }

    fun onBackPressed() {
        val currentState = _viewState.value
        val context = cancelledFormContextUseCase(currentState)

        CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.UserCancelled(context))
    }
}
