package com.mercadopago.sdk.android.example.presentation.coremethods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.interactor.coreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.MPResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.MPResult
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.example.data.mappers.toInstallmentModel
import com.mercadopago.sdk.android.example.data.model.Installment
import com.mercadopago.sdk.android.example.domain.model.LogType
import com.mercadopago.sdk.android.example.presentation.coremethods.state.PaymentScreenDialogState
import com.mercadopago.sdk.android.example.presentation.coremethods.state.PaymentScreenViewState
import com.mercadopago.sdk.android.example.utils.LogHelper
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

internal const val CARD_NUMBER_BIN_LENGTH = 6
internal const val DEFAULT_MAX_CARD_LENGTH = 19
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
internal const val DEFAULT_CARD_MASK = "#### #### #### ####"

internal class PaymentScreenViewModel(
    private val coreMethods: CoreMethods = MercadoPagoSDK.getInstance().coreMethods,
) : ViewModel() {

    private val _viewState = MutableStateFlow(PaymentScreenViewState())
    val viewState: StateFlow<PaymentScreenViewState> = _viewState

    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    fun generateToken(
        cardNumberState: PCIFieldState,
        expirationDateState: PCIFieldState,
        securityCodeState: PCIFieldState,
    ) {
        viewModelScope.launch {
            if (viewState.value.cardNumberState.length != viewState.value.cardNumberState.maxLength) {
                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        error = Pair(
                            true,
                            "Please, fill the card number"
                        )
                    )
                )
                return@launch
            }
            val result = coreMethods.generateCardToken(
                cardNumberState = cardNumberState,
                expirationDateState = expirationDateState,
                securityCodeState = securityCodeState,
                buyerIdentification = BuyerIdentification(
                    name = viewState.value.identificationState.identificationNameValue,
                    number = viewState.value.identificationState.identificationValue,
                    type = viewState.value.identificationState.selectedIdentification?.name
                )
            )

            when (result) {
                is MPResult.Success -> {
                    LogHelper.log(
                        message = "generateToken - Success",
                        type = LogType.Network,
                        response = gson.toJson(result.data),
                    )
                    onDialogStateChanged(PaymentScreenDialogState.CardToken(token = result.data.token))
                }

                is MPResult.Error -> {
                    when (result.error) {
                        is MPResultError.Request -> {
                            LogHelper.log(
                                message = "generateToken - Error",
                                type = LogType.Network,
                                response = (result.error as MPResultError.Request).message,
                            )
                            _viewState.value = _viewState.value.copy(
                                dialogState = PaymentScreenDialogState.Error(
                                    title = "Generate Token Error",
                                    description = (result.error as MPResultError.Request).message,
                                ),
                            )
                        }

                        is MPResultError.Validation -> {
                            LogHelper.log(
                                message = "generateToken - Error",
                                type = LogType.Network,
                                response = (result.error as MPResultError.Validation).message,
                            )
                            _viewState.value = _viewState.value.copy(
                                dialogState = PaymentScreenDialogState.Error(
                                    title = "Generate Token Error",
                                    description = (result.error as MPResultError.Validation).message,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }

    fun getInstallment(
        bin: String,
        amount: BigDecimal
    ) {
        viewModelScope.launch {
            val result = coreMethods.getInstallments(
                bin = bin,
                amount = amount,
            )

            when (result) {
                is MPResult.Success -> {
                    LogHelper.log(
                        message = "getInstallment - Success",
                        type = LogType.Network,
                        response = gson.toJson(result.data),
                    )
                    _viewState.value = _viewState.value.copy(
                        installmentsState = _viewState.value.installmentsState.copy(
                            showList = true,
                            installments = result.data.getOrNull(0)?.payerCost?.toInstallmentModel()
                                .orEmpty(),
                        )
                    )
                }

                is MPResult.Error -> {
                    when (result.error) {
                        is MPResultError.Request -> {
                            LogHelper.log(
                                message = "getInstallment - Error",
                                type = LogType.Network,
                                response = (result.error as MPResultError.Request).message,
                            )
                        }

                        is MPResultError.Validation -> {
                            LogHelper.log(
                                message = "getInstallment - Error",
                                type = LogType.Network,
                                response = (result.error as MPResultError.Validation).message,
                            )
                        }
                    }
                }
            }
        }
    }

    fun getIdentificationTypes() {
        viewModelScope.launch {
            val result = coreMethods.getIdentificationTypes()
            when (result) {
                is MPResult.Success -> {
                    LogHelper.log(
                        message = "getIdentificationTypes - Success",
                        type = LogType.Network,
                        response = gson.toJson(result.data),
                    )
                    _viewState.value = _viewState.value.copy(
                        identificationState = _viewState.value.identificationState.copy(
                            identificationList = result.data,
                            selectedIdentification = result.data.firstOrNull(),
                        )
                    )
                }

                is MPResult.Error -> {
                    when (result.error) {
                        is MPResultError.Request -> {
                            LogHelper.log(
                                message = "getIdentificationTypes - Error",
                                type = LogType.Network,
                                response = (result.error as MPResultError.Request).message,
                            )
                        }

                        is MPResultError.Validation -> {
                            LogHelper.log(
                                message = "getIdentificationTypes - Error",
                                type = LogType.Network,
                                response = (result.error as MPResultError.Validation).message,
                            )
                        }
                    }
                }
            }
        }
    }

    fun getCardIssuers(bin: String, paymentMethodId: String) {
        viewModelScope.launch {
            val result = coreMethods.getCardIssuers(bin, paymentMethodId)

            when (result) {
                is MPResult.Success -> {
                    LogHelper.log(
                        message = "getCardIssuers - Success",
                        type = LogType.Network,
                        response = gson.toJson(result.data),
                    )
                    _viewState.value = _viewState.value.copy(
                        cardIssuers = result.data,
                        cardNumberState = _viewState.value.cardNumberState.copy(image = result.data[0].thumbnail)
                    )
                }

                is MPResult.Error -> {
                    when (result.error) {
                        is MPResultError.Request -> {
                            LogHelper.log(
                                message = "getCardIssuers - Error",
                                type = LogType.Network,
                                response = (result.error as MPResultError.Request).message,
                            )
                        }

                        is MPResultError.Validation -> {
                            LogHelper.log(
                                message = "getPaymentMethods - Error",
                                type = LogType.Network,
                                response = (result.error as MPResultError.Validation).message,
                            )
                        }
                    }
                }
            }
        }
    }

    fun getPaymentMethods(bin: String) {
        viewModelScope.launch {
            val result = coreMethods.getPaymentMethods(bin = bin)

            when (result) {
                is MPResult.Success -> {
                    LogHelper.log(
                        message = "getPaymentMethods - Success",
                        type = LogType.Network,
                        response = gson.toJson(result.data),
                    )
                    _viewState.value = _viewState.value.copy(
                        secureCodeState = _viewState.value.secureCodeState.copy(
                            secureCodeLength = result.data[0].card?.securityCode?.length ?: 3
                        )

                    )
                    getCardIssuers(
                        bin = bin,
                        paymentMethodId = result.data[0].id!!
                    )
                    updateCardMaskState(
                        result.data.firstOrNull()?.card?.length?.max ?: DEFAULT_MAX_CARD_LENGTH
                    )
                }

                is MPResult.Error -> {
                    when (result.error) {
                        is MPResultError.Request -> {
                            LogHelper.log(
                                message = "getPaymentMethods - Error",
                                type = LogType.Network,
                                response = (result.error as MPResultError.Request).message,
                            )
                        }

                        is MPResultError.Validation -> {
                            LogHelper.log(
                                message = "getPaymentMethods - Error",
                                type = LogType.Network,
                                response = (result.error as MPResultError.Validation).message,
                            )
                        }
                    }
                }
            }
        }
    }

    fun onExpirationDateEvent(event: ExpirationDateTextFieldEvent) {
        LogHelper.log(
            message = "onExpirationDateEvent - $event",
            type = LogType.Function,
            response = event.toString(),
        )
        when (event) {
            is ExpirationDateTextFieldEvent.OnInputFilled -> {
                _viewState.value = _viewState.value.copy(
                    expirationDateState = _viewState.value.expirationDateState.copy(
                        filled = event.isFilled
                    ),
                )
            }

            is ExpirationDateTextFieldEvent.IsValid -> {
                _viewState.value = _viewState.value.copy(
                    expirationDateState = _viewState.value.expirationDateState.copy(
                        valid = event.isValid,
                        error = Pair(!event.isValid, "Invalid expiration date")
                    )
                )
            }

            is ExpirationDateTextFieldEvent.OnFocusChanged -> {
                _viewState.value = _viewState.value.copy(
                    expirationDateState = _viewState.value.expirationDateState.copy(
                        isFocused = event.isFocused
                    )
                )
            }

            is ExpirationDateTextFieldEvent.OnLengthChanged -> {
                _viewState.value = _viewState.value.copy(
                    expirationDateState = _viewState.value.expirationDateState.copy(
                        length = event.length
                    )
                )
            }
        }
    }

    fun onSecurityCodeEvent(event: SecurityCodeTextFieldEvent) {
        LogHelper.log(
            message = "onSecurityCodeEvent - $event",
            type = LogType.Function,
            response = event.toString(),
        )
        when (event) {
            is SecurityCodeTextFieldEvent.OnFocusChanged -> {
                _viewState.value = _viewState.value.copy(
                    secureCodeState = _viewState.value.secureCodeState.copy(
                        isFocused = event.isFocused,
                        error = Pair(
                            !_viewState.value.secureCodeState.filled
                                && _viewState.value.secureCodeState.isFocused,
                            "Please, fill the security code"
                        )
                    )
                )
            }

            is SecurityCodeTextFieldEvent.OnLengthChanged -> {
                _viewState.value = _viewState.value.copy(
                    secureCodeState = _viewState.value.secureCodeState.copy(
                        length = event.length
                    )
                )
            }

            is SecurityCodeTextFieldEvent.OnInputFilled -> {
                _viewState.value = _viewState.value.copy(
                    secureCodeState = _viewState.value.secureCodeState.copy(
                        filled = event.isFilled,
                        error = Pair(false, "")
                    )
                )
            }
        }
    }

    fun onCardNumberEvent(event: CardNumberTextFieldEvent) {
        LogHelper.log(
            message = "onCardNumberEvent - $event",
            type = LogType.Function,
            response = event.toString(),
        )
        when (event) {
            is CardNumberTextFieldEvent.OnFocusChanged -> {
                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        isFocused = event.isFocused
                    )
                )
            }

            is CardNumberTextFieldEvent.OnLengthChanged -> {
                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        length = event.length
                    )
                )
            }

            is CardNumberTextFieldEvent.OnLastFourDigitsFilled -> {
                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        lastFourDigits = event.lastFourDigits
                    )
                )
            }

            is CardNumberTextFieldEvent.IsValid -> {
                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        isValid = event.isValid,
                        error = Pair(!event.isValid, "Invalid card number")
                    )
                )
            }

            is CardNumberTextFieldEvent.OnBinChanged -> {
                if ((event.cardBin?.length ?: 0) < CARD_NUMBER_BIN_LENGTH) {
                    _viewState.value =
                        _viewState.value.copy(
                            cardNumberState = _viewState.value.cardNumberState.copy(image = null),
                            installmentsState = _viewState.value.installmentsState.copy(showList = false)
                        )
                    updateCardMaskState(DEFAULT_MAX_CARD_LENGTH)
                } else {
                    getInstallment(
                        bin = event.cardBin.orEmpty(),
                        amount = 1000.0.toBigDecimal(),
                    )
                    getPaymentMethods(bin = event.cardBin.orEmpty())
                }

                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        cardBin = event.cardBin
                    )
                )

                getInstallment(
                    bin = event.cardBin.orEmpty(),
                    amount = 1000.0.toBigDecimal(),
                )
            }
        }
    }

    fun onInstallmentSelected(value: Installment) {
        _viewState.value = _viewState.value.copy(
            installmentsState = _viewState.value.installmentsState.copy(
                selectedInstallment = value
            )
        )
    }

    fun onIdentificationTypeValueChanged(value: String) {
        _viewState.value = _viewState.value.copy(
            identificationState = _viewState.value.identificationState.copy(
                identificationValue = value
            )
        )
    }

    fun onIdentificationTypeChanged(identificationType: IdentificationType) {
        _viewState.value = _viewState.value.copy(
            identificationState = _viewState.value.identificationState.copy(
                selectedIdentification = identificationType,
            )
        )
    }

    fun onCardHolderNameChanged(value: String) {
        _viewState.value = _viewState.value.copy(
            identificationState = _viewState.value.identificationState.copy(
                identificationNameValue = value
            )
        )
    }

    fun onDialogStateChanged(dialogState: PaymentScreenDialogState) {
        _viewState.value = _viewState.value.copy(dialogState = dialogState)
    }

    private fun updateCardMaskState(cardLength: Int) {
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
                }
            )
        )
    }
}
