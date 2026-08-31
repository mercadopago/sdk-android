package com.mercadopago.sdk.android.checkout.presentation.navigation.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mercadopago.sdk.android.checkout.core.model.MPCardType
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.presentation.cardpayment.CardPaymentScreen
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentViewEvent

@Composable
internal fun CheckoutFormDestination(
    param: CheckoutFormParam,
    actions: CheckoutFormActions,
) {
    val viewEvent by param.viewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is CardPaymentViewEvent.OnSuccess -> {
                param.viewModel.onViewEventConsumed()
                handleCardPaymentSuccess(event, actions)
            }

            is CardPaymentViewEvent.OnPaymentConfirmed -> {
                param.viewModel.onViewEventConsumed()
                actions.onMarkScreenPresented(Screen.CARD_FORM)
                actions.onOpenReview(event.params)
            }

            is CardPaymentViewEvent.OnFailure -> {
                param.viewModel.onViewEventConsumed()
                actions.onFinishCheckout(MercadoPagoCheckoutResult.Error(event.error))
            }

            is CardPaymentViewEvent.OnUserCancelled -> {
                param.viewModel.onViewEventConsumed()
                actions.onFinishCheckout(MercadoPagoCheckoutResult.UserCancelled(event.context))
            }

            is CardPaymentViewEvent.OnBackPressed -> {
                param.viewModel.onViewEventConsumed()
                actions.onFinishCheckout(MercadoPagoCheckoutResult.UserCancelled(event.context))
            }

            null -> Unit
        }
    }

    CardPaymentScreen(viewModel = param.viewModel)
}

private fun handleCardPaymentSuccess(
    event: CardPaymentViewEvent.OnSuccess,
    actions: CheckoutFormActions,
) {
    when {
        event.payment is MPPaymentData.CardTransaction &&
            MPCardType.fromString(event.payment.paymentTypeId) == MPCardType.CREDIT &&
            event.installment.quotas.isNotEmpty() -> {
            actions.onMarkScreenPresented(Screen.INSTALLMENTS)
            actions.onOpenInstallments(event.installment, event.payment)
        }
        event.payment is MPPaymentData.CardTransaction && event.installment.quotas.size == 1 ->
            actions.onInstallmentConfirmed(event.installment.quotas.first().installments ?: 1)
        event.installment.quotas.isNotEmpty() -> {
            actions.onMarkScreenPresented(Screen.INSTALLMENTS)
            actions.onOpenInstallments(event.installment, event.payment)
        }
        event.payment is MPPaymentData.CardSave ->
            actions.onFinishCheckout(MercadoPagoCheckoutResult.Success(event.payment))
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
