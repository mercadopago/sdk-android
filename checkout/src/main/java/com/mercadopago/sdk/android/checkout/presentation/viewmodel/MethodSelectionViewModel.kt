package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import com.mercadopago.sdk.android.checkout.presentation.shared.ButtonState
import com.mercadopago.sdk.android.checkout.presentation.shared.FooterState
import com.mercadopago.sdk.android.checkout.presentation.shared.withButtonEnabled
import com.mercadopago.sdk.android.checkout.presentation.state.MethodSelectionScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.MethodSelectionViewEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class MethodSelectionViewModel(
    private val screenData: MethodSelectionScreenData,
) : ViewModel() {
    private val _screenState = MutableStateFlow(buildInitialState())
    val screenState: StateFlow<MethodSelectionScreenState> = _screenState.asStateFlow()

    private val _viewEvent = MutableStateFlow<MethodSelectionViewEvent?>(null)
    val viewEvent: StateFlow<MethodSelectionViewEvent?> = _viewEvent.asStateFlow()

    fun selectOption(
        optionId: String,
    ) {
        if (_screenState.value.isArrowLayout) {
            val option = screenData.options.find { it.id == optionId } ?: return
            _viewEvent.value = MethodSelectionViewEvent.OnOptionSelected(option)
        } else {
            _screenState.update { currentState ->
                currentState.copy(
                    selectedOptionId = optionId,
                    footerState = currentState.footerState.withButtonEnabled(true),
                )
            }
        }
    }

    fun confirmSelection() {
        val state = _screenState.value
        val selectedId = state.selectedOptionId ?: return
        val option = state.options.find { it.id == selectedId } ?: return
        _viewEvent.value = MethodSelectionViewEvent.OnOptionSelected(option)
    }

    fun goBack() {
        // navigation delegated to caller; tracking placeholder
    }

    fun onViewEventConsumed() {
        _viewEvent.value = null
    }

    private fun buildInitialState(): MethodSelectionScreenState {
        val isArrowLayout = screenData.selectionType == SelectionDisplayType.Chevron
        val ctaButtonState = if (isArrowLayout) {
            null
        } else {
            screenData.footer?.button?.let { ButtonState(enabled = false, isLoading = false) }
        }
        return MethodSelectionScreenState(
            headerTitle = screenData.headerTitle,
            options = screenData.options,
            selectedOptionId = null,
            footerState = FooterState(
                title = screenData.footer?.totalLabel.orEmpty(),
                subtitle = screenData.footer?.totalAmount.orEmpty(),
                buttonLabel = if (isArrowLayout) null else screenData.footer?.button?.label,
                isVisible = true,
                buttonState = ctaButtonState,
            ),
            isArrowLayout = isArrowLayout,
        )
    }
}
