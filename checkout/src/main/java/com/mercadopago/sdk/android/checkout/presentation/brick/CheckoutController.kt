package com.mercadopago.sdk.android.checkout.presentation.brick

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.presentation.cardpayment.CardPaymentScreen
import com.mercadopago.sdk.android.checkout.presentation.installments.InstallmentsScreen
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import com.mercadopago.sdk.android.components.MPProgressIndicator
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
private data object CheckoutGraph

@Composable
internal fun CheckoutController(
    checkoutConfiguration: CheckoutConfiguration?,
    navController: NavHostController = rememberNavController(),
    checkoutControllerViewModel: CheckoutControllerViewModel = koinViewModel {
        parametersOf(checkoutConfiguration)
    },
) {
    val screenState by checkoutControllerViewModel.screenState.collectAsState()
    LaunchedEffect(Unit) { checkoutControllerViewModel.load() }

    var pendingInstallmentData by remember { mutableStateOf<MPInstallmentData?>(null) }

    when (val state = screenState) {
        is CheckoutControllerViewModel.ScreenState.Loading -> LoadingScreen()

        is CheckoutControllerViewModel.ScreenState.Ready -> NavHost(
            navController = navController,
            startDestination = CheckoutDestination.Form,
            route = CheckoutGraph::class,
            enterTransition = { slideInHorizontally { it } },
            exitTransition = { slideOutHorizontally { it } },
        ) {
            composable<CheckoutDestination.Form> {
                CardFormScreenDestination(
                    data = state.initData,
                    onNavigateToInstallments = { installmentData ->
                        pendingInstallmentData = installmentData
                        navController.navigate(CheckoutDestination.Installment)
                    },
                )
            }

            composable<CheckoutDestination.Installment> {
                val data = pendingInstallmentData ?: return@composable
                InstallmentsScreenDestination(
                    data = data,
                    onBackClick = { navController.popBackStack() },
                )
            }
        }
    }
}

@Composable
private fun CardFormScreenDestination(
    data: CardFormInitializationOutput,
    onNavigateToInstallments: (MPInstallmentData) -> Unit,
) {
    val cardPaymentViewModel: CardPaymentViewModel = koinViewModel { parametersOf(data) }
    val viewEvent by cardPaymentViewModel.viewEvent.collectAsState()

    // Roda ao montar/remontar o Form (entrada inicial ou volta após pop do Installment).
    // Reseta o loading do submit anterior pra evitar a "piscada" do loader durante a transição.
    LaunchedEffect(Unit) {
        cardPaymentViewModel.clearSubmitState()
    }

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is CardPaymentViewEvent.OnSuccess -> {
                onNavigateToInstallments(event.installment)
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

            null -> Unit
        }
    }

    CardPaymentScreen(viewModel = cardPaymentViewModel)
}

@Composable
private fun InstallmentsScreenDestination(
    data: MPInstallmentData,
    onBackClick: () -> Unit,
) {
    val installmentsViewModel: InstallmentsViewModel = koinViewModel { parametersOf(data) }
    val viewEvent by installmentsViewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is InstallmentViewEvent.OnSuccess -> {
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
        onBackClick = onBackClick,
    )
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        MPProgressIndicator()
    }
}
