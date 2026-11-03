package com.mercadopago.sdk.android.checkout.presentation.navigation

import androidx.lifecycle.ViewModel
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Suppress("TooManyFunctions")
internal class CheckoutFlowViewModel : ViewModel() {
    private val _state = MutableStateFlow(CheckoutFlowState())
    val state: StateFlow<CheckoutFlowState> = _state.asStateFlow()
    val paymentFeedback = PaymentFeedbackStore()

    fun setInstallmentContext(
        installmentData: MPInstallmentData,
        paymentData: MPPaymentData,
        origin: ReviewOrigin,
    ) {
        _state.update { currentState ->
            currentState.copy(
                installmentContext = InstallmentContext(
                    installmentData = installmentData,
                    paymentData = paymentData,
                    origin = origin,
                    securityCodeConfig = currentState.securityCodeContext,
                ),
                securityCodeContext = null,
            )
        }
    }

    fun clearInstallmentContext() {
        _state.update { currentState ->
            currentState.copy(installmentContext = null)
        }
    }

    fun setSecurityCodeContext(
        config: SecurityCodeScreenConfig,
    ) {
        _state.update { currentState ->
            currentState.copy(
                securityCodeContext = config,
            )
        }
    }

    fun clearSecurityCodeContext() {
        _state.update { currentState -> currentState.copy(securityCodeContext = null) }
    }

    fun setReviewContext(
        params: ProcessOrderParams,
        origin: ReviewOrigin,
    ) {
        _state.update { currentState ->
            currentState.copy(reviewContext = ReviewContext(params = params, origin = origin))
        }
    }

    fun clearReviewContext() {
        _state.update { currentState -> currentState.copy(reviewContext = null) }
    }

    fun setOfflineMethodSelectorContext(
        data: MethodSelectionScreenData,
    ) {
        _state.update { currentState ->
            currentState.copy(
                offlineMethodSelectorContext = data,
            )
        }
    }

    fun clearOfflineMethodSelectorContext() {
        _state.update { currentState -> currentState.copy(offlineMethodSelectorContext = null) }
    }

    fun setStatusScreenContext(
        context: StatusScreenContext,
    ) {
        _state.update { currentState -> currentState.copy(statusScreenContext = context) }
    }

    fun clearStatusScreenContext() {
        _state.update { currentState -> currentState.copy(statusScreenContext = null) }
    }

    fun clearAll() {
        _state.value = CheckoutFlowState()
        paymentFeedback.clear()
    }

    override fun onCleared() {
        clearAll()
        super.onCleared()
    }
}
