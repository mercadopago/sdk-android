package com.mercadopago.sdk.android.example.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.interactor.coreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.example.mappers.toInstallmentModel
import com.mercadopago.sdk.android.example.presentation.data.Installment
import com.mercadopago.sdk.android.example.presentation.state.PaymentScreenViewState
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

internal const val CARD_NUMBER_BIN_LENGTH = 6

@Suppress("TooManyFunctions")
internal class PaymentScreenViewModel(
    private val coreMethods: CoreMethods = MercadoPagoSDK.getInstance().coreMethods,
) : ViewModel() {

    private val _viewState = MutableStateFlow(PaymentScreenViewState())
    val viewState: StateFlow<PaymentScreenViewState> = _viewState

    init {
        getIdentificationTypes()
    }

    fun generateToken(
        cardNumberState: PCIFieldState,
        expirationDateState: PCIFieldState,
        securityCodeState: PCIFieldState,
    ) {
        viewModelScope.launch {
            val result = coreMethods.generateCardToken(
                cardNumberState = cardNumberState,
                expirationDateState = expirationDateState,
                securityCodeState = securityCodeState
            )

            when (result) {
                is Result.Success -> {
                    print(result.data.token)
                }

                is Result.Error -> {
                    when (result.error) {
                        is ResultError.Request -> {
                            print((result.error as ResultError.Request).message)
                        }

                        is ResultError.Validation -> {
                            print((result.error as ResultError.Validation).message)
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
                is Result.Success -> {
                    _viewState.value = _viewState.value.copy(
                        installmentsState = _viewState.value.installmentsState.copy(
                            showList = true,
                            installments = result.data.getOrNull(0)?.payerCost?.toInstallmentModel().orEmpty(),
                        )
                    )
                }

                is Result.Error -> {
                    when (result.error) {
                        is ResultError.Request -> {
                            print((result.error as ResultError.Request).message)
                        }

                        is ResultError.Validation -> {
                            print((result.error as ResultError.Validation).message)
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
                is Result.Success -> {
                    _viewState.value = _viewState.value.copy(
                        identificationState = _viewState.value.identificationState.copy(
                            identificationList = result.data,
                            selectedIdentification = result.data.firstOrNull(),
                        )
                    )
                }

                is Result.Error -> {
                    when (result.error) {
                        is ResultError.Request -> {
                            print((result.error as ResultError.Request).message)
                        }

                        is ResultError.Validation -> {
                            print((result.error as ResultError.Validation).message)
                        }
                    }
                }
            }
        }
    }

    fun getCardIssuers(bin: Int, paymentMethodId: String) {
        viewModelScope.launch {
            val result = coreMethods.getCardIssuers(bin, paymentMethodId)

            when (result) {
                is Result.Success -> {
                    _viewState.value = _viewState.value.copy(
                        cardIssuers = result.data,
                        cardNumberState = _viewState.value.cardNumberState.copy(image = result.data[0].thumbnail)
                    )
                }

                is Result.Error -> {
                    when (result.error) {
                        is ResultError.Request -> {
                            print((result.error as ResultError.Request).message)
                        }

                        is ResultError.Validation -> {
                            print((result.error as ResultError.Validation).message)
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
                is Result.Success -> {
                    _viewState.value = _viewState.value.copy(
                        secureCodeState = _viewState.value.secureCodeState.copy(
                            secureCodeLength = result.data[0].card?.securityCode?.length ?: 3
                        )
                    )
                    getCardIssuers(
                        bin = result.data[0].card?.bin!!,
                        paymentMethodId = result.data[0].id!!
                    )
                }

                is Result.Error -> {
                    when (result.error) {
                        is ResultError.Request -> {
                            print((result.error as ResultError.Request).message)
                        }

                        is ResultError.Validation -> {
                            print((result.error as ResultError.Validation).message)
                        }
                    }
                }
            }
        }
    }

    fun onExpirationDateEvent(event: ExpirationDateTextFieldEvent) {
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
                        valid = event.isValid
                    )
                )
                onFormChanged()
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
        when (event) {
            is SecurityCodeTextFieldEvent.OnFocusChanged -> {
                _viewState.value = _viewState.value.copy(
                    secureCodeState = _viewState.value.secureCodeState.copy(
                        isFocused = event.isFocused
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
                        filled = event.isFilled
                    )
                )
                onFormChanged()
            }
        }
    }

    fun onCardNumberEvent(event: CardNumberTextFieldEvent) {
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
                        isValid = event.isValid
                    )
                )
                onFormChanged()
            }

            is CardNumberTextFieldEvent.OnBinChanged -> {
                if ((event.cardBin?.length ?: 0) < CARD_NUMBER_BIN_LENGTH) {
                    _viewState.value =
                        _viewState.value.copy(
                            cardNumberState = _viewState.value.cardNumberState.copy(image = null),
                            installmentsState = _viewState.value.installmentsState.copy(showList = false)
                        )
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

    fun onFormChanged() {
        _viewState.value = _viewState.value.copy(
            formIsValid = _viewState.value.secureCodeState.filled &&
                _viewState.value.expirationDateState.valid &&
                _viewState.value.identificationState.identificationValue.isNotEmpty() &&
                _viewState.value.installmentsState.selectedInstallment != null
        )
    }
}
