package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.buildScreensParam
import com.mercadopago.sdk.android.checkout.core.model.internal.getOrder
import com.mercadopago.sdk.android.checkout.core.model.internal.getOrderId
import com.mercadopago.sdk.android.checkout.core.model.internal.hasReviewAndConfirm
import com.mercadopago.sdk.android.checkout.core.model.internal.isCardTransaction
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
import com.mercadopago.sdk.android.checkout.presentation.factory.CardPaymentScreenStateFactory
import com.mercadopago.sdk.android.checkout.presentation.mapper.applyCardBinData
import com.mercadopago.sdk.android.checkout.presentation.mapper.toCardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.model.CancelReason
import com.mercadopago.sdk.android.checkout.presentation.shared.withButtonLoading
import com.mercadopago.sdk.android.checkout.presentation.state.CARD_NUMBER_BIN_LENGTH
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.MessageError
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledFormContextUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
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

@Suppress("TooManyFunctions")
internal class CardPaymentViewModel(
    private val checkoutConfiguration: CheckoutConfiguration?,
    private val initializeCardFormUseCase: InitializeCardFormUseCase,
    private val getCardBinUseCase: GetCardBinUseCase,
    private val generateTokenUseCase: GenerateTokenUseCase,
    private val processOrderUseCase: ProcessOrderUseCase,
    private val cardPaymentScreenStateFactory: CardPaymentScreenStateFactory,
) : ViewModel() {
    private val _viewState = MutableStateFlow(CardPaymentScreenState())
    val viewState: StateFlow<CardPaymentScreenState> = _viewState

    private val _viewEvent = MutableStateFlow<CardPaymentViewEvent?>(null)
    val viewEvent: StateFlow<CardPaymentViewEvent?> = _viewEvent.asStateFlow()

    private val cancelledFormContextUseCase = CancelledFormContextUseCase()

    private var pendingOrderData: PendingOrderData? = null
    private var transactionAmount: java.math.BigDecimal? = null

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
                _viewState.value = _viewState.value.copy(
                    identificationTypeState = _viewState.value.identificationTypeState.copy(
                        selected = event.identificationType,
                        placeHolder = event.identificationType.placeholder.orEmpty(),
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

    fun onBackPressed(
        reason: CancelReason = CancelReason.SystemBack,
    ) {
        analyticsTracker.trackUserCanceled(reason)
        val result = cancelledFormContextUseCase(_viewState.value)
        val context: MPUserCancelledContext = when (checkoutConfiguration?.checkoutType) {
            is MPCheckoutType.CardSave -> MPUserCancelledContext.CardSave(result.fields)
            is MPCheckoutType.CardTransaction, null -> MPUserCancelledContext.CardTransaction(
                fields = result.fields,
                screens = result.screens,
            )
            is MPCheckoutType.Payment -> MPUserCancelledContext.Payment(
                screens = result.screens,
            )
        }
        _viewEvent.value = CardPaymentViewEvent.OnUserCancelled(context)
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

    fun onInvalidInstallmentData(
        error: MercadoPagoCheckoutError,
    ) {
        _viewEvent.value = CardPaymentViewEvent.OnFailure(error)
    }

    fun initialization() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            val isCardTransaction = checkoutConfiguration.isCardTransaction()
            initializeCardFormUseCase(
                orderId = checkoutConfiguration.getOrderId().takeIf { isCardTransaction },
                clientToken = checkoutConfiguration.getOrder()?.clientToken.takeIf { isCardTransaction },
                checkoutType = checkoutConfiguration.toCheckoutType(),
                screens = checkoutConfiguration?.buildScreensParam(),
            ).fold(
                onSuccess = { data ->
                    transactionAmount = data.amount
                    _viewState.value = data.toCardPaymentScreenState(
                        totalAmount = transactionAmount,
                    )
                },
                onError = { error ->
                    analyticsTracker.trackInitializeError(error)
                    _viewEvent.value = CardPaymentViewEvent.OnFailure(error)
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
        val amount = transactionAmount
        val payer = pendingOrderData?.payer ?: Payer()
        val token = pendingOrderData?.token.orEmpty()
        val paymentData = buildPaymentData(token = token, payer = payer) as? MPPaymentData.CardTransaction

        if (amount == null || paymentData == null) {
            _viewEvent.value = CardPaymentViewEvent.OnFailure(
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
                amount = amount,
                installmentAmount = installmentAmount,
            )

            _viewEvent.value = CardPaymentViewEvent.OnPaymentConfirmed(
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
                    amount = transactionAmount?.toPlainString(),
                    checkoutType = checkoutConfiguration.toCheckoutType(),
                    processingMode = PROCESSING_MODE,
                    filter = CardBinFilter(
                        excludedPaymentTypes = excludedTypes,
                        excludedPaymentMethods = excludedMethods,
                    ),
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
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(
                footerState = _viewState.value.footerState.withButtonLoading(true),
            )
            val payer = Payer(
                documentType = buyerIdentification.type,
                documentNumber = buyerIdentification.number,
            )
            generateTokenUseCase(
                cardNumberState = cardNumberState,
                expirationDateState = expirationDateState,
                securityCodeState = securityCodeState,
                buyerIdentification = buyerIdentification,
            ).fold(
                onSuccess = { cardToken -> handleToken(token = cardToken.token, payer = payer) },
                onError = { checkoutError ->
                    analyticsTracker.trackSubmitError(checkoutError)
                    _viewEvent.value = CardPaymentViewEvent.OnFailure(checkoutError)
                },
            ).apply {
                _viewState.value = _viewState.value.copy(
                    footerState = _viewState.value.footerState.withButtonLoading(false),
                )
            }
        }
    }

    private fun handleToken(
        token: String,
        payer: Payer,
    ) {
        if (checkoutConfiguration.isCardTransaction()) {
            pendingOrderData = PendingOrderData(token = token, payer = payer)
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
            _viewEvent.value = CardPaymentViewEvent.OnSuccess(
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
            _viewEvent.value = CardPaymentViewEvent.OnFailure(
                checkoutConfiguration?.checkoutType.unsupportedTypeError(
                    localized = ErrorLocalized.TOKENIZATION,
                ),
            )
        }
    }

    private suspend fun processOrder(
        installments: Int,
    ) {
        val amount = transactionAmount ?: run {
            CheckoutCallbackHolder.notify(
                MercadoPagoCheckoutResult.Error(
                    checkoutConfiguration?.checkoutType.unsupportedTypeError(
                        localized = ErrorLocalized.ORDER_PROCESS,
                    ),
                ),
            )
            _viewState.value = _viewState.value.copy(isLoading = false)
            return
        }
        val orderId = checkoutConfiguration.getOrderId()
        val token = pendingOrderData?.token.orEmpty()
        val payer = pendingOrderData?.payer ?: Payer()

        val params = checkoutConfiguration.toProcessOrderParams(
            screenState = viewState.value,
            installment = installments,
            token = token,
            amount = amount,
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
                if (paymentData != null) {
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
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(error))
                _viewState.value = _viewState.value.copy(isLoading = false)
                pendingOrderData = null
            },
        )
    }

    private fun buildPaymentData(
        token: String,
        payer: Payer,
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

            else -> null
        }
}
