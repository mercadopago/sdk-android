package com.mercadopago.sdk.android.checkout.presentation.navigation.form

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.presentation.navigation.CheckoutFlowViewModel
import com.mercadopago.sdk.android.checkout.presentation.navigation.ReviewOrigin
import com.mercadopago.sdk.android.checkout.presentation.navigation.checkoutGraphEntry
import com.mercadopago.sdk.android.checkout.presentation.navigation.rememberCheckoutNavigator
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardPaymentViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

internal fun NavGraphBuilder.formDestination(
    navController: NavHostController,
    checkoutConfiguration: CheckoutConfiguration?,
) {
    composable<CheckoutDestination.Form> { backStackEntry ->
        val graphEntry = checkoutGraphEntry(navController, backStackEntry)
        val flowViewModel: CheckoutFlowViewModel = koinViewModel(viewModelStoreOwner = graphEntry)
        val navigator = rememberCheckoutNavigator(navController, flowViewModel)
        val cardPaymentViewModel: CardPaymentViewModel = koinViewModel(
            viewModelStoreOwner = graphEntry,
        ) { parametersOf(checkoutConfiguration) }

        CheckoutFormDestination(
            param = CheckoutFormParam(cardPaymentViewModel),
            actions = CheckoutFormActions(
                onOpenInstallments = navigator::openInstallments,
                onInstallmentConfirmed = cardPaymentViewModel::onInstallmentConfirmed,
                onOpenReview = { params -> navigator.openReview(params, ReviewOrigin.Form) },
                onFinishCheckout = navigator::finishCheckout,
                onInvalidInstallmentData = cardPaymentViewModel::onInvalidInstallmentData,
                onMarkScreenPresented = cardPaymentViewModel::markScreenPresented,
            ),
        )
    }
}
