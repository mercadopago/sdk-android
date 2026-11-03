package com.mercadopago.sdk.android.checkout.presentation.navigation

import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination

internal interface CheckoutNavigationHost {
    fun navigate(
        destination: CheckoutDestination,
    )

    fun pop(): Boolean

    fun popTo(
        destination: CheckoutDestination,
    ): Boolean
}
