package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.presentation.brick.InstallmentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.mapper.toInstallmentsScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class InstallmentsViewModel(
    private val installmentData: MPInstallmentData,
    private val paymentData: MPPaymentData,
) : ViewModel() {
    private val selectedNumber = MutableStateFlow(installmentData.selectedInstallment)

    val viewState: StateFlow<InstallmentsScreenState> = selectedNumber
        .map { installmentData.copy(selectedInstallment = it).toInstallmentsScreenState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = installmentData.toInstallmentsScreenState(),
        )

    private val _viewEvent = MutableStateFlow<InstallmentViewEvent?>(null)
    val viewEvent: StateFlow<InstallmentViewEvent?> = _viewEvent.asStateFlow()

    fun onViewEventConsumed() {
        _viewEvent.value = null
    }

    fun onInstallmentSelected(
        installment: Int,
    ) {
        if (installmentData.quotas.none { it.installments == installment }) return
        when (installmentData.display.displayType) {
            InstallmentsDisplayType.RadioButton -> {
                selectedNumber.value = installment
            }
            InstallmentsDisplayType.Chevron -> {
                emitSuccess(installment)
            }
        }
    }

    fun onPayClicked() {
        if (installmentData.display.displayType != InstallmentsDisplayType.RadioButton) return
        val number = selectedNumber.value
            ?: viewState.value.installmentsState.firstOrNull { it.isSelected }?.number
            ?: return
        emitSuccess(number)
    }

    private fun emitSuccess(
        installment: Int,
    ) {
        _viewEvent.value = InstallmentViewEvent.OnSuccess(
            paymentData.copy(installment = installment),
        )
    }
}
