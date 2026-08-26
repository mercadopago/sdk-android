package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class CheckoutCoordinatorViewModel : ViewModel() {
    private val _state = MutableStateFlow(CheckoutCoordinatorState())
    val state: StateFlow<CheckoutCoordinatorState> = _state.asStateFlow()

    fun setInstallmentData(
        installmentData: MPInstallmentData,
        paymentData: MPPaymentData,
    ) {
        _state.value = _state.value.copy(
            installmentData = installmentData,
            paymentData = paymentData,
        )
    }

    fun clearInstallmentData() {
        _state.value = _state.value.copy(
            installmentData = null,
            paymentData = null,
        )
    }

    fun setSecurityCodeConfig(
        config: SecurityCodeScreenConfig,
    ) {
        _state.value = _state.value.copy(securityCodeConfig = config)
    }

    fun clearSecurityCodeConfig() {
        _state.value = _state.value.copy(securityCodeConfig = null)
    }

    fun setReviewConfirmParams(
        params: ProcessOrderParams,
    ) {
        _state.value = _state.value.copy(reviewConfirmParams = params)
    }

    fun clearReviewConfirmParams() {
        _state.value = _state.value.copy(reviewConfirmParams = null)
    }

    fun setMethodSelectionData(
        data: MethodSelectionScreenData,
    ) {
        _state.value = _state.value.copy(methodSelectionData = data)
    }

    fun clearMethodSelectionData() {
        _state.value = _state.value.copy(methodSelectionData = null)
    }

    fun notifyReviewConfirmLoadFailed() {
        _state.value = _state.value.copy(reviewConfirmLoadFailed = true)
    }

    fun clearReviewConfirmLoadFailed() {
        _state.value = _state.value.copy(reviewConfirmLoadFailed = false)
    }
}

internal data class CheckoutCoordinatorState(
    val installmentData: MPInstallmentData? = null,
    val paymentData: MPPaymentData? = null,
    val securityCodeConfig: SecurityCodeScreenConfig? = null,
    val reviewConfirmParams: ProcessOrderParams? = null,
    val methodSelectionData: MethodSelectionScreenData? = null,
    val reviewConfirmLoadFailed: Boolean = false,
)
