package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class PaymentBrickViewModel : ViewModel() {
    private val _viewState = MutableStateFlow(PaymentBrickScreenState())
    val viewState: StateFlow<PaymentBrickScreenState> = _viewState.asStateFlow()

    // TECH_DEBT: definir UserCancelledContext específico para PaymentBrick e notificar CheckoutCallbackHolder
    fun onBackPressed() = Unit
}
