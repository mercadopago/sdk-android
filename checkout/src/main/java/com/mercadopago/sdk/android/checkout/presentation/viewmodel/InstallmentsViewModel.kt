package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.QuotaState
import com.mercadopago.sdk.android.checkout.presentation.mapper.toInstallmentsScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class InstallmentsViewModel(
    private val installmentData: MPInstallmentData = MPInstallmentData(),
    // tech-debt: parâmetro será removido na PR 4/4 quando DataModule migrar para installmentData
    @Suppress("UnusedPrivateMember") private val paymentData: MPPaymentData? = null,
) : ViewModel() {
    private val initialSelection = installmentData.selectedInstallment
        ?: installmentData.quotas.firstOrNull { it.state == QuotaState.Selected }?.installments
        ?: installmentData.quotas
            .firstOrNull { it.state != QuotaState.Disabled }
            ?.installments
            ?.takeIf { installmentData.display.displayType == InstallmentsDisplayType.RadioButton }
    private val selectedNumber = MutableStateFlow(initialSelection)

    val viewState: StateFlow<InstallmentsScreenState> = selectedNumber
        .map { installmentData.copy(selectedInstallment = it).toInstallmentsScreenState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = installmentData.copy(selectedInstallment = initialSelection).toInstallmentsScreenState(),
        )

    private val _viewEvent = MutableStateFlow<InstallmentViewEvent?>(null)
    val viewEvent: StateFlow<InstallmentViewEvent?> = _viewEvent.asStateFlow()

    fun onViewEventConsumed() {
        _viewEvent.value = null
    }

    // tech-debt: alias será removido na PR 4/4 quando CheckoutController migrar para onViewEventConsumed
    fun clearViewEvent() = onViewEventConsumed()

    fun onInstallmentSelected(
        installment: Int,
    ) {
        val quota = installmentData.quotas.firstOrNull { it.installments == installment } ?: return
        if (quota.state == QuotaState.Disabled) return
        when (installmentData.display.displayType) {
            InstallmentsDisplayType.RadioButton -> selectedNumber.value = installment
            InstallmentsDisplayType.Chevron -> _viewEvent.value = InstallmentViewEvent.OnSuccess(installment)
        }
    }

    fun onPayClicked() {
        if (installmentData.display.displayType != InstallmentsDisplayType.RadioButton) return
        val number = selectedNumber.value ?: return
        _viewEvent.value = InstallmentViewEvent.OnSuccess(number)
    }
}
