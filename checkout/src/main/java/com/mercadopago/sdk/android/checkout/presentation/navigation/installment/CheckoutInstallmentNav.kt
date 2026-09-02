package com.mercadopago.sdk.android.checkout.presentation.navigation.installment

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.startsWithPayment
import com.mercadopago.sdk.android.checkout.presentation.navigation.CheckoutFlowViewModel
import com.mercadopago.sdk.android.checkout.presentation.navigation.CheckoutLoadDestination
import com.mercadopago.sdk.android.checkout.presentation.navigation.ReviewOrigin
import com.mercadopago.sdk.android.checkout.presentation.navigation.checkoutGraphEntry
import com.mercadopago.sdk.android.checkout.presentation.navigation.rememberCheckoutNavigator
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.PaymentBrickViewModel
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
        val paymentBrickViewModel: PaymentBrickViewModel = koinViewModel(
            viewModelStoreOwner = graphEntry,
        ) { parametersOf(checkoutConfiguration) }
        val isPaymentCheckout = checkoutConfiguration.startsWithPayment()

        CheckoutInstallmentDestination(
            param = CheckoutInstallmentParam(
                installmentData = installmentContext.installmentData,
                paymentData = installmentContext.paymentData,
                checkoutConfiguration = checkoutConfiguration,
                cardPaymentViewModel = cardPaymentViewModel,
                paymentBrickViewModel = paymentBrickViewModel,
            ),
            actions = CheckoutInstallmentActions(
                onOpenReview = { params -> navigator.openReview(params, ReviewOrigin.Installment) },
                onFinishCheckout = navigator::finishCheckout,
                onReturnToPaymentSelectorWithGenericError =
                    navigator::returnToPaymentSelectorWithGenericError,
                onBackClick = navigator::navigateUpFromInstallments,
                onMarkScreenPresented = if (isPaymentCheckout) {
                    paymentBrickViewModel::markScreenPresented
                } else {
                    cardPaymentViewModel::markScreenPresented
                },
                onInstallmentConfirmed = if (isPaymentCheckout) {
                    paymentBrickViewModel::onInstallmentConfirmed
                } else {
                    cardPaymentViewModel::onInstallmentConfirmed
                },
            ),
        )
    }
}
