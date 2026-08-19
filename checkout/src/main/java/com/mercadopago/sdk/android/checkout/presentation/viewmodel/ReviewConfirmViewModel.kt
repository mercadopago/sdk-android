package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchReviewConfirmUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.ProcessOrderUseCase
import com.mercadopago.sdk.android.checkout.presentation.mapper.toScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.ReviewConfirmScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.ReviewConfirmViewEvent
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledPaymentContextUseCase
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class ReviewConfirmViewModel(
    private val processOrderParams: ProcessOrderParams,
    private val sellerInfo: MPSellerInfo?,
    private val fetchReviewConfirmUseCase: FetchReviewConfirmUseCase,
    private val processOrderUseCase: ProcessOrderUseCase,
    private val cancelledPaymentContextUseCase: CancelledPaymentContextUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<ReviewConfirmScreenState>(ReviewConfirmScreenState.Loading)
    val state: StateFlow<ReviewConfirmScreenState> = _state.asStateFlow()

    private val _viewEvent = MutableStateFlow<ReviewConfirmViewEvent?>(null)
    val viewEvent: StateFlow<ReviewConfirmViewEvent?> = _viewEvent.asStateFlow()

    init {
        markScreenPresented()
        loadReviewConfirm()
    }

    fun onConfirmClicked() {
        val currentState = _state.value as? ReviewConfirmScreenState.Success ?: return
        if (currentState.isLoading) return
        viewModelScope.launch {
            _state.value = currentState.copy(isLoading = true)
            when (val result = processOrderUseCase(processOrderParams)) {
                is Result.Success -> {
                    _state.value = currentState.copy(isLoading = false)
                    _viewEvent.value = ReviewConfirmViewEvent.OnPaymentSuccess(
                        MPPaymentData.Payment(
                            orderId = result.data.id,
                            orderStatus = result.data.status,
                            paymentMethodId = processOrderParams.paymentMethodId,
                            paymentTypeId = processOrderParams.paymentMethodType,
                        ),
                    )
                }
                is Result.Error -> {
                    _state.value = currentState.copy(isLoading = false)
                    _viewEvent.value = ReviewConfirmViewEvent.OnPaymentError(result.error)
                }
            }
        }
    }

    fun onModifyPaymentMethodClicked(
        itemType: String,
    ) {
        _viewEvent.value = ReviewConfirmViewEvent.OnModifyPaymentMethod(itemType)
    }

    fun onModifyEmailClicked() {
        _viewEvent.value = ReviewConfirmViewEvent.OnModifyEmail
    }

    fun onBackPressed() {
        _viewEvent.value = ReviewConfirmViewEvent.OnUserCancelled(
            context = cancelledPaymentContextUseCase(),
        )
    }

    fun onViewEventConsumed() {
        _viewEvent.value = null
    }

    private fun markScreenPresented() {
        cancelledPaymentContextUseCase.markScreenPresented(Screen.REVIEW_AND_CONFIRM)
    }

    private fun loadReviewConfirm() {
        viewModelScope.launch {
            _state.value = ReviewConfirmScreenState.Loading
            when (
                val result = fetchReviewConfirmUseCase(
                    processOrderParams = processOrderParams,
                    sellerInfo = sellerInfo,
                )
            ) {
                is Result.Success -> _state.value = result.data.toScreenState()
                is Result.Error -> _state.value = ReviewConfirmScreenState.Error(result.error)
            }
        }
    }
}
