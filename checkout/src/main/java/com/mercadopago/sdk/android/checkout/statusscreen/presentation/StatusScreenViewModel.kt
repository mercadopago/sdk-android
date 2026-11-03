package com.mercadopago.sdk.android.checkout.statusscreen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.analytics.StatusScreenCloseSource
import com.mercadopago.sdk.android.checkout.analytics.StatusScreenReceiptOutcome
import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.isStatusScreenContractViolation
import com.mercadopago.sdk.android.checkout.statusscreen.domain.usecase.GetStatusScreenUseCase
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class StatusScreenViewModel(
    private val clientToken: String,
    private val orderId: String,
    private val lastFourDigits: String?,
    private val sellerInfo: MPSellerInfo?,
    private val checkoutType: String? = null,
    private val getStatusScreenUseCase: GetStatusScreenUseCase,
    val paymentProcessed: OrderProcessOutput.PaymentProcessed? = null,
    private val orderStatus: String? = null,
) : ViewModel() {
    private val _state = MutableStateFlow<StatusScreenState>(StatusScreenState.Idle)
    val state: StateFlow<StatusScreenState> = _state.asStateFlow()
    private val analyticsTracker = StatusScreenAnalyticsTracker()

    fun load() {
        if (_state.value != StatusScreenState.Idle) return
        _state.value = StatusScreenState.Loading
        analyticsTracker.trackLoadStarted()
        viewModelScope.launch {
            when (
                val result = getStatusScreenUseCase(
                    clientToken = clientToken,
                    orderId = orderId,
                    lastFourDigits = lastFourDigits,
                    sellerInfo = sellerInfo,
                    checkoutType = checkoutType,
                )
            ) {
                is Result.Success -> {
                    _state.value = StatusScreenState.Ready(result.data)
                    analyticsTracker.trackLoadSuccess(statusType = result.data.statusType)
                }
                is Result.Error -> {
                    _state.value = StatusScreenState.Unavailable(
                        paymentProcessed = paymentProcessed,
                        lastFourDigits = lastFourDigits,
                        orderStatus = orderStatus,
                    )
                    if (result.error.isStatusScreenContractViolation()) {
                        analyticsTracker.trackContractViolation()
                    } else {
                        analyticsTracker.trackLoadFailure()
                    }
                }
            }
        }
    }

    fun trackRender() {
        val ready = _state.value as? StatusScreenState.Ready ?: return
        analyticsTracker.trackRender(statusType = ready.data.statusType, canRetry = ready.data.canRetry)
    }

    fun trackRetry() {
        val statusType = (_state.value as? StatusScreenState.Ready)?.data?.statusType
        analyticsTracker.trackRetry(statusType = statusType)
    }

    fun trackReceipt(
        outcome: StatusScreenReceiptOutcome,
    ) {
        val statusType = (_state.value as? StatusScreenState.Ready)?.data?.statusType
        analyticsTracker.trackReceipt(outcome = outcome, statusType = statusType)
    }

    fun trackClose(
        source: StatusScreenCloseSource,
    ) {
        val statusType = (_state.value as? StatusScreenState.Ready)?.data?.statusType
        analyticsTracker.trackClose(source = source, statusType = statusType)
    }
}
