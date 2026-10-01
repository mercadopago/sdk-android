package com.mercadopago.sdk.android.checkout.analytics

import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.observability.domain.classifier.NativeErrorType
import com.mercadopago.sdk.android.checkout.domain.model.ObservedCheckoutError

internal class CheckoutErrorObservability {
    fun input(
        error: ObservedCheckoutError,
    ): NativeErrorInput = error.nativeErrorInput

    fun cancellationInput(): NativeErrorInput = NativeErrorInput.create(NativeErrorType.USER_CANCELLATION)
}
