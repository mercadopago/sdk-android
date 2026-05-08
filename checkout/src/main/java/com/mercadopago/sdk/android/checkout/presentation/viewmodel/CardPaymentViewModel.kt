package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.getAmount
import com.mercadopago.sdk.android.checkout.core.model.internal.getAmountOrZero
import com.mercadopago.sdk.android.checkout.core.model.internal.toCheckoutType
import com.mercadopago.sdk.android.checkout.data.remote.utils.PROCESSING_MODE
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.extensions.extractCardFilters
import com.mercadopago.sdk.android.checkout.domain.extensions.isComplete
import com.mercadopago.sdk.android.checkout.domain.extensions.toMask
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.Payer
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.usecase.CardBinFilter
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.InitializeCardFormUseCase
import com.mercadopago.sdk.android.checkout.presentation.extensions.fold
import com.mercadopago.sdk.android.checkout.presentation.extensions.getCurrencyString
import com.mercadopago.sdk.android.checkout.presentation.extensions.getTotal
import com.mercadopago.sdk.android.checkout.presentation.extensions.getTotalDecimalPart
import com.mercadopago.sdk.android.checkout.presentation.extensions.isBeingCleared
import com.mercadopago.sdk.android.checkout.presentation.extensions.toCurrencyString
import com.mercadopago.sdk.android.checkout.presentation.mapper.applyCardBinData
import com.mercadopago.sdk.android.checkout.presentation.mapper.toCardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.model.CancelReason
import com.mercadopago.sdk.android.checkout.presentation.state.CARD_NUMBER_BIN_LENGTH
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsScreenState
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
import java.math.BigDecimal
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState.Companion.create as createPCIFieldState

private const val FIRST_INSTALLMENT = 1
private const val INSTALLMENTS_SEPARATOR = "x"

