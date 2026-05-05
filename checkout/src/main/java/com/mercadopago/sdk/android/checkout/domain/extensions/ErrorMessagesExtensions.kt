package com.mercadopago.sdk.android.checkout.domain.extensions

internal const val UNABLE_RESOURCE = "unable_to_process_resource"

internal fun String.isPaymentMethodNotFound(): Boolean =
    contains("not found", ignoreCase = true) ||
        contains(UNABLE_RESOURCE, ignoreCase = true)
