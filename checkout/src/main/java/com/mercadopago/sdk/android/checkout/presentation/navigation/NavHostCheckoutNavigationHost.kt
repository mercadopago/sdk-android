@file:JvmName("CheckoutNavigatorKt")

package com.mercadopago.sdk.android.checkout.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination

private class NavHostCheckoutNavigationHost(
    private val navController: NavHostController,
) : CheckoutNavigationHost {
    override fun navigate(
        destination: CheckoutDestination,
    ) {
        navController.navigate(destination) {
            launchSingleTop = true
        }
    }

    override fun pop(): Boolean = navController.popBackStack()

    override fun popTo(
        destination: CheckoutDestination,
    ): Boolean =
        navController.popBackStack(
            route = destination,
            inclusive = false,
        )
}

@Composable
internal fun rememberCheckoutNavigator(
    navController: NavHostController,
    flowViewModel: CheckoutFlowViewModel,
): CheckoutNavigator {
    val navigationHost = remember(navController) { NavHostCheckoutNavigationHost(navController) }
    return remember(navigationHost, flowViewModel) {
        CheckoutNavigator(
            navigationHost = navigationHost,
            flowViewModel = flowViewModel,
        )
    }
}
