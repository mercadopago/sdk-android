package com.mercadopago.sdk.android.checkout.domain.model

import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorInput

internal data class ObservedCheckoutError(
    val publicError: MercadoPagoCheckoutError,
    val nativeErrorInput: NativeErrorInput,
)
