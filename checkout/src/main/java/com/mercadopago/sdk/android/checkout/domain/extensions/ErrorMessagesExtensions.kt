package com.mercadopago.sdk.android.checkout.domain.extensions

internal const val NOT_FOUND = "not found"
internal const val UNABLE_RESOURCE = "Unable to acquire requested payment method from the resource"

internal fun String.isPaymentMethodNotFound(): Boolean =
    this.contains(NOT_FOUND, ignoreCase = true) ||
        this.contains(UNABLE_RESOURCE, ignoreCase = true)
