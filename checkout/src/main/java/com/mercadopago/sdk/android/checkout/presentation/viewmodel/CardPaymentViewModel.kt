package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormDropdownSelection
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormInitializeError
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormInputValidation
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormSubmit
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormSubmitError
import com.mercadopago.sdk.android.checkout.analytics.metricCardFormUserCanceledError
import com.mercadopago.sdk.android.checkout.analytics.toAnalyticsString
import com.mercadopago.sdk.android.checkout.analytics.toErrorTypeString
import com.mercadopago.sdk.android.checkout.core.model.CardType
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.getCardFormAmount
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.extensions.extractCardFilters
import com.mercadopago.sdk.android.checkout.domain.extensions.getLength
import com.mercadopago.sdk.android.checkout.domain.extensions.getMessage
import com.mercadopago.sdk.android.checkout.domain.extensions.getPlaceholder
import com.mercadopago.sdk.android.checkout.domain.extensions.isComplete
import com.mercadopago.sdk.android.checkout.domain.extensions.isOptional
import com.mercadopago.sdk.android.checkout.domain.extensions.isPaymentMethodNotFound
import com.mercadopago.sdk.android.checkout.domain.extensions.matchesCardBrand
import com.mercadopago.sdk.android.checkout.domain.extensions.matchesCardType
import com.mercadopago.sdk.android.checkout.domain.extensions.toMask
import com.mercadopago.sdk.android.checkout.domain.mapper.CountryCodeToLocaleMapper
import com.mercadopago.sdk.android.checkout.domain.model.CardData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.Payer
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCode
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardDataByBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.InitializeCardFormUseCase
import com.mercadopago.sdk.android.checkout.presentation.extensions.fold
import com.mercadopago.sdk.android.checkout.presentation.extensions.getPlaceholder
import com.mercadopago.sdk.android.checkout.presentation.extensions.isBeingCleared
import com.mercadopago.sdk.android.checkout.presentation.extensions.toCardBrandErrorMessage
import com.mercadopago.sdk.android.checkout.presentation.extensions.toCardTypeErrorMessage
import com.mercadopago.sdk.android.checkout.presentation.factory.CardPaymentScreenStateFactory
import com.mercadopago.sdk.android.checkout.presentation.mapper.toCardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.CARD_NUMBER_BIN_LENGTH
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberErrorType
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.MessageError
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentState
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledFormContextUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.checkout.presentation.validation.CardPaymentValidator
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield.IdentificationTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.simpletextfield.SimpleTextFieldEvent
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Suppress(
    "TooManyFunctions",
    "LongParameterList",
    "LargeClass",
)
internal class CardPaymentViewModel(
    private val stateFactory: CardPaymentScreenStateFactory,
    private val checkoutConfiguration: CheckoutConfiguration?,
    private val getCardDataByBinUseCase: GetCardDataByBinUseCase,
    private val initializeCardFormUseCase: InitializeCardFormUseCase,
    private val generateTokenUseCase: GenerateTokenUseCase,
    private val cancelledFormContextUseCase: CancelledFormContextUseCase,
    private val validator: CardPaymentValidator,
) : ViewModel() {
    private val genericErrorMessage: String
        get() = stateFactory.getGenericErrorMessage()

    private val _viewState = MutableStateFlow(CardPaymentScreenState())
    val viewState: StateFlow<CardPaymentScreenState> = _viewState

    private var isCancelling = false

    enum class CancelReason(val analyticsValue: String) {
        SystemBack("user_tapped_back_button"),
        UiButton("user_tapped_ui_back_button"),
    }

    fun initialization() {
        viewModelScope.launch {
            updateLoadingState(true)
            val locale = CountryCodeToLocaleMapper.map(MercadoPagoSDK.countryCode).toLanguageTag()
            val amount = checkoutConfiguration?.getCardFormAmount()?.toPlainString().orEmpty()
            val checkoutType = checkoutConfiguration?.checkoutType.toString()
            initializeCardFormUseCase(
                locale = locale,
                amount = amount,
                checkoutType = checkoutType,
            ).fold(
                onSuccess = { data ->
                    _viewState.value = data.toCardPaymentScreenState()
                },
                onError = { error ->
                    trackInitializeError(error)
                    CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(error))
                },
            ).apply {
                updateLoadingState(false)
            }
        }
    }

    fun onCardNumberEvent(
        event: CardNumberTextFieldEvent,
    ) {
        when (event) {
            is CardNumberTextFieldEvent.OnFocusChanged -> {
                val isValid = _viewState.value.cardNumberState.isValid
                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        isFocused = event.isFocused,
                    ),
                )
                if (!event.isFocused) {
                    trackInputValidation("card_number", isValid)
                    handleCardNumberInputError()
                    if (_viewState.value.messageError.description.isNotEmpty()) {
                        _viewState.value = _viewState.value.copy(showMessage = true)
                    }
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
                    updateCardNumberErrorState(emptyList())
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
                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        isValid = event.isValid,
                    ),
                )
                handleCardNumberLuhnValidation(event.isValid)
            }

            is CardNumberTextFieldEvent.OnBinChanged -> {
                handleBinChanged(event.cardBin)
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

            is ExpirationDateTextFieldEvent.IsValid -> {
                trackInputValidation("expiration_date", event.isValid)
                _viewState.value = _viewState.value.copy(
                    expirationDateState = _viewState.value.expirationDateState.copy(
                        isValid = event.isValid,
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

    fun onTooltipClick() {
        _viewState.value = _viewState.value.copy(
            showTooltip = !_viewState.value.showTooltip,
        )
    }

    fun onMessageClick() {
        _viewState.value = _viewState.value.copy(
            showMessage = false,
            messageError = MessageError(),
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
                trackDropdownSelection(event.identificationType.id.orEmpty())
                _viewState.value = _viewState.value.copy(
                    identificationTypeState = _viewState.value.identificationTypeState.copy(
                        selected = event.identificationType,
                        placeHolder = event.identificationType.getPlaceholder().orEmpty(),
                    ),
                )
            }
        }
    }

    fun onBackPressed(
        reason: CancelReason = CancelReason.SystemBack,
    ) {
        isCancelling = true
        trackUserCanceled(reason)
        val currentState = _viewState.value
        val context = cancelledFormContextUseCase(currentState)

        CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.UserCancelled(context))
    }

    private fun getPaymentMethods() {
        viewModelScope.launch {
            val cardNumberState = _viewState.value.cardNumberState
            getCardDataByBinUseCase(
                bin = cardNumberState.cardBin.orEmpty(),
                amount = checkoutConfiguration?.getCardFormAmount(),
                paymentMethods = checkoutConfiguration?.paymentMethods,
            ).fold(
                onSuccess = { cardData ->
                    updateStateWithCardData(cardData)
                    handleCardBrandValidation(cardData)
                    handleCardTypeValidation(cardData)
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
                            handleResultError(error)
                        }
                    }
                },
            )
        }
    }

    private fun handleCardNumberLuhnValidation(
        isValid: Boolean,
    ) {
        updateCardNumberError<CardNumberErrorType.LuhnValidation> {
            if (!isValid) CardNumberErrorType.LuhnValidation else null
        }
    }

    private fun handleCardNumberInputError() {
        updateCardNumberError<CardNumberErrorType.FieldValidation> {
            val error = validator.validateCardNumber(viewState.value.cardNumberState)
            if (error.isNotEmpty()) CardNumberErrorType.FieldValidation(error) else null
        }
    }

    private fun handleCardNumberServiceError(
        error: MercadoPagoCheckoutError.ServiceError,
    ) {
        updateCardNumberError<CardNumberErrorType.PaymentMethodNotFound> {
            if (error.message.orEmpty().isPaymentMethodNotFound()) {
                CardNumberErrorType.PaymentMethodNotFound
            } else {
                null
            }
        }
    }

    private fun handleCardBrandValidation(
        cardData: CardData,
    ) {
        updateCardNumberError<CardNumberErrorType.CardBrandNotAccepted> {
            val (_, cardBrands) = checkoutConfiguration?.paymentMethods.extractCardFilters()
            if (!cardData.paymentMethod.matchesCardBrand(cardBrands)) {
                val brand = com.mercadopago.sdk.android.checkout.core.model.CardBrand.fromString(
                    cardData.paymentMethod.id.orEmpty(),
                )
                CardNumberErrorType.CardBrandNotAccepted(brand)
            } else {
                null
            }
        }
    }

    private fun handleCardTypeValidation(
        cardData: CardData,
    ) {
        updateCardNumberError<CardNumberErrorType.CardTypeNotAccepted> {
            val (cardTypes, _) = checkoutConfiguration?.paymentMethods.extractCardFilters()
            if (!cardData.paymentMethod.matchesCardType(cardTypes)) {
                val cardType = CardType.fromString(
                    cardData.paymentMethod.paymentTypeId.orEmpty(),
                )
                CardNumberErrorType.CardTypeNotAccepted(cardType)
            } else {
                null
            }
        }
    }

    private fun updateCardNumberErrorState(
        errors: List<CardNumberErrorType>,
    ) {
        val cardNumberState = viewState.value.cardNumberState
        val errorMessage: String = when {
            errors.any { it is CardNumberErrorType.LuhnValidation && cardNumberState.isComplete() } -> {
                cardNumberState.validation.errorInvalid
            }

            errors.any { it is CardNumberErrorType.PaymentMethodNotFound } -> {
                cardNumberState.validation.errorInvalid
            }

            errors.any { it is CardNumberErrorType.CardBrandNotAccepted } -> {
                val error = errors.filterIsInstance<CardNumberErrorType.CardBrandNotAccepted>().first()
                error.brand.toCardBrandErrorMessage(stateFactory.getStringProvider())
            }

            errors.any { it is CardNumberErrorType.CardTypeNotAccepted } -> {
                val error = errors.filterIsInstance<CardNumberErrorType.CardTypeNotAccepted>().first()
                error.cardType?.value?.toCardTypeErrorMessage(stateFactory.getStringProvider()) ?: ""
            }

            errors.any { it is CardNumberErrorType.FieldValidation } -> {
                errors.filterIsInstance<CardNumberErrorType.FieldValidation>()
                    .first().message
            }

            else -> ""
        }

        _viewState.value = _viewState.value.copy(
            cardNumberState = _viewState.value.cardNumberState.copy(
                error = errorMessage,
                isValid = errors.isEmpty(),
                errorTypes = errors,
            ),
        )

        hasFormErrors()
    }

    private fun updateStateWithCardData(
        cardData: CardData,
    ) {
        _viewState.value = _viewState.value.copy(
            secureCodeState = buildSecurityCodeState(cardData.securityCode),
            cardIssuers = listOfNotNull(cardData.cardIssuer),
            cardNumberState = _viewState.value.cardNumberState.copy(
                image = cardData.cardIssuer?.thumbnail,
                mask = cardData.getLength().toMask(),
                maxLength = cardData.getLength(),
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
        helper = if (securityCode.isOptional()) _viewState.value.secureCodeState.helper else "",
        placeHolder = securityCode.getPlaceholder(stateFactory.getStringProvider()),
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
            generateTokenUseCase(
                cardNumberState = cardNumberState,
                expirationDateState = expirationDateState,
                securityCodeState = securityCodeState,
                buyerIdentification = buyerIdentification,
            ).fold(
                onSuccess = { cardToken ->
                    val paymentData = MPPaymentData(
                        transactionAmount = checkoutConfiguration?.getCardFormAmount(),
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
                    trackSubmit()
                    CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Success(paymentData))
                },
                onError = { checkoutError ->
                    trackSubmitError(checkoutError)
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
            val currentState = _viewState.value.cardNumberState
            _viewState.value = _viewState.value.copy(
                cardNumberState = currentState.copy(
                    image = null,
                    mask = currentState.maxLength.toMask(),
                ),
                installmentsState = _viewState.value.installmentsState.copy(showList = false),
            )
        } else {
            getPaymentMethods()
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
            if (shouldUpdateError) trackInputValidation("cvv", securityCodeError.isEmpty())
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
        if (shouldUpdateError) trackInputValidation("card_holder", cardHolderError.isEmpty())
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
        if (shouldUpdateError) trackInputValidation("document", identificationError.isEmpty())
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
        val isCardNumberFocused = _viewState.value.cardNumberState.isFocused
        _viewState.value = _viewState.value.copy(
            messageError = MessageError(
                title = error.message.orEmpty(),
                description = genericErrorMessage,
            ),
            showMessage = !isCardNumberFocused,
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
                    isVisible = isFormValid,
                ),
            )
        }
    }

    private fun updateLoadingState(
        isLoading: Boolean,
    ) {
        _viewState.value = _viewState.value.copy(isLoading = isLoading)
    }

    private inline fun <reified T : CardNumberErrorType> updateCardNumberError(
        errorFactory: () -> T?,
    ) {
        val currentErrors = _viewState.value.cardNumberState.errorTypes.toMutableList()
        currentErrors.removeAll { it is T }
        errorFactory()?.let { currentErrors.add(it) }
        updateCardNumberErrorState(currentErrors)
    }

    // region Analytics Tracking

    private fun trackInitializeError(
        error: MercadoPagoCheckoutError,
    ) {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormInitializeError(errorType = error.toErrorTypeString()),
        )
    }

    private fun trackInputValidation(
        field: String,
        isValid: Boolean,
    ) {
        if (isCancelling || _viewState.value.isLoading) return
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormInputValidation(field = field, isInputValid = isValid),
        )
    }

    private fun trackDropdownSelection(
        type: String,
    ) {
        if (isCancelling || _viewState.value.isLoading) return
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormDropdownSelection(dropdownSelectionType = type),
        )
    }

    private fun trackSubmit() {
        val state = _viewState.value
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormSubmit(
                cardBrand = state.paymentState.paymentMethodId.orEmpty(),
                transactionAmount = checkoutConfiguration?.getCardFormAmount()?.toDouble(),
                issuer = state.cardIssuers.firstOrNull()?.id.orEmpty(),
                paymentType = CardType.fromString(
                    state.paymentState.paymentTypeId.orEmpty(),
                )?.toAnalyticsString(),
            ),
        )
    }

    private fun trackSubmitError(
        error: MercadoPagoCheckoutError,
    ) {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormSubmitError(errorType = error.toErrorTypeString()),
        )
    }

    private fun trackUserCanceled(
        reason: CancelReason,
    ) {
        MPAnalytics.tryGetInstance()?.trackMetric(
            metricCardFormUserCanceledError(errorType = reason.analyticsValue),
        )
    }
    // endregion
}
