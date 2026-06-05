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
import com.mercadopago.sdk.android.checkout.core.model.internal.getOrderId
import com.mercadopago.sdk.android.checkout.core.model.internal.toCheckoutType
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
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
    var pendingInstallmentData by remember { mutableStateOf<MPInstallmentData?>(null) }
    var pendingPaymentData by remember { mutableStateOf<MPPaymentData?>(null) }

    NavHost(
        navController = navController,
        startDestination = CheckoutDestination.Form,
        route = CheckoutGraph::class,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { it } },
    ) {
        composable<CheckoutDestination.Form> {
            val graphEntry = remember { navController.getBackStackEntry<CheckoutGraph>() }
            val cardPaymentViewModel: CardPaymentViewModel = koinViewModel(
                viewModelStoreOwner = graphEntry,
            ) { parametersOf(checkoutConfiguration) }
            CardFormScreenDestination(
                cardPaymentViewModel = cardPaymentViewModel,
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
            val graphEntry = remember { navController.getBackStackEntry<CheckoutGraph>() }
            val cardPaymentViewModel: CardPaymentViewModel = koinViewModel(
                viewModelStoreOwner = graphEntry,
            ) { parametersOf(checkoutConfiguration) }
            val cardPaymentViewState by cardPaymentViewModel.viewState.collectAsState()

            InstallmentsScreenDestination(
                installmentData = installmentData,
                paymentData = paymentData,
                checkoutType = checkoutConfiguration.toCheckoutType(),
                orderId = checkoutConfiguration?.getOrderId().orEmpty(),
                onInstallmentConfirmed = { installments ->
                    cardPaymentViewModel.onInstallmentConfirmed(installments)
                },
                isLoading = cardPaymentViewState.isLoading,
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}

@Composable
private fun CardFormScreenDestination(
    cardPaymentViewModel: CardPaymentViewModel,
    onNavigateToInstallments: (MPInstallmentData, MPPaymentData) -> Unit,
) {
    val viewEvent by cardPaymentViewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is CardPaymentViewEvent.OnSuccess -> {
                when {
                    event.payment is MPPaymentData.CardTransaction &&
                        event.installment.quotas.size == 1 ->
                        cardPaymentViewModel.onInstallmentConfirmed(
                            event.installment.quotas.first().installments ?: 1,
                        )
                    event.installment.quotas.isNotEmpty() -> {
                        cardPaymentViewModel.markInstallmentsPresented()
                        onNavigateToInstallments(event.installment, event.payment)
                    }
                    else ->
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

            null -> Unit
        }
    }

    CardPaymentScreen(viewModel = cardPaymentViewModel)
}

@Suppress("LongParameterList")
@Composable
private fun InstallmentsScreenDestination(
    installmentData: MPInstallmentData,
    paymentData: MPPaymentData,
    checkoutType: String,
    orderId: String,
    onInstallmentConfirmed: (Int) -> Unit,
    isLoading: Boolean,
    onBackClick: () -> Unit,
) {
    val installmentsViewModel: InstallmentsViewModel = koinViewModel {
        parametersOf(installmentData, paymentData, checkoutType, orderId)
    }
    val viewEvent by installmentsViewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is InstallmentViewEvent.OnSuccess -> {
                when (paymentData) {
                    is MPPaymentData.CardTransaction -> onInstallmentConfirmed(event.installment)
                    is MPPaymentData.CardSave -> {
                        CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Success(paymentData))
                    }
                }
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
        isLoading = isLoading,
        onBackClick = {
            installmentsViewModel.onBackPressed()
            onBackClick()
        },
    )
}
