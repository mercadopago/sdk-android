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

internal inline fun <A, B, C> Result<A, B>.map(
    transform: (A) -> C,
): Result<C, B> =
    when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> this
    }

internal inline fun <A, B, C> Result<A, B>.flatMap(
    transform: (A) -> Result<C, B>,
): Result<C, B> =
    when (this) {
        is Result.Success -> transform(data)
        is Result.Error -> this
    }

internal suspend inline fun <A, B, C> Result<A, B>.suspendFlatMap(
    crossinline transform: suspend (A) -> Result<C, B>,
): Result<C, B> =
    when (this) {
        is Result.Success -> transform(data)
        is Result.Error -> this
    }
