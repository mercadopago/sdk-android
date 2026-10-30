package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.domain.extensions.toAnalyticsErrorType
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.domain.usecase.GenerateTokenWithCardIdUseCase
import com.mercadopago.sdk.android.checkout.presentation.shared.withButtonEnabled
import com.mercadopago.sdk.android.checkout.presentation.shared.withButtonLoading
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledPaymentContextUseCase
import com.mercadopago.sdk.android.checkout.presentation.validation.SecurityCodeVerifier
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class SecurityCodeViewModel(
    config: SecurityCodeScreenConfig,
    private val cardId: String = config.cardId,
    private val generateTokenUseCase: GenerateTokenWithCardIdUseCase,
    private val cancelledPaymentContextUseCase: CancelledPaymentContextUseCase,
    private val securityCodeVerifier: SecurityCodeVerifier = SecurityCodeVerifier(),
) : ViewModel() {
    private val analyticsTracker = SecurityCodeAnalyticsTracker(
        paymentMethodId = config.paymentMethodId,
        paymentTypeId = config.paymentTypeId,
        issuerId = config.issuerId,
        cardId = config.cardId,
        isLoading = { _viewState.value.footerState.buttonState?.isLoading == true },
    )

    private val _viewState = MutableStateFlow(config.toInitialState())
    val viewState: StateFlow<SecurityCodeScreenState> = _viewState.asStateFlow()

    private val _viewEvent = MutableStateFlow<SecurityCodeViewEvent?>(null)
    val viewEvent: StateFlow<SecurityCodeViewEvent?> = _viewEvent.asStateFlow()

    init {
        markScreenPresented()
        analyticsTracker.trackView()
    }

    fun onSecurityCodeEvent(
        event: SecurityCodeTextFieldEvent,
    ) {
        when (event) {
            is SecurityCodeTextFieldEvent.OnFocusChanged -> {
                updateField { it.copy(isFocused = event.isFocused) }
                if (!event.isFocused) validate()
            }

            is SecurityCodeTextFieldEvent.OnLengthChanged -> {
                updateField { it.copy(length = event.length) }
                _viewState.value = _viewState.value.copy(fieldError = null)
            }

            is SecurityCodeTextFieldEvent.OnInputFilled -> {
                updateField { it.copy(filled = event.isFilled) }
                _viewState.value = _viewState.value.copy(
                    footerState = _viewState.value.footerState.withButtonEnabled(event.isFilled),
                )
            }

            is SecurityCodeTextFieldEvent.IsValid -> {
                updateField { it.copy(isValid = event.isValid) }
            }
        }
    }

    fun onContinue(
        securityCodeState: PCIFieldState,
    ) {
        if (!validate()) return
        analyticsTracker.trackContinue()
        _viewState.value = _viewState.value.copy(
            footerState = _viewState.value.footerState.withButtonLoading(true),
        )
        viewModelScope.launch {
            when (val result = generateTokenUseCase(cardId, securityCodeState)) {
                is Result.Success -> {
                    _viewEvent.value = SecurityCodeViewEvent.OnTokenSuccess(
                        cardId = cardId,
                        token = result.data,
                    )
                }
                is Result.Error -> {
                    analyticsTracker.trackContinueError(result.error.toAnalyticsErrorType())
                    _viewEvent.value = SecurityCodeViewEvent.OnTokenError(error = result.error)
                }
            }
            _viewState.value = _viewState.value.copy(
                footerState = _viewState.value.footerState.withButtonLoading(false),
            )
        }
    }

    fun onUserCancelled() {
        analyticsTracker.trackBack()
        _viewEvent.value = SecurityCodeViewEvent.OnUserCancelled(
            context = cancelledPaymentContextUseCase(),
        )
    }

    fun onViewEventConsumed() {
        _viewEvent.value = null
    }

    override fun onCleared() {
        analyticsTracker.trackBack()
        super.onCleared()
    }

    private fun validate(): Boolean {
        val error = securityCodeVerifier.verify(_viewState.value.securityCodeState)
        _viewState.value = _viewState.value.copy(fieldError = error.ifEmpty { null })
        return error.isEmpty()
    }

    private inline fun updateField(
        transform: (SecurityCodeState) -> SecurityCodeState,
    ) {
        _viewState.value = _viewState.value.copy(
            securityCodeState = transform(_viewState.value.securityCodeState),
        )
    }

    private fun SecurityCodeScreenConfig.toInitialState(): SecurityCodeScreenState =
        SecurityCodeScreenState(
            title = title,
            securityCodeState = SecurityCodeState(
                label = securityCodeState.label,
                placeHolder = securityCodeState.placeholder,
                helper = securityCodeState.helper,
                error = securityCodeState.error,
                maxLength = securityCodeState.maxLength,
                validation = ValidationState(
                    errorEmpty = securityCodeState.error,
                    errorIncomplete = securityCodeState.error,
                ),
            ),
            footerState = footerState,
            cardTitle = cardTitle,
            cardDescription = cardDescription,
            cardImageUrl = cardImageUrl,
        )

    private fun markScreenPresented() {
        cancelledPaymentContextUseCase.markScreenPresented(Screen.SECURITY_CODE)
    }
}
