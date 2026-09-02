package com.mercadopago.sdk.android.checkout.presentation.navigation.securitycode

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.presentation.navigation.CheckoutFlowViewModel
import com.mercadopago.sdk.android.checkout.presentation.navigation.CheckoutLoadDestination
import com.mercadopago.sdk.android.checkout.presentation.navigation.checkoutGraphEntry
import com.mercadopago.sdk.android.checkout.presentation.navigation.rememberCheckoutNavigator
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.PaymentBrickViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

internal fun NavGraphBuilder.securityCodeDestination(
    navController: NavHostController,
    checkoutConfiguration: CheckoutConfiguration?,
    startDestination: CheckoutDestination,
) {
    composable<CheckoutDestination.SecurityCode> { backStackEntry ->
        val graphEntry = checkoutGraphEntry(navController, backStackEntry)
        val flowViewModel: CheckoutFlowViewModel = koinViewModel(viewModelStoreOwner = graphEntry)
        val navigator = rememberCheckoutNavigator(navController, flowViewModel)
        val config = remember(backStackEntry) { flowViewModel.state.value.securityCodeContext }
        if (config == null) {
            CheckoutLoadDestination(navigator, startDestination)
            return@composable
        }
        val paymentBrickViewModel: PaymentBrickViewModel = koinViewModel(
            viewModelStoreOwner = graphEntry,
        ) { parametersOf(checkoutConfiguration) }

        LaunchedEffect(Unit) {
            paymentBrickViewModel.markScreenPresented(Screen.SECURITY_CODE)
        }
        CheckoutSecurityCodeDestination(
            param = CheckoutSecurityCodeParam(config),
            actions = CheckoutSecurityCodeActions(
                onTokenSuccess = { cardId, token ->
                    navigator.navigateUpFromSecurityCode()
                    paymentBrickViewModel.processOrder(cardId = cardId, token = token)
                },
                onTokenError = {
                    navigator.navigateUpFromSecurityCode()
                    paymentBrickViewModel.onTokenError()
                },
                onUserCancelled = navigator::navigateUpFromSecurityCode,
            ),
        )
    }
}
