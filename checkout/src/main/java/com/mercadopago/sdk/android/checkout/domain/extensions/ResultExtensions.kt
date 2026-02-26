package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.coremethods.domain.utils.Result

/**
 * Fold the result into a single value by providing handlers for both success and error cases.
 *
 * @param onSuccess Handler for success case, receives the success data
 * @param onError Handler for error case, receives the error
 * @return The result of applying the appropriate handler
 */
internal inline fun <A, B, C> Result<A, B>.fold(
    onSuccess: (A) -> C,
    onError: (B) -> C,
): C =
    when (this) {
        is Result.Success -> onSuccess(data)
        is Result.Error -> onError(error)
    }

/**
 * FlatMap allows chaining operations that return Results.
 * If this result is Success, applies transform. If Error, returns the error.
 *
 * @param transform Function that takes success value and returns a new Result
 * @return The result of transform if Success, or the original Error
 */
internal inline fun <A, B, C> Result<A, B>.flatMap(
    transform: (A) -> Result<C, B>,
): Result<C, B> =
    when (this) {
        is Result.Success -> transform(data)
        is Result.Error -> this
    }

/**
 * Map the success value of the result.
 *
 * @param transform Function to transform the success value
 * @return A new result with the transformed success value, or the original error
 */
internal inline fun <A, B, C> Result<A, B>.map(
    transform: (A) -> C,
): Result<C, B> =
    when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> this
    }

/**
 * Perform a side effect if the result is a success.
 *
 * @param action Action to perform with the success data
 * @return The original result
 */
internal inline fun <A, B> Result<A, B>.onSuccess(
    action: (A) -> Unit,
): Result<A, B> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

/**
 * Perform a side effect if the result is an error.
 *
 * @param action Action to perform with the error
 * @return The original result
 */
internal inline fun <A, B> Result<A, B>.onError(
    action: (B) -> Unit,
): Result<A, B> {
    if (this is Result.Error) {
        action(error)
    }
    return this
}
