package com.mercadopago.sdk.android.example.presentation.home

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.mercadopago.sdk.android.example.navigation.SampleDestination
import com.mercadopago.sdk.android.example.presentation.checkout.CheckoutFlowScreen
import com.mercadopago.sdk.android.example.presentation.checkout.CheckoutMenuScreen
import com.mercadopago.sdk.android.example.presentation.coremethods.PaymentExampleScreen
import com.mercadopago.sdk.android.example.presentation.features.SampleFeaturesScreen
import com.mercadopago.sdk.android.example.presentation.sdkinitializer.SdkInitializerScreen

@Composable
internal fun SampleNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SampleDestination.Home,
        modifier = modifier,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { it } },
    ) {
        composable<SampleDestination.Home> {
            SampleFeaturesScreen(
                onFeatureClick = { destination ->
                    navController.navigate(destination)
                },
            )
        }
        composable<SampleDestination.SDKInstance> {
            SdkInitializerScreen()
        }
        composable<SampleDestination.CoreMethods> {
            PaymentExampleScreen()
        }
        composable<SampleDestination.Checkout> {
            CheckoutMenuScreen(
                onFlowSelected = { type ->
                    navController.navigate(SampleDestination.CheckoutFlow(type))
                },
            )
        }
        composable<SampleDestination.CheckoutFlow> { backStackEntry ->
            val flow = backStackEntry.toRoute<SampleDestination.CheckoutFlow>()
            CheckoutFlowScreen(type = flow.type)
        }
    }
}
