package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.domain.extensions.fold
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickCardParams
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchPaymentBrickCardUseCase
import com.mercadopago.sdk.android.checkout.presentation.state.NewCardScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the new card flow within PaymentBrick.
 *
 * Fetches card form configuration via [FetchPaymentBrickCardUseCase] using the Order ID and BIN.
 * Full wiring (navigation, card form) is covered by A20.
 */
internal class NewCardViewModel(
    private val fetchCardUseCase: FetchPaymentBrickCardUseCase,
) : ViewModel() {
    private val _viewState = MutableStateFlow(NewCardScreenState(isLoading = true))
    val viewState: StateFlow<NewCardScreenState> = _viewState.asStateFlow()

    fun loadCardData(
        orderId: String,
        bin: String,
    ) {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true, isError = false)
            fetchCardUseCase(FetchPaymentBrickCardParams(orderId = orderId, bin = bin)).fold(
                onSuccess = { output ->
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        cardFormTitle = output.translations.cardFormTitle,
                        continueButtonLabel = output.translations.cardFormFooterButtonLabel,
                    )
                },
                onError = {
                    _viewState.value = _viewState.value.copy(isLoading = false, isError = true)
                },
            )
        }
    }
}
