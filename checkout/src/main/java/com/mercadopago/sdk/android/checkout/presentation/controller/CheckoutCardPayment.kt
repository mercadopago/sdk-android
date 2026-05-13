package com.mercadopago.sdk.android.checkout.presentation.controller

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.presentation.cardpayment.CardPaymentScreen
import com.mercadopago.sdk.android.checkout.presentation.installments.InstallmentsScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@SuppressLint("RestrictedApi")
@Composable
internal fun MPCardPayment(
    checkoutConfiguration: CheckoutConfiguration?,
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = currentBackStackEntry?.destination?.route

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
            CardPaymentScreen(
                viewModel = koinViewModel { parametersOf(checkoutConfiguration) },
            )
        }

        composable<SampleDestination.Installment> {
            InstallmentsScreen(
                viewModel = koinViewModel(),
                onBackClick = { navController.popBackStack() },
                onInstallmentSelected = { Log.i("InstallmentsScreen", "onItemClick: $it") },
            )
        }
    }
}

@Serializable
internal sealed interface SampleDestination {
    @Serializable
    object Form : SampleDestination

    @Serializable
    object Installment : SampleDestination
}

internal fun SampleDestination.isRoute(
    route: String?,
): Boolean {
    return this::class.qualifiedName == route
}
