package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class InstallmentsViewModel(
    private val paymentData: MPPaymentData,
) : ViewModel() {
    private val _viewState = MutableStateFlow(InstallmentsScreenState())
    val viewState: StateFlow<InstallmentsScreenState> = _viewState.asStateFlow()

    private val _viewEvent = MutableStateFlow<InstallmentViewEvent?>(null)
    val viewEvent: StateFlow<InstallmentViewEvent?> = _viewEvent.asStateFlow()

    fun clearViewEvent() {
        _viewEvent.value = null
    }

    fun onInstallmentSelected(
        installment: Int,
    ) {
        _viewEvent.value = InstallmentViewEvent.OnSuccess(paymentData.copy(installment = installment))
    }

    fun onPayClicked() = Unit
}
