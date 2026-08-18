package com.mercadopago.sdk.android.checkout.presentation.usecase

import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.Screen

internal class CancelledPaymentContextUseCase {
    private val visitedScreens = mutableListOf(Screen.PAYMENT_METHOD_SELECTOR)

    fun markScreenPresented(
        screen: Screen,
    ) {
        if (!visitedScreens.contains(screen)) {
            visitedScreens.add(screen)
        }
    }

    operator fun invoke(): MPUserCancelledContext.Payment =
        MPUserCancelledContext.Payment(
            screens = visitedScreens.toList(),
        )
}
