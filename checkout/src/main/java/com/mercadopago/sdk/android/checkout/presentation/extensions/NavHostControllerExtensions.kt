package com.mercadopago.sdk.android.checkout.presentation.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.mercadopago.sdk.android.checkout.presentation.CheckoutGraph
import com.mercadopago.sdk.android.checkout.presentation.viewmodel.CheckoutCoordinatorViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.ParametersDefinition

internal fun NavHostController.getCheckoutGraphEntryOrNull(): NavBackStackEntry? =
    try {
        getBackStackEntry<CheckoutGraph>()
    } catch (e: IllegalArgumentException) {
        null
    }

@Composable
internal inline fun <reified VM : ViewModel> NavHostController.checkoutGraphViewModel(
    backStackEntry: NavBackStackEntry,
    noinline parameters: ParametersDefinition? = null,
): VM? {
    val graphEntry = remember(backStackEntry) { getCheckoutGraphEntryOrNull() } ?: return null
    return koinViewModel(
        viewModelStoreOwner = graphEntry,
        parameters = parameters,
    )
}

@Composable
internal inline fun <reified VM : ViewModel> NavHostController.checkoutGraphViewModels(
    backStackEntry: NavBackStackEntry,
    noinline parameters: ParametersDefinition? = null,
): CheckoutGraphViewModels<VM>? {
    val graphEntry = remember(backStackEntry) { getCheckoutGraphEntryOrNull() } ?: return null
    return CheckoutGraphViewModels(
        coordinator = koinViewModel(viewModelStoreOwner = graphEntry),
        viewModel = koinViewModel(
            viewModelStoreOwner = graphEntry,
            parameters = parameters,
        ),
    )
}

internal data class CheckoutGraphViewModels<out VM : ViewModel>(
    val coordinator: CheckoutCoordinatorViewModel,
    val viewModel: VM,
)
