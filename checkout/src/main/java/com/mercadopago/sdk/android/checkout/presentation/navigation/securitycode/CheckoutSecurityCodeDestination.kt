package com.mercadopago.sdk.android.checkout.presentation.navigation.securitycode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mercadopago.sdk.android.checkout.presentation.cvv.SecurityCodeScreen
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeViewEvent
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.SecurityCodeViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun CheckoutSecurityCodeDestination(
    param: CheckoutSecurityCodeParam,
    actions: CheckoutSecurityCodeActions,
) {
    val viewModel: SecurityCodeViewModel = koinViewModel { parametersOf(param.config) }
    val viewEvent by viewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is SecurityCodeViewEvent.OnTokenSuccess -> {
                viewModel.onViewEventConsumed()
                actions.onTokenSuccess(event.cardId, event.token)
            }

            is SecurityCodeViewEvent.OnTokenError -> {
                viewModel.onViewEventConsumed()
                actions.onTokenError()
            }

            is SecurityCodeViewEvent.OnUserCancelled -> {
                viewModel.onViewEventConsumed()
                actions.onUserCancelled()
            }

            null -> Unit
        }
    }

    SecurityCodeScreen(viewModel = viewModel)
}
