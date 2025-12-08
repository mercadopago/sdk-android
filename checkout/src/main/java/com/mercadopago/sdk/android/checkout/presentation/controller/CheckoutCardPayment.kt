package com.mercadopago.sdk.android.checkout.presentation.controller

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mercadopago.sdk.android.checkout.presentation.cardpayment.CardPaymentScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MPCardPayment() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = currentBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            CardPaymentNavHost(
                navController = navController,
                modifier = Modifier.padding(paddingValues),
            )
        }
    }
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
            CardPaymentScreen(viewModel = koinViewModel())
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

internal fun SampleDestination.isRoute(route: String?): Boolean {
    return this::class.qualifiedName == route
}
