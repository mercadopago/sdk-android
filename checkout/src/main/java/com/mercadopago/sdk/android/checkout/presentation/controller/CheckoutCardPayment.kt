package com.mercadopago.sdk.android.checkout.presentation.controller

import android.annotation.SuppressLint
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.presentation.cardpayment.CardPaymentScreen
import com.mercadopago.sdk.android.checkout.presentation.event.InstallmentsScreenEvent
import com.mercadopago.sdk.android.checkout.presentation.installments.InstallmentsScreen
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.InstallmentsViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun MPCardPayment(
    checkoutConfiguration: CheckoutConfiguration?,
) {
    val navController = rememberNavController()

    CardPaymentNavHost(
        navController = navController,
        checkoutConfiguration = checkoutConfiguration,
    )
}

@Composable
internal fun CardPaymentNavHost(
    navController: NavHostController,
    checkoutConfiguration: CheckoutConfiguration?,
    modifier: Modifier = Modifier,
) {
    var installmentsArgs by remember { mutableStateOf<CardPaymentViewEvent.NavigateToInstallments?>(null) }

    NavHost(
        navController = navController,
        startDestination = SampleDestination.Form,
        modifier = modifier,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { it } },
    ) {
        composable<SampleDestination.Form> {
            CardFormDestination(
                checkoutConfiguration = checkoutConfiguration,
                onNavigateToInstallments = { event ->
                    installmentsArgs = event
                    navController.navigate(SampleDestination.Installment)
                },
            )
        }

        composable<SampleDestination.Installment> {
            installmentsArgs?.let { args ->
                InstallmentsDestination(
                    navController = navController,
                    checkoutConfiguration = checkoutConfiguration,
                    installmentsArgs = args,
                )
            }
        }
    }
}

@Composable
private fun CardFormDestination(
    checkoutConfiguration: CheckoutConfiguration?,
    onNavigateToInstallments: (CardPaymentViewEvent.NavigateToInstallments) -> Unit,
) {
    val cardPaymentViewModel: CardPaymentViewModel = koinViewModel { parametersOf(checkoutConfiguration) }
    val viewEvent by cardPaymentViewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is CardPaymentViewEvent.NavigateToInstallments -> {
                onNavigateToInstallments(event)
                cardPaymentViewModel.onViewEventConsumed()
            }
            is CardPaymentViewEvent.OnBackPressed,
            null,
            -> Unit
        }
    }

    CardPaymentScreen(viewModel = cardPaymentViewModel)
}

@SuppressLint("RestrictedApi")
@Composable
private fun InstallmentsDestination(
    navController: NavHostController,
    checkoutConfiguration: CheckoutConfiguration?,
    installmentsArgs: CardPaymentViewEvent.NavigateToInstallments,
) {
    val formBackStackEntry = remember { navController.getBackStackEntry<SampleDestination.Form>() }
    val cardPaymentViewModel: CardPaymentViewModel = koinViewModel(
        viewModelStoreOwner = formBackStackEntry,
    ) { parametersOf(checkoutConfiguration) }
    val installmentsViewModel: InstallmentsViewModel = koinViewModel { parametersOf(checkoutConfiguration) }
    val viewEvent by installmentsViewModel.viewEvent.collectAsState()

    LaunchedEffect(installmentsArgs) {
        installmentsViewModel.setup(
            payerCosts = installmentsArgs.payerCosts,
            lastFourDigits = installmentsArgs.lastFourDigits,
            paymentMethodId = installmentsArgs.paymentMethodId,
        )
    }

    LaunchedEffect(viewEvent) {
        when (val event = viewEvent) {
            is InstallmentsScreenEvent.ConfirmPayment -> {
                cardPaymentViewModel.generateTokenAndPay(event.installment)
            }
            else -> Unit
        }
    }

    InstallmentsScreen(
        viewModel = installmentsViewModel,
        onBackClick = { navController.popBackStack() },
        onInstallmentSelected = { navController.popBackStack() },
    )
}

@Serializable
internal sealed interface SampleDestination {
    @Serializable
    data object Form : SampleDestination

    @Serializable
    data object Installment : SampleDestination
}
