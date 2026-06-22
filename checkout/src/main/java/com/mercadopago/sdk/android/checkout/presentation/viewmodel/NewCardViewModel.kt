package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.domain.extensions.fold
import com.mercadopago.sdk.android.checkout.domain.mapper.toInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickCardParams
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchPaymentBrickCardUseCase
import com.mercadopago.sdk.android.checkout.presentation.state.NewCardScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.NewCardViewEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the new card flow within PaymentBrick.
 *
 * Fetches card form configuration via [FetchPaymentBrickCardUseCase] using the Order ID and BIN.
 * When the response contains installment data, emits [NewCardViewEvent.NavigateToInstallments]
 * so the controller can route to the installment selector (A24).
 * Full navigation wiring (A20) depends on a pending architectural analysis.
 */
internal class NewCardViewModel(
    private val fetchCardUseCase: FetchPaymentBrickCardUseCase,
) : ViewModel() {
    private val _viewState = MutableStateFlow(NewCardScreenState(isLoading = true))
    val viewState: StateFlow<NewCardScreenState> = _viewState.asStateFlow()

    private val _viewEvent = MutableStateFlow<NewCardViewEvent?>(null)
    val viewEvent: StateFlow<NewCardViewEvent?> = _viewEvent.asStateFlow()

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
                    output.installment?.let { installmentConfig ->
                        _viewEvent.value = NewCardViewEvent.NavigateToInstallments(
                            installmentData = installmentConfig.toInstallmentData(output.translations),
                        )
                    }
                },
                onError = {
                    _viewState.value = _viewState.value.copy(isLoading = false, isError = true)
                },
            )
        }
    }

    fun onViewEventConsumed() {
        _viewEvent.value = null
    }
}
