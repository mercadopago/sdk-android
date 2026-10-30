package com.mercadopago.sdk.android.checkout.presentation.navigation.installment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mercadopago.sdk.android.checkout.core.model.internal.getOrderId
import com.mercadopago.sdk.android.checkout.core.model.internal.toCheckoutType
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.presentation.installments.InstallmentsScreen
import com.mercadopago.sdk.android.checkout.presentation.navigation.ReviewOrigin
import com.mercadopago.sdk.android.checkout.presentation.state.CardFormViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun CheckoutInstallmentDestination(
    param: CheckoutInstallmentParam,
    actions: CheckoutInstallmentActions,
) {
    val cardPaymentViewState by param.cardPaymentViewModel.viewState.collectAsState()
    val cardPaymentViewEvent by param.cardPaymentViewModel.viewEvent.collectAsState()
    val paymentBrickViewState by param.paymentBrickViewModel.viewState.collectAsState()
    val paymentBrickViewEvent by param.paymentBrickViewModel.viewEvent.collectAsState()

    LaunchedEffect(cardPaymentViewEvent, param.origin) {
        if (param.origin != ReviewOrigin.Form) return@LaunchedEffect

        when (val event = cardPaymentViewEvent) {
            is CardFormViewEvent.OnPaymentConfirmed -> {
                param.cardPaymentViewModel.onViewEventConsumed()
                actions.onMarkScreenPresented(Screen.CARD_FORM)
                actions.onOpenReview(event.params)
            }
            is CardFormViewEvent.OnOrderProcessed ->
                actions.orderProcessed.handle(event, param.cardPaymentViewModel::onViewEventConsumed)
            is CardFormViewEvent.OnOrderProcessFailed ->
                actions.orderProcessed.handle(event, param.cardPaymentViewModel::onViewEventConsumed)
            else -> Unit
        }
    }

    LaunchedEffect(paymentBrickViewEvent, param.origin) {
        if (param.origin != ReviewOrigin.Payment) return@LaunchedEffect

        when (val event = paymentBrickViewEvent) {
            is PaymentBrickViewEvent.OnPaymentReadyForReview -> {
                param.paymentBrickViewModel.onViewEventConsumed()
                actions.onOpenReview(event.params)
            }
            is PaymentBrickViewEvent.OnOrderProcessed ->
                actions.orderProcessed.handle(event, param.paymentBrickViewModel::onViewEventConsumed)
            is PaymentBrickViewEvent.OnOrderProcessFailed ->
                actions.orderProcessed.handle(event, param.paymentBrickViewModel::onViewEventConsumed)
            PaymentBrickViewEvent.OnTokenizationError -> {
                param.paymentBrickViewModel.onViewEventConsumed()
                actions.onReturnToPaymentSelectorWithGenericError()
            }
            else -> Unit
        }
    }

    val isLoading = if (param.origin == ReviewOrigin.Payment) {
        paymentBrickViewState.isLoading
    } else {
        cardPaymentViewState.isLoading
    }

    InstallmentsScreenDestination(
        param = InstallmentsScreenParam(
            installmentData = param.installmentData,
            paymentData = param.paymentData,
            checkoutType = param.checkoutConfiguration.toCheckoutType(),
            orderId = param.checkoutConfiguration?.getOrderId().orEmpty(),
            isLoading = isLoading,
        ),
        actions = actions,
    )
}

private class InstallmentsScreenParam(
    val installmentData: MPInstallmentData,
    val paymentData: MPPaymentData,
    val checkoutType: String,
    val orderId: String,
    val isLoading: Boolean,
)

@Composable
private fun InstallmentsScreenDestination(
    param: InstallmentsScreenParam,
    actions: CheckoutInstallmentActions,
) {
    val installmentsViewModel: InstallmentsViewModel = koinViewModel {
        parametersOf(
            param.installmentData,
            param.paymentData,
            param.checkoutType,
            param.orderId,
        )
    }
    val viewEvent by installmentsViewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is InstallmentViewEvent.OnSuccess -> {
                installmentsViewModel.onViewEventConsumed()
                actions.confirmInstallment(param.paymentData, event.installment)
            }

            is InstallmentViewEvent.OnFailure -> {
                installmentsViewModel.onViewEventConsumed()
                actions.onFinishCheckout(MercadoPagoCheckoutResult.Error(event.error))
            }

            is InstallmentViewEvent.OnUserCancelled -> {
                installmentsViewModel.onViewEventConsumed()
                actions.onFinishCheckout(MercadoPagoCheckoutResult.UserCancelled(event.context))
            }

            null -> Unit
        }
    }

    InstallmentsScreen(
        viewModel = installmentsViewModel,
        isLoading = param.isLoading,
        onBackClick = {
            installmentsViewModel.onBackPressed()
            actions.onBackClick()
        },
    )
}
