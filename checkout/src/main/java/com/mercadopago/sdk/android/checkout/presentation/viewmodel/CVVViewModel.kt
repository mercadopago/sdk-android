package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.mercadopago.sdk.android.checkout.domain.mapper.toCVVScreenData
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeScreenOutput
import com.mercadopago.sdk.android.checkout.domain.usecase.CVVValidationResult
import com.mercadopago.sdk.android.checkout.domain.usecase.ValidateCVUseCase
import com.mercadopago.sdk.android.checkout.presentation.state.CVVScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel for the CVV entry screen.
 *
 * Validation operates on the **length** of the CVV field — never on the raw value.
 * The actual CVV stays inside `PCIFieldState` in `:core-methods` (PCI rule).
 *
 * @param securityCodeScreen BFF-supplied CVV screen config from `/initialization`.
 * @param cvvExpectedLength Expected CVV digit count from `SecurityCodeOutput.length`.
 * @param validateCVUseCase CVV validation logic (injectable for testing).
 */
internal class CVVViewModel(
    securityCodeScreen: SecurityCodeScreenOutput,
    private val cvvExpectedLength: Int,
    private val validateCVUseCase: ValidateCVUseCase = ValidateCVUseCase(),
) : ViewModel() {
    private val _viewState = MutableStateFlow(
        CVVScreenState(screenData = securityCodeScreen.toCVVScreenData(cvvExpectedLength)),
    )
    val viewState: StateFlow<CVVScreenState> = _viewState.asStateFlow()

    fun onCVVLengthChanged(
        cvvLength: Int,
    ) {
        val isValid = validateCVUseCase(cvvLength, cvvExpectedLength) is CVVValidationResult.Valid
        _viewState.value = _viewState.value.copy(
            cvvLength = cvvLength,
            isContinueEnabled = isValid,
            errorMessage = null,
        )
    }

    fun onContinue() {
        val currentLength = _viewState.value.cvvLength
        if (validateCVUseCase(currentLength, cvvExpectedLength) !is CVVValidationResult.Valid) {
            _viewState.value = _viewState.value.copy(isContinueEnabled = false)
        }
    }
}
