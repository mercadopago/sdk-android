package com.mercadopago.sdk.android.googlepay.domain.model

internal fun MPGooglePayErrorCode.toError(
    message: String,
) = MPGooglePayError(code = this, message = message)
