package com.mercadopago.sdk.android.checkout.domain.model

import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorDiagnostic

internal data class ObservedCheckoutError(
    val publicError: MercadoPagoCheckoutError,
    val nativeCode: NativeErrorCode,
    val httpStatus: Int? = null,
    val diagnostic: NativeErrorDiagnostic? = null,
)
