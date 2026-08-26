package com.mercadopago.sdk.android.checkout.presentation.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.withFrameNanos
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.mercadopago.sdk.android.checkout.presentation.CheckoutGraph
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination

@Composable
internal fun NavHostController.rememberCheckoutGraphEntry(): NavBackStackEntry? {
    val entry by produceState<NavBackStackEntry?>(initialValue = null, this) {
        while (value == null) {
            value = runCatching { getBackStackEntry<CheckoutGraph>() }.getOrNull()
            if (value == null) withFrameNanos { }
        }
    }
    return entry
}

internal fun NavHostController.popBackStackToPayment() {
    while (
        currentDestination?.route != CheckoutDestination.Payment::class.qualifiedName &&
        popBackStack()
        ) Unit
}
