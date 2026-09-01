package com.mercadopago.sdk.android.checkout.presentation.navigation.payment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.presentation.navigation.PaymentFeedback
import com.mercadopago.sdk.android.checkout.presentation.paymentbrick.PaymentBrickScreen
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent

@Composable
internal fun CheckoutPaymentDestination(
    param: CheckoutPaymentParam,
    actions: CheckoutPaymentActions,
) {
    val viewEvent by param.viewModel.viewEvent.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val genericErrorMessage = stringResource(R.string.card_form_generic_error)

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is PaymentBrickViewEvent.OnOptionSelected -> {
                param.viewModel.onViewEventConsumed()
                actions.onOpenForm()
            }

            is PaymentBrickViewEvent.OnSecurityCodeRequired -> {
                param.viewModel.onViewEventConsumed()
                actions.onOpenSecurityCode(event.config)
            }

            is PaymentBrickViewEvent.OnPaymentReadyForReview -> {
                param.viewModel.onViewEventConsumed()
                actions.onOpenReview(event.params)
            }

            is PaymentBrickViewEvent.OnInstallmentsRequired -> handleInstallmentsRequired(event, param, actions)

            is PaymentBrickViewEvent.OnFailure -> {
                param.viewModel.onViewEventConsumed()
                actions.onFinishCheckout(MercadoPagoCheckoutResult.Error(event.error))
            }

            PaymentBrickViewEvent.OnTokenizationError -> {
                param.viewModel.onViewEventConsumed()
                actions.onShowFeedback(PaymentFeedback.GenericError)
            }

            is PaymentBrickViewEvent.OnUserCancelled -> {
                param.viewModel.onViewEventConsumed()
                actions.onFinishCheckout(MercadoPagoCheckoutResult.UserCancelled(event.context))
            }

            is PaymentBrickViewEvent.OnOfflineMethodSelected -> {
                param.viewModel.onViewEventConsumed()
                param.viewModel.markScreenPresented(Screen.OFFLINE_METHOD_SELECTOR)
                actions.onOpenOfflineMethodSelector(event.screenData)
            }

            null -> Unit
        }
    }

    LaunchedEffect(param.feedback, genericErrorMessage) {
        val event = param.feedback ?: return@LaunchedEffect
        try {
            when (event.feedback) {
                PaymentFeedback.GenericError -> snackbarHostState.showSnackbar(genericErrorMessage)
            }
        } finally {
            actions.onFeedbackConsumed(event)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PaymentBrickScreen(viewModel = param.viewModel)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
        )
    }
}

private fun handleInstallmentsRequired(
    event: PaymentBrickViewEvent.OnInstallmentsRequired,
    param: CheckoutPaymentParam,
    actions: CheckoutPaymentActions,
) {
    param.viewModel.onViewEventConsumed()
    param.viewModel.markScreenPresented(Screen.INSTALLMENTS)
    actions.onOpenInstallments(event.installmentData, event.paymentData)
}
