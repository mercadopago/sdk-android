package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.mapper.getLength
import com.mercadopago.sdk.android.checkout.domain.mapper.isOptional
import com.mercadopago.sdk.android.checkout.domain.mapper.toMask
import com.mercadopago.sdk.android.checkout.domain.model.CardData
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCode
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardDataByBinUseCase
import com.mercadopago.sdk.android.checkout.presentation.extensions.fold
import com.mercadopago.sdk.android.checkout.presentation.extensions.toCountStringPlaceholder
import com.mercadopago.sdk.android.checkout.presentation.state.CARD_NUMBER_BIN_LENGTH
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.DEFAULT_MAX_CARD_LENGTH
import com.mercadopago.sdk.android.checkout.presentation.state.MessageError
import com.mercadopago.sdk.android.checkout.presentation.validation.CardHolderVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.CardNumberVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.ExpirationDateVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.IdentificationTypeVerifier
import com.mercadopago.sdk.android.checkout.presentation.validation.SecurityCodeVerifier
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.interactor.coreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
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
import java.math.BigDecimal

private const val HELPER_TEXT_OPTIONAL = "Dado opcional"
private const val ERROR_GET_CARD_DATA = "Get card data error"

@Suppress(
    "TooManyFunctions",
    "UnusedPrivateProperty",
) // ViewModel requires multiple event handlers for card payment form
internal class CardPaymentViewModel(
    private val checkoutConfiguration: CheckoutConfiguration?,
    private val coreMethods: CoreMethods = MercadoPagoSDK.getInstance().coreMethods,
    private val getCardDataByBinUseCase: GetCardDataByBinUseCase,
) : ViewModel() {
    private val _viewState = MutableStateFlow(CardPaymentScreenState())
    val viewState: StateFlow<CardPaymentScreenState> = _viewState

    fun generateToken(
        cardNumberState: PCIFieldState,
        expirationDateState: PCIFieldState,
        securityCodeState: PCIFieldState,
    ) {
        viewModelScope.launch {
            val currentState = _viewState.value
            val hasErrors = currentState.cardNumberState.error.isNotEmpty() ||
                currentState.expirationDateState.error.isNotEmpty() ||
                currentState.secureCodeState.error.isNotEmpty() ||
                currentState.cardHolderState.error.isNotEmpty() ||
                currentState.identificationTypeState.error.isNotEmpty()
            if (hasErrors) {
                return@launch
            }
            if (currentState.cardNumberState.length != currentState.cardNumberState.maxLength) {
                _viewState.value = _viewState.value.copy(
                    cardNumberState = currentState.cardNumberState.copy(
                        error = "Please, fill the card number",
                    ),
                )
                return@launch
            }
            _viewState.value = _viewState.value.copy(isLoading = true)
            val result = coreMethods.generateCardToken(
                cardNumberState = cardNumberState,
                expirationDateState = expirationDateState,
                securityCodeState = securityCodeState,
                buyerIdentification = BuyerIdentification(
                    name = viewState.value.cardHolderState.value,
                    number = viewState.value.identificationTypeState.value,
                    type = viewState.value.identificationTypeState.selected?.name,
                ),
            )
            _viewState.value = _viewState.value.copy(isLoading = false)
            when (result) {
                is Result.Success -> {
                }

                is Result.Error -> {
                    handleResultError(result.error, "Generate Token Error")
                }
            }
        }
    }

    fun getIdentificationTypes() {
        viewModelScope.launch {
            val result = coreMethods.getIdentificationTypes()
            when (result) {
                is Result.Success -> {
                    _viewState.value = _viewState.value.copy(
                        identificationTypeState = _viewState.value.identificationTypeState.copy(
                            identificationTypes = result.data,
                            selected = result.data.firstOrNull(),
                        ),
                    )
                }

                is Result.Error -> {
                    handleResultError(result.error, "Get Identification type")
                }
            }
        }
    }

    fun getPaymentMethods(
        bin: String,
        amount: BigDecimal?,
    ) {
        viewModelScope.launch {
            getCardDataByBinUseCase(bin, amount).fold(
                onSuccess = { cardData ->
                    updateStateWithCardData(cardData)
                    updateCardMaskState(cardData.getLength())
                },
                onError = { error ->
                    handleResultError(error, ERROR_GET_CARD_DATA)
                },
            )
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
            ),
            installmentsState = buildInstallmentsState(cardData.installments),
        )
    }

    private fun buildSecurityCodeState(
        securityCode: SecurityCode,
    ) = _viewState.value.secureCodeState.copy(
        secureCodeLength = securityCode.length,
        placeHolder = securityCode.length.toCountStringPlaceholder("Ex:"),
        optional = securityCode.isOptional(),
        helper = if (securityCode.isOptional()) HELPER_TEXT_OPTIONAL else "",
    )

    private fun buildInstallmentsState(
        installments: List<com.mercadopago.sdk.android.coremethods.domain.model.Installment>?,
    ) = _viewState.value.installmentsState.copy(
        showList = installments.isNullOrEmpty().not(),
        installments = installments?.firstOrNull()?.payerCost.orEmpty(),
    )

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
            }

            is ExpirationDateTextFieldEvent.IsValid -> {
                _viewState.value = _viewState.value.copy(
                    expirationDateState = _viewState.value.expirationDateState.copy(
                        valid = event.isValid,
                        error = "Invalid expiration date",
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
                if (!event.isFocused && !_viewState.value.secureCodeState.optional) {
                    handleSecurityCodeInputError()
                }
            }

            is SecurityCodeTextFieldEvent.OnLengthChanged -> {
                _viewState.value = _viewState.value.copy(
                    secureCodeState = _viewState.value.secureCodeState.copy(
                        length = event.length,
                    ),
                )
            }

            is SecurityCodeTextFieldEvent.OnInputFilled -> {
                _viewState.value = _viewState.value.copy(
                    secureCodeState = _viewState.value.secureCodeState.copy(
                        filled = event.isFilled,
                        error = "Please, fill the security code",
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
            }

            is CardNumberTextFieldEvent.IsValid -> {
                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        isValid = event.isValid,
                        error = "Invalid card number",
                    ),
                )
            }

            is CardNumberTextFieldEvent.OnBinChanged -> {
                handleBinChanged(event.cardBin)
            }
        }
    }

    fun onInstallmentSelected(
        value: PayerCost,
    ) {
        _viewState.value = _viewState.value.copy(
            installmentsState = _viewState.value.installmentsState.copy(
                selectedInstallment = value,
            ),
        )
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

    private fun handleBinChanged(
        cardBin: String?,
    ) {
        if ((cardBin?.length ?: 0) < CARD_NUMBER_BIN_LENGTH) {
            _viewState.value = _viewState.value.copy(
                cardNumberState = _viewState.value.cardNumberState.copy(image = null),
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
        _viewState.value = currentState.copy(
            cardNumberState = currentState.cardNumberState.copy(error = cardNumberError),
        )
    }

    private fun handleExpirationDateInputError() {
        val currentState = _viewState.value
        val expirationDateError = ExpirationDateVerifier.verify(currentState.expirationDateState)
        _viewState.value = currentState.copy(
            expirationDateState = currentState.expirationDateState.copy(error = expirationDateError),
        )
    }

    private fun handleSecurityCodeInputError() {
        val currentState = _viewState.value
        val securityCodeError = SecurityCodeVerifier.verify(currentState.secureCodeState)
        _viewState.value = currentState.copy(
            secureCodeState = currentState.secureCodeState.copy(error = securityCodeError),
        )
    }

    private fun handleCardHolderInputError() {
        val currentState = _viewState.value
        val cardHolderError = CardHolderVerifier.verify(currentState.cardHolderState)
        _viewState.value = currentState.copy(
            cardHolderState = currentState.cardHolderState.copy(error = cardHolderError),
        )
    }

    private fun handleIdentificationTypeInputError() {
        val currentState = _viewState.value
        val identificationError =
            IdentificationTypeVerifier.verify(currentState.identificationTypeState)
        _viewState.value = currentState.copy(
            identificationTypeState = currentState.identificationTypeState.copy(error = identificationError),
        )
    }

    private fun handleResultError(
        error: ResultError,
        title: String,
    ) {
        val message = when (error) {
            is ResultError.Request -> error.message
            is ResultError.Validation -> error.message
        }
        _viewState.value = _viewState.value.copy(
            messageError = MessageError(title = title, description = message),
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
}
