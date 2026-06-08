package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.mercadopago.sdk.android.checkout.domain.extensions.isValidEmailFormat
import com.mercadopago.sdk.android.checkout.domain.model.EmailInitializationOutput
import com.mercadopago.sdk.android.checkout.presentation.state.EmailFieldState
import com.mercadopago.sdk.android.checkout.presentation.state.EmailScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import com.mercadopago.sdk.android.checkout.presentation.validation.EmailVerifier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class EmailViewModel : ViewModel() {
    private val _viewState = MutableStateFlow<EmailScreenState?>(null)
    val viewState: StateFlow<EmailScreenState?> = _viewState.asStateFlow()

    private val verifier = EmailVerifier()

    fun initialize(
        output: EmailInitializationOutput,
    ) {
        val value = output.prefilledEmail.orEmpty()
        _viewState.value = EmailScreenState(
            title = output.title,
            buttonLabel = output.buttonLabel,
            isButtonEnabled = value.isValidEmailFormat(),
            fieldState = EmailFieldState(
                label = output.fieldLabel,
                placeHolder = output.fieldPlaceholder,
                showPlaceHolder = true,
                value = value,
                isValid = value.isValidEmailFormat(),
                validation = ValidationState(
                    errorEmpty = output.errorFieldEmpty,
                    errorInvalid = output.errorEmailInvalid,
                ),
            ),
        )
    }

    fun onEmailChanged(
        email: String,
    ) {
        val current = _viewState.value ?: return
        val error = verifier.verify(email, current.fieldState)
        _viewState.value = current.copy(
            isButtonEnabled = email.isValidEmailFormat(),
            fieldState = current.fieldState.copy(
                value = email,
                error = error,
                isValid = email.isValidEmailFormat(),
                filled = email.isNotEmpty(),
            ),
        )
    }

    fun onFocusChanged(
        isFocused: Boolean,
    ) {
        val current = _viewState.value ?: return
        _viewState.value = current.copy(
            fieldState = current.fieldState.copy(isFocused = isFocused),
        )
    }
}
