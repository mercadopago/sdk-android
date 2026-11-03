package com.mercadopago.sdk.android.checkout.presentation.navigation.form

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
import com.mercadopago.sdk.android.checkout.core.model.MPCardType
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.presentation.cardform.CardFormScreen
import com.mercadopago.sdk.android.checkout.presentation.state.CardFormViewEvent

@Composable
internal fun CheckoutFormDestination(
    param: CheckoutFormParam,
    actions: CheckoutFormActions,
) {
    val viewEvent by param.viewModel.viewEvent.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val genericErrorMessage = stringResource(R.string.card_form_generic_error)

    LaunchedEffect(viewEvent, genericErrorMessage) {
        when (val event = viewEvent) {
            is CardFormViewEvent.OnSuccess -> {
                param.viewModel.onViewEventConsumed()
                handleCardFormSuccess(event, actions)
            }

            is CardFormViewEvent.OnPaymentConfirmed -> {
                param.viewModel.onViewEventConsumed()
                actions.onMarkScreenPresented(Screen.CARD_FORM)
                actions.onOpenReview(event.params)
            }

            is CardFormViewEvent.OnOrderProcessed ->
                actions.orderProcessed.handle(event, param.viewModel::onViewEventConsumed)

            is CardFormViewEvent.OnOrderProcessFailed ->
                actions.orderProcessed.handle(event, param.viewModel::onViewEventConsumed)

            CardFormViewEvent.OnReviewConfirmError -> {
                try {
                    snackbarHostState.showSnackbar(genericErrorMessage)
                } finally {
                    param.viewModel.onViewEventConsumed()
                }
            }

            is CardFormViewEvent.OnFailure -> {
                param.viewModel.onViewEventConsumed()
                actions.onFinishCheckout(MercadoPagoCheckoutResult.Error(event.error))
            }

            CardFormViewEvent.OnTokenizationError -> {
                param.viewModel.onViewEventConsumed()
                actions.onReturnToPaymentSelectorWithGenericError()
            }

            CardFormViewEvent.OnReturnToPaymentSelector -> {
                param.viewModel.onViewEventConsumed()
                actions.onReturnToPaymentSelector()
            }

            is CardFormViewEvent.OnUserCancelled -> {
                param.viewModel.onViewEventConsumed()
                actions.onFinishCheckout(MercadoPagoCheckoutResult.UserCancelled(event.context))
            }

            is CardFormViewEvent.OnBackPressed -> {
                param.viewModel.onViewEventConsumed()
                actions.onFinishCheckout(MercadoPagoCheckoutResult.UserCancelled(event.context))
            }

            null -> Unit
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CardFormScreen(viewModel = param.viewModel)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
        )
    }
}

private fun handleCardFormSuccess(
    event: CardFormViewEvent.OnSuccess,
    actions: CheckoutFormActions,
) {
    when {
        event.payment is MPPaymentData.CardTransaction &&
            MPCardType.fromString(event.payment.paymentTypeId) == MPCardType.CREDIT &&
            event.installment.quotas.isNotEmpty() -> {
            actions.onMarkScreenPresented(Screen.INSTALLMENTS)
            actions.onOpenInstallments(event.installment, event.payment)
        }
        event.payment is MPPaymentData.CardTransaction && event.installment.quotas.size <= 1 ->
            actions.onInstallmentConfirmed(event.installment.quotas.firstOrNull()?.installments ?: 1)
        event.installment.quotas.isNotEmpty() -> {
            actions.onMarkScreenPresented(Screen.INSTALLMENTS)
            actions.onOpenInstallments(event.installment, event.payment)
        }
        event.payment is MPPaymentData.CardSave ->
            actions.onFinishCheckout(MercadoPagoCheckoutResult.Success(event.payment))
        event.payment is MPPaymentData.Payment ->
            actions.onInstallmentConfirmed(1)
        else ->
            actions.onInvalidInstallmentData(
                MercadoPagoCheckoutError.UnknownError(
                    code = ErrorCode.UNKNOWN,
                    messageError = "CardTransaction received with no installment quotas",
                    localized = "checkout",
                    throwable = null,
                ),
            )
    }
}
