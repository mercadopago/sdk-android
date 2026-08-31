package com.mercadopago.sdk.android.checkout.presentation.navigation.installment

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
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

internal fun NavGraphBuilder.installmentDestination(
    navController: NavHostController,
    checkoutConfiguration: CheckoutConfiguration?,
    startDestination: CheckoutDestination,
) {
    composable<CheckoutDestination.Installment> { backStackEntry ->
        val graphEntry = checkoutGraphEntry(navController, backStackEntry)
        val flowViewModel: CheckoutFlowViewModel = koinViewModel(viewModelStoreOwner = graphEntry)
        val navigator = rememberCheckoutNavigator(navController, flowViewModel)
        val installmentContext = remember(backStackEntry) { flowViewModel.state.value.installmentContext }
        if (installmentContext == null) {
            CheckoutLoadDestination(navigator, startDestination)
            return@composable
        }
        val cardPaymentViewModel: CardPaymentViewModel = koinViewModel(
            viewModelStoreOwner = graphEntry,
        ) { parametersOf(checkoutConfiguration) }

        CheckoutInstallmentDestination(
            param = CheckoutInstallmentParam(
                installmentData = installmentContext.installmentData,
                paymentData = installmentContext.paymentData,
                checkoutConfiguration = checkoutConfiguration,
                cardPaymentViewModel = cardPaymentViewModel,
            ),
            actions = CheckoutInstallmentActions(
                onOpenReview = { params -> navigator.openReview(params, ReviewOrigin.Installment) },
                onInstallmentConfirmed = cardPaymentViewModel::onInstallmentConfirmed,
                onBackClick = navigator::navigateUpFromInstallments,
                onFinishCheckout = navigator::finishCheckout,
                onMarkScreenPresented = cardPaymentViewModel::markScreenPresented,
            ),
        )
    }
}
