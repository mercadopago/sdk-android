package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
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
) : ViewModel() {
    private val _selectedNumber = MutableStateFlow(installmentData.selectedInstallment)

    val viewState: StateFlow<InstallmentsScreenState> = _selectedNumber
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
        if (installmentData.display.displayType != InstallmentsDisplayType.RadioButton) return
        if (installmentData.quotas.none { it.installments == installment }) return
        _selectedNumber.value = installment
    }

    fun onPayClicked() {
        val number = _selectedNumber.value
            ?: viewState.value.installmentsState.firstOrNull { it.isSelected }?.number
            ?: return
        _viewEvent.value = InstallmentViewEvent.OnSuccess(
            installmentData.copy(selectedInstallment = number),
        )
    }
}
