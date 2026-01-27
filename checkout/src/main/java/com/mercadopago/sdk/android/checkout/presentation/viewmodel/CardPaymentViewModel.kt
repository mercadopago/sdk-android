package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.presentation.state.CARD_NUMBER_BIN_LENGTH
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.DEFAULT_CARD_MASK
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

private const val CARD_LENGTH_8_MASK = "#### ####"
private const val CARD_LENGTH_9_MASK = "#### #####"
private const val CARD_LENGTH_10_MASK = "#### ######"
private const val CARD_LENGTH_11_MASK = "#### #### ###"
private const val CARD_LENGTH_12_MASK = "#### #### ####"
private const val CARD_LENGTH_13_MASK = "#### ###### ###"
private const val CARD_LENGTH_14_MASK = "#### ###### ####"
private const val CARD_LENGTH_15_MASK = "#### ###### #####"
private const val CARD_LENGTH_17_MASK = "#### #### #### #####"
private const val CARD_LENGTH_19_MASK = "#### #### #### #### ###"

@Suppress("TooManyFunctions") // ViewModel requires multiple event handlers for card payment form
internal class CardPaymentViewModel(
    private val coreMethods: CoreMethods = MercadoPagoSDK.getInstance().coreMethods,
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

    fun getInstallments(
        bin: String,
        amount: BigDecimal,
    ) {
        viewModelScope.launch {
            val result = coreMethods.getInstallments(bin = bin, amount = amount)
            when (result) {
                is Result.Success -> {
                    _viewState.value = _viewState.value.copy(
                        installmentsState = _viewState.value.installmentsState.copy(
                            showList = true,
                            installments = result.data.getOrNull(0)?.payerCost.orEmpty(),
                        ),
                    )
                }

                is Result.Error -> {
                    handleResultError(result.error, "Get Installment error")
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

    fun getCardIssuers(
        bin: String,
        paymentMethodId: String,
    ) {
        viewModelScope.launch {
            val result = coreMethods.getCardIssuers(bin, paymentMethodId)
            when (result) {
                is Result.Success -> {
                    _viewState.value = _viewState.value.copy(
                        cardIssuers = result.data,
                        cardNumberState = _viewState.value.cardNumberState.copy(
                            image = result.data.getOrNull(0)?.thumbnail,
                        ),
                    )
                }

                is Result.Error -> {
                    handleResultError(result.error, "Get Card Issuer error")
                }
            }
        }
    }

    fun getPaymentMethods(
        bin: String,
    ) {
        viewModelScope.launch {
            val result = coreMethods.getPaymentMethods(bin = bin)
            when (result) {
                is Result.Success -> {
                    val paymentMethod = result.data.firstOrNull()
                    _viewState.value = _viewState.value.copy(
                        secureCodeState = _viewState.value.secureCodeState.copy(
                            secureCodeLength = paymentMethod?.card?.securityCode?.length ?: 3,
                        ),
                    )
                    paymentMethod?.id?.let { paymentMethodId ->
                        getCardIssuers(bin = bin, paymentMethodId = paymentMethodId)
                    }
                    updateCardMaskState(
                        paymentMethod?.card?.length?.max ?: DEFAULT_MAX_CARD_LENGTH,
                    )
                }

                is Result.Error -> {
                    handleResultError(result.error, "Get Payment methods error")
                }
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
                    handleInputErrors()
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
                    handleInputErrors()
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
                    handleInputErrors()
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

    fun onTooltipClick(
    ) {
        _viewState.value = _viewState.value.copy(
            showTooltip = !_viewState.value.showTooltip
        )
    }

    fun onMessageClick(
    ) {
        _viewState.value = _viewState.value.copy(
            showMessage = false
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
                    handleInputErrors()
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
                    handleInputErrors()
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
            getInstallments(bin = cardBin.orEmpty(), amount = 1000.0.toBigDecimal())
            getPaymentMethods(bin = cardBin.orEmpty())
        }
        _viewState.value = _viewState.value.copy(
            cardNumberState = _viewState.value.cardNumberState.copy(
                cardBin = cardBin,
            ),
        )
    }

    private fun handleInputErrors() {
        val currentState = _viewState.value
        val cardNumberError = CardNumberVerifier.verify(currentState.cardNumberState)
        val expirationDateError = ExpirationDateVerifier.verify(currentState.expirationDateState)
        val securityCodeError = SecurityCodeVerifier.verify(currentState.secureCodeState)
        val cardHolderError = CardHolderVerifier.verify(currentState.cardHolderState)
        val identificationError =
            IdentificationTypeVerifier.verify(currentState.identificationTypeState)
        _viewState.value = currentState.copy(
            cardNumberState = currentState.cardNumberState.copy(error = cardNumberError),
            expirationDateState = currentState.expirationDateState.copy(error = expirationDateError),
            secureCodeState = currentState.secureCodeState.copy(error = securityCodeError),
            cardHolderState = currentState.cardHolderState.copy(error = cardHolderError),
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
                mask = when (cardLength) {
                    8 -> CARD_LENGTH_8_MASK
                    9 -> CARD_LENGTH_9_MASK
                    10 -> CARD_LENGTH_10_MASK
                    11 -> CARD_LENGTH_11_MASK
                    12 -> CARD_LENGTH_12_MASK
                    13 -> CARD_LENGTH_13_MASK
                    14 -> CARD_LENGTH_14_MASK
                    15 -> CARD_LENGTH_15_MASK
                    16 -> DEFAULT_CARD_MASK
                    17 -> CARD_LENGTH_17_MASK
                    19 -> CARD_LENGTH_19_MASK
                    else -> DEFAULT_CARD_MASK
                },
            ),
        )
    }
}
