package com.mercadopago.sdk.android.checkout.presentation.brick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.getAmount
import com.mercadopago.sdk.android.checkout.core.model.internal.getAmountOrZero
import com.mercadopago.sdk.android.checkout.core.model.internal.toCheckoutType
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.usecase.InitializeCardFormUseCase
import com.mercadopago.sdk.android.checkout.presentation.extensions.fold
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardFormAnalyticsTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Mirrors `CardFormBrickViewModel.swift` from iOS — minimal responsibilities:
 *  - drives the `Loading` / `Ready(initData)` lifecycle for the brick
 *  - calls the init use case once when the brick is first composed
 *
 * Does not hold cross-screen state, navigation, or the public callback. Those live on the
 * `CheckoutBrick` composable.
 */
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
                amount = configuration?.getAmountOrZero().orEmpty(),
                checkoutType = configuration.toCheckoutType(),
            ).fold(
                onSuccess = { initData ->
                    _screenState.value = ScreenState.Ready(
                        initData.copy(
                            transactionAmount = configuration?.getAmount(),
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
