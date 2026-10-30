package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.buildScreensParam
import com.mercadopago.sdk.android.checkout.core.model.internal.getOrderId
import com.mercadopago.sdk.android.checkout.core.model.internal.hasReviewAndConfirm
import com.mercadopago.sdk.android.checkout.core.model.internal.hasStatusScreen
import com.mercadopago.sdk.android.checkout.core.model.internal.paymentBrickClientToken
import com.mercadopago.sdk.android.checkout.core.model.internal.paymentBrickOrderId
import com.mercadopago.sdk.android.checkout.core.model.internal.requiresOrderProcessing
import com.mercadopago.sdk.android.checkout.core.model.internal.toCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.toProcessOrderParams
import com.mercadopago.sdk.android.checkout.core.model.internal.unsupportedTypeError
import com.mercadopago.sdk.android.checkout.data.remote.utils.PROCESSING_MODE
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.extensions.extractCardFilters
import com.mercadopago.sdk.android.checkout.domain.extensions.isComplete
import com.mercadopago.sdk.android.checkout.domain.extensions.toMask
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.Payer
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.domain.usecase.CardBinFilter
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.InitializeCardFormUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.ProcessOrderUseCase
import com.mercadopago.sdk.android.checkout.presentation.extensions.fold
import com.mercadopago.sdk.android.checkout.presentation.extensions.isBeingCleared
import com.mercadopago.sdk.android.checkout.presentation.extensions.isEmpty
import com.mercadopago.sdk.android.checkout.presentation.factory.CardFormScreenStateFactory
import com.mercadopago.sdk.android.checkout.presentation.mapper.applyCardBinData
import com.mercadopago.sdk.android.checkout.presentation.mapper.toCardFormScreenState
import com.mercadopago.sdk.android.checkout.presentation.model.CancelReason
import com.mercadopago.sdk.android.checkout.presentation.shared.withButtonLoading
import com.mercadopago.sdk.android.checkout.presentation.state.CARD_NUMBER_BIN_LENGTH
import com.mercadopago.sdk.android.checkout.presentation.state.CardFormScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.CardFormViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.MessageError
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledFormContextUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield.IdentificationTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.simpletextfield.SimpleTextFieldEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions", "LargeClass")
internal class CardFormViewModel(
    private val checkoutConfiguration: CheckoutConfiguration?,
    private val initializeCardFormUseCase: InitializeCardFormUseCase,
    private val getCardBinUseCase: GetCardBinUseCase,
    private val generateTokenUseCase: GenerateTokenUseCase,
    private val processOrderUseCase: ProcessOrderUseCase,
    private val cardPaymentScreenStateFactory: CardFormScreenStateFactory,
) : ViewModel() {
    val cardNumberPCIState = PCIFieldState.create()
    val expirationDatePCIState = PCIFieldState.create()
    val securityCodePCIState = PCIFieldState.create()
    val cardHolderPCIState = PCIFieldState.create()
    val identificationPCIState = PCIFieldState.create()

    private val _viewState = MutableStateFlow(CardFormScreenState())
    val viewState: StateFlow<CardFormScreenState> = _viewState

    private val _viewEvent = MutableStateFlow<CardFormViewEvent?>(null)
    val viewEvent: StateFlow<CardFormViewEvent?> = _viewEvent.asStateFlow()

    private val cancelledFormContextUseCase = CancelledFormContextUseCase().apply {
        markScreenPresented(Screen.CARD_FORM)
    }

    private var pendingOrderData: PendingOrderData? = null
    private var transactionAmount: java.math.BigDecimal? = null
    private var isTokenizing = false
    private var isInitializationStarted = false

    private val analyticsTracker = CardFormAnalyticsTracker(
        isLoading = { _viewState.value.isLoading },
    )

    private val errorHandler = CardFormFieldErrorHandler(
        analyticsTracker = analyticsTracker,
    )

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
                    analyticsTracker.trackInputValidation("card_number", isValid)
                    _viewState.value = errorHandler.applyCardNumberFieldError(_viewState.value)
                }
            }

            is CardNumberTextFieldEvent.OnLengthChanged -> {
                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        length = event.length,
                    ),
                )
                _viewState.value = if (event.length.isEmpty()) {
                    errorHandler.clearCardNumberErrors(_viewState.value)
                } else {
                    errorHandler.applyCardNumberFieldError(_viewState.value, shouldShowError = false)
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
                _viewState.value = errorHandler.applyLuhnValidation(_viewState.value, event.isValid)
            }

            is CardNumberTextFieldEvent.OnBinChanged -> {
                onBinChanged(event.cardBin)
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
                    _viewState.value = errorHandler.applyExpirationDateError(_viewState.value)
                }
            }

            is ExpirationDateTextFieldEvent.OnFocusChanged -> {
                _viewState.value = _viewState.value.copy(
                    expirationDateState = _viewState.value.expirationDateState.copy(
                        isFocused = event.isFocused,
                    ),
                )
                if (!event.isFocused) {
                    _viewState.value = errorHandler.applyExpirationDateError(_viewState.value)
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
                    _viewState.value = errorHandler.applyExpirationDateError(
                        _viewState.value,
                        shouldUpdateError = false,
                    )
                }
            }

            is ExpirationDateTextFieldEvent.IsValid -> {
                analyticsTracker.trackInputValidation("expiration_date", event.isValid)
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
                    _viewState.value = errorHandler.applySecurityCodeError(_viewState.value)
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
                    _viewState.value = errorHandler.applySecurityCodeError(_viewState.value)
                }
                if (event.length.isBeingCleared(previousLength)) {
                    _viewState.value = errorHandler.applySecurityCodeError(
                        _viewState.value,
                        shouldUpdateError = false,
                    )
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
                    _viewState.value = errorHandler.applyCardHolderError(
                        _viewState.value,
                        shouldUpdateError = false,
                    )
                }
            }

            is SimpleTextFieldEvent.OnFocusChanged -> {
                _viewState.value = _viewState.value.copy(
                    cardHolderState = _viewState.value.cardHolderState.copy(
                        isFocused = event.isFocused,
                    ),
                )
                if (!event.isFocused) {
                    _viewState.value = errorHandler.applyCardHolderError(_viewState.value)
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
                    _viewState.value = errorHandler.applyIdentificationTypeError(
                        _viewState.value,
                        shouldUpdateError = false,
                    )
                }
                if (viewState.value.identificationTypeState.isComplete(event.value.length)) {
                    _viewState.value = errorHandler.applyIdentificationTypeError(
                        _viewState.value,
                        shouldUpdateError = true,
                    )
                }
            }

            is IdentificationTextFieldEvent.OnFocusChanged -> {
                _viewState.value = _viewState.value.copy(
                    identificationTypeState = _viewState.value.identificationTypeState.copy(
                        isFocused = event.isFocused,
                    ),
                )
                if (!event.isFocused) {
                    _viewState.value = errorHandler.applyIdentificationTypeError(_viewState.value)
                }
            }

            is IdentificationTextFieldEvent.OnTypeSelected -> {
                analyticsTracker.trackDropdownSelection(event.identificationType.id.orEmpty())
                selectIdentificationType(event.identificationType)
            }
        }
    }

    private fun selectIdentificationType(
        identificationType: IdentificationType,
    ) {
        val current = _viewState.value.identificationTypeState
        val typeChanged = current.selected?.id != identificationType.id
        if (typeChanged) identificationPCIState.clear()
        val base = if (typeChanged) {
            current.copy(value = "", error = "", filled = false, isValid = false)
        } else {
            current
        }
        _viewState.value = _viewState.value.copy(
            identificationTypeState = base.copy(
                selected = identificationType,
                placeHolder = identificationType.placeholder.orEmpty(),
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
            messageError = MessageError(),
        )
    }

    fun onBackPressed(
        reason: CancelReason = CancelReason.SystemBack,
    ) {
        if (checkoutConfiguration?.checkoutType is MPCheckoutType.Payment) {
            resetFormState()
            _viewEvent.value = CardFormViewEvent.OnReturnToPaymentSelector
            return
        }
        analyticsTracker.trackUserCanceled(reason)
        val result = cancelledFormContextUseCase(_viewState.value)
        val context: MPUserCancelledContext = when (checkoutConfiguration?.checkoutType) {
            is MPCheckoutType.CardSave -> MPUserCancelledContext.CardSave(result.fields)
            else -> MPUserCancelledContext.CardTransaction(
                fields = result.fields,
                screens = result.screens,
            )
        }
        _viewEvent.value = CardFormViewEvent.OnUserCancelled(context)
    }

    private fun resetFormState() {
        cardNumberPCIState.clear()
        expirationDatePCIState.clear()
        securityCodePCIState.clear()
        cardHolderPCIState.clear()
        identificationPCIState.clear()
        pendingOrderData = null
        transactionAmount = null
        isInitializationStarted = false
        _viewState.value = CardFormScreenState()
    }

    internal fun reviewConfirmCancellationContext(): MPUserCancelledContext.CardTransaction {
        cancelledFormContextUseCase.markScreenPresented(Screen.REVIEW_AND_CONFIRM)
        val result = cancelledFormContextUseCase(_viewState.value)
        return MPUserCancelledContext.CardTransaction(
            fields = result.fields,
            screens = result.screens,
        )
    }

    fun onInstallmentConfirmed(
        installment: Int,
    ) {
        if (_viewState.value.isLoading) return

        _viewState.value = _viewState.value.copy(isLoading = true)

        if (checkoutConfiguration.hasReviewAndConfirm()) {
            emitPaymentConfirmedEvent(installment)
        } else {
            viewModelScope.launch {
                processOrder(installments = installment)
            }
        }
    }

    fun markScreenPresented(
        screen: Screen,
    ) {
        cancelledFormContextUseCase.markScreenPresented(screen)
    }

    fun onViewEventConsumed() {
        _viewEvent.value = null
        _viewState.value = _viewState.value.copy(isLoading = false)
    }

    fun onReviewConfirmError() {
        _viewEvent.value = CardFormViewEvent.OnReviewConfirmError
    }

    fun onInvalidInstallmentData(
        error: MercadoPagoCheckoutError,
    ) {
        _viewEvent.value = CardFormViewEvent.OnFailure(error)
    }

    fun initialization() {
        if (isInitializationStarted) return
        isInitializationStarted = true

        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            initializeCardFormUseCase(
                orderId = checkoutConfiguration.paymentBrickOrderId(),
                clientToken = checkoutConfiguration.paymentBrickClientToken(),
                checkoutType = checkoutConfiguration.toCheckoutType(),
                screens = checkoutConfiguration?.buildScreensParam(),
            ).fold(
                onSuccess = { data ->
                    transactionAmount = data.amount
                    _viewState.value = data.toCardFormScreenState(
                        totalAmount = transactionAmount,
                    )
                },
                onError = { error ->
                    isInitializationStarted = false
                    analyticsTracker.trackInitializeError(error)
                    _viewEvent.value = CardFormViewEvent.OnFailure(error.publicError)
                },
            ).apply {
                _viewState.value = _viewState.value.copy(isLoading = false)
            }
        }
    }

    fun onSubmit(
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

    private fun emitPaymentConfirmedEvent(
        installment: Int,
    ) {
        val payer = pendingOrderData?.payer
        val token = pendingOrderData?.token.orEmpty()
        val paymentData = buildPaymentData(token = token, payer = payer)

        if (paymentData == null || paymentData is MPPaymentData.CardSave) {
            _viewEvent.value = CardFormViewEvent.OnFailure(
                checkoutConfiguration?.checkoutType.unsupportedTypeError(
                    localized = ErrorLocalized.ORDER_PROCESS,
                ),
            )
        } else {
            val installmentAmount = viewState.value.installmentsState.installments
                .firstOrNull { it.installments == installment }
                ?.installmentAmount
            val processOrderParams = checkoutConfiguration.toProcessOrderParams(
                screenState = viewState.value,
                installment = installment,
                token = token,
                cardId = pendingOrderData?.cardId,
                installmentAmount = installmentAmount,
            )

            _viewEvent.value = CardFormViewEvent.OnPaymentConfirmed(
                payment = paymentData,
                params = processOrderParams,
            )
        }
    }

    private fun onBinChanged(
        cardBin: String?,
    ) {
        _viewState.value = _viewState.value.copy(
            cardNumberState = _viewState.value.cardNumberState.copy(cardBin = cardBin),
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
            val (excludedTypes, excludedMethods) = checkoutConfiguration?.paymentMethodConfigs.extractCardFilters()
            viewModelScope.launch {
                getCardBinUseCase(
                    bin = cardBin.orEmpty(),
                    orderId = checkoutConfiguration.paymentBrickOrderId().orEmpty(),
                    clientToken = checkoutConfiguration.paymentBrickClientToken(),
                    amount = transactionAmount?.toPlainString(),
                    checkoutType = checkoutConfiguration.toCheckoutType(),
                    processingMode = PROCESSING_MODE,
                    filter = CardBinFilter(
                        excludedPaymentTypes = excludedTypes,
                        excludedPaymentMethods = excludedMethods,
                    ),
                    screens = checkoutConfiguration?.buildScreensParam(),
                ).fold(
                    onSuccess = { data ->
                        val updated = _viewState.value.applyCardBinData(data)
                        _viewState.value = if (updated.secureCodeState.length > 0) {
                            errorHandler.applySecurityCodeError(updated)
                        } else {
                            updated
                        }
                    },
                    onError = { error ->
                        _viewState.value = if (error is MercadoPagoCheckoutError.ServiceError) {
                            errorHandler.applyPaymentMethodNotFoundError(
                                state = _viewState.value,
                                message = error.errorMessage,
                            )
                        } else {
                            errorHandler.handleResultError(
                                state = _viewState.value,
                                message = error.errorMessage,
                                genericErrorMessage = cardPaymentScreenStateFactory.getGenericErrorMessage(),
                            )
                        }
                    },
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
        if (isTokenizing) return
        isTokenizing = true
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(
                footerState = _viewState.value.footerState.withButtonLoading(true),
            )
            val payer = buyerIdentification.type?.let { type ->
                buyerIdentification.number?.let { number -> Payer(documentType = type, documentNumber = number) }
            }
            generateTokenUseCase(
                cardNumberState = cardNumberState,
                expirationDateState = expirationDateState,
                securityCodeState = securityCodeState,
                buyerIdentification = buyerIdentification,
            ).fold(
                onSuccess = { cardToken -> handleToken(cardToken = cardToken, payer = payer) },
                onError = { checkoutError ->
                    analyticsTracker.trackSubmitError(checkoutError)
                    _viewEvent.value = if (checkoutConfiguration?.checkoutType is MPCheckoutType.Payment) {
                        resetFormState()
                        CardFormViewEvent.OnTokenizationError
                    } else {
                        CardFormViewEvent.OnFailure(checkoutError.publicError)
                    }
                },
            ).apply {
                isTokenizing = false
                _viewState.value = _viewState.value.copy(
                    footerState = _viewState.value.footerState.withButtonLoading(false),
                )
            }
        }
    }

    private fun handleToken(
        cardToken: CardToken,
        payer: Payer?,
    ) {
        val token = cardToken.token
        if (checkoutConfiguration.requiresOrderProcessing()) {
            pendingOrderData = PendingOrderData(
                token = token,
                cardId = cardToken.cardId,
                payer = payer,
            )
        }
        analyticsTracker.trackSubmit(
            cardBrand = viewState.value.paymentState.paymentMethodId.orEmpty(),
            transactionAmount = transactionAmount?.toDouble() ?: 0.0,
            issuer = viewState.value.cardIssuers.firstOrNull()?.id.orEmpty(),
            paymentTypeId = viewState.value.paymentState.paymentTypeId.orEmpty(),
        )
        val paymentData = buildPaymentData(token = token, payer = payer)
        if (paymentData != null) {
            val state = _viewState.value
            _viewEvent.value = CardFormViewEvent.OnSuccess(
                payment = paymentData,
                installment = MPInstallmentData(
                    quotas = state.installmentsState.installments,
                    display = MPInstallmentData.InstallmentDisplay(
                        title = state.installmentsState.title,
                        currencySymbol = state.currencySymbol,
                        displayType = state.installmentsState.displayType,
                        footer = MPInstallmentData.InstallmentFooterDisplay(
                            footerTitle = state.installmentsState.totalLabel,
                            lastFourDigits = state.cardNumberState.lastFourDigits,
                            brand = state.paymentState.paymentMethodId.orEmpty(),
                            buttonLabel = state.installmentsState.buttonLabel,
                        ),
                    ),
                ),
            )
        } else {
            _viewEvent.value = CardFormViewEvent.OnFailure(
                checkoutConfiguration?.checkoutType.unsupportedTypeError(
                    localized = ErrorLocalized.TOKENIZATION,
                ),
            )
        }
    }

    private suspend fun processOrder(
        installments: Int,
    ) {
        val orderId = checkoutConfiguration.getOrderId()
        val token = pendingOrderData?.token.orEmpty()
        val payer = pendingOrderData?.payer

        val params = checkoutConfiguration.toProcessOrderParams(
            screenState = viewState.value,
            installment = installments,
            token = token,
            cardId = pendingOrderData?.cardId,
        )

        processOrderUseCase(params).fold(
            onSuccess = { orderOutput ->
                analyticsTracker.trackOrderSubmit(
                    orderId = orderOutput.id,
                    orderStatus = orderOutput.status,
                )
                val paymentData = buildPaymentData(
                    token = token,
                    payer = payer,
                    orderOutput = orderOutput,
                )
                if (paymentData != null && checkoutConfiguration.hasStatusScreen()) {
                    _viewEvent.value = CardFormViewEvent.OnOrderProcessed(
                        payment = paymentData,
                        params = params,
                        paymentProcessed = orderOutput.paymentProcessed,
                    )
                } else if (paymentData != null) {
                    CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Success(paymentData))
                } else {
                    CheckoutCallbackHolder.notify(
                        MercadoPagoCheckoutResult.Error(
                            checkoutConfiguration?.checkoutType.unsupportedTypeError(
                                localized = ErrorLocalized.TOKENIZATION,
                            ),
                        ),
                    )
                }
                _viewState.value = _viewState.value.copy(isLoading = false)
                pendingOrderData = null
            },
            onError = { error ->
                analyticsTracker.trackOrderError(error = error, orderId = orderId)
                if (checkoutConfiguration.hasStatusScreen()) {
                    _viewEvent.value = CardFormViewEvent.OnOrderProcessFailed(
                        error = error.publicError,
                        statusDetail = error.statusDetail,
                        params = params,
                    )
                } else {
                    CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(error.publicError))
                }
                _viewState.value = _viewState.value.copy(isLoading = false)
                pendingOrderData = null
            },
        )
    }

    private fun buildPaymentData(
        token: String,
        payer: Payer?,
        orderOutput: OrderProcessOutput? = null,
    ): MPPaymentData? =
        when (checkoutConfiguration?.checkoutType) {
            is MPCheckoutType.CardSave -> MPPaymentData.CardSave(
                token = token,
                paymentMethodId = viewState.value.paymentState.paymentMethodId.orEmpty(),
                paymentTypeId = viewState.value.paymentState.paymentTypeId.orEmpty(),
                issuerId = viewState.value.cardIssuers.firstOrNull()?.id,
                payer = payer,
            )

            is MPCheckoutType.CardTransaction -> MPPaymentData.CardTransaction(
                orderId = orderOutput?.id.orEmpty(),
                orderStatus = orderOutput?.status.orEmpty(),
                paymentMethodId = viewState.value.paymentState.paymentMethodId.orEmpty(),
                paymentTypeId = viewState.value.paymentState.paymentTypeId.orEmpty(),
            )

            is MPCheckoutType.Payment -> MPPaymentData.Payment(
                orderId = orderOutput?.id.orEmpty(),
                orderStatus = orderOutput?.status.orEmpty(),
                paymentMethodId = viewState.value.paymentState.paymentMethodId.orEmpty(),
                paymentTypeId = viewState.value.paymentState.paymentTypeId.orEmpty(),
            )

            else -> null
        }
}
