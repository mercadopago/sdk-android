package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class PaymentBrickViewModel : ViewModel() {
    private val _viewState = MutableStateFlow(PaymentBrickScreenState())
    val viewState: StateFlow<PaymentBrickScreenState> = _viewState.asStateFlow()

    private val _viewEvent = MutableStateFlow<PaymentBrickViewEvent?>(null)
    val viewEvent: StateFlow<PaymentBrickViewEvent?> = _viewEvent.asStateFlow()

    fun onOptionSelected(
        optionId: String,
    ) {
        _viewEvent.value = PaymentBrickViewEvent.OnOptionSelected(optionId)
    }

    fun onViewEventConsumed() {
        _viewEvent.value = null
    }

    // TECH_DEBT: definir UserCancelledContext específico para PaymentBrick e emitir um
    // viewEvent de cancelamento para o CheckoutController notificar o CheckoutCallbackHolder.
    fun onBackPressed() = Unit
}
