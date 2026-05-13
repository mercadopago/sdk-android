package com.mercadopago.sdk.android.checkout.presentation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.presentation.cardpayment.CardPaymentScreen
import com.mercadopago.sdk.android.checkout.presentation.installments.InstallmentsScreen
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun CheckoutController(
    checkoutConfiguration: CheckoutConfiguration?,
    navController: NavHostController = rememberNavController(),
) {
    var pendingPaymentData by remember { mutableStateOf<MPPaymentData?>(null) }

    NavHost(
        navController = navController,
        startDestination = CheckoutDestination.Form,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { it } },
    ) {
        composable<CheckoutDestination.Form> {
            CardFormScreenDestination(
                checkoutConfiguration = checkoutConfiguration,
                onNavigateToInstallments = { paymentData ->
                    pendingPaymentData = paymentData
                    navController.navigate(CheckoutDestination.Installment)
                },
            )
        }

        composable<CheckoutDestination.Installment> {
            val paymentData = pendingPaymentData ?: return@composable
            InstallmentsScreenDestination(
                paymentData = paymentData,
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}

@Composable
private fun CardFormScreenDestination(
    checkoutConfiguration: CheckoutConfiguration?,
    onNavigateToInstallments: (MPPaymentData) -> Unit,
) {
    val viewModel: CardPaymentViewModel = koinViewModel { parametersOf(checkoutConfiguration) }
    val viewEvent by viewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is CardPaymentViewEvent.OnSuccess -> {
                if (event.installment.showInstallment) {
                    onNavigateToInstallments(event.payment)
                } else {
                    CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Success(event.payment))
                }
                viewModel.clearViewEvent()
            }

            is CardPaymentViewEvent.OnFailure -> {
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(event.error))
                viewModel.clearViewEvent()
            }

            is CardPaymentViewEvent.OnUserCancelled -> {
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.UserCancelled(event.context))
                viewModel.clearViewEvent()
            }

            null -> Unit
        }
    }

    CardPaymentScreen(viewModel = viewModel)
}

@Composable
private fun InstallmentsScreenDestination(
    paymentData: MPPaymentData,
    onBackClick: () -> Unit,
) {
    val viewModel: InstallmentsViewModel = koinViewModel { parametersOf(paymentData) }
    val viewEvent by viewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is InstallmentViewEvent.OnSuccess -> {
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Success(event.payment))
                viewModel.clearViewEvent()
            }

            is InstallmentViewEvent.OnFailure -> {
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(event.error))
                viewModel.clearViewEvent()
            }

            is InstallmentViewEvent.OnUserCancelled -> {
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.UserCancelled(event.context))
                viewModel.clearViewEvent()
            }

            null -> Unit
        }
    }

    InstallmentsScreen(viewModel = viewModel, onBackClick = onBackClick)
}
