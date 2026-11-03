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
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CardFormViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

internal fun NavGraphBuilder.formDestination(
    navController: NavHostController,
    checkoutConfiguration: CheckoutConfiguration?,
) {
    composable<CheckoutDestination.Form> { backStackEntry ->
        val graphEntry = checkoutGraphEntry(navController, backStackEntry)
        val flowViewModel: CheckoutFlowViewModel = koinViewModel(viewModelStoreOwner = graphEntry)
        val navigator = rememberCheckoutNavigator(navController, flowViewModel, checkoutConfiguration)
        val cardPaymentViewModel: CardFormViewModel = koinViewModel(
            viewModelStoreOwner = graphEntry,
        ) { parametersOf(checkoutConfiguration) }

        CheckoutFormDestination(
            param = CheckoutFormParam(cardPaymentViewModel),
            actions = CheckoutFormActions(
                onOpenInstallments = { installmentData, paymentData ->
                    navigator.openInstallments(installmentData, paymentData, ReviewOrigin.Form)
                },
                onInstallmentConfirmed = cardPaymentViewModel::onInstallmentConfirmed,
                onOpenReview = { params -> navigator.openReview(params, ReviewOrigin.Form) },
                onFinishCheckout = navigator::finishCheckout,
                orderProcessed = navigator.orderProcessedActions,
                onInvalidInstallmentData = cardPaymentViewModel::onInvalidInstallmentData,
                onMarkScreenPresented = cardPaymentViewModel::markScreenPresented,
                onReturnToPaymentSelectorWithGenericError = navigator::returnToPaymentSelectorWithGenericError,
                onReturnToPaymentSelector = navigator::returnToPaymentSelector,
            ),
        )
    }
}
