package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.getCardFormAmount
import com.mercadopago.sdk.android.checkout.core.model.internal.getCardFormAmountOrZero
import com.mercadopago.sdk.android.checkout.core.model.internal.toCheckoutType
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.usecase.InitializeCardFormUseCase
import com.mercadopago.sdk.android.checkout.presentation.extensions.fold
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class CheckoutControllerViewModel(
    private val configuration: CheckoutConfiguration?,
    private val initializeCardFormUseCase: InitializeCardFormUseCase,
) : ViewModel() {
    sealed interface ScreenState {
        data object Loading : ScreenState

        data class Ready(val initData: CardFormInitializationOutput) : ScreenState
    }

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    private val analyticsTracker = CardFormAnalyticsTracker(
        isCancelling = { false },
        isLoading = { _screenState.value is ScreenState.Loading },
    )

    fun load() {
        if (_screenState.value !is ScreenState.Loading) return
        viewModelScope.launch {
            initializeCardFormUseCase(
                amount = configuration?.getCardFormAmountOrZero().orEmpty(),
                checkoutType = configuration.toCheckoutType(),
            ).fold(
                onSuccess = { data ->
                    _screenState.value = ScreenState.Ready(
                        data.copy(
                            transactionAmount = configuration?.getCardFormAmount(),
                            paymentMethods = configuration?.paymentMethods.orEmpty(),
                            checkoutType = configuration.toCheckoutType(),
                        ),
                    )
                },
                onError = { error ->
                    analyticsTracker.trackInitializeError(error)
                    CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(error))
                },
            )
        }
    }
}
