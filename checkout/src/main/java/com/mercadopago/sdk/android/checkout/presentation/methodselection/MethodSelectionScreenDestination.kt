package com.mercadopago.sdk.android.checkout.presentation.methodselection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionOption
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.presentation.state.MethodSelectionViewEvent
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.MethodSelectionViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun MethodSelectionScreenDestination(
    screenData: MethodSelectionScreenData,
    onOptionSelected: (MethodSelectionOption) -> Unit,
    onBackClick: () -> Unit,
) {
    val viewModel: MethodSelectionViewModel = koinViewModel { parametersOf(screenData) }
    val screenState by viewModel.screenState.collectAsState()
    val viewEvent by viewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        viewEvent?.let { event ->
            when (event) {
                is MethodSelectionViewEvent.OnOptionSelected -> {
                    onOptionSelected(event.option)
                    viewModel.onViewEventConsumed()
                }
            }
        }
    }

    MethodSelectionScreen(
        screenState = screenState,
        onOptionTap = { option -> viewModel.selectOption(option.id) },
        onCtaTap = { viewModel.confirmSelection() },
        onBack = {
            viewModel.goBack()
            onBackClick()
        },
    )
}
