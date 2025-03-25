package com.mercadopago.sdk.android.coremethods.domain.utils

/**
 * Result is a generic class that represents a result of an operation.
 */
sealed class Result<out A, out B> {

    /**
     * This class represents a success result.
     * @param data [A] The success value.
     */
    data class Success<A> constructor(
        val data: A,
    ) : Result<A, Nothing>()

    /**
     * This class represents an error result.
     * @param error [B] The error value.
     */
    data class Error<B> constructor(
        val error: B,
    ) : Result<Nothing, B>()
}
