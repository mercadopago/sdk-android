package com.mercadopago.sdk.android.checkout.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.mercadopago.sdk.android.checkout.presentation.loading.LoadingScreen
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination

@Composable
internal fun CheckoutLoadDestination(
    navigator: CheckoutNavigator,
    startDestination: CheckoutDestination,
) {
    LoadingScreen()
    LaunchedEffect(navigator, startDestination) {
        navigator.recoverTo(startDestination)
    }
}
