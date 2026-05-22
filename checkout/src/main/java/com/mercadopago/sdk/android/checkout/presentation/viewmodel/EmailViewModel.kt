package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.mercadopago.sdk.android.checkout.presentation.state.EmailScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class EmailViewModel : ViewModel() {
    private val _viewState = MutableStateFlow<EmailScreenState?>(null)
    val viewState: StateFlow<EmailScreenState?> = _viewState.asStateFlow()

    fun initialize(
        translate: EmailScreenState.Translate,
        baseEmail: String? = null,
    ) {
        _viewState.value = buildInitialState(translate, baseEmail)
    }

    fun onEmailChanged(
        newValue: String,
    ) {
        val current = _viewState.value ?: return
        _viewState.value = current.copy(
            value = newValue,
            isError = isInvalidFormat(newValue),
            isButtonEnabled = isValidEmail(newValue),
        )
    }

    fun resolveErrorMessage(
        state: EmailScreenState,
    ): String {
        if (!state.isError) return ""
        val translate = state.translate
        return when {
            state.value.isBlank() -> translate.errorFieldEmpty
            !isValidEmail(state.value) -> translate.errorEmailInvalid
            else -> translate.errorFieldRequired
        }
    }

    private fun buildInitialState(
        translate: EmailScreenState.Translate,
        baseEmail: String?,
    ): EmailScreenState {
        val value = baseEmail.orEmpty()
        return EmailScreenState(
            translate = translate,
            value = value,
            isError = false,
            isButtonEnabled = isValidEmail(value),
        )
    }

    private fun isValidEmail(
        value: String,
    ): Boolean {
        return value.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }

    private fun isInvalidFormat(
        value: String,
    ): Boolean {
        return value.isNotBlank() && !isValidEmail(value)
    }
}
