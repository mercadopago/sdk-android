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
        labels: EmailScreenState.Labels,
        baseEmail: String? = null,
    ) {
        _viewState.value = buildInitialState(labels, baseEmail)
    }

    fun onEmailChanged(
        newValue: String,
    ) {
        val current = _viewState.value ?: return
        _viewState.value = current.copy(
            email = newValue,
            isError = isInvalidFormat(newValue),
            isButtonEnabled = isValidEmail(newValue),
        )
    }

    fun resolveErrorMessage(
        state: EmailScreenState,
    ): String {
        if (!state.isError) return ""
        val translate = state.labels
        return when {
            state.email.isBlank() -> translate.errorFieldEmpty
            !isValidEmail(state.email) -> translate.errorEmailInvalid
            else -> translate.errorFieldRequired
        }
    }

    private fun buildInitialState(
        labels: EmailScreenState.Labels,
        baseEmail: String?,
    ): EmailScreenState {
        val value = baseEmail.orEmpty()
        return EmailScreenState(
            labels = labels,
            email = value,
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
