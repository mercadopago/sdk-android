package com.mercadopago.sdk.android.checkout.presentation.controller

import android.annotation.SuppressLint
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.presentation.cardpayment.CardPaymentScreen
import com.mercadopago.sdk.android.checkout.presentation.installments.InstallmentsScreen
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
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
                onNavigateToInstallments = {
                    navController.navigate(SampleDestination.Installment)
                },
            )
        }

        composable<SampleDestination.Installment> {
            InstallmentsDestination(
                navController = navController,
                checkoutConfiguration = checkoutConfiguration,
            )
        }
    }
}

@Composable
private fun CardFormDestination(
    checkoutConfiguration: CheckoutConfiguration?,
    onNavigateToInstallments: () -> Unit,
) {
    val cardPaymentViewModel: CardPaymentViewModel = koinViewModel { parametersOf(checkoutConfiguration) }
    val viewEvent by cardPaymentViewModel.viewEvent.collectAsState()

    LaunchedEffect(viewEvent) {
        when (viewEvent) {
            CardPaymentViewEvent.NavigateToInstallments -> {
                onNavigateToInstallments()
                cardPaymentViewModel.onViewEventConsumed()
            }
            null -> Unit
        }
    }

    CardPaymentScreen(viewModel = cardPaymentViewModel)
}

/**
 * Reuses Form's back-stack entry as ViewModel store so Form and Installments share the same
 * [CardPaymentViewModel] instance. `getBackStackEntry<T>()` is annotated `@RestrictTo` on the
 * current Navigation Compose API but is the documented way to scope a typed destination's
 * ViewModel.
 */
@SuppressLint("RestrictedApi")
@Composable
private fun InstallmentsDestination(
    navController: NavHostController,
    checkoutConfiguration: CheckoutConfiguration?,
) {
    val formBackStackEntry = remember { navController.getBackStackEntry<SampleDestination.Form>() }
    val cardPaymentViewModel: CardPaymentViewModel = koinViewModel(
        viewModelStoreOwner = formBackStackEntry,
    ) { parametersOf(checkoutConfiguration) }

    InstallmentsScreen(
        viewModel = cardPaymentViewModel,
        onBackClick = { navController.popBackStack() },
    )
}

@Serializable
internal sealed interface SampleDestination {
    @Serializable
    data object Form : SampleDestination

    @Serializable
    data object Installment : SampleDestination
}
