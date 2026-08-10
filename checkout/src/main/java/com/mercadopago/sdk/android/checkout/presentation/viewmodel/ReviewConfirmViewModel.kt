package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmRequest
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmScreenState
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchReviewConfirmUseCase
import com.mercadopago.sdk.android.checkout.presentation.mapper.toScreenState
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class ReviewConfirmViewModel(
    private val request: ReviewConfirmRequest,
    private val fetchReviewConfirmUseCase: FetchReviewConfirmUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<ReviewConfirmScreenState>(ReviewConfirmScreenState.Loading)
    val state: StateFlow<ReviewConfirmScreenState> = _state.asStateFlow()

    init {
        loadReviewConfirm()
    }

    private fun loadReviewConfirm() {
        viewModelScope.launch {
            _state.value = ReviewConfirmScreenState.Loading
            when (val result = fetchReviewConfirmUseCase(request)) {
                is Result.Success -> _state.value = result.data.toScreenState()
                is Result.Error -> _state.value = ReviewConfirmScreenState.Error(result.error)
            }
        }
    }
}
