package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.mercadopago.sdk.android.checkout.domain.extensions.isInvalidEmailFormat
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
        val isError = newValue.isInvalidEmailFormat()
        val isValid = isValidEmail(newValue)
        _viewState.value = current.copy(
            email = newValue,
            isError = isError,
            isButtonEnabled = isValid,
            errorMessage = resolveErrorMessage(current.labels, isError),
        )
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
            errorMessage = "",
        )
    }

    private fun resolveErrorMessage(
        labels: EmailScreenState.Labels,
        isError: Boolean,
    ): String = if (isError) labels.errorEmailInvalid else ""

    private fun isValidEmail(
        value: String,
    ): Boolean = value.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(value).matches()
}
