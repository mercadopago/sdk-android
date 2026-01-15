package com.mercadopago.sdk.android.checkout.presentation.controller

import android.annotation.SuppressLint
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mercadopago.sdk.android.checkout.presentation.cardpayment.CardPaymentScreen
import com.mercadopago.sdk.android.components.MPHeader
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@SuppressLint("RestrictedApi")
@Composable
internal fun MPCardPayment() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = currentBackStackEntry?.destination?.route


    CardPaymentNavHost(
        navController = navController,
    )
}

@Composable
internal fun CardPaymentNavHost(
    navController: NavHostController,
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
                viewModel = koinViewModel(),
                onBackClick = { navController.popBackStack() },
            )
        }

        composable<SampleDestination.Installment> {
            CardPaymentInstallmentScreen()
        }
    }
}

@Composable
internal fun CardPaymentInstallmentScreen() {
    // Installment screen placeholder
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
