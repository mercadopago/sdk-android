package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError

private const val UNSUPPORTED = "Unsupported checkout type"

internal fun MPCheckoutType<*, *>?.unsupportedTypeError(
    localized: ErrorLocalized,
): MercadoPagoCheckoutError.ConfigurationError =
    MercadoPagoCheckoutError.ConfigurationError(
        code = ErrorCode.INTEGRATION_ERROR,
        messageError = "$UNSUPPORTED: ${this?.let { it::class.simpleName } ?: "null"}",
        localized = localized.name,
    )
