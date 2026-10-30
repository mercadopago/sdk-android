package com.mercadopago.sdk.android.checkout.presentation.navigation.reviewconfirm

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.presentation.navigation.CheckoutFlowViewModel
import com.mercadopago.sdk.android.checkout.presentation.navigation.CheckoutLoadDestination
import com.mercadopago.sdk.android.checkout.presentation.navigation.checkoutGraphEntry
import com.mercadopago.sdk.android.checkout.presentation.navigation.rememberCheckoutNavigator
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardFormViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

internal fun NavGraphBuilder.reviewConfirmDestination(
    navController: NavHostController,
    checkoutConfiguration: CheckoutConfiguration?,
    startDestination: CheckoutDestination,
) {
    composable<CheckoutDestination.ReviewConfirm> { backStackEntry ->
        val graphEntry = checkoutGraphEntry(navController, backStackEntry)
        val flowViewModel: CheckoutFlowViewModel = koinViewModel(viewModelStoreOwner = graphEntry)
        val navigator = rememberCheckoutNavigator(navController, flowViewModel, checkoutConfiguration)
        val reviewContext = remember(backStackEntry) { flowViewModel.state.value.reviewContext }
        if (reviewContext == null) {
            CheckoutLoadDestination(navigator, startDestination)
            return@composable
        }
        val cardPaymentViewModel = if (
            checkoutConfiguration?.checkoutType is MPCheckoutType.CardTransaction
        ) {
            koinViewModel<CardFormViewModel>(viewModelStoreOwner = graphEntry) {
                parametersOf(checkoutConfiguration)
            }
        } else {
            null
        }

        CheckoutReviewConfirmDestination(
            param = CheckoutReviewConfirmParam(
                reviewContext = reviewContext,
                checkoutConfiguration = checkoutConfiguration,
                cardPaymentViewModel = cardPaymentViewModel,
            ),
            actions = CheckoutReviewConfirmActions(
                onNavigateUp = navigator::navigateUpFromReview,
                onReturnToPaymentSelector = navigator::returnToPaymentSelector,
                onReturnToPaymentSelectorWithGenericError =
                    navigator::returnToPaymentSelectorWithGenericError,
                onReturnToCardTransactionWithGenericError = {
                    navigator.recoverTo(CheckoutDestination.Form)
                    cardPaymentViewModel?.onReviewConfirmError()
                },
                onNavigateToStatusScreenForResolution = navigator::navigateToStatusScreenForResolution,
                onFinishCheckout = navigator::finishCheckout,
                onFinishForEmailChange = navigator::finishForEmailChange,
            ),
        )
    }
}