@Suppress("TooManyFunctions")
internal class CardPaymentViewModel(
    private val checkoutConfiguration: CheckoutConfiguration?,
    private val getCardBinUseCase: GetCardBinUseCase,
    private val initializeCardFormUseCase: InitializeCardFormUseCase,
    private val generateTokenUseCase: GenerateTokenUseCase,
) : ViewModel() {
    private val cancelledFormContextUseCase = CancelledFormContextUseCase()
    private val _viewState = MutableStateFlow(CardPaymentScreenState())
    val viewState: StateFlow<CardPaymentScreenState> = _viewState

    private val _viewEvent = MutableStateFlow<CardPaymentViewEvent?>(null)
    val viewEvent: StateFlow<CardPaymentViewEvent?> = _viewEvent.asStateFlow()

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
        val currentState = _viewState.value
        val context = cancelledFormContextUseCase(currentState)
        CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.UserCancelled(context))
    }

    fun initialization() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            initializeCardFormUseCase(
                amount = checkoutConfiguration?.getAmountOrZero().orEmpty(),
                checkoutType = checkoutConfiguration.toCheckoutType(),
            ).fold(
                onSuccess = { data ->
                    _viewState.value = data.toCardPaymentScreenState()
                },
                onError = { error ->
                    analyticsTracker.trackInitializeError(error)
                    CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(error))
                },
            ).apply {
                _viewState.value = _viewState.value.copy(isLoading = false)
            }
        }
    }

    fun onSubmit() {
        _viewState.value.let { state ->
            val hasIdentificationError = state.identificationTypeState.show &&
                state.identificationTypeState.error.isNotEmpty()
            val hasErrors = state.cardNumberState.error.isNotEmpty() ||
                state.expirationDateState.error.isNotEmpty() ||
                state.secureCodeState.error.isNotEmpty() ||
                state.cardHolderState.error.isNotEmpty() ||
                hasIdentificationError
            if (!hasErrors) {
                if (state.installmentsState.showList) {
                    _viewState.value = state.copy(installmentsScreen = buildInstallmentsScreen(state))
                    _viewEvent.value = CardPaymentViewEvent.NavigateToInstallments
                } else {
                    generateToken(
                        buyerIdentification = state.toBuyerIdentification(),
                        installment = FIRST_INSTALLMENT,
                    )
                }
            }
        }
    }

    fun onViewEventConsumed() {
        _viewEvent.value = null
    }

    fun onInstallmentSelected(
        installment: Int,
    ) {
        val current = _viewState.value.installmentsScreen
        if (current.displayType != InstallmentsDisplayType.RadioButton) return
        _viewState.value = _viewState.value.copy(
            installmentsScreen = current.copy(
                installmentsState = current.installmentsState.map {
                    it.copy(isSelected = it.number == installment)
                },
            ),
        )
    }

    fun onPayClicked() {
        val selected = _viewState.value.installmentsScreen.installmentsState.firstOrNull { it.isSelected }
            ?: return
        generateToken(
            buyerIdentification = _viewState.value.toBuyerIdentification(),
            installment = selected.number,
        )
    }

    private fun buildInstallmentsScreen(
        state: CardPaymentScreenState,
    ): InstallmentsScreenState {
        val installmentsCopy = state.installmentsState
        val displayType = installmentsCopy.displayType
        val amount = checkoutConfiguration?.getAmount() ?: BigDecimal.ZERO
        val title = when (displayType) {
            InstallmentsDisplayType.Chevron -> installmentsCopy.headerChevron
            InstallmentsDisplayType.RadioButton -> installmentsCopy.headerRadio
        }
        val installments = installmentsCopy.installments
            .toInstallmentStates(interestFreeLabel = installmentsCopy.interestFreeLabel)
            .let { list ->
                if (displayType == InstallmentsDisplayType.RadioButton) {
                    list.mapIndexed { index, item -> item.copy(isSelected = index == 0) }
                } else {
                    list
                }
            }
        val brand = state.paymentState.paymentMethodId.toBrandLabel()
        val lastFourDigits = state.cardNumberState.lastFourDigits
        val subtitle = listOf(brand, "****", lastFourDigits)
            .filter { it.isNotEmpty() }
            .joinToString(separator = " ")
        return InstallmentsScreenState(
            title = title,
            displayType = displayType,
            installmentsState = installments,
            footerState = FooterState(
                title = installmentsCopy.totalLabel,
                currencySymbol = null.getCurrencyString(),
                amountIntegerPart = amount.getTotal(),
                amountDecimalPart = amount.getTotalDecimalPart(),
                subtitle = subtitle,
                buttonLabel = installmentsCopy.payButtonLabel
                    .takeIf { displayType == InstallmentsDisplayType.RadioButton && it.isNotEmpty() },
            ),
        )
    }

    private fun String?.toBrandLabel(): String =
        this.orEmpty()
            .split('_')
            .filter { it.isNotEmpty() }
            .joinToString(separator = " ") { it.replaceFirstChar(Char::uppercaseChar) }

    private fun List<Quota>.toInstallmentStates(
        interestFreeLabel: String,
    ): List<InstallmentState> =
        map { quota ->
            val installmentAmount = quota.installmentAmount
            val totalAmount = quota.totalAmount
            val isInterestFree = totalAmount != null &&
                installmentAmount != null &&
                installmentAmount.compareTo(totalAmount) == 0
            InstallmentState(
                text = "${quota.installments} $INSTALLMENTS_SEPARATOR ${installmentAmount?.toCurrencyString()}",
                description = "",
                trailing = when {
                    quota.installments == FIRST_INSTALLMENT -> ""
                    isInterestFree -> interestFreeLabel
                    else -> totalAmount?.toCurrencyString().orEmpty()
                },
                interestFree = isInterestFree,
                isSelected = false,
                number = quota.installments ?: FIRST_INSTALLMENT,
            )
        }

    private fun CardPaymentScreenState.toBuyerIdentification(): BuyerIdentification =
        BuyerIdentification(
            name = cardHolderState.value,
            number = identificationTypeState.value,
            type = identificationTypeState.selected?.name,
        )

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
            val (cardTypes, cardBrands) = checkoutConfiguration?.paymentMethods.extractCardFilters()
            viewModelScope.launch {
                getCardBinUseCase(
                    bin = cardBin.orEmpty(),
                    amount = checkoutConfiguration?.getAmount()?.toPlainString(),
                    checkoutType = checkoutConfiguration.toCheckoutType(),
                    processingMode = PROCESSING_MODE,
                    filter = CardBinFilter(cardTypes = cardTypes, cardBrands = cardBrands),
                ).fold(
                    onSuccess = { data ->
                        _viewState.value = _viewState.value.applyCardBinData(data)
                    },
                    onError = { },
                )
            }
        }
    }

    private fun generateToken(
        buyerIdentification: BuyerIdentification,
        installment: Int,
    ) {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            generateTokenUseCase(
                cardNumberState = cardNumberPCIState,
                expirationDateState = expirationDatePCIState,
                securityCodeState = securityCodePCIState,
                buyerIdentification = buyerIdentification,
            ).fold(
                onSuccess = { cardToken ->
                    val paymentData = MPPaymentData(
                        transactionAmount = checkoutConfiguration?.getAmount(),
                        token = cardToken.token,
                        installment = installment,
                        paymentMethodId = viewState.value.paymentState.paymentMethodId.orEmpty(),
                        paymentTypeId = viewState.value.paymentState.paymentTypeId.orEmpty(),
                        issuerId = viewState.value.cardIssuers.firstOrNull()?.id,
                        payer = Payer(
                            documentType = buyerIdentification.type,
                            documentNumber = buyerIdentification.number,
                        ),
                    )
                    analyticsTracker.trackSubmit(
                        cardBrand = viewState.value.paymentState.paymentMethodId.orEmpty(),
                        transactionAmount = checkoutConfiguration?.getAmount()?.toDouble(),
                        issuer = viewState.value.cardIssuers.firstOrNull()?.id.orEmpty(),
                        paymentTypeId = viewState.value.paymentState.paymentTypeId.orEmpty(),
                    )
                    CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Success(paymentData))
                },
                onError = { checkoutError ->
                    analyticsTracker.trackSubmitError(checkoutError)
                    CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(checkoutError))
                },
            ).apply {
                _viewState.value = _viewState.value.copy(isLoading = false)
            }
        }
    }
}
