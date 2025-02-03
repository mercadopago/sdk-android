package com.mercadopago.sdk.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeFieldEvent
import com.mercadopago.sdk.android.presentation.state.PaymentScreenViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentScreenViewModel(
    private val coreMethods: CoreMethods = CoreMethods.getInstance(),
) : ViewModel() {

    private val _viewState = MutableStateFlow(PaymentScreenViewState())
    val viewState: StateFlow<PaymentScreenViewState> = _viewState

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
                }

                is Result.Error -> {
                }
            }
        }
    }

    fun onExpirationDateEvent(event: ExpirationDateFieldEvent) {
        when (event) {
            is ExpirationDateFieldEvent.OnInputFilled -> {
                _viewState.value = _viewState.value.copy(
                    expirationDateState = _viewState.value.expirationDateState.copy(
                        filled = event.isFilled
                    ),
                )
            }

            is ExpirationDateFieldEvent.IsValid -> {
                _viewState.value = _viewState.value.copy(
                    expirationDateState = _viewState.value.expirationDateState.copy(
                        valid = !event.isValid
                    )
                )
            }

            is ExpirationDateFieldEvent.OnFocusChanged -> {
                _viewState.value = _viewState.value.copy(
                    expirationDateState = _viewState.value.expirationDateState.copy(
                        isFocused = event.isFocused
                    )
                )
            }

            is ExpirationDateFieldEvent.OnLengthChanged -> {
                _viewState.value = _viewState.value.copy(
                    expirationDateState = _viewState.value.expirationDateState.copy(
                        length = event.length
                    )
                )
            }
        }
    }

    fun onSecurityCodeEvent(event: SecurityCodeFieldEvent) {
        when (event) {
            is SecurityCodeFieldEvent.OnFocusChanged -> {
                _viewState.value = _viewState.value.copy(
                    secureCodeState = _viewState.value.secureCodeState.copy(
                        isFocused = event.isFocused
                    )
                )
            }

            is SecurityCodeFieldEvent.OnLengthChanged -> {
                _viewState.value = _viewState.value.copy(
                    secureCodeState = _viewState.value.secureCodeState.copy(
                        length = event.length
                    )
                )
            }

            is SecurityCodeFieldEvent.OnInputFilled -> {
                _viewState.value = _viewState.value.copy(
                    secureCodeState = _viewState.value.secureCodeState.copy(
                        filled = event.isFilled
                    )
                )
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
            }

            is CardNumberTextFieldEvent.OnBinChanged -> {
                _viewState.value = _viewState.value.copy(
                    cardNumberState = _viewState.value.cardNumberState.copy(
                        cardBin = event.cardBin
                    )
                )
            }
        }
    }
}
