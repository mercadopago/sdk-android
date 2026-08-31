package com.mercadopago.sdk.android.checkout.presentation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.startsWithPayment
import com.mercadopago.sdk.android.checkout.presentation.navigation.checkoutDestinations
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination

@Composable
internal fun CheckoutController(
    checkoutConfiguration: CheckoutConfiguration?,
    navController: NavHostController = rememberNavController(),
) {
    val startDestination = if (checkoutConfiguration.startsWithPayment()) {
        CheckoutDestination.Payment
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
        checkoutDestinations(
            navController = navController,
            checkoutConfiguration = checkoutConfiguration,
            startDestination = startDestination,
        )
    }
}
