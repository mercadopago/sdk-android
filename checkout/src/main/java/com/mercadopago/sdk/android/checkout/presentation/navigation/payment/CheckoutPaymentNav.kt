package com.mercadopago.sdk.android.checkout.presentation.navigation.payment

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.presentation.navigation.CheckoutFlowViewModel
import com.mercadopago.sdk.android.checkout.presentation.navigation.ReviewOrigin
import com.mercadopago.sdk.android.checkout.presentation.navigation.checkoutGraphEntry
import com.mercadopago.sdk.android.checkout.presentation.navigation.rememberCheckoutNavigator
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.PaymentBrickViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

internal fun NavGraphBuilder.paymentDestination(
    navController: NavHostController,
    checkoutConfiguration: CheckoutConfiguration?,
) {
    composable<CheckoutDestination.Payment> { backStackEntry ->
        val graphEntry = checkoutGraphEntry(navController, backStackEntry)
        val flowViewModel: CheckoutFlowViewModel = koinViewModel(viewModelStoreOwner = graphEntry)
        val navigator = rememberCheckoutNavigator(navController, flowViewModel)
        val paymentBrickViewModel: PaymentBrickViewModel = koinViewModel(
            viewModelStoreOwner = graphEntry,
        ) { parametersOf(checkoutConfiguration) }
        val paymentFeedback by flowViewModel.paymentFeedback.event.collectAsState()

        CheckoutPaymentDestination(
            param = CheckoutPaymentParam(
                viewModel = paymentBrickViewModel,
                feedback = paymentFeedback,
            ),
            actions = CheckoutPaymentActions(
                onOpenForm = navigator::openForm,
                onOpenSecurityCode = navigator::openSecurityCode,
                onOpenInstallments = navigator::openInstallments,
                onOpenReview = { params -> navigator.openReview(params, ReviewOrigin.Payment) },
                onFinishCheckout = navigator::finishCheckout,
                onOpenOfflineMethodSelector = navigator::openOfflineMethodSelector,
                onShowFeedback = flowViewModel.paymentFeedback::show,
                onFeedbackConsumed = flowViewModel.paymentFeedback::consume,
            ),
        )
    }
}
