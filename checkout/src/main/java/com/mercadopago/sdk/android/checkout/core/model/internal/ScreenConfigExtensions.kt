package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.domain.model.Screen

internal fun ScreenConfig.toScreen(): Screen =
    when (this) {
        is ScreenConfig.ReviewAndConfirm -> Screen.REVIEW_AND_CONFIRM
        is ScreenConfig.StatusScreen -> Screen.STATUS_SCREEN
    }

internal fun CheckoutConfiguration?.getOnEmailChangeRequested(): (() -> Unit)? =
    this?.screenConfigs
        ?.filterIsInstance<ScreenConfig.ReviewAndConfirm>()
        ?.firstOrNull()
        ?.onEmailChangeRequested

internal fun CheckoutConfiguration?.getOnStatusScreenExit(): (() -> Unit)? =
    this?.screenConfigs
        ?.filterIsInstance<ScreenConfig.StatusScreen>()
        ?.firstOrNull()
        ?.onExit
