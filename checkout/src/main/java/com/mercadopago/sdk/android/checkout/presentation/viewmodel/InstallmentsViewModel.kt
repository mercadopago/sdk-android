package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.analytics.InstallmentsCancelReason
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.QuotaState
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import com.mercadopago.sdk.android.checkout.presentation.mapper.toInstallmentsScreenState
import com.mercadopago.sdk.android.checkout.presentation.shared.withButtonLoading
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class InstallmentsViewModel(
    private val installmentData: MPInstallmentData,
    private val paymentData: MPPaymentData,
    checkoutType: String,
    orderId: String,
    private val analyticsTracker: InstallmentsAnalyticsTracker = InstallmentsAnalyticsTracker(
        checkoutType = checkoutType,
        paymentData = paymentData,
        installmentData = installmentData,
        orderId = orderId,
    ),
) : ViewModel() {
    private val initialSelection = installmentData.selectedInstallment
        ?: installmentData.quotas.firstOrNull { it.state == QuotaState.Success }?.installments
        ?: installmentData.quotas
            .firstOrNull()
            ?.installments
            ?.takeIf { installmentData.display.displayType == SelectionDisplayType.RadioButton }
    private val selectedNumber = MutableStateFlow(initialSelection)
    private val buttonLoadingFlow = MutableStateFlow(false)
    private var cancelTracked = false

    val viewState: StateFlow<InstallmentsScreenState> = combine(
        selectedNumber,
        buttonLoadingFlow,
    ) { selected, loading ->
        installmentData.copy(selectedInstallment = selected)
            .toInstallmentsScreenState()
            .run { copy(footerState = footerState.withButtonLoading(loading)) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = installmentData.copy(selectedInstallment = initialSelection)
            .toInstallmentsScreenState(),
    )

    private val _viewEvent = MutableStateFlow<InstallmentViewEvent?>(null)
    val viewEvent: StateFlow<InstallmentViewEvent?> = _viewEvent.asStateFlow()

    init {
        analyticsTracker.trackInitialize()
    }

    fun onViewEventConsumed() {
        _viewEvent.value = null
        buttonLoadingFlow.value = false
    }

    fun onInstallmentSelected(
        installment: Int,
    ) {
        val quota = installmentData.quotas.firstOrNull { it.installments == installment } ?: return
        when (installmentData.display.displayType) {
            SelectionDisplayType.RadioButton -> {
                analyticsTracker.trackSelected(installment)
                selectedNumber.value = installment
            }
            SelectionDisplayType.Chevron -> {
                analyticsTracker.trackSubmit(quota)
                _viewEvent.value = InstallmentViewEvent.OnSuccess(installment)
            }
        }
    }

    fun onPayClicked() {
        if (installmentData.display.displayType != SelectionDisplayType.RadioButton) return
        val number = selectedNumber.value ?: return
        installmentData.quotas
            .firstOrNull { it.installments == number }
            ?.let { quota ->
                analyticsTracker.trackSubmit(quota)
                viewModelScope.launch {
                    buttonLoadingFlow.value = true
                    delay(SUBMIT_LOADING_DELAY_MS)
                    _viewEvent.value = InstallmentViewEvent.OnSuccess(number)
                }
            }
    }

    fun onBackPressed() {
        if (!cancelTracked) {
            cancelTracked = true
            analyticsTracker.trackUserCanceled(InstallmentsCancelReason.BackPressed)
        }
    }

    override fun onCleared() {
        if (!cancelTracked) {
            analyticsTracker.trackUserCanceled(InstallmentsCancelReason.UserDismissed)
        }
        super.onCleared()
    }

    private companion object {
        const val SUBMIT_LOADING_DELAY_MS = 300L
    }
}
