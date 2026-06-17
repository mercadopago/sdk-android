package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickInitializationParams
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchPaymentBrickInitializationUseCase
import com.mercadopago.sdk.android.checkout.presentation.mapper.toScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class PaymentBrickViewModel(
    private val checkoutConfiguration: CheckoutConfiguration?,
    private val fetchInitializationUseCase: FetchPaymentBrickInitializationUseCase,
) : ViewModel() {
    private val _viewState = MutableStateFlow(PaymentBrickScreenState(isLoading = true))
    val viewState: StateFlow<PaymentBrickScreenState> = _viewState.asStateFlow()

    private val _viewEvent = MutableStateFlow<PaymentBrickViewEvent?>(null)
    val viewEvent: StateFlow<PaymentBrickViewEvent?> = _viewEvent.asStateFlow()

    init {
        loadInitialization()
    }

    private fun loadInitialization() {
        val params = checkoutConfiguration?.buildInitializationParams() ?: run {
            _viewState.value = PaymentBrickScreenState(isError = true, isLoading = false)
            return
        }
        viewModelScope.launch {
            _viewState.value = PaymentBrickScreenState(isLoading = true)
            when (val result = fetchInitializationUseCase(params)) {
                is Result.Success -> _viewState.value = result.data.toScreenState()
                is Result.Error -> _viewState.value = PaymentBrickScreenState(isError = true)
            }
        }
    }

    fun onOptionSelected(
        optionId: String,
    ) {
        _viewEvent.value = PaymentBrickViewEvent.OnOptionSelected(optionId)
    }

    fun onViewEventConsumed() {
        _viewEvent.value = null
    }

    // TECH_DEBT: define specific UserCancelledContext for PaymentBrick and emit a cancellation
    // viewEvent so CheckoutController can notify CheckoutCallbackHolder.
    fun onBackPressed() = Unit

    private fun CheckoutConfiguration.buildInitializationParams(): FetchPaymentBrickInitializationParams? {
        val paymentType = checkoutType as? MPCheckoutType.Payment ?: return null
        return FetchPaymentBrickInitializationParams(
            orderId = paymentType.order.orderId,
            totalAmount = paymentType.order.amount.toPlainString(),
            cardIds = paymentType.cardIds?.joinToString(","),
        )
    }
}
