package com.mercadopago.sdk.android.checkout.presentation.navigation.payment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.presentation.navigation.PaymentFeedback
import com.mercadopago.sdk.android.checkout.presentation.paymentbrick.PaymentBrickScreen
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent
import com.mercadopago.sdk.android.components.MPMessage
import com.mercadopago.sdk.android.components.MPMessageType
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

@Composable
internal fun CheckoutPaymentDestination(
    param: CheckoutPaymentParam,
    actions: CheckoutPaymentActions,
) {
    val viewEvent by param.viewModel.viewEvent.collectAsState()
    var isErrorMessageVisible by remember { mutableStateOf(false) }
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

            is PaymentBrickViewEvent.OnOrderProcessed, is PaymentBrickViewEvent.OnOrderProcessFailed ->
                handleOrderProcessed(event, param, actions)

            is PaymentBrickViewEvent.OnInstallmentsRequired -> handleInstallmentsRequired(event, param, actions)

            is PaymentBrickViewEvent.OnFailure -> {
                param.viewModel.onViewEventConsumed()
                actions.onFinishCheckout(MercadoPagoCheckoutResult.Error(event.error))
            }

            PaymentBrickViewEvent.OnInstallmentsUnavailable,
            PaymentBrickViewEvent.OnTokenizationError,
            -> handleGenericError(param, actions)

            is PaymentBrickViewEvent.OnUserCancelled -> {
                param.viewModel.onViewEventConsumed()
                actions.onFinishCheckout(MercadoPagoCheckoutResult.UserCancelled(event.context))
            }

            is PaymentBrickViewEvent.OnOfflineMethodSelected -> handleOfflineMethodSelected(event, param, actions)

            null -> Unit
        }
    }

    LaunchedEffect(param.feedback) {
        val event = param.feedback ?: return@LaunchedEffect
        try {
            when (event.feedback) {
                PaymentFeedback.GenericError -> isErrorMessageVisible = true
            }
        } finally {
            actions.onFeedbackConsumed(event)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PaymentBrickScreen(viewModel = param.viewModel)
        if (isErrorMessageVisible) {
            PaymentBrickErrorMessage(
                text = genericErrorMessage,
                onDismiss = { isErrorMessageVisible = false },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun PaymentBrickErrorMessage(
    text: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MPMessage(
        text = text,
        type = MPMessageType.Negative,
        modifier = modifier
            .navigationBarsPadding()
            .padding(
                horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                vertical = MercadoPagoTheme.spacing.paddings.xtiny,
            ),
        onDismiss = onDismiss,
    )
}

private fun handleGenericError(
    param: CheckoutPaymentParam,
    actions: CheckoutPaymentActions,
) {
    param.viewModel.onViewEventConsumed()
    actions.onShowFeedback(PaymentFeedback.GenericError)
}

private fun handleOfflineMethodSelected(
    event: PaymentBrickViewEvent.OnOfflineMethodSelected,
    param: CheckoutPaymentParam,
    actions: CheckoutPaymentActions,
) {
    param.viewModel.onViewEventConsumed()
    param.viewModel.markScreenPresented(Screen.OFFLINE_METHOD_SELECTOR)
    actions.onOpenOfflineMethodSelector(event.screenData)
}

private fun handleOrderProcessed(
    event: PaymentBrickViewEvent,
    param: CheckoutPaymentParam,
    actions: CheckoutPaymentActions,
) {
    val onConsumed = param.viewModel::onViewEventConsumed
    when (event) {
        is PaymentBrickViewEvent.OnOrderProcessed -> actions.orderProcessed.handle(event, onConsumed)
        is PaymentBrickViewEvent.OnOrderProcessFailed -> actions.orderProcessed.handle(event, onConsumed)
        else -> Unit
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
