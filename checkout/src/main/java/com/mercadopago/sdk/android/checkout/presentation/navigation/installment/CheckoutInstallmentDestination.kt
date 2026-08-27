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
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentViewEvent
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

    LaunchedEffect(cardPaymentViewEvent) {
        when (val event = cardPaymentViewEvent) {
            is CardPaymentViewEvent.OnPaymentConfirmed -> {
                param.cardPaymentViewModel.onViewEventConsumed()
                actions.onMarkScreenPresented(Screen.CARD_FORM)
                actions.onOpenReview(event.params)
            }
            else -> Unit
        }
    }

    InstallmentsScreenDestination(
        param = InstallmentsScreenParam(
            installmentData = param.installmentData,
            paymentData = param.paymentData,
            checkoutType = param.checkoutConfiguration.toCheckoutType(),
            orderId = param.checkoutConfiguration?.getOrderId().orEmpty(),
            isLoading = cardPaymentViewState.isLoading,
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
                when (param.paymentData) {
                    is MPPaymentData.CardTransaction -> actions.onInstallmentConfirmed(event.installment)
                    is MPPaymentData.CardSave -> actions.onFinishCheckout(
                        MercadoPagoCheckoutResult.Success(param.paymentData),
                    )
                    is MPPaymentData.Payment -> actions.onInstallmentConfirmed(event.installment)
                }
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
