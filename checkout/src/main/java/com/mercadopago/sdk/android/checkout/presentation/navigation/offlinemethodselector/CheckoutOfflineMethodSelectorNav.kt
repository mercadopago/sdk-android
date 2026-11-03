package com.mercadopago.sdk.android.checkout.presentation.navigation.offlinemethodselector

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.presentation.navigation.CheckoutFlowViewModel
import com.mercadopago.sdk.android.checkout.presentation.navigation.CheckoutLoadDestination
import com.mercadopago.sdk.android.checkout.presentation.navigation.ReviewOrigin
import com.mercadopago.sdk.android.checkout.presentation.navigation.checkoutGraphEntry
import com.mercadopago.sdk.android.checkout.presentation.navigation.rememberCheckoutNavigator
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.PaymentBrickViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

internal fun NavGraphBuilder.offlineMethodSelectorDestination(
    navController: NavHostController,
    checkoutConfiguration: CheckoutConfiguration?,
    startDestination: CheckoutDestination,
) {
    composable<CheckoutDestination.OfflineMethodSelector> { backStackEntry ->
        val graphEntry = checkoutGraphEntry(navController, backStackEntry)
        val flowViewModel: CheckoutFlowViewModel = koinViewModel(viewModelStoreOwner = graphEntry)
        val navigator = rememberCheckoutNavigator(navController, flowViewModel, checkoutConfiguration)
        val screenData = remember(backStackEntry) { flowViewModel.state.value.offlineMethodSelectorContext }
        if (screenData == null) {
            CheckoutLoadDestination(navigator, startDestination)
            return@composable
        }
        val paymentBrickViewModel: PaymentBrickViewModel = koinViewModel(
            viewModelStoreOwner = graphEntry,
        ) { parametersOf(checkoutConfiguration) }
        val paymentBrickViewEvent by paymentBrickViewModel.viewEvent.collectAsState()

        LaunchedEffect(paymentBrickViewEvent) {
            when (val event = paymentBrickViewEvent) {
                is PaymentBrickViewEvent.OnOrderProcessed ->
                    navigator.orderProcessedActions.handle(event, paymentBrickViewModel::onViewEventConsumed)
                is PaymentBrickViewEvent.OnOrderProcessFailed ->
                    navigator.orderProcessedActions.handle(event, paymentBrickViewModel::onViewEventConsumed)
                else -> Unit
            }
        }

        CheckoutOfflineMethodSelectorDestination(
            param = CheckoutOfflineMethodSelectorParam(
                screenData = screenData,
                checkoutConfiguration = checkoutConfiguration,
            ),
            actions = CheckoutOfflineMethodSelectorActions(
                onOpenReview = { params ->
                    navigator.openReview(params, ReviewOrigin.OfflineMethodSelector)
                },
                onProcessOrder = paymentBrickViewModel::processOrder,
                onBackClick = navigator::navigateUpFromOfflineMethodSelector,
            ),
        )
    }
}
