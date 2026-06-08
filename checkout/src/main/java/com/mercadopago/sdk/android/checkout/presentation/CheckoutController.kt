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
import com.mercadopago.sdk.android.checkout.core.model.internal.toCheckoutType
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.presentation.cardpayment.CardPaymentScreen
import com.mercadopago.sdk.android.checkout.presentation.installments.InstallmentsScreen
import com.mercadopago.sdk.android.checkout.presentation.loading.LoadingScreen
import com.mercadopago.sdk.android.checkout.presentation.paymentbrick.PaymentBrickScreen
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentViewEvent
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
    var pendingInstallmentData by remember { mutableStateOf<MPInstallmentData?>(null) }
    var pendingPaymentData by remember { mutableStateOf<MPPaymentData?>(null) }

    val startDestination: CheckoutDestination =
        if (checkoutConfiguration.startsWithPaymentBrick()) {
            CheckoutDestination.Loading
        } else {
            CheckoutDestination.Form
        }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = CheckoutGraph::class,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { it } },
    ) {
        composable<CheckoutDestination.Loading> {
            LoadingScreen()
            LaunchedEffect(Unit) {
                navController.navigate(CheckoutDestination.PaymentBrick) {
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
                onNavigateToInstallments = { installmentData, paymentData ->
                    pendingInstallmentData = installmentData
                    pendingPaymentData = paymentData
                    navController.navigate(CheckoutDestination.Installment)
                },
            )
        }

        composable<CheckoutDestination.Installment> {
            val installmentData = pendingInstallmentData ?: return@composable
            val paymentData = pendingPaymentData ?: return@composable
            InstallmentsScreenDestination(
                installmentData = installmentData,
                paymentData = paymentData,
                checkoutType = checkoutConfiguration.toCheckoutType(),
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
    onNavigateToInstallments: (MPInstallmentData, MPPaymentData) -> Unit,
) {
    val cardPaymentViewModel: CardPaymentViewModel = koinViewModel {
        parametersOf(checkoutConfiguration)
    }
    val viewEvent by cardPaymentViewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is CardPaymentViewEvent.OnSuccess -> {
                if (checkoutConfiguration.showsInstallments()) {
                    cardPaymentViewModel.markInstallmentsPresented()
                    onNavigateToInstallments(event.installment, event.payment)
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
    installmentData: MPInstallmentData,
    paymentData: MPPaymentData,
    checkoutType: String,
    onBackClick: () -> Unit,
) {
    val installmentsViewModel: InstallmentsViewModel = koinViewModel {
        parametersOf(installmentData, paymentData, checkoutType)
    }
    val viewEvent by installmentsViewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is InstallmentViewEvent.OnSuccess -> {
                val updated = when (paymentData) {
                    is MPPaymentData.CardTransaction -> paymentData.copy(installment = event.installment)
                    is MPPaymentData.CardSave -> paymentData
                }
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Success(updated))
                installmentsViewModel.onViewEventConsumed()
            }

            is InstallmentViewEvent.OnFailure -> {
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(event.error))
                installmentsViewModel.onViewEventConsumed()
            }

            is InstallmentViewEvent.OnUserCancelled -> {
                CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.UserCancelled(event.context))
                installmentsViewModel.onViewEventConsumed()
            }

            null -> Unit
        }
    }

    InstallmentsScreen(
        viewModel = installmentsViewModel,
        onBackClick = {
            installmentsViewModel.onBackPressed()
            onBackClick()
        },
    )
}
