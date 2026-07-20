package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.presentation.shared.withButtonEnabled
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import com.mercadopago.sdk.android.checkout.presentation.validation.SecurityCodeVerifier
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Drives the CVV (security code) screen shown for a saved card before tokenization.
 *
 * The screen is only reached when [com.mercadopago.sdk.android.checkout.domain.usecase.GetSecurityCodeScreenUseCase]
 * returns a non-null config, which the [PaymentBrickViewModel] forwards as [SecurityCodeScreenConfig].
 *
 * PCI: the raw CVV never reaches this ViewModel. It only observes length/focus/filled through
 * [SecurityCodeTextFieldEvent]; the actual digits live inside the field's `PCIFieldState` and are
 * handed straight to tokenization (wired in A24).
 */
internal class SecurityCodeViewModel(
    config: SecurityCodeScreenConfig,
    private val securityCodeVerifier: SecurityCodeVerifier = SecurityCodeVerifier(),
) : ViewModel() {
    private val _viewState = MutableStateFlow(config.toInitialState())
    val viewState: StateFlow<SecurityCodeScreenState> = _viewState.asStateFlow()

    private val _viewEvent = MutableStateFlow<SecurityCodeViewEvent?>(null)
    val viewEvent: StateFlow<SecurityCodeViewEvent?> = _viewEvent.asStateFlow()

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
                // Clear the error as soon as the user resumes typing.
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

    /** Validates the CVV. When invalid, publishes [SecurityCodeScreenState.fieldError] and does not advance. */
    fun onContinue() {
        if (!validate()) return
        // Tokenization is wired in A24.
    }

    fun onUserCancelled() {
        _viewEvent.value = SecurityCodeViewEvent.OnUserCancelled(
            MPUserCancelledContext.Payment(screens = listOf(Screen.SECURITY_CODE)),
        )
    }

    fun onViewEventConsumed() {
        _viewEvent.value = null
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
}
