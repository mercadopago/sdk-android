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
import com.mercadopago.sdk.android.checkout.core.model.internal.showsInstallments
import com.mercadopago.sdk.android.checkout.core.model.internal.startsWithPaymentBrick
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.presentation.cardpayment.CardPaymentScreen
import com.mercadopago.sdk.android.checkout.presentation.installments.InstallmentsScreen
import com.mercadopago.sdk.android.checkout.presentation.loading.LoadingScreen
import com.mercadopago.sdk.android.checkout.presentation.paymentbrick.PaymentBrickScreen
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.PaymentBrickViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun CheckoutController(
    checkoutConfiguration: CheckoutConfiguration?,
    navController: NavHostController = rememberNavController(),
) {
    var pendingPayment by remember { mutableStateOf<MPPaymentData?>(null) }

    val initialDestination: CheckoutDestination =
        if (checkoutConfiguration.startsWithPaymentBrick()) {
            CheckoutDestination.PaymentBrick
        } else {
            CheckoutDestination.Form
        }

    NavHost(
        navController = navController,
        startDestination = CheckoutDestination.Loading,
        route = CheckoutGraph::class,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { it } },
    ) {
        composable<CheckoutDestination.Loading> {
            LoadingScreen()
            LaunchedEffect(Unit) {
                navController.navigate(initialDestination) {
                    popUpTo(CheckoutDestination.Loading) { inclusive = true }
                }
            }
        }

        composable<CheckoutDestination.PaymentBrick> {
            PaymentBrickScreenDestination(
                onNavigateToForm = { navController.navigate(CheckoutDestination.Form) },
            )
        }

        composable<CheckoutDestination.Form> {
            CardFormScreenDestination(
                checkoutConfiguration = checkoutConfiguration,
                onNavigateToInstallments = { payment ->
                    pendingPayment = payment
                    navController.navigate(CheckoutDestination.Installments)
                },
            )
        }

        composable<CheckoutDestination.Installments> {
            InstallmentsScreenDestination(
                payment = pendingPayment,
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}

@Composable
private fun PaymentBrickScreenDestination(
    onNavigateToForm: () -> Unit,
) {
    val paymentBrickViewModel: PaymentBrickViewModel = koinViewModel()
    val viewEvent by paymentBrickViewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (viewEvent) {
            is PaymentBrickViewEvent.OnOptionSelected -> {
                onNavigateToForm()
                paymentBrickViewModel.onViewEventConsumed()
            }

            null -> Unit
        }
    }

    PaymentBrickScreen(viewModel = paymentBrickViewModel)
}

@Composable
private fun CardFormScreenDestination(
    checkoutConfiguration: CheckoutConfiguration?,
    onNavigateToInstallments: (MPPaymentData) -> Unit,
) {
    val cardPaymentViewModel: CardPaymentViewModel = koinViewModel {
        parametersOf(checkoutConfiguration)
    }
    val viewEvent by cardPaymentViewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is CardPaymentViewEvent.OnSuccess -> {
                if (checkoutConfiguration.showsInstallments()) {
                    onNavigateToInstallments(event.payment)
                } else {
                    CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Success(event.payment))
                }
                cardPaymentViewModel.onViewEventConsumed()
            }

            is CardPaymentViewEvent.OnFailure -> {
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(event.error))
                cardPaymentViewModel.onViewEventConsumed()
            }

            is CardPaymentViewEvent.OnUserCancelled -> {
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.UserCancelled(event.context))
                cardPaymentViewModel.onViewEventConsumed()
            }

            is CardPaymentViewEvent.OnBackPressed -> {
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.UserCancelled(event.context))
                cardPaymentViewModel.onViewEventConsumed()
            }

            null -> Unit
        }
    }

    CardPaymentScreen(viewModel = cardPaymentViewModel)
}

@Composable
private fun InstallmentsScreenDestination(
    payment: MPPaymentData?,
    onBackClick: () -> Unit,
) {
    val installmentsViewModel: InstallmentsViewModel = koinViewModel()

    InstallmentsScreen(
        viewModel = installmentsViewModel,
        onBackClick = onBackClick,
        onInstallmentSelected = {
            payment?.let { CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Success(it)) }
        },
    )
}
