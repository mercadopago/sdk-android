package com.mercadopago.sdk.android.checkout.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.presentation.CheckoutGraph
import com.mercadopago.sdk.android.checkout.presentation.navigation.form.formDestination
import com.mercadopago.sdk.android.checkout.presentation.navigation.installment.installmentDestination
import com.mercadopago.sdk.android.checkout.presentation.navigation.offlinemethodselector.offlineMethodSelectorDestination
import com.mercadopago.sdk.android.checkout.presentation.navigation.payment.paymentDestination
import com.mercadopago.sdk.android.checkout.presentation.navigation.reviewconfirm.reviewConfirmDestination
import com.mercadopago.sdk.android.checkout.presentation.navigation.securitycode.securityCodeDestination
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination

internal fun NavGraphBuilder.checkoutDestinations(
    navController: NavHostController,
    checkoutConfiguration: CheckoutConfiguration?,
    startDestination: CheckoutDestination,
) {
    paymentDestination(navController, checkoutConfiguration)
    securityCodeDestination(navController, checkoutConfiguration, startDestination)
    formDestination(navController, checkoutConfiguration)
    reviewConfirmDestination(navController, checkoutConfiguration, startDestination)
    installmentDestination(navController, checkoutConfiguration, startDestination)
    offlineMethodSelectorDestination(navController, checkoutConfiguration, startDestination)
}

@Composable
internal fun checkoutGraphEntry(
    navController: NavHostController,
    backStackEntry: NavBackStackEntry,
): NavBackStackEntry =
    remember(backStackEntry) {
        navController.getBackStackEntry<CheckoutGraph>()
    }
