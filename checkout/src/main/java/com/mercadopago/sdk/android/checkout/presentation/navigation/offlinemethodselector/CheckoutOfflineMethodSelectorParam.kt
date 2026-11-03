package com.mercadopago.sdk.android.checkout.presentation.navigation.offlinemethodselector

import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData

internal class CheckoutOfflineMethodSelectorParam(
    val screenData: MethodSelectionScreenData,
    val checkoutConfiguration: CheckoutConfiguration?,
)
