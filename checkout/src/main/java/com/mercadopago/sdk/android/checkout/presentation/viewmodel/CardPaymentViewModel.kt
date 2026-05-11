package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.data.remote.utils.PROCESSING_MODE
import com.mercadopago.sdk.android.checkout.domain.extensions.extractCardFilters
import com.mercadopago.sdk.android.checkout.domain.extensions.isComplete
import com.mercadopago.sdk.android.checkout.domain.extensions.toMask
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.Payer
import com.mercadopago.sdk.android.checkout.domain.usecase.CardBinFilter
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardBinUseCase
import com.mercadopago.sdk.android.checkout.presentation.brick.CardPaymentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.extensions.fold
import com.mercadopago.sdk.android.checkout.presentation.extensions.isBeingCleared
import com.mercadopago.sdk.android.checkout.presentation.extensions.map
import com.mercadopago.sdk.android.checkout.presentation.extensions.onSuccess
import com.mercadopago.sdk.android.checkout.presentation.mapper.applyCardBinData
import com.mercadopago.sdk.android.checkout.presentation.mapper.toBuyerIdentification
import com.mercadopago.sdk.android.checkout.presentation.mapper.toCardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.mapper.toMPInstallmentData
import com.mercadopago.sdk.android.checkout.presentation.model.CancelReason
import com.mercadopago.sdk.android.checkout.presentation.state.CARD_NUMBER_BIN_LENGTH
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.MessageError
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledFormContextUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield.IdentificationTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.simpletextfield.SimpleTextFieldEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState.Companion.create as createPCIFieldState

private const val FIRST_INSTALLMENT = 1

@Suppress("TooManyFunctions")
internal class CardPaymentViewModel(
    private val initializationOutput: CardFormInitializationOutput,
    private val getCardBinUseCase: GetCardBinUseCase,
    private val generateTokenUseCase: GenerateTokenUseCase,
) : ViewModel() {
    private val cancelledFormContextUseCase = CancelledFormContextUseCase()

    private val _viewState = MutableStateFlow(initializationOutput.toCardPaymentScreenState())
    val viewState: StateFlow<CardPaymentScreenState> = _viewState

    private val _viewEvent = MutableStateFlow<CardPaymentViewEvent?>(null)
    val viewEvent: StateFlow<CardPaymentViewEvent?> = _viewEvent.asStateFlow()

    fun onViewEventConsumed() {
        _viewEvent.value = null
    }

    fun clearSubmitState() {
        if (_viewState.value.isLoading) {
            _viewState.value = _viewState.value.copy(isLoading = false)
        }
    }

    val cardNumberPCIState = createPCIFieldState()
    val expirationDatePCIState = createPCIFieldState()
    val securityCodePCIState = createPCIFieldState()

    private var isCancelling = false

    private val analyticsTracker = CardFormAnalyticsTracker(
        isCancelling = { isCancelling },
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
                val previousLength = _viewState.value.cardNumberState.length
                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        length = event.length,
                    ),
                )
                if (event.length.isBeingCleared(previousLength)) {
                    _viewState.value =
                        errorHandler.applyCardNumberErrorState(_viewState.value, emptyList())
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
                val identificationTypeState = _viewState.value.identificationTypeState
                val placeHolder = event.identificationType.id
                    ?.let { identificationTypeState.placeholdersByTypeId[it] }
                    .orEmpty()
                _viewState.value = _viewState.value.copy(
                    identificationTypeState = identificationTypeState.copy(
                        selected = event.identificationType,
                        placeHolder = placeHolder,
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
        isCancelling = true
        analyticsTracker.trackUserCanceled(reason)
        val context = cancelledFormContextUseCase(_viewState.value)
        _viewEvent.value = CardPaymentViewEvent.OnUserCancelled(context)
    }

    fun onSubmit() {
        val state = _viewState.value
        if (state.hasErrors()) return
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            tokenize(buyer = state.toBuyerIdentification()).fold(
                onSuccess = { paymentData ->
                    analyticsTracker.trackSubmit(
                        cardBrand = state.paymentState.paymentMethodId.orEmpty(),
                        transactionAmount = initializationOutput.transactionAmount?.toDouble(),
                        issuer = state.cardIssuers.firstOrNull()?.id.orEmpty(),
                        paymentTypeId = state.paymentState.paymentTypeId.orEmpty(),
                    )
                    _viewEvent.value = CardPaymentViewEvent.OnSuccess(
                        payment = paymentData,
                        installment = state.toMPInstallmentData(initializationOutput.transactionAmount),
                    )
                },
                onError = { error ->
                    analyticsTracker.trackSubmitError(error)
                    _viewState.value = _viewState.value.copy(isLoading = false)
                    _viewEvent.value = CardPaymentViewEvent.OnFailure(error)
                },
            )
        }
    }

    private suspend fun tokenize(
        buyer: BuyerIdentification,
    ) = generateTokenUseCase(
        cardNumberState = cardNumberPCIState,
        expirationDateState = expirationDatePCIState,
        securityCodeState = securityCodePCIState,
        buyerIdentification = buyer,
    ).map { cardToken ->
        val state = _viewState.value
        MPPaymentData(
            transactionAmount = initializationOutput.transactionAmount,
            token = cardToken.token,
            installment = FIRST_INSTALLMENT,
            paymentMethodId = state.paymentState.paymentMethodId.orEmpty(),
            paymentTypeId = state.paymentState.paymentTypeId.orEmpty(),
            issuerId = state.cardIssuers.firstOrNull()?.id,
            payer = Payer(
                documentType = buyer.type,
                documentNumber = buyer.number,
            ),
        )
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
            val (cardTypes, cardBrands) = initializationOutput.paymentMethods.extractCardFilters()
            viewModelScope.launch {
                getCardBinUseCase(
                    bin = cardBin.orEmpty(),
                    amount = initializationOutput.transactionAmount?.toPlainString(),
                    checkoutType = initializationOutput.checkoutType,
                    processingMode = PROCESSING_MODE,
                    filter = CardBinFilter(cardTypes = cardTypes, cardBrands = cardBrands),
                ).onSuccess { data ->
                    _viewState.value = _viewState.value.applyCardBinData(data)
                }
            }
        }
    }
}

private fun CardPaymentScreenState.hasErrors(): Boolean {
    val hasIdentificationError = identificationTypeState.show &&
        identificationTypeState.error.isNotEmpty()
    return cardNumberState.error.isNotEmpty() ||
        expirationDateState.error.isNotEmpty() ||
        secureCodeState.error.isNotEmpty() ||
        cardHolderState.error.isNotEmpty() ||
        hasIdentificationError
}
