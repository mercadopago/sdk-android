package com.mercadopago.sdk.android.core.exception

/**
 * Represents an exception that occurred during a network operation.
 *
 * @property body The response body from the failed network request as a string.
 */
data class NetworkException(
    val body: String,
) : Exception()
